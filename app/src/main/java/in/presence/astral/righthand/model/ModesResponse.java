package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModesResponse {
    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private List<Mode> message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Mode> getMessage() {
        return message;
    }

    public void setMessage(List<Mode> message) {
        this.message = message;
    }
}
