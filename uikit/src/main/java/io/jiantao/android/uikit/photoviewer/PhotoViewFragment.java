package io.jiantao.android.uikit.photoviewer;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

import io.jiantao.android.uikit.R;

/**
 * @author jiantao
 * @date 2017/11/14.
 */
public class PhotoViewFragment extends Fragment implements DragPhotoView.OnPhotoViewActionListener{

    private static final String ARGUMENTS_IMAGE = "argumens-image";
    private ProgressBar mProgressBar;
    private DragPhotoView mImageView;

    private DragPhotoView.OnPhotoViewActionListener dismissListener;
    public static PhotoViewFragment newInstance(String imageUrl) {
        PhotoViewFragment rawImageViewerFragment = new PhotoViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENTS_IMAGE, imageUrl);
        rawImageViewerFragment.setArguments(bundle);
        return rawImageViewerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_raw_imageview_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = view.findViewById(R.id.siv_raw_imageview);
        mImageView.setDismissListener(this);
        mImageView.setDoubleTapZoomScale(2.0f);
        mImageView.setMaxScale(3.0f);

        mImageView.setOnImageEventListener(new SubsamplingScaleImageView.DefaultOnImageEventListener() {
            @Override
            public void onImageLoaded() {
                super.onImageLoaded();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onImageLoadError(Exception e) {
                super.onImageLoadError(e);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mProgressBar = view.findViewById(R.id.progressbar);
        loadThumb();
        loadImageView();
    }


    private void loadImageView() {
        final String url = getArguments().getString(ARGUMENTS_IMAGE);
        Glide.with(this).downloadOnly().load(url)
                /* todo replace error icon */
                .apply(new RequestOptions().error(R.mipmap.qq_refresh_success))
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        mImageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });

    }

    private void loadThumb() {
        // TODO: 2017/11/14
    }

    @Override
    public void onDismiss() {
        if(getActivity() != null){
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        onDismiss();
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(v.getContext(), "onLongClicked", Toast.LENGTH_SHORT).show();
        return true;
    }
}
