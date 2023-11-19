package com.example.mylistapp.ui.search;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mylistapp.databinding.FragmentSearchBinding;
import com.example.mylistapp.dto.ItemDto;

import java.util.ArrayList;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ItemDto> listData = new ArrayList<>();
    private SearchRecyclerViewAdapter.OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(int pos, String action);
    }

    public void setOnItemClickListener(SearchRecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SearchRecyclerViewAdapter.ViewHolder(FragmentSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final SearchRecyclerViewAdapter.ViewHolder holder, int position) {
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
        public TextView itemTxt;
        public ImageButton checkedBtn;

        public ViewHolder(FragmentSearchBinding binding) {
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