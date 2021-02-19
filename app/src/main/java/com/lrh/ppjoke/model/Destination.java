package com.lrh.ppjoke.model;

/**
 * Created by LRH on 2020/11/15 0015
 */
public class Destination {

    /**
     * asStarter : false
     * isFrament : true
     * needLogin : false
     * pageUrl : main/tabs/no
     * id : 54152602
     * clazzName : com.lrh.ppjoke.ui.notifications.NotificationsFragment
     */

    private boolean asStarter;
    private boolean isFrament;
    private boolean needLogin;
    private String pageUrl;
    private int id;
    private String clazzName;

    public boolean isAsStarter() {
        return asStarter;
    }

    public void setAsStarter(boolean asStarter) {
        this.asStarter = asStarter;
    }

    public boolean isIsFrament() {
        return isFrament;
    }

    public void setIsFrament(boolean isFrament) {
        this.isFrament = isFrament;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
