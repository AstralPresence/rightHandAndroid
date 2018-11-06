package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Mode implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("controlData")
    private List<ModeParticular> controlData;

    public Mode(String name,List<ModeParticular> controlData){
        this.name=name;
        this.controlData=controlData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModeParticular> getControlData() {
        return controlData;
    }

    public void setControlData(List<ModeParticular> controlData) {
        this.controlData = controlData;
    }
}