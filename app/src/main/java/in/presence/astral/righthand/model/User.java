package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Maha Perriyava on 4/5/2018.
 */

public class User implements Serializable {

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("accessGroupType")
    private String accessGroupType;

    @SerializedName("accessGroupName")
    private String accessGroupName;

    @SerializedName("validity")
    private String validity;

    public User(String email, String accessGroupType,String username,String accessGroupName, String validity){
        this.accessGroupType=accessGroupType;
        this.email=email;
        this.accessGroupName=accessGroupName;
        this.username=username;
        this.validity=validity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessGroupType() {
        return accessGroupType;
    }

    public void setAccessGroupType(String accessGroupType) {
        this.accessGroupType = accessGroupType;
    }

    public String getAccessGroupName() {
        return accessGroupName;
    }

    public void setAccessGroupName(String accessGroupName) {
        this.accessGroupName = accessGroupName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
