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

    @SerializedName("accessGroupType")
    private String accessGroupType;

    @SerializedName("userValidity")
    private long userValidity;

    public User(String email, String accessGroupType, long userValidity){
        this.accessGroupType=accessGroupType;
        this.email=email;
        this.userValidity=userValidity;
    }

}
