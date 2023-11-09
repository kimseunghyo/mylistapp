package com.example.mylistapp.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mylistapp.databinding.FragmentHomeBinding;
import com.example.mylistapp.dto.ItemDto;

import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ItemDto> listData = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(int id, String action);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(ItemDto itemDto) {
        listData.add(itemDto);
    }

    public void removeItems() {
        listData.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemTxt;
        public ImageButton checkedBtn;

        public ViewHolder(FragmentHomeBinding binding) {
            super(binding.getRoot());
            this.itemTxt = binding.itemTxt;
            this.checkedBtn = binding.checkedBtn;

            itemTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    int id = listData.get(pos).getId();

                    if (id != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(id, "select");
                        }
                    }
                }
            });

            checkedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    int id = listData.get(pos).getId();

                    if (id != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(id, "check");
                        }
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemTxt.getText() + "'";
        }

        public void onBind(ItemDto itemDto) {
            itemTxt.setText(itemDto.getItem());

            if (itemDto.getChecked()) {
                checkedBtn.setColorFilter(Color.parseColor("#53EA41"));
            }
            else {
                checkedBtn.setColorFilter(Color.parseColor("#cccccc"));
            }
        }
    }
}