package io.bingbin.bingbinandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import io.bingbin.bingbinandroid.tensorflow.Classifier;
import io.bingbin.bingbinandroid.utils.ClassifyHelper;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private final int GALLERY_PICTURE = 233;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        Button btnGallery = getActivity().findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_PICTURE);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if (columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor

            Log.d("picturePath", picturePath);

            File imgFile = new File(picturePath);
            ((ImageView) getActivity().findViewById(R.id.imageView)).setImageBitmap(BitmapFactory.decodeFile(picturePath));

            List<Classifier.Recognition> results = ClassifyHelper.Classify(getActivity(), imgFile);
            String resultStr = "";
            for(Classifier.Recognition r : results) {
                resultStr += r.getTitle() + " : " + r.getConfidence();
            }
        }
    }

}
