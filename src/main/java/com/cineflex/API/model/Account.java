package com.cineflex.API.model;

import java.time.LocalDateTime;

public class Account {
    Long id;
    String username;
    String email;
    String password;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Boolean verify;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
    public Boolean getVerify() {
        return verify;
    }
    public void setVerify(Boolean verify) {
        this.verify = verify;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
        result = prime * result + ((updatedTime == null) ? 0 : updatedTime.hashCode());
        result = prime * result + ((verify == null) ? 0 : verify.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (createdTime == null) {
            if (other.createdTime != null)
                return false;
        } else if (!createdTime.equals(other.createdTime))
            return false;
        if (updatedTime == null) {
            if (other.updatedTime != null)
                return false;
        } else if (!updatedTime.equals(other.updatedTime))
            return false;
        if (verify == null) {
            if (other.verify != null)
                return false;
        } else if (!verify.equals(other.verify))
            return false;
        return true;
    }
    public Account() {
    }
    public Account(Long id, String username, String email, String password, LocalDateTime createdTime,
            LocalDateTime updatedTime, Boolean verify) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.verify = verify;
    }
    @Override
    public String toString() {
        return "Account [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + ", verify=" + verify + "]";
    }


    

}
