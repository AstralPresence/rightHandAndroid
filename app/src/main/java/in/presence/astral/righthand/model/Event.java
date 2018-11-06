package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {


    @SerializedName("id")
    private String topic;

    @SerializedName("timeStamp")
    private String timeStamp;

    @SerializedName("log")
    private String log;


    public Event(String topic, String timeStamp, String log){
        this.log=log;
        this.topic=topic;
        this.timeStamp=timeStamp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
