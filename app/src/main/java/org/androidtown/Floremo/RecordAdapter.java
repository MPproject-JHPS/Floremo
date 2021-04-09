package org.androidtown.Floremo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    private String[] mData = new String[0]; //수정할 것!!나중에 array list로 만들어서 item을 넣어야함.
    private LayoutInflater mInflater;


    // Data is passed into the constructor
    public RecordAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.record_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String animal = mData[position]; //수정할 것 !! data[] array에서 받아온다.
        holder.myTextView.setText(animal); // 수정할 것 !!

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder<ImgeView> extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.flowerImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }


    //Method that executes code for the action received
    private void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number" + getItem(position).toString()
        +", which is at cell position"+position);
    }

}
