package org.androidtown.Floremo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private ArrayList<Item> mData;

    // Data is passed into the constructor
    public RecordAdapter(ArrayList<Item> mData) {
            this.mData = mData;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView flowerimage;
        //ImageButton del;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerimage = itemView.findViewById(R.id.flowerImg);
            //del = itemView.findViewById(R.id.btnDelete);
        }

        public void onBind(Item item){
            //이미지 생성. 수정해야함!!!
           //flowerimage.(item.getInstance().getImage());
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.onBind(mData.get(position));

       //롱클릭 이벤트
       //길게 누르면 아이템 삭제.
       holder.flowerimage.setTag(position);
       holder.flowerimage.setOnLongClickListener(new View.OnLongClickListener(){
           @Override
           //데이터가 삭제되면 아이템을 지운다.
           public boolean onLongClick(View v){
               int btnPosition = (int) v.getTag();
               AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
               alert.setTitle("아이템 삭제");
               String message = "기록을 삭제하시겠습니까?";
               alert.setMessage(message);
               alert.setPositiveButton("예", new DialogInterface.OnClickListener(){

                   @Override
                   public void onClick(DialogInterface dialog, int which){
                       dialog.dismiss();
                       mData.remove(btnPosition);
                       notifyDataSetChanged();
                   }
               });
               alert.show();
               return true;
           }
       });

    }

    @Override
    public long getItemId(int position){return position;}

    @Override
    public int getItemCount(){return mData.size();}

    public void addItem(Item item){
        mData.add(item);
        notifyDataSetChanged();
    }
}
