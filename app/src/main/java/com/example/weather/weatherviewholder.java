package com.example.weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class weatherviewholder extends RecyclerView.ViewHolder {
    TextView rvtime;
    TextView rvtemp;
    TextView rvwind;
    ImageView rvimage;
    CardView container;

    public weatherviewholder(@NonNull View itemView) {
        super(itemView);
        rvimage  = itemView.findViewById(R.id.rvimage);
        rvwind = itemView.findViewById(R.id.rvwind);
        rvtemp = itemView.findViewById(R.id.rvtemp);
        rvtime = itemView.findViewById(R.id.rvtime);
        container = itemView.findViewById(R.id.container);
    }
}
