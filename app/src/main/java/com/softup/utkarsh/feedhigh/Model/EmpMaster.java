package com.softup.utkarsh.feedhigh.Model;

public class EmpMaster extends User {
    private String Department;
    private String Email;
    private String Mobile;
    private String Name;
    private String Password;

    public EmpMaster() {
    }

    public EmpMaster(String department, String email, String mobile, String name, String password) {
        Department = department;
        Email = email;
        Mobile = mobile;
        Name = name;
        Password = password;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
