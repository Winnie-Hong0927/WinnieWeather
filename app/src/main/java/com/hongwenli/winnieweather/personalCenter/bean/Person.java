package com.hongwenli.winnieweather.personalCenter.bean;

public class Person {
    private String username;
    private String name;
    private int age;
    private String desc;
    private String password;
    public Person(){}
    public Person(String username, String name, int age, String desc) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.desc = desc;
    }

    public Person(String username, String name, int age, String desc,  String password) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.desc = desc;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", desc='" + desc + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
