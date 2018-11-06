package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Maha Perriyava on 4/5/2018.
 */

public class AGControls implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("uids")
    private List<String> uids;

    @SerializedName("accessAllowed")
    private List<ControlAccessObject> accessAllowed;

    public AGControls(String name, String type, List<String> uids){
        this.name=name;
        this.type=type;
        this.uids=uids;
    }

}
