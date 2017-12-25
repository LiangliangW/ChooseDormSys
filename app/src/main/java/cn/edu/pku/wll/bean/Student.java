package cn.edu.pku.wll.bean;

/**
 * Created by WLL on 2017/12/25.
 */

public class Student {

    /**
     * studentid : 1301210899
     * name : 暂时保密
     * gender : 女
     * vcode : asdf12
     * room : 5101
     * building : 5
     * location : 大兴
     * grade : 2013
     */

    private int id;
    private String name;
    private String gender;
    private String vCode;
    private String room;
    private int building;
    private String location;
    private int grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
