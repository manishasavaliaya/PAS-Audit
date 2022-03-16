package com.passurvey.requestresponse;

/**
 * Created by jekson on 21-06-2016.
 */
public class LoginRequestResponse {
    int status;
    String message;
    LoginData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public class LoginData {

        int user_id;
        String first_name;
        String last_name;
        String gender;
        String email;
        String phone;
        String address;
        String profile_pic;
        String device_token;
        String device_type;
        String user_latitude;
        String user_longitude;
        int enable_notification;
        int password_set;
        int login_status;
        int wallet_amount;
        int app_rating;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getUser_latitude() {
            return user_latitude;
        }

        public void setUser_latitude(String user_latitude) {
            this.user_latitude = user_latitude;
        }

        public String getUser_longitude() {
            return user_longitude;
        }

        public void setUser_longitude(String user_longitude) {
            this.user_longitude = user_longitude;
        }

        public int getEnable_notification() {
            return enable_notification;
        }

        public void setEnable_notification(int enable_notification) {
            this.enable_notification = enable_notification;
        }

        public int getPassword_set() {
            return password_set;
        }

        public void setPassword_set(int password_set) {
            this.password_set = password_set;
        }

        public int getLogin_status() {
            return login_status;
        }

        public void setLogin_status(int login_status) {
            this.login_status = login_status;
        }

        public int getWallet_amount() {
            return wallet_amount;
        }

        public void setWallet_amount(int wallet_amount) {
            this.wallet_amount = wallet_amount;
        }

        public int getApp_rating() {
            return app_rating;
        }

        public void setApp_rating(int app_rating) {
            this.app_rating = app_rating;
        }
    }
}
