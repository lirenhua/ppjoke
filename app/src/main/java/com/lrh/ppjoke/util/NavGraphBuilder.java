package com.lrh.ppjoke.util;

import android.content.ComponentName;

import com.lrh.libnavnotation.FragmentDestination;
import com.lrh.ppjoke.model.Destination;

import java.util.HashMap;

import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

/**
 * Created by LRH on 2020/11/15 0015
 */
public class NavGraphBuilder {
    public static void build(NavController navController) {
        NavigatorProvider provider = navController.getNavigatorProvider();

        FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);

        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Destination destination : destConfig.values()) {
            if (destination.isIsFrament()) {
                FragmentNavigator.Destination fragmentDestination = fragmentNavigator.createDestination();
                fragmentDestination.setClassName(destination.getClazzName());
                fragmentDestination.setId(destination.getId());
                fragmentDestination.addDeepLink(destination.getPageUrl());

                navGraph.addDestination(fragmentDestination);
            } else {
                ActivityNavigator.Destination activityDestination = activityNavigator.createDestination();
                activityDestination.setId(destination.getId());
                activityDestination.setComponentName(new ComponentName(AppGlobals.getApplication().getPackageName(), destination.getClazzName()));
                activityDestination.addDeepLink(destination.getPageUrl());


                navGraph.addDestination(activityDestination);
            }

            if (destination.isAsStarter()) {
                navGraph.setStartDestination(destination.getId());
            }
        }

        navController.setGraph(navGraph);
    }
}
