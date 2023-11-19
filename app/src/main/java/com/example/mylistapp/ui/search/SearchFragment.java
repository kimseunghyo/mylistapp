package com.example.mylistapp.ui.search;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.mylistapp.R;
import com.example.mylistapp.db.DBHelper;
import com.example.mylistapp.dto.ItemDto;
import com.example.mylistapp.ui.checked.CheckedFragment;
import com.example.mylistapp.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A fragment representing a list of Items.
 */
public class SearchFragment extends Fragment {
    private EditText editText;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private SearchRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchFragment newInstance(int columnCount) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        Context context = view.getContext();
        dbHelper = new DBHelper(context);
        recyclerView = view.findViewById(R.id.itemList);

        adapter = new SearchRecyclerViewAdapter();
        adapter.setOnItemClickListener(new SearchRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id, String action) {
                if (action.equals("select")) {
                }
                else if (action.equals("check")) {
                    dbHelper.updateItemCheck(id);
                    getItems(adapter, String.valueOf(editText.getText()));

                    Fragment fragment01 = requireActivity().getSupportFragmentManager().findFragmentByTag("Home");
                    if (fragment01 instanceof HomeFragment) {
                        HomeFragment homeFragment = (HomeFragment) fragment01;
                        homeFragment.refresh();
                    }

                    Fragment fragment02 = requireActivity().getSupportFragmentManager().findFragmentByTag("Checked");
                    if (fragment02 instanceof CheckedFragment) {
                        CheckedFragment checkedFragment = (CheckedFragment) fragment02;
                        checkedFragment.refresh();
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);
        getItems(adapter, "");

        editText = (EditText) view.findViewById(R.id.editText);
        editText.addTextChangedListener(new MyTextWatcher());
        editText.setOnTouchListener(new MyTouchListener());

        return view;
    }

    public class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getItems(adapter, String.valueOf(editText.getText()));

            if (editText.length() > 0 ) {
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_search_24, 0, R.drawable.baseline_close_24, 0);
            }
            else {
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_search_24, 0, 0, 0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableRight = editText.getCompoundDrawables()[2];

                if(drawableRight != null && event.getRawX() >= (editText.getRight() - drawableRight.getBounds().width())) {
                    editText.getText().clear();
                    return true;
                }
            }
            return false;
        }
    }

    public void getItems(SearchRecyclerViewAdapter adapter, String word) {
        adapter.removeItems();

        try {
            Cursor cursor = dbHelper.getSearchItemList(word);

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
            cursor.close();
            adapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            Log.d(TAG, "DB Error");
        }
    }

    public void refresh() {
        getItems(adapter, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
    }
}