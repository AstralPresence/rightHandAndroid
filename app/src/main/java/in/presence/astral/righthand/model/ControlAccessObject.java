package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ControlAccessObject implements Serializable {

    @SerializedName("controlTopic")
    private String controlTopic;

    @SerializedName("timeRestrictions")
    private List<TimeRestriction> timeRestrictions;

    public ControlAccessObject(String controlTopic, List<TimeRestriction> timeRestrictions ){
        this.controlTopic=controlTopic;
        this.timeRestrictions=timeRestrictions;
    }

    public String getControlTopic() {
        return controlTopic;
    }

    public void setControlTopic(String controlTopic) {
        this.controlTopic = controlTopic;
    }

    public List<TimeRestriction> getTimeRestrictions() {
        return timeRestrictions;
    }

    public void setTimeRestrictions(List<TimeRestriction> timeRestrictions) {
        this.timeRestrictions = timeRestrictions;
    }
}
