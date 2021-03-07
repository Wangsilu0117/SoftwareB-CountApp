package com.hyl.accountbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OptionsDialog extends BottomSheetDialogFragment {

    TextView confirmButton;//确认按钮
    TextView cancelButton;//取消按钮
    TextView title;//标题

    List<Item> primaryItems = new ArrayList<>();
    HashMap<Integer, List<Item>> allItems = new HashMap<>();
    List<Item> secondaryItems = new ArrayList<>();
    RecyclerView primaryList;//一级列表
    RecyclerView secondaryList;//二级列表
    PrimaryAdapter primaryAdapter;//一级适配器
    SecondaryAdapter secondaryAdapter;//二级适配器


    public interface OnPrimaryItemSelectedListener {
        void OnItemSelected(int primaryId, int position);
    }

    public interface OnAddClickListener {
        void OnAddClick(View v);
    }

    public interface OnConfirmListener {
        void OnConfirm(String primaryText, String secondaryText);//////////////////////////////////////////////
        //void OnConfirm(int primaryId, int secondaryId);
    }

    @NonNull
    OnPrimaryItemSelectedListener innerOnPrimarySelectedListener;
    OnPrimaryItemSelectedListener onPrimarySelectedListener;
    OnAddClickListener onAddClickListener;
    OnConfirmListener onConfirmListener;

    //深色色调
    int colorDeep = Color.parseColor("#29B6F6");
    //浅色色调
    int colorLight = Color.parseColor("#E1F5FE");

    String titleText = "选择";

    /**
     * 设置加号点击事件
     */
    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    /**
     * 设置一级菜单点击事件
     */
    public void setOnPrimarySelectedListener(OnPrimaryItemSelectedListener onPrimarySelectedListener) {
        this.onPrimarySelectedListener = onPrimarySelectedListener;
    }

    /**
     * 设置确定事件
     */
    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    /**
     * 设置标题
     */
    public void setTitle(String titleText) {
        this.titleText = titleText;
    }

    public void addPrimaryItem(int id, int iconId, String text) {
        primaryItems.add(new Item(id, iconId, text));
    }

    public void addSecondaryItem(int parentId, int id, int iconId, String text) {
        if (!allItems.containsKey(parentId)) {
            allItems.put(parentId, new ArrayList<>());
        }
        allItems.get(parentId).add(new Item(id, iconId, text));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.options_dialog, container, false);
        initViews(v);
        return v;
    }


    private void initViews(View v) {
        title = v.findViewById(R.id.title);
        title.setText(titleText);
        primaryList = v.findViewById(R.id.primary_list);
        primaryAdapter = new PrimaryAdapter();
        primaryList.setAdapter(primaryAdapter);
        primaryList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        secondaryList = v.findViewById(R.id.secondary_list);
        secondaryAdapter = new SecondaryAdapter();
        secondaryList.setAdapter(secondaryAdapter);
        secondaryList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        secondaryList.setBackgroundColor(colorLight);

        innerOnPrimarySelectedListener = (primaryId, position) -> {
            int size = secondaryItems.size();
            secondaryItems.clear();
            secondaryAdapter.notifyItemRangeRemoved(0,size);
            if(allItems.get(primaryId)!=null){
                secondaryItems.addAll(allItems.get(primaryId));
            }
            secondaryAdapter.setSelectedIndex(0);
            secondaryAdapter.notifyItemRangeInserted(0,secondaryItems.size());
        };

        confirmButton = v.findViewById(R.id.confirm);
        cancelButton = v.findViewById(R.id.cancel);
        confirmButton.setOnClickListener(view -> {
            if (onConfirmListener != null) {
                onConfirmListener.OnConfirm(primaryAdapter.selectedText, secondaryAdapter.selectedText);
                dismiss();//////////点击确认后可直接退出
            }
        });
        confirmButton.setTextColor(colorDeep);
        cancelButton.setOnClickListener(view -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (primaryItems.size() > 0) {
            primaryAdapter.setSelectedIndex(0);
        }
    }

    static class Item {
        int id;//项目id
        int iconId;//图片id
        String text;

        public Item(int id, int iconId, String text) {
            this.id = id;
            this.iconId = iconId;
            this.text = text;
        }

        public int getIconId() {
            return iconId;
        }

        public String getText() {
            return text;
        }

        public int getId() {
            return id;
        }

    }

    class PrimaryAdapter extends RecyclerView.Adapter<PrimaryAdapter.PHolder> {

        private static final int TYPE_ITEM = 411;
        private static final int TYPE_ADD = 495;
        int selectedIndex;
        int selectedId;
        String selectedText;


        @NonNull
        @Override
        public PHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int layoutId = R.layout.options_primary_item;
            if (viewType == TYPE_ADD) {
                layoutId = R.layout.options_primary_item_add;
            }
            return new PHolder(getLayoutInflater().inflate(layoutId, parent, false), viewType);
        }


        public void setSelectedIndex(int index) {
            this.selectedIndex = index;
            selectedId = primaryItems.get(index).getId();
            selectedText = primaryItems.get(index).getText();//////////////////////////////
            innerOnPrimarySelectedListener.OnItemSelected(selectedId, selectedIndex);
        }

        @Override
        public void onBindViewHolder(@NonNull PHolder holder, int position) {
            if (holder.type != TYPE_ADD) {
                Item primaryItem = primaryItems.get(position);
                holder.setChecked(position == selectedIndex);
                holder.icon.setImageResource(primaryItem.getIconId());
                holder.text.setText(primaryItem.getText());
                holder.card.setOnClickListener(view -> {
                    if (selectedId != primaryItem.getId()) {//未被选中
                        int oldSelected = selectedIndex;//原来被选中的那位
                        selectedIndex = position;
                        selectedId = primaryItem.getId();
                        selectedText = primaryItem.getText();///////////////////////////////////
                        notifyItemChanged(oldSelected);
                        holder.setChecked(true);
                        innerOnPrimarySelectedListener.OnItemSelected(selectedId, position);
                        if(onPrimarySelectedListener !=null){
                            onPrimarySelectedListener.OnItemSelected(selectedId,position);
                        }
                    }

                });
            } else { //是”添加“按钮
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onAddClickListener != null) {
                            onAddClickListener.OnAddClick(view);
                        }
                    }
                });
            }

        }

        @Override
        public int getItemViewType(int position) {
            if (position == primaryItems.size()) return TYPE_ADD;
            else return TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return primaryItems.size() + 1;
        }

        class PHolder extends RecyclerView.ViewHolder {
            int type;
            TextView text;
            ImageView icon;
            CardView card;
            ImageView rect;//小三角形

            public PHolder(@NonNull View itemView, int type) {
                super(itemView);
                text = itemView.findViewById(R.id.text);
                icon = itemView.findViewById(R.id.icon);
                card = itemView.findViewById(R.id.card);
                rect = itemView.findViewById(R.id.rect);
                this.type = type;
            }

            public void setChecked(boolean checked) {
                if (type != TYPE_ADD) {
                    if (checked) { //被选中
                        card.setCardBackgroundColor(colorDeep);
                        icon.setColorFilter(Color.WHITE);
                        text.setTextColor(colorDeep);
                        rect.setVisibility(View.VISIBLE);
                        rect.setColorFilter(colorLight);
                    } else {
                        rect.setVisibility(View.INVISIBLE);
                        card.setCardBackgroundColor(colorLight);
                        icon.setColorFilter(colorDeep);
                        text.setTextColor(Color.parseColor("#ADADAD"));
                    }
                }

            }
        }
    }

    class SecondaryAdapter extends RecyclerView.Adapter<SecondaryAdapter.SHolder> {

        int selectedIndex;
        int selectedId;
        String selectedText;

        @NonNull
        @Override
        public SHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SHolder(getLayoutInflater().inflate(R.layout.options_secondary_item, parent, false));
        }
        public void setSelectedIndex(int index) {
            this.selectedIndex = index;
            if(index<secondaryItems.size()){
                selectedId = secondaryItems.get(index).getId();
                selectedText = secondaryItems.get(index).getText();/////////////////////////////////////
            }

        }
        @Override
        public void onBindViewHolder(@NonNull SHolder holder, int position) {
            Item Item = secondaryItems.get(position);
            holder.setChecked(position == selectedIndex);
            holder.icon.setImageResource(Item.getIconId());
            holder.text.setText(Item.getText());
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedId != Item.getId()) {//未被选中
                        int oldSelected = selectedIndex;//原来被选中的那位
                        selectedIndex = position;
                        selectedId = Item.getId();
                        selectedText = Item.getText();///////////////////////////////////
                        notifyItemChanged(oldSelected);
                        holder.setChecked(true);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return secondaryItems.size();
        }

        class SHolder extends RecyclerView.ViewHolder {
            TextView text;
            ImageView icon;
            CardView card;

            public SHolder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(R.id.text);
                icon = itemView.findViewById(R.id.icon);
                card = itemView.findViewById(R.id.card);
            }

            public void setChecked(boolean checked) {
                if (checked) { //被选中
                    card.setCardBackgroundColor(colorDeep);
                    text.setTextColor(Color.WHITE);
                } else {
                    card.setCardBackgroundColor(Color.WHITE);
                    text.setTextColor(Color.GRAY);
                }
            }
        }
    }
}
