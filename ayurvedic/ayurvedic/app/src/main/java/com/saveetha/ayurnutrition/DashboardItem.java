package com.saveetha.ayurnutrition;

public class DashboardItem {

    private String name;
    private String subText;

    public DashboardItem(String name, String subText) {
        this.name = name;
        this.subText = subText;
    }

    public String getName() {
        return name;
    }

    public String getSubText() {
        return subText;
    }
}
