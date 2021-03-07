package com.hyl.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hyl.accountbook.R;
import com.hyl.bean.records;
import com.hyl.util.BarChartUtil;
import com.hyl.view.CircleImageView;

import java.util.List;

public class BarChartRankAdapter extends RecyclerView.Adapter<BarChartRankAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<records> mDatas;
    private Integer colorIn;
    private Integer colorOut;

    public void setmDatas(List<records> mDatas) {
        this.mDatas = mDatas;
    }

    public BarChartRankAdapter(Context context, List<records> datas, Integer colorIn, Integer colorOut){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.colorIn = colorIn;
        this.colorOut = colorOut;
    }


    @Override
    public int getItemCount() {
        return (mDatas== null) ? 0 : mDatas.size();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.barchart_rank_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.category.setText(mDatas.get(position).getCatagory1() + "-" + mDatas.get(position).getCatagory2());
        holder.time.setText(mDatas.get(position).getDate());
        holder.money.setText(mDatas.get(position).getAmount() + "");
        holder.circleBg.setImageDrawable(new ColorDrawable(mDatas.get(position).getType() == 0 ? colorIn : colorOut));
        if (!mDatas.get(position).getCatagory2().equals("")) {
            holder.circleImg.setImageDrawable(BarChartUtil.getTypeDrawable(mDatas.get(position).getCatagory2()));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView category;
        private TextView time;
        private TextView money;
        private CircleImageView circleBg;
        private ImageView circleImg;

        public ViewHolder(View view){
            super(view);

            category = (TextView) view.findViewById(R.id.bar_rankitem_category);
            time = (TextView) view.findViewById(R.id.bar_rankitem_time);
            money = (TextView) view.findViewById(R.id.bar_rankitem_money);
            circleBg = (CircleImageView) view.findViewById(R.id.bar_rankitem_circle_bg);
            circleImg = (ImageView) view.findViewById(R.id.bar_rankitem_circle_img);

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }
        }
    }

}

