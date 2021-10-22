package io.jiantao.android.uikit.photoviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import io.jiantao.android.uikit.R;

/**
 * 大图浏览界面
 * 1. 支持超大图高清浏览
 * 2. 仿微信各种效果
 *
 * @author jiantao
 * @date 2017/11/14
 */

public class PhotoViewerActivity extends AppCompatActivity {
    private static final String EXTRA_POSITION = "photoviewer_extra_position";
    private static final String EXTRA_IMAGES = "photoviewer_extra_images";
    private static final String TAG = PhotoViewerActivity.class.getSimpleName();

    public static void startPhotoViewerActivity(Context context, String[] images, int position) {
        ArrayList<String> imageList;
        if (images != null) {
            imageList = new ArrayList<>(images.length);
            imageList.addAll(Arrays.asList(images));
        } else {
            imageList = null;
        }
        startPhotoViewerActivity(context, imageList, position);
    }

    public static void startPhotoViewerActivity(Context context, ArrayList<String> images, int position) {
        Intent intent = new Intent(context, PhotoViewerActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGES, images);
        intent.putExtra(EXTRA_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_viewpager);
        hideSystemUI();
        PhotoViewPager viewPager = findViewById(R.id.view_pager);
        setupViews(viewPager);
    }

    private void showSystemUI() {
        if (isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //change navigationbar bg color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setupViews(PhotoViewPager viewPager) {
        final ArrayList<String> images = getIntent().getStringArrayListExtra(EXTRA_IMAGES);
        if (images == null || images.isEmpty()) {
            Log.e(TAG, "images is null ");
            // TODO: 2017/11/15 show a error view
            return;
        }

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                final String image = images.get(i);
                return PhotoViewFragment.newInstance(image);
            }

            @Override
            public int getCount() {
                return images.size();
            }
        };

        final int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem((position >= images.size() || position <= 0) ? 0 : position);
    }

}
