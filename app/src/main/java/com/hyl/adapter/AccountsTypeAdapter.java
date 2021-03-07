package com.hyl.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hyl.accountbook.CreateAccountsActivity;
import com.hyl.accountbook.R;
import com.hyl.bean.Accounts;
import com.hyl.bean.AccountsType;
import com.hyl.bean.records;
import com.hyl.stickyheader.StickyHeaderGridAdapter;
import com.hyl.view.SwipeMenuView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AccountsTypeAdapter extends StickyHeaderGridAdapter {
    private Context mContext;
    private List<AccountsType> mAccountsType;

    public void setmAccountsType(List<AccountsType> AccountsType) {
        this.mAccountsType = AccountsType;
    }

    public AccountsTypeAdapter(Context context, List<AccountsType> AccountsType) {
        this.mContext = context;
        this. mAccountsType = AccountsType;
    }


    @Override
    public int getSectionCount() {
        return mAccountsType == null ? 0 : mAccountsType.size();
    }

    @Override
    public int getSectionItemCount(int section) {
        return (mAccountsType == null || mAccountsType.get(section).getAccountsList() == null) ? 0 : mAccountsType.get(section).getAccountsList().size();
    }

    @Override
    public StickyHeaderGridAdapter.HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.accounts_header, parent, false);
        return new MyHeaderViewHolder(view);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.accouts_item, parent, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int section) {
        final MyHeaderViewHolder holder = (MyHeaderViewHolder)viewHolder;
        holder.header_type.setText(mAccountsType.get(section).getType() + "账户");
        holder.header_balance.setText("资产：" + mAccountsType.get(section).getBalance() + "元");
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, final int section, final int position) {
        final MyItemViewHolder holder = (MyItemViewHolder)viewHolder;
        final String label = mAccountsType.get(section).getAccountsList().get(position).getName();
        holder.item_name.setText(label);
        Glide.with(mContext).load(mAccountsType.get(section).getAccountsList().get(position).getImageId()).into(holder.item_image);
        holder.item_balance.setText("余额：" + mAccountsType.get(section).getAccountsList().get(position).getBalance() + "元");
        // 侧滑删除
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AccountsTypeAdapter", "delete!");

                final int section = getAdapterPositionSection(holder.getAdapterPosition());
                final int offset = getItemSectionOffset(section, holder.getAdapterPosition());
                Log.d("AccountsTypeAdapter", "delete " + label);
                Snackbar.make(v, "是否删除账户 " + label + " 的所有数据？删除后将不可撤回", Snackbar.LENGTH_LONG)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DataSupport.deleteAll(records.class, "account = ?", label);
                                DataSupport.deleteAll(Accounts.class, "name = ?", label);
                                mAccountsType.get(section).getAccountsList().remove(offset);
                                notifySectionItemRemoved(section, offset);
                                Toast.makeText(holder.item_delete.getContext(), "账户 "+label + " 已删除！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        // 侧滑修改
        holder.item_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int section = getAdapterPositionSection(holder.getAdapterPosition());
                final int offset = getItemSectionOffset(section, holder.getAdapterPosition());
                Intent intent = new Intent(mContext, CreateAccountsActivity.class);
                intent.putExtra("name", label);
                mContext.startActivity(intent);
            }
        });
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.item_layout.getContext(), "点击--"+label, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class MyHeaderViewHolder extends HeaderViewHolder {
        TextView header_type;
        TextView header_balance;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            header_type = (TextView) itemView.findViewById(R.id.accounts_header_type);
            header_balance = (TextView) itemView.findViewById(R.id.accounts_header_balance);
        }
    }

    public static class MyItemViewHolder extends ItemViewHolder {
        ImageView item_image;
        TextView item_name;
        TextView item_balance;
        Button item_delete;
        Button item_change;
        RelativeLayout item_layout;
        SwipeMenuView mSwipeMenuView;

        MyItemViewHolder(View itemView) {
            super(itemView);
            item_image = (ImageView) itemView.findViewById(R.id.accounts_image);
            item_name = (TextView) itemView.findViewById(R.id.accounts_name);
            item_delete = (Button) itemView.findViewById(R.id.accounts_delete);
            item_change = (Button) itemView.findViewById(R.id.accounts_change);
            item_balance = (TextView) itemView.findViewById(R.id.accounts_balance);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.accounts_item_layout);
            mSwipeMenuView = (SwipeMenuView) itemView.findViewById(R.id.swipe_menu);
        }
    }

}

