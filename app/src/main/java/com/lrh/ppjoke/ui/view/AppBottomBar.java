package com.lrh.ppjoke.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.lrh.ppjoke.R;
import com.lrh.ppjoke.model.BottomBar;
import com.lrh.ppjoke.model.BottomBar.Tabs;
import com.lrh.ppjoke.model.Destination;
import com.lrh.ppjoke.util.AppConfig;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by LRH on 2020/11/16 0016
 */
public class AppBottomBar extends BottomNavigationView {
    private static int[] sIcons = new int[]{R.drawable.icon_tab_find, R.drawable.icon_tab_home, R.drawable.icon_tab_mine
            , R.drawable.icon_tab_movie, R.drawable.icon_tab_publish, R.drawable.icon_tab_sofa};

    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        BottomBar bottomBar = AppConfig.getBottomBar();
        List<Tabs> tabs = bottomBar.getTabs();

        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(bottomBar.getActiveColor()), Color.parseColor(bottomBar.getInActiveColor())};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setSelectedItemId(bottomBar.getSelectTab());

        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.Tabs tab = tabs.get(i);
            if (!tab.isEnable()) {
                return;
            }
            int id = getId(tab.getPageUrl());
            if (id < 0) {
                return;
            }
            MenuItem menuItem = getMenu().add(0, id, tab.getIndex(), tab.getTitle());
            menuItem.setIcon(sIcons[tab.getIndex()]);
        }

        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.Tabs tab = tabs.get(i);
            int iconSize = dp2px(tab.getSize());

            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(tab.getIndex());
            itemView.setIconSize(iconSize);

            if (TextUtils.isEmpty(tab.getTitle())) {
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.getTintColor())));
                // 不让按钮上下浮动
                itemView.setShifting(false);
            }
        }
    }

    private int dp2px(int size) {
        float value = getContext().getResources().getDisplayMetrics().density * size + 0.5f;
        return (int) value;
    }

    private int getId(String pageUrl) {
        Destination destination = AppConfig.getDestConfig().get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.getId();
    }
}
