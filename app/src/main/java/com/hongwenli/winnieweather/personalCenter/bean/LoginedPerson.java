package com.hongwenli.winnieweather.personalCenter.bean;

public class LoginedPerson {
    private static Person loginedPerson;
    public static void setLoginedPerson(Person person){
        loginedPerson = person;
    }
    public static Person getLoginedPerson(){
        if(loginedPerson!=null)
            return loginedPerson;
        else return new Person();
    }
}
