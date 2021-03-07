package com.hyl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hyl.accountbook.R;
import com.hyl.bean.records;

import java.util.List;

public class PieChartRankAdapter extends RecyclerView.Adapter<PieChartRankAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<records> mDatas;

    public void setmDatas(List<records> mDatas) {
        this.mDatas = mDatas;
    }

    public PieChartRankAdapter(Context context, List<records> datas){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this. mDatas = datas;
    }


    @Override
    public int getItemCount() {
        return (mDatas== null) ? 0 : mDatas.size();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.piechart_rank_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.rank.setText(position+1+"");
        holder.account.setText(mDatas.get(position).getAccount() + "账户");
        holder.time.setText(mDatas.get(position).getDate());
        holder.member.setText(mDatas.get(position).getMember());
        holder.money.setText(mDatas.get(position).getAmount() + "");

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView account;
        private TextView time;
        private TextView member;
        private TextView money;
        private TextView rank;

        public ViewHolder(View view){
            super(view);

            account = (TextView) view.findViewById(R.id.pie_rankitem_account);
            time = (TextView) view.findViewById(R.id.pie_rankitem_time);
            member = (TextView) view.findViewById(R.id.pie_rankitem_member);
            money = (TextView) view.findViewById(R.id.pie_rankitem_money);
            rank = (TextView) view.findViewById(R.id.pie_rankitem_rank);

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }
        }
    }

}

