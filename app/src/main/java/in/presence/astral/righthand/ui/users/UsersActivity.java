package in.presence.astral.righthand.ui.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.adapter.ControlsAdapter;
import in.presence.astral.righthand.adapter.UsersAdapter;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.User;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlFragment;
import timber.log.Timber;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    RecyclerView usersList;
    UsersAdapter usersAdapter;
    UserViewModel userViewModel;
    FloatingActionButton addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usersList = findViewById(R.id.usersList);
        usersAdapter = new UsersAdapter(this);
        usersList.setAdapter(usersAdapter);
        usersList.setLayoutManager(new LinearLayoutManager(this));

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                Timber.i("on changed called");
                usersAdapter.setUsers(users);
            }
        });


        addUser = findViewById(R.id.addUser);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddUserDialogClass addUserDialogClass = new AddUserDialogClass(UsersActivity.this);
                WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
                lWindowParams.copyFrom(addUserDialogClass.getWindow().getAttributes());
                lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; // this is where the magic happens
                lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                addUserDialogClass.show();// I was told to call show first I am not sure if this it to cause layout to happen so that we can override width?
                addUserDialogClass.getWindow().setAttributes(lWindowParams);
            }
        });
    }

    public class AddUserDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public ImageButton addAG;
        public Button createUser;
        public Spinner agList;

        public AddUserDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.adduser_dialog);
            addAG = findViewById(R.id.addAG);
            createUser = findViewById(R.id.createUser);
            agList = findViewById(R.id.agList);
            addAG.setOnClickListener(this);
            createUser.setOnClickListener(this);



            userViewModel.getAccessGroupNames().observe(UsersActivity.this, new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> agNames) {

                    Timber.i("on changed called");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UsersActivity.this,
                            android.R.layout.simple_spinner_item, agNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    agList.setAdapter(adapter);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addAG: {
                    startActivity(new Intent(UsersActivity.this, CreateNewAccessGroupActivity.class));
                    dismiss();
                    break;
                }
                case R.id.createUser: {

                }
            }
        }
    }
}
