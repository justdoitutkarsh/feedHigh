package com.softup.utkarsh.feedhigh.Model;

public class EmpMaster extends User {
    private String Department;
    private String Email;
    private String Mobile;
    private String Name;
    private String Password;
    private String AvatarUrl;
    private String Designation;

    public EmpMaster() {
    }

    public EmpMaster(String department, String email, String mobile, String name, String password,String designation) {
        Department = department;
        Email = email;
        Mobile = mobile;
        Name = name;
        Password = password;
        Designation = designation;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
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
    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }
}
