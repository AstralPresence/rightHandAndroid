package in.presence.astral.righthand.room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "events", primaryKeys = {"topic"})
public class Event {

    @NonNull
    @ColumnInfo(name = "topic")
    private String topic;

    @NonNull
    @ColumnInfo(name = "timeStamp")
    private String timeStamp;

    @NonNull
    @ColumnInfo(name = "log")
    private String log;

    public Event(String topic, String timeStamp, String log) {
        this.topic = topic;
        this.timeStamp = timeStamp;
        this.log = log;
    }

    @NonNull
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(@NonNull String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
    }

    @NonNull
    public String getLog() {
        return log;
    }

    public void setLog(@NonNull String log) {
        this.log = log;
    }
}
