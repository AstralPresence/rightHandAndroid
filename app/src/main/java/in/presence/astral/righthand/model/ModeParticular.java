package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeParticular {

    @SerializedName("controlTopic")
    private String controlTopic;

    @SerializedName("controlStatus")
    private int controlStatus;

    public ModeParticular(String controlTopic, int controlStatus){
        this.controlTopic=controlTopic;
        this.controlStatus=controlStatus;
    }

    public int getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(int controlStatus) {
        this.controlStatus = controlStatus;
    }

    public String getControlTopic() {
        return controlTopic;
    }

    public void setControlTopic(String controlTopic) {
        this.controlTopic = controlTopic;
    }
}
