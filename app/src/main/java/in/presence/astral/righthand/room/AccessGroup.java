package in.presence.astral.righthand.room;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import in.presence.astral.righthand.model.ControlAccessObject;

@Entity(tableName = "access_group", primaryKeys = {"name"})
public class AccessGroup {


    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "uids")
    private String uids;

    @ColumnInfo(name = "controlTopic")
    private String controlTopic;

    @ColumnInfo(name = "startTime")
    private int startTime;

    @ColumnInfo(name = "endTime")
    private int endTime;



    public AccessGroup(String name,  String uids,String controlTopic, int startTime, int endTime) {
        this.name = name;
        this.uids = uids;
        this.controlTopic=controlTopic;
        this.startTime =startTime;
        this.endTime =endTime;
    }
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getUids() {
        return uids;
    }

    public void setUids(@NonNull String uids) {
        this.uids = uids;
    }

    public String getControlTopic() {
        return controlTopic;
    }

    public void setControlTopic(String controlTopic) {
        this.controlTopic = controlTopic;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
