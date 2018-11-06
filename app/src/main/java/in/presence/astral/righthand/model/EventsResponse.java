package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Maha Perriyava on 4/5/2018.
 */

public class EventsResponse implements Serializable {

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private List<Event> message;

    public List<Event> getMessage() {
        return message;
    }

    public void setMessage(List<Event> message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
