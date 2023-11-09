package com.example.mylistapp.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.example.mylistapp.R;
import com.example.mylistapp.db.DBHelper;
import com.example.mylistapp.dto.ItemDto;
import com.example.mylistapp.ui.checked.CheckedFragment;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment {
    private EditText editText;
    private ImageButton itemAddBtn;
    private ImageButton checkedBtn;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private HomeRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);
        Context context = view.getContext();
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.itemList);

        adapter = new HomeRecyclerViewAdapter();
        adapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id, String action) {
                if (action.equals("select")) {
                }
                else if (action.equals("check")) {
                    dbHelper.updateItemCheck(id);
                    getItems(adapter);

                    Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("Checked");
                    if (fragment instanceof CheckedFragment) {
                        CheckedFragment checkedFragment = (CheckedFragment) fragment;
                        checkedFragment.refresh();
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);
        getItems(adapter);

        itemAddBtn = (ImageButton) view.findViewById(R.id.itemAddBtn);
        editText = (EditText) view.findViewById(R.id.editText);
        editText.addTextChangedListener(new MyTextWatcher());
        editText.setOnKeyListener(new MyKeyListener());

        return view;
    }

    public void getItems(HomeRecyclerViewAdapter adapter) {
        adapter.removeItems();

        try {
            Cursor cursor = dbHelper.getItemList();

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

    public class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editText.length() > 0 ) {
                    //itemAddBtn.setVisibility(View.VISIBLE);
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_close_24, 0);
                }
                else {
                    //itemAddBtn.setVisibility(View.INVISIBLE);
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class MyKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && (editText.length() > 0)) {
                String item = editText.getText().toString();
                dbHelper.insertItem(item);
                editText.setText("");
                getItems(adapter);
                return true;
            }
            return false;
        }
    }

    public void refresh() {
        getItems(adapter);
    }
}