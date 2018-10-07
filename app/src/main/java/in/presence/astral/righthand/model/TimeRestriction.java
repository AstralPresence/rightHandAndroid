package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TimeRestriction implements Serializable {

    @SerializedName("start")
    private int start;

    @SerializedName("end")
    private int end;

    public TimeRestriction(int start, int end){
        this.start=start;
        this.end=end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
