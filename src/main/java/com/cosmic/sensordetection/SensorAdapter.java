package com.cosmic.sensordetection;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {

    private final Context context;
    private final List<Sensor> sensors;

    public SensorAdapter(Context context, List<Sensor> sensors) {
        this.context = context;
        this.sensors = sensors;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sensor_item_layout, parent, false);
        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        if(position %2 == 1){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            holder.sensor_name.setTextColor(ContextCompat.getColor(context, R.color.black));
        }else{
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.sensor_name.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        holder.sensor_name.setText(sensors.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    static class SensorViewHolder extends RecyclerView.ViewHolder {

        TextView sensor_name;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            sensor_name = itemView.findViewById(R.id.sensor_name);
        }
    }
}
