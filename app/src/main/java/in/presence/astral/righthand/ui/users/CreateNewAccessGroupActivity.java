package in.presence.astral.righthand.ui.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.adapter.UsersAdapter;
import in.presence.astral.righthand.room.User;
import timber.log.Timber;

import android.os.Bundle;

import java.util.List;

public class CreateNewAccessGroupActivity extends AppCompatActivity {

    RecyclerView usersList;
    UsersAdapter usersAdapter;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_access_group);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                Timber.i("on changed called");
                usersAdapter.setUsers(users);
            }
        });

    }
}
