package com.example.mylistapp.ui.productView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mylistapp.R;
import com.example.mylistapp.db.DBHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductViewFragment extends Fragment {
    private TextView listItemTxt;
    private TextInputEditText nameTxt;
    private ImageView prodImg;
    private TextInputEditText priceTxt;
    private TextInputEditText linkTxt;
    private TextInputEditText etcTxt;
    private String listItem;
    private int id;
    private DBHelper dbHelper;

    public ProductViewFragment() {
        // Required empty public constructor
    }

    public static ProductViewFragment newInstance(String param1, String param2) {
        ProductViewFragment fragment = new ProductViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            listItem = getArguments().getString("listItem");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_view, container, false);
        Context context = view.getContext();
        //Bundle args = getArguments();

        listItemTxt = (TextView) view.findViewById(R.id.listItemTxt);
        listItemTxt.setText(listItem);
        nameTxt = (TextInputEditText) view.findViewById(R.id.nameTxt);
        prodImg = (ImageView) view.findViewById(R.id.prodImg);
        priceTxt = (TextInputEditText) view.findViewById(R.id.priceTxt);
        linkTxt = (TextInputEditText) view.findViewById(R.id.linkTxt);
        etcTxt = (TextInputEditText) view.findViewById(R.id.etcTxt);

        dbHelper = new DBHelper(context);
        getSelectedProduct(id);

        return view;
    }

    public void getSelectedProduct(int id) {
        try {
            Cursor cursor = dbHelper.getProductView(id);

            while (cursor.moveToNext()) {
                int price = cursor.getInt(3);
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String result = decimalFormat.format(Double.valueOf(price));

                nameTxt.setText(cursor.getString(1));
                priceTxt.setText(result + " " + cursor.getString(4)); // , 처리
                linkTxt.setText(cursor.getString(5));
                etcTxt.setText(cursor.getString(6));

                Glide.with(prodImg.getContext())
                        .load(cursor.getString(2))
                        .error(R.drawable.baseline_broken_image_24)
                        .fallback(R.drawable.baseline_insert_photo_24)
                        .fitCenter()
                        .into(prodImg);
            }
            cursor.close();
        }
        catch (Exception e) {

        }
    }
}