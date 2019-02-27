package in.presence.astral.righthand.room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "controls", primaryKeys = {"ctrl_group","room","name"})
public class Control {


    @NonNull
    @ColumnInfo(name = "ctrl_group")
    private String group;

    @NonNull
    @ColumnInfo(name = "room")
    private String room;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "status")
    private float status;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "displayName")
    private String displayName;

    @ColumnInfo(name = "ip")
    private String ip;

    public Control(String group, String room, String name, String type, String displayName,float status, String ip) {
        this.group = group;
        this.room = room;
        this.name = name;
        this.type = type;
        this.displayName= displayName;
        this.status=status;
        this.ip=ip;
    }

    @NonNull
    public String getGroup() {
        return group;
    }

    public void setGroup(@NonNull String group) {
        this.group = group;
    }

    @NonNull
    public String getRoom() {
        return room;
    }

    public void setRoom(@NonNull String room) {
        this.room = room;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
