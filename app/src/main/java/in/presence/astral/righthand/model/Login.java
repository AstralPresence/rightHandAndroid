package in.presence.astral.righthand.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DevOpsTrends on 7/11/2017.
 */

public class Login {


    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public Login(String email, String password) {
        this.password=password;
        this.email=email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
