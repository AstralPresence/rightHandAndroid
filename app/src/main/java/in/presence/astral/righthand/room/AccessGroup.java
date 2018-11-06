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

    @ColumnInfo(name = "controls")
    private String controlsAllowed;



    public AccessGroup(String name,  String controlsAllowed) {
        this.name = name;
        this.controlsAllowed = controlsAllowed;
    }
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getControlsAllowed() {
        return controlsAllowed;
    }

    public void setControlsAllowed(String controlsAllowed) {
        this.controlsAllowed = controlsAllowed;
    }
}
