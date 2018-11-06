package in.presence.astral.righthand.room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "modes", primaryKeys = {"name","topic","state"})
public class Mode {


    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "topic")
    private String topic;

    @NonNull
    @ColumnInfo(name = "state")
    private int state;

    public Mode(String topic, String name, int state) {
        this.topic = topic;
        this.state = state;
        this.name = name;
    }


    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
    }

    @NonNull
    public int getState() {
        return state;
    }

    public void setState(@NonNull int state) {
        this.state = state;
    }
}
