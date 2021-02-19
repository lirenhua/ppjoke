package com.lrh.libnavcompiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.lrh.libnavnotation.ActivityDestination;
import com.lrh.libnavnotation.FragmentDestination;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Created by LRH on 2020/11/15 0015
 * APP页面导航信息收集注解处理器
 * <p>
 * AutoService注解：就这么一标记，annotationProcessor  project()应用一下,编译时就能自动执行该类了。
 * <p>
 * SupportedSourceVersion注解:声明我们所支持的jdk版本
 * <p>
 * SupportedAnnotationTypes:声明该注解处理器想要处理那些注解
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.lrh.libnavnotation.FragmentDestination", "com.lrh.libnavnotation.ActivityDestination"})
public class NavProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private static final String OUTPUT_FILE_NAME = "destination.json";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        // 打印消息
        messager = processingEnvironment.getMessager();
        // 生成文件
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> fragmentElements = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityElements = roundEnvironment.getElementsAnnotatedWith(ActivityDestination.class);

        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            HashMap<String, JSONObject> destMap = new HashMap<>();

            handleDestination(fragmentElements, FragmentDestination.class, destMap);
            handleDestination(activityElements, ActivityDestination.class, destMap);
            FileOutputStream fos = null;
            OutputStreamWriter os = null;

            try {
                //filer.createResource()意思是创建源文件
                //我们可以指定为class文件输出的地方，
                //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
                //StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
                //StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了
                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
                String resourcePath = resource.toUri().getPath();
                messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath:" + resourcePath);

                //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
                //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径
                String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
                String assetsPath = appPath + "src/main/assets/";

                File file = new File(assetsPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                //此处就是稳健的写入了
                File outPutFile = new File(file, OUTPUT_FILE_NAME);
                if (outPutFile.exists()) {
                    outPutFile.delete();
                }
                outPutFile.createNewFile();

                //利用fastjson把收集到的所有的页面信息 转换成JSON格式的。并输出到文件中
                String content = JSON.toJSONString(destMap);
                fos = new FileOutputStream(outPutFile);
                os = new OutputStreamWriter(fos, "UTF-8");
                os.write(content);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private void handleDestination(Set<? extends Element> typeElements, Class<? extends Annotation> annotationClazz, HashMap<String, JSONObject> destMap) {
        for (Element element : typeElements) {
            TypeElement typeElement = (TypeElement) element;
            String clazzName = typeElement.getQualifiedName().toString();
            int id = Math.abs(typeElement.hashCode());
            String pageUrl = null;
            boolean needLogin = false;
            boolean asStarter = false;
            boolean isFrament = false;

            Annotation annotation = typeElement.getAnnotation(annotationClazz);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination fragmentDestination = (FragmentDestination) annotation;
                pageUrl = fragmentDestination.pageUrl();
                needLogin = fragmentDestination.needLogin();
                asStarter = fragmentDestination.asStarter();
                isFrament = true;
            } else if (annotation instanceof ActivityDestination) {
                ActivityDestination fragmentDestination = (ActivityDestination) annotation;
                pageUrl = fragmentDestination.pageUrl();
                needLogin = fragmentDestination.needLogin();
                asStarter = fragmentDestination.asStarter();
                isFrament = false;
            }

            if (destMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的pageUrl：" + clazzName);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("clazzName", clazzName);
                jsonObject.put("id", id);
                jsonObject.put("pageUrl", pageUrl);
                jsonObject.put("needLogin", needLogin);
                jsonObject.put("asStarter", asStarter);
                jsonObject.put("isFrament", isFrament);

                destMap.put(pageUrl, jsonObject);
            }
        }
    }
}
