package com.hyl.adapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

//import com.wenjue.simpleaccount_v2.R;
//import com.wenjue.simpleaccount_v2.records;


import com.bumptech.glide.Glide;
import com.hyl.accountbook.CreateAccountsActivity;
import com.hyl.accountbook.R;
import com.hyl.bean.Accounts;
import com.hyl.bean.records;
import com.hyl.stickyheader.StickyHeaderGridAdapter;
import com.hyl.view.CircleImageView;
import com.hyl.view.SwipeMenuView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<records> mRecordList;
    private SwipeMenuView mListView;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View recordView;
        ImageView recordImage;
        TextView recordCatagory1;
        TextView recordCatagory2;
        TextView recordItem;
        TextView recordPrice;
        TextView recordYear;
        TextView recordMonth;
        TextView recordDay;
        TextView recordHour;
        TextView recordMin;
        private CircleImageView recordcircleBg;
        int recordID;
        Button recordDelete;

        public ViewHolder(View view) {
            super(view);
            recordView = view;
            recordImage = (ImageView) view.findViewById(R.id.record_image);
            recordItem = (TextView) view.findViewById(R.id.record_item);
            recordPrice = (TextView) view.findViewById(R.id.record_price);
            recordCatagory1 = (TextView) view.findViewById(R.id.record_catagory1);
            recordCatagory2 = (TextView) view.findViewById(R.id.record_catagory2);
            recordYear = (TextView) view.findViewById(R.id.record_year);
            recordMonth = (TextView) view.findViewById(R.id.record_month);
            recordDay = (TextView) view.findViewById(R.id.record_day);
            recordHour = (TextView) view.findViewById(R.id.record_hour);
            recordMin = (TextView) view.findViewById(R.id.record_min);
            recordDelete = (Button) view.findViewById(R.id.record_delete);
            recordcircleBg = (CircleImageView) view.findViewById(R.id.record_image_bg);

//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (mOnLongItemClickListener != null) {
//                        mOnLongItemClickListener.onLongItemClick(v, getAdapterPosition());
//                        Log.d(TAG, "onLongClick: longclick");
//                    }
//                    return true;
//                }
//            });
        }
    }

    public MyAdapter(List<records> fruitList) {
        mRecordList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.recordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                records record = mRecordList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + record.getItem(), Toast.LENGTH_SHORT).show();
                Toast.makeText(v.getContext(), "the id is " + record.getId(), Toast.LENGTH_SHORT).show();
            }
        });
//        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Fruit fruit = mFruitList.get(position);
//                Toast.makeText(v.getContext(), "you clicked image " + fruit.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        return holder;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        records record = mRecordList.get(position);
//        DecimalFormat df = new DecimalFormat( "0.00 ");
//        holder.fruitImage.setImageResource(fruit.getImageId());
        if(record.getType() != 2){//如果不是转账：
            holder.recordCatagory1.setText(record.getCatagory1()+"  ");
            holder.recordCatagory2.setText(record.getCatagory2()+"  ");
            holder.recordItem.setText(record.getItem()+"  ");

        }else{
            holder.recordCatagory1.setText(record.getAccount()+"转账至");
            holder.recordCatagory2.setText(record.getTransfer()+"  ");
        }

        if(record.getType() == 0){//如果是收入
            holder.recordPrice.setText("+"+record.getAmount());//注意要将getAmount返回的int转成string,保留两位小数
        }else{
            holder.recordPrice.setText("-"+record.getAmount());//注意要将getAmount返回的int转成string,保留两位小数
        }
//        holder.recordPrice.setText(record.getAmount()+"");//注意要将getAmount返回的int转成string
        holder.recordYear.setText(record.getDate().substring(0,4) + "年");
        holder.recordMonth.setText(record.getDate().substring(5,7)+ "月");
        holder.recordDay.setText(record.getDate().substring(8)+ "日");
        holder.recordImage.setImageResource(record.getImageId());
        holder.recordID = record.getId();

        if(record.getType()==0){//收入
            holder.recordImage.setColorFilter(Color.GREEN);
        }else if(record.getType()==1){//支出
            holder.recordImage.setColorFilter(Color.RED);
        }else{
            holder.recordImage.setImageResource(R.mipmap.accounts_fuzhai);
        }

        holder.recordDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                DataSupport.deleteAll(records.class, "id=?", holder.recordID+"");
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }





//    public interface OnRecyclerViewLongItemClickListener {
//        void onLongItemClick(View view, int position);
//    }

}
