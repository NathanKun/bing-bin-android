package io.bingbin.bingbinandroid.views.mainActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.CommonUtil;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * EcoPointFragment.
 *
 * @author Junyang HE
 */
public class GetEcoPointFragment extends Fragment {

    @BindView(R.id.getecopoint_ecopoint_got_textview)
    TextView getecopointEcopointGotTextview;
    @BindView(R.id.getecopoint_share_btn)
    Button getecopointShareBtn;
    @BindView(R.id.getecopoint_myecopoint_btn)
    Button getecopointMyecopointBtn;
    @BindView(R.id.getecopoint_master_layout)
    ConstraintLayout getecopointMasterLayout;

    private MainActivity activity;
    private Unbinder unbinder;

    public GetEcoPointFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EcoPointFragment.
     */
    public static GetEcoPointFragment newInstance() {
        return new GetEcoPointFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;
    }

    @OnClick(R.id.getecopoint_share_btn)
    void shareOnClick(View view) {
        if(isStoragePermissionGranted()) {
            String shareSubject = "Trion mieux, vivons mieux - Bing Bin";
            String shareBody = "Bing Bin app reconnaît vos déchets et vous propose la poubelle appropriée. \n" +
                    "Le tri n'a jamais été si simple et amusant qu'avec Bing Bin. \n" +
                    "www.bingbin.io \n" +
                    "Suivez nous: http://www.facebook.com/bingbinsort/";

            String filepath = saveShareImage();

            Uri imageUri = Uri.fromFile(new File(filepath));
            Log.d("uri", imageUri.toString());

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            sharingIntent.setType("image/*");
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sharingIntent, "Partager"));
        }
    }

    @OnClick(R.id.getecopoint_myecopoint_btn)
    void myEcoPointOnClick(View view) {
        activity.viewPager.setCurrentItem(0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_getecopoint, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setEcoPoint(int ep) {
        // show eco point got
        getecopointMasterLayout.post(() -> getecopointEcopointGotTextview.setText(String.valueOf(ep)));
    }

    private String saveShareImage() {
        Bitmap shareImg = BitmapFactory.decodeResource(activity.getResources(), R.drawable.head_fr);
        final String dirpath = Environment.getExternalStorageDirectory() + File.separator + "BingBin";
        final String filepath = dirpath + File.separator + "BingBin.png";
        File dir = new File(dirpath);

        boolean doSave = true;
        if (!dir.exists()) {
            doSave = dir.mkdirs();
        }
        if (doSave) {
            CommonUtil.saveBitmapToFile(dir,"BingBin.png", shareImg, 100);
        }
        else {
            Log.e("Share","Couldn't create target directory.");
        }

        return filepath;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 22) {
            if (checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission","Permission is granted");
                return true;
            } else {
                Log.v("Permission","Permission is revoked");
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("Permission","Permission: " + permissions[0] + " was " + grantResults[0]);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            shareOnClick(null);
        }
    }
}
