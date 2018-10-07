package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Maha Perriyava on 4/5/2018.
 */

public class ControlsResponse implements Serializable {
    @SerializedName("results")
    private List<Control> results;

    public List<Control> getResults() {
        return results;
    }
}
