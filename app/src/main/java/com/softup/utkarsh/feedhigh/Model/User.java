package com.softup.utkarsh.feedhigh.Model;

public class User {
    private String email,password,name,phone,empId,Designation;

    public User() {
    }

    public User(String email, String password, String name, String phone,String empId,String Designation) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.empId=empId;
        this.Designation=Designation;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }
}
