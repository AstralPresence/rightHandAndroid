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
    private Float status;

    public Control(String group, String room, String name) {
        this.group = group;
        this.room = room;
        this.name = name;
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

    public Float getStatus() {
        return status;
    }

    public void setStatus(Float status) {
        this.status = status;
    }

}
