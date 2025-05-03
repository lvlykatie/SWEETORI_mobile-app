package com.example.sweetori.dto.response;

import com.google.gson.annotations.SerializedName;

public class ResLoginDTO {
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("refresh_token")
    private String refresh_token;
    @SerializedName("userLogin")
    private UserLogin userLogin;

    public ResLoginDTO(String access_token, String refresh_token, UserLogin userLogin) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.userLogin = userLogin;
    }

    public ResLoginDTO() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    //    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
    public static class UserLogin {
        private String username;
        private String email;
        private String firstName;
        private String lastName;

        public UserLogin(String username, String email, String firstName, String lastName) {
            this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public UserLogin() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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
    }
}