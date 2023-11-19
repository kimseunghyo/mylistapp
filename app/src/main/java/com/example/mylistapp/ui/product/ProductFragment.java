package com.example.mylistapp.ui.product;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylistapp.R;
import com.example.mylistapp.db.DBHelper;
import com.example.mylistapp.dto.ProductDto;
import com.example.mylistapp.ui.home.HomeFragment;
import com.example.mylistapp.ui.productAdd.ProductAddFragment;
import com.example.mylistapp.ui.productView.ProductViewFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A fragment representing a list of Items.
 */
public class ProductFragment extends Fragment {
    private TextView listItemTxt;
    private FloatingActionButton addFloBtn;
    private int listId;
    private String listItem;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private ProductRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductFragment newInstance(int columnCount) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        Context context = view.getContext();
        Bundle args = getArguments();

        if (args != null) {
            listId = args.getInt("listId");
            listItem = args.getString("listItem");
            listItemTxt = view.findViewById(R.id.listItemTxt);
            listItemTxt.setText(listItem);
        }

        dbHelper = new DBHelper(context);
        recyclerView = view.findViewById(R.id.prodList);

        adapter = new ProductRecyclerViewAdapter();

        adapter.setOnItemClickListener(new ProductRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                ProductViewFragment productViewFragment = new ProductViewFragment();

                Bundle bundle = new Bundle();
                bundle.putString("listItem", listItem);
                bundle.putInt("id", id);
                productViewFragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.hide(ProductFragment.this);
                transaction.add(R.id.mainFrame, productViewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        recyclerView.setAdapter(adapter);
        getProducts(adapter);

        addFloBtn = view.findViewById(R.id.addFloBtn);
        addFloBtn.setOnClickListener(new MyClickListener());

        return view;
    }

    public class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ProductAddFragment productAddFragment = new ProductAddFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("listId", listId);
            bundle.putString("listItem", listItem);
            productAddFragment.setArguments(bundle);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.hide(ProductFragment.this);
            transaction.add(R.id.mainFrame, productAddFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void getProducts(ProductRecyclerViewAdapter adapter) {
        adapter.removeProducts();

        try {
            Cursor cursor = dbHelper.getProductList(listId);

            while (cursor.moveToNext()) {
                ProductDto prodDto = new ProductDto();
                prodDto.setId(cursor.getInt(0));
                prodDto.setName(cursor.getString(1));
                prodDto.setImage(cursor.getString(2));
                prodDto.setPrice(cursor.getInt(3));
                prodDto.setPriceUnit(cursor.getString(4));
                prodDto.setLink(cursor.getString(5));
                prodDto.setEtc(cursor.getString(6));

                adapter.addProduct(prodDto);
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            Log.d(TAG, "DB Error");
        }
    }

    public void refresh() {
        getProducts(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        Log.println(Log.ASSERT, "resume", "resume success");
    }
}