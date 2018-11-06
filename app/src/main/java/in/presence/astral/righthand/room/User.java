package in.presence.astral.righthand.room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "users", primaryKeys = {"email"})
public class User {

    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "accessGroupType")
    private String accessGroupType;

    @NonNull
    @ColumnInfo(name = "accessGroupName")
    private String accessGroupName;

    @NonNull
    @ColumnInfo(name = "validity")
    private String validity;

    public User(String username, String email, String accessGroupType, String accessGroupName, String validity) {
        this.username = username;
        this.email = email;
        this.accessGroupName = accessGroupName;
        this.accessGroupType = accessGroupType;
        this.validity = validity;

    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getAccessGroupType() {
        return accessGroupType;
    }

    public void setAccessGroupType(@NonNull String accessGroupType) {
        this.accessGroupType = accessGroupType;
    }

    @NonNull
    public String getAccessGroupName() {
        return accessGroupName;
    }

    public void setAccessGroupName(@NonNull String accessGroupName) {
        this.accessGroupName = accessGroupName;
    }

    @NonNull
    public String getValidity() {
        return validity;
    }

    public void setValidity(@NonNull String validity) {
        this.validity = validity;
    }
}
