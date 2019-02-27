package in.presence.astral.righthand.ui.roomcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.adapter.AccessControlsAdapter;
import in.presence.astral.righthand.adapter.UsersAdapter;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.User;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlViewModel;
import in.presence.astral.righthand.ui.users.UserViewModel;
import in.presence.astral.righthand.ui.users.UsersActivity;
import timber.log.Timber;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class CreateNewAccessGroupActivity extends AppCompatActivity {

    RecyclerView acceessControlsList;
    AccessControlsAdapter accessControlsAdapter;
    RoomControlViewModel mViewModel;

    ArrayAdapter<String> groupsAdapter,roomsAdapter;
    Spinner roomSpinner, groupSpinner;
    String selectedGroup, selectedRoom;
    RecyclerView controlsAccessList;

    TextView done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_access_group);

        mViewModel = ViewModelProviders.of(this).get(RoomControlViewModel.class);

        roomSpinner = findViewById(R.id.roomSpinner);
        groupSpinner = findViewById(R.id.groupSpinner);

        controlsAccessList = findViewById(R.id.accessControlsList);

        done = findViewById(R.id.done);

        accessControlsAdapter = new AccessControlsAdapter(this);
        controlsAccessList.setAdapter(accessControlsAdapter);
        controlsAccessList.setLayoutManager(new LinearLayoutManager(this));

        mViewModel.getDistinctGroups().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> groups) {



                groupsAdapter = new ArrayAdapter<String>(CreateNewAccessGroupActivity.this,
                        android.R.layout.simple_spinner_item, groups);
                groupsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                groupSpinner.setAdapter(groupsAdapter);


            }
        });

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGroup = groupsAdapter.getItem(i);

                mViewModel.getDistinctRooms(selectedGroup).observe(CreateNewAccessGroupActivity.this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> rooms) {



                        roomsAdapter = new ArrayAdapter<String>(CreateNewAccessGroupActivity.this,
                                android.R.layout.simple_spinner_item, rooms);
                        roomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        roomSpinner.setAdapter(roomsAdapter);


                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRoom = roomsAdapter.getItem(i);
                Timber.i("room selected %s",selectedRoom);

                mViewModel.getRoomControls(selectedGroup,selectedRoom).observe(CreateNewAccessGroupActivity.this, new Observer<List<Control>>() {
                    @Override
                    public void onChanged(List<Control> controls) {
                        Timber.i("on change called settting controls to adapter");
                        accessControlsAdapter.setControls(controls);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
