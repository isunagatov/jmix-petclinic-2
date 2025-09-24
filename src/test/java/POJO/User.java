package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class User {
    private String SubSystem;
    private String UserRole;
    private String UserLogin;
    private String UserPassword;
    private String GroupKK;
    private String UserEmail;


    public String _entityName;
    public String _instanceName;
    public String id;
    public boolean active;
    public int version;
    @JsonProperty("username")
    private String UserName;
    public String lastName;
    public Date lastModifiedDate;
    public String firstName;
    public Date createdDate;
    public String createdBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSubSystem() {
        return SubSystem;
    }

    public void setSubSystem(String subSystem) {
        SubSystem = subSystem;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String UserRole) {
        this.UserRole = UserRole;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserLogin() {
        return UserLogin;
    }

    public void setUserLogin(String UserLogin) {
        this.UserLogin = UserLogin;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String UserPassword) {
        this.UserPassword = UserPassword;
    }


    public void setGroupKK(String groupKK) {
        this.GroupKK = groupKK;
    }

    public String getGroupKK() {
        return GroupKK;
    }

    public String getEmail() {
        return UserEmail;
    }

    public void setEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }

    @Override
    public String toString() {
        return "User: UserName = " + this.UserName + " UserLogin = " + this.UserLogin;
    }
}
