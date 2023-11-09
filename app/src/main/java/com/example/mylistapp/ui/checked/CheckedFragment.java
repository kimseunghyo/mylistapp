package com.example.mylistapp.ui.checked;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylistapp.R;
import com.example.mylistapp.db.DBHelper;
import com.example.mylistapp.dto.ItemDto;
import com.example.mylistapp.ui.home.HomeFragment;
import com.example.mylistapp.ui.home.HomeRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 */
public class CheckedFragment extends Fragment {
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private CheckedRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CheckedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checked_list, container, false);
        Context context = view.getContext();
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.itemList);

        adapter = new CheckedRecyclerViewAdapter();
        adapter.setOnItemClickListener(new CheckedRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id, String action) {
                if (action.equals("select")) {
                    // 아이템 한 개를 선택했을 때
                }
                else if (action.equals("check")) {
                    dbHelper.updateItemCheck(id);
                    getItems(adapter);

                    Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("Home");
                    if (fragment instanceof HomeFragment) {
                        HomeFragment homeFragment = (HomeFragment) fragment;
                        homeFragment.refresh();
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);
        getItems(adapter);
        return view;
    }

    public void getItems(CheckedRecyclerViewAdapter adapter) {
        adapter.removeItems();

        try {
            Cursor cursor = dbHelper.getCheckedItemList();

            while (cursor.moveToNext()) {
                ItemDto itemDto = new ItemDto();
                itemDto.setId(cursor.getInt(0));
                itemDto.setItem(cursor.getString(1));

                if (cursor.getInt(2) == 1) {
                    itemDto.setChecked(true);
                }
                else {
                    itemDto.setChecked(false);
                }
                adapter.addItem(itemDto);
            }

            adapter.notifyDataSetChanged();
            dbHelper.close();
        }
        catch (Exception e) {
            Log.d(TAG, "DB Error");
        }
    }

    public void refresh() {
        getItems(adapter);
    }
}