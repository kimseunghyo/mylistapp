package com.example.mylistapp.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.mylistapp.R;
import com.example.mylistapp.db.DBHelper;
import com.example.mylistapp.dto.ItemDto;
import com.example.mylistapp.ui.checked.CheckedFragment;
import com.example.mylistapp.ui.product.ProductFragment;
import com.example.mylistapp.ui.productAdd.ProductAddFragment;
import com.example.mylistapp.ui.search.SearchFragment;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment {
    private EditText editText;
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

        dbHelper = new DBHelper(context);
        recyclerView = view.findViewById(R.id.itemList);

        adapter = new HomeRecyclerViewAdapter();
        adapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id, String item, String action) {
                if (action.equals("select")) {
                    ProductFragment productFragment = new ProductFragment();
                    //ProductAddFragment productFragment = new ProductAddFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt("listId", id);
                    bundle.putString("listItem", item);
                    productFragment.setArguments(bundle);

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.hide(HomeFragment.this);
                    transaction.add(R.id.mainFrame, productFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }

                else if (action.equals("check")) {
                    dbHelper.updateItemCheck(id);
                    getItems(adapter);

                    Fragment fragment01 = requireActivity().getSupportFragmentManager().findFragmentByTag("Checked");
                    if (fragment01 instanceof CheckedFragment) {
                        CheckedFragment checkedFragment = (CheckedFragment) fragment01;
                        checkedFragment.refresh();
                    }

                    Fragment fragment02 = requireActivity().getSupportFragmentManager().findFragmentByTag("Search");
                    if (fragment02 instanceof SearchFragment) {
                        SearchFragment searchFragment = (SearchFragment) fragment02;
                        searchFragment.refresh();
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);
        getItems(adapter);

        //itemAddBtn = (ImageButton) view.findViewById(R.id.itemSearchBtn);
        editText = (EditText) view.findViewById(R.id.editText);
        editText.addTextChangedListener(new MyTextWatcher());
        editText.setOnKeyListener(new MyKeyListener());
        editText.setOnTouchListener(new MyTouchListener());


        return view;
    }

    public class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editText.length() > 0 ) {
                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, R.drawable.baseline_close_24, 0);
                }
            else {
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0);
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

    public class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableRight = editText.getCompoundDrawables()[2];

                if(drawableRight != null && event.getRawX() >= (editText.getRight() - drawableRight.getBounds().width())) {
                    editText.setText("");
                    return true;
                }
            }
            return false;
        }
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
            cursor.close();
            adapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            Log.d(TAG, "DB Error");
        }
    }

    public void refresh() {
        getItems(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
    }
}