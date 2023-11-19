package com.example.mylistapp.ui.productAdd;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mylistapp.R;
import com.example.mylistapp.db.DBHelper;
import com.example.mylistapp.dto.ProductDto;
import com.example.mylistapp.ui.product.ProductFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductAddFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText nameTxt;
    private ImageView prodImg;
    private TextInputEditText priceTxt;
    private RadioGroup priceBtnGroup;
    private RadioButton priceBtn;
    private TextInputEditText linkTxt;
    private TextInputEditText etcTxt;
    private Button addBtn;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result = "";
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private DBHelper dbHelper;
    private int listId;
    private String listItem;
    private TextView listItemTxt;
    private Uri uri;

    public ProductAddFragment() {
        // Required empty public constructor
    }

    public static ProductAddFragment newInstance(String param1, String param2) {
        ProductAddFragment fragment = new ProductAddFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_add, container, false);
        Context context = view.getContext();
        Bundle args = getArguments();

        dbHelper = new DBHelper(context);

        if (args != null) {
            listId = args.getInt("listId");
            listItem = args.getString("listItem");
            listItemTxt = view.findViewById(R.id.listItemTxt);
            listItemTxt.setText(listItem);
        }

        nameTxt = (TextInputEditText) view.findViewById(R.id.nameTxt);
        prodImg = (ImageView) view.findViewById(R.id.prodImg);
        priceTxt = (TextInputEditText) view.findViewById(R.id.priceTxt);
        priceBtnGroup = (RadioGroup) view.findViewById(R.id.priceBtnGroup);
        linkTxt = (TextInputEditText) view.findViewById(R.id.linkTxt);
        etcTxt = (TextInputEditText) view.findViewById(R.id.etcTxt);
        addBtn = (Button) view.findViewById(R.id.addBtn);

        priceTxt.addTextChangedListener(new MyTextWatcher());
        prodImg.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            uri = intent.getData();

                            if (uri != null) {
                                //productImg.setImageURI(uri);
                                Glide.with(view.getContext()).load(uri).fitCenter().into(prodImg); // .centerCrop() .thumbnail(0.1f)
                                prodImg.setAdjustViewBounds(true);
                            }
                        }
                    }
                }
        );

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.prodImg) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.setAction(Intent.ACTION_PICK);
            activityResultLauncher.launch(intent);

//            Glide.with(v.getContext())
//                    .load("https://upload.wikimedia.org/wikipedia/en/thumb/f/f2/Chiikawa_volume_1_cover.jpg/220px-Chiikawa_volume_1_cover.jpg")
//                    .fitCenter()
//                    .into(prodImg);
        }

        if (v.getId() == R.id.addBtn) {
            String name = Objects.toString(nameTxt.getText(), null);
            String image = Objects.toString(uri, null); // productImg
            String link = Objects.toString(linkTxt.getText(), null);
            String etc = Objects.toString(etcTxt.getText(), null);

            String priceStr = Objects.toString(priceTxt.getText(), null).replace(",", "");
            int price;
            if (priceStr != null && !priceStr.isEmpty()) {
                price = Integer.parseInt(priceStr);
            }
            else {
                price = 0;
            }

            int selectedId = priceBtnGroup.getCheckedRadioButtonId();
            priceBtn =  getActivity().findViewById(selectedId);
            String priceUnit = Objects.toString(priceBtn.getText(), null);

            dbHelper.insertProduct(name, image, price, priceUnit, link, etc, listId);

            ProductFragment productFragment = new ProductFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("listId", listId);
            bundle.putString("listItem", listItem);
            productFragment.setArguments(bundle);

            //requireActivity().getSupportFragmentManager().popBackStack();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFrame, productFragment);
            //transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)) {
                result = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                priceTxt.setText(result);
                priceTxt.setSelection(result.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
    }
}
