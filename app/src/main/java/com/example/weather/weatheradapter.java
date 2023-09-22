package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class weatheradapter extends RecyclerView.Adapter<weatherviewholder> {
    private Context context;
    private ArrayList<weatherecycler> weatherlist;
    private HashMap<String, Integer> customImageMapping;

    public weatheradapter(Context context, ArrayList<weatherecycler>weatherlist,HashMap<String, Integer> customImageMapping) {
        this.context = context;
        this.weatherlist = weatherlist;
        this.customImageMapping = customImageMapping;

    }
    @NonNull
    @Override
    public weatherviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new weatherviewholder(LayoutInflater.from(context).inflate(R.layout.viewholderitems,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull weatherviewholder holder, int position) {
        weatherecycler model = weatherlist.get(position);
        holder.rvtemp.setText(model.getTemp() + "°C");
        holder.rvwind.setText(model.getWindspeed() + "Km/h");
        SimpleDateFormat dateinput = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat dateoutput = new SimpleDateFormat("hh:mm aa");
        try{
            Date outputime = dateinput.parse(model.getTime());
            holder.rvtime.setText(dateoutput.format(outputime));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (customImageMapping.containsKey(model.getCondition())) {
            Integer customImageResourceId = customImageMapping.get(model.getCondition());
            holder.rvimage.setImageResource(customImageResourceId);
        } else {
            // Handle the case where there's no custom image defined for the current condition
            // You can load a default image or do something else here
            holder.rvimage.setImageResource(R.drawable.clearskyimage); // Replace with your default image resource
        }
    }

    @Override
    public int getItemCount() {
        return weatherlist.size();
    }
}
