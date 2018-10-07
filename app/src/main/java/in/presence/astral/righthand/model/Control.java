package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Control implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("group")
    private String group;

    @SerializedName("room")
    private String room;

    @SerializedName("status")
    private long status;

    public Control(String name, String displayName, String group, String room, long status){
        this.name=name;
        this.room=room;
        this.displayName=displayName;
        this.group=group;
        this.status=status;
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

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}
