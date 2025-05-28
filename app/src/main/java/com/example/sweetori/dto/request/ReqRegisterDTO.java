package com.example.sweetori.dto.request;

public class ReqRegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String gender;
    private String phoneNumber;
    private String buyingAddress;
    private String shippingAddress;
    private Role role = new Role(2); // Mặc định roleId = 2

    // Constructor mặc định (cần cho Retrofit)


    public ReqRegisterDTO() {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.buyingAddress = buyingAddress;
        this.shippingAddress = shippingAddress;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBuyingAddress() {
        return buyingAddress;
    }

    public void setBuyingAddress(String buyingAddress) {
        this.buyingAddress = buyingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // --------------- Lớp Role ---------------
    public static class Role {
        private int roleId;

        public Role(int roleId) {
            this.roleId = roleId;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }
    }
}