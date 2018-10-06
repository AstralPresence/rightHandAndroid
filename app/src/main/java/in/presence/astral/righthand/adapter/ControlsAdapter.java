package in.presence.astral.righthand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.room.Control;

public class ControlsAdapter extends RecyclerView.Adapter<ControlsAdapter.ControlViewHolder> {

    class ControlViewHolder extends RecyclerView.ViewHolder {
        private final TextView controlName;
        private final ImageView controlImage;
        private final SeekBar controlSeek;
        private final Switch controlSwitch;

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
        }
    }

    void setControls(List<Control> controls){
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