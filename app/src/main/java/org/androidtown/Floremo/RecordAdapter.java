package org.androidtown.Floremo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    //Item class를 arrayList에 담는다.
    private Context context;
    private ArrayList<Memo> mData;
    private OnMemoListener mOnMemoListener;

    // 데이터를 constructor에 집어넣는다.
    public RecordAdapter(ArrayList<Memo> mData, Context context, OnMemoListener onMemoListener) {
        this.mData = mData;
        this.context = context;
        this.mOnMemoListener = onMemoListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnMemoListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(mData.get(position).getFlowerImg())
                .into(holder.flowerImg);
        holder.flowerImg.setTag(position);


    }

    //위치 보내기
    public int sendPosition(@NonNull ViewHolder holder){
        return holder.getAdapterPosition();
    }

    @Override
    public int getItemCount(){
        //mData가 null이 아니면 size를 가져오고 null이면 0을 리턴
        return (mData != null ? mData.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView flowerImg;
        EditText editText;
        ImageView img;
        OnMemoListener onMemoListener;
        public ViewHolder(@NonNull View itemView, OnMemoListener onMemoListener) {
            super(itemView);
            this.flowerImg = itemView.findViewById(R.id.flowerImg);
            this.onMemoListener = onMemoListener;
//            this.editText = itemView.findViewById(R.id.content);
//            this.
            itemView.setOnClickListener(this);


            //롱클릭 이벤트, 길게 누르면 선책
//            flowerImg.setOnLongClickListener(new View.OnLongClickListener(){
//                @Override
//                //선택된 아이템을 지운다.
//                public boolean onLongClick(View v){
//                    int btnPosition = (int) v.getTag();
//                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
//                    alert.setTitle("아이템 삭제");
//                    String message = "기록을 삭제하시겠습니까?";
//                    alert.setMessage(message);
//                    alert.setPositiveButton("예", new DialogInterface.OnClickListener(){
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which){
//                            dialog.dismiss();
//                            mData.remove(btnPosition);
//                            notifyDataSetChanged();
//                        }
//                    });
//                    alert.show();
//                    return true;
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            onMemoListener.onFlowerClick(getAdapterPosition());
        }
    }
    public interface OnMemoListener{
        void onFlowerClick(int position);
    }
}
