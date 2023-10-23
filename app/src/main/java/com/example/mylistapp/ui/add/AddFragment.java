package com.example.mylistapp.ui.add;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.GenericLifecycleObserver;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mylistapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText productTxt;
    private ImageView productImg;
    private TextInputEditText priceTxt;
    private TextInputEditText linkTxt;
    private TextInputEditText etcTxt;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result = "";
    private ActivityResultLauncher<Intent> activityResultLauncher;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        productTxt = (TextInputEditText) view.findViewById(R.id.productTxt);
        productImg = (ImageView) view.findViewById(R.id.productImg);
        priceTxt = (TextInputEditText) view.findViewById(R.id.priceTxt);
        linkTxt = (TextInputEditText) view.findViewById(R.id.linkTxt);
        etcTxt = (TextInputEditText) view.findViewById(R.id.etcTxt);

        priceTxt.addTextChangedListener(new TxtWatcher());
        productImg.setOnClickListener(this);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            Uri uri = intent.getData();

                            if (uri != null) {
                                productImg.setImageURI(uri);
                                productImg.setAdjustViewBounds(true);
                            }
                        }
                    }
                }
        );

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        // 핸드폰 갤러리에 있는 사진 가져오게 하기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_PICK);
        activityResultLauncher.launch(intent);
    }

    private class TxtWatcher implements TextWatcher {

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
}
