package com.example.weather;

public class weatherecycler {
    public String time;
    public String temp;
    public String icon;
    public String windspeed;
    public String condition;

    public weatherecycler(String time, String temp, String icon, String windspeed,String condition) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this.windspeed = windspeed;
        this.condition = condition;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
