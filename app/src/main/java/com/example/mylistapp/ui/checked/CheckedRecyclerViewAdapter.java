package com.example.mylistapp.ui.checked;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mylistapp.databinding.FragmentCheckedBinding;
import com.example.mylistapp.dto.ItemDto;

import java.util.ArrayList;

public class CheckedRecyclerViewAdapter extends RecyclerView.Adapter<CheckedRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ItemDto> listData = new ArrayList<>();
    private CheckedRecyclerViewAdapter.OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(int pos, String action);
    }

    public void setOnItemClickListener(CheckedRecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public CheckedRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CheckedRecyclerViewAdapter.ViewHolder(FragmentCheckedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final CheckedRecyclerViewAdapter.ViewHolder holder, int position) {
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

        public ViewHolder(FragmentCheckedBinding binding) {
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
