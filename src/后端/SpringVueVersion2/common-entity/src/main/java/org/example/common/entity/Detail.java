package org.example.common.entity;

import java.sql.Date;

public class Detail {
    private String userID;
    private String sex;
    private String age;
    private long birthday;
    private String location;
    private String persionalSign;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPersionalSign() {
        return persionalSign;
    }

    public void setPersionalSign(String persionalSign) {
        this.persionalSign = persionalSign;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "userID='" + userID + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", birthday=" + birthday +
                ", location='" + location + '\'' +
                ", persionalSign='" + persionalSign + '\'' +
                '}';
    }
}
