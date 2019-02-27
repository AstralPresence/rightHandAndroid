package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Control implements Serializable {

    @SerializedName("type")
    private String type;

    @SerializedName("controlName")
    private String name;

    @SerializedName("controlStatus")
    private float status;

    @SerializedName("displayName")
    private String displayName;


    @SerializedName("ip")
    private String ip;


    @SerializedName("groupName")
    private String group;

    @SerializedName("room")
    private String room;

    public Control(String name, String displayName, String group, String room, float status, String type, String ip ){
        this.name=name;
        this.room=room;
        this.displayName=displayName;
        this.ip=ip;
        this.group=group;
        this.status=status;
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
