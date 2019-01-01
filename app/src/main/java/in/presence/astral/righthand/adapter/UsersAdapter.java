package in.presence.astral.righthand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.User;
import in.presence.astral.righthand.ui.main.MainActivity;
import timber.log.Timber;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView userType;

        private UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userType = itemView.findViewById(R.id.userType);
        }
    }

    private final LayoutInflater mInflater;
    private List<User> mUsers; // Cached copy of controls

    public UsersAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (mUsers != null) {
            User user = mUsers.get(position);
            holder.userName.setText(user.getEmail());
            holder.userType.setText(user.getAccessGroupType());

        }
    }

    public void setUsers(List<User> users){
        mUsers = users;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mControls has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mUsers != null)
            return mUsers.size();
        else return 0;
    }
}