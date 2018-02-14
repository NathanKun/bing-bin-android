package io.bingbin.bingbinandroid.views.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.bingbin.bingbinandroid.R;


/**
 * Fragment contains button to gallery and button to CameraActivity.
 * This fragment doesn't contain any business logic of camera.
 * The real activity of camera is CameraActivity
 *
 * @author Junyang HE
 */
public class CameraBlankFragment extends Fragment {
    private MainActivity activity;

    public CameraBlankFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CameraBlankFragment.
     */
    public static CameraBlankFragment newInstance() {
        return new CameraBlankFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getUserVisibleHint(). This value is by default true, so
        setUserVisibleHint(false);
        this.activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // button to start gallery
        Button btnGallery = activity.findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent, activity.GALLERY_PICTURE);
        });

        // button to start camera activity
        Button btnTakePhoto = activity.findViewById(R.id.btn_takephoto);
        btnTakePhoto.setOnClickListener(view -> {
            activity.startClassifyActivity(""); // no uri, so ClassifyActivity will start CameraActivity
        });
    }
}
