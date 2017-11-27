package io.bingbin.bingbinandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A blank fragment contains an icon of camera. only for viewpager swiping.
 * This fragment doesn't contain any business logic of camera.
 * The real activity of camera is CameraActivity
 *
 * @author Junyang HE
 */
public class CameraBlankFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }
}
