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
import in.presence.astral.righthand.ui.main.MainActivity;
import timber.log.Timber;

public class ControlsAdapter extends RecyclerView.Adapter<ControlsAdapter.ControlViewHolder> {

    class ControlViewHolder extends RecyclerView.ViewHolder {
        private final TextView controlName;
        private final ImageView controlImage;
        private final SeekBar controlSeek;
        private final SwitchCompat controlSwitch;

        private ControlViewHolder(View itemView) {
            super(itemView);
            controlName = itemView.findViewById(R.id.controlName);
            controlImage = itemView.findViewById(R.id.controlImage);
            controlSeek = itemView.findViewById(R.id.controlSeekBar);
            controlSwitch = itemView.findViewById(R.id.controlSwitch);
        }
    }

    private final LayoutInflater mInflater;
    private List<Control> mControls; // Cached copy of controls

    public ControlsAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public ControlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.control_list_item, parent, false);
        return new ControlViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ControlViewHolder holder, int position) {
        if (mControls != null) {
            Control current = mControls.get(position);
            holder.controlName.setText(current.getName());
            if(current.getName().toLowerCase().contains("fan")){
                holder.controlImage.setImageResource(R.drawable.ic_fan_black_24dp);
                holder.controlSwitch.setVisibility(View.GONE);
                holder.controlSeek.setVisibility(View.VISIBLE);
                holder.controlSeek.setTag(current.getName());
                holder.controlSeek.setProgress((int) current.getStatus());
                holder.controlSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        String controlName = (String)seekBar.getTag();
                        Timber.i("control name %s",controlName);
                        EventBus.getDefault().post(new MainActivity.MessageEvent("mqtt",controlName,i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            } else {
                holder.controlSwitch.setTag(current.getName());
                if(current.getStatus()==1){
                    holder.controlSwitch.setChecked(true);
                } else {
                    holder.controlSwitch.setChecked(false);
                }
                if(current.getName().toLowerCase().contains("light")){
                    holder.controlImage.setImageResource(R.drawable.ic_lightbulb_black_24dp);
                } else {
                    holder.controlImage.setImageResource(R.drawable.ic_power_black_24dp);
                }
                holder.controlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        String controlName = (String)compoundButton.getTag();
                        Timber.i("control name %s",controlName);
                        if(b){
                            EventBus.getDefault().post(new MainActivity.MessageEvent("mqtt",controlName,1));
                        } else {

                            EventBus.getDefault().post(new MainActivity.MessageEvent("mqtt",controlName,0));
                        }
                    }
                });
            }
        }
    }

    public void setControls(List<Control> controls){
        mControls = controls;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mControls has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mControls != null)
            return mControls.size();
        else return 0;
    }
}