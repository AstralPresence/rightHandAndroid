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

    @SerializedName("UIDs")
    private List<String> uids;

    @SerializedName("accessAllowed")
    private List<ControlAccessObject> accessAllowed;

    public AccessGroup(String name, String type,List<String> uids){
        this.name=name;
        this.type=type;
        this.uids=uids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

    public List<ControlAccessObject> getAccessAllowed() {
        return accessAllowed;
    }

    public void setAccessAllowed(List<ControlAccessObject> accessAllowed) {
        this.accessAllowed = accessAllowed;
    }
}
