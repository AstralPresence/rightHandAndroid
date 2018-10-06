package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Maha Perriyava on 4/5/2018.
 */

public class AccessGroup implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("uids")
    private List<String> uids;

    public AccessGroup(String email, String accessGroupType, long userValidity){
        this.accessGroupType=accessGroupType;
        this.email=email;
        this.userValidity=userValidity;
    }

}
