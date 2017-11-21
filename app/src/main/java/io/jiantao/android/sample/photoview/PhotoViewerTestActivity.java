package io.jiantao.android.sample.photoview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import io.jiantao.android.sample.R;
import io.jiantao.android.uikit.photoviewer.PhotoViewerActivity;

/**
 * @author jiantao
 * @date 2017/11/15
 */

public class PhotoViewerTestActivity extends AppCompatActivity {
    String[] datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_just_recyclerview);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        MyAdapter adapter = new MyAdapter();

        recyclerView.setAdapter(adapter);
        this.datas = new String[]{
                "file:///android_asset/images/world.jpg",
                "file:///android_asset/images/vim.png",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631307274&di=d09126f1869f54776278fd5f0c9d3336&imgtype=0&src=http%3A%2F%2Fs8.sinaimg.cn%2Fmw690%2F002TonnOgy6Kt5kAENp57%26690",
                "https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D220/sign=96eb7ce240540923b569647ca259d1dc/50da81cb39dbb6fd493c67e70024ab18962b378f.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378213&di=2b99e488570aa1c30e0e24d7d081e155&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fdc54564e9258d10901b67673d858ccbf6d814d9e.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378212&di=10dcbff202ef94bfe5959c9655655fac&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0824ab18972bd40704fe413d72899e510fb30930.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378212&di=f5439ed208c3883d9d676c3df0ba819d&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F50da81cb39dbb6fd58e018e70024ab18962b37b3.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378209&di=7a5d753a7cb9aaae0205155157506907&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F279759ee3d6d55fb5cb87de664224f4a21a4ddf0.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378209&di=ffc2fbead7cd456b925c63b49371afa8&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd53f8794a4c27d1e2d39c40512d5ad6edcc438d0.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378208&di=4f8a392f7eace96a6138ad4e6d6d0859&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fcrop%253D0%252C0%252C1024%252C701%2Fsign%3Da9975128841001e95a734e4f853e57dc%2F91529822720e0cf3dd91563a0346f21fbf09aa88.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378208&di=7c081fa6bdf42086327bee6445c0fc12&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Ff3d3572c11dfa9ecbd268ad66bd0f703908fc1e3.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378207&di=fce21bae87ec6a140a3b99c862e8d1bb&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F242dd42a2834349b62a9b144c0ea15ce37d3be40.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507631378416&di=ae6bef8c8a2e2ce3fbd8ce982028c8ff&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fdcc451da81cb39dbbf279a97d9160924aa18300f.jpg",

                "http://pic13.nipic.com/20110307/2222821_094740634000_2.jpg",
                "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=95cb35f35ee736d14c1e844bf33925b7/2e2eb9389b504fc2f8a7cb2aefdde71190ef6d20.jpg",
                "http://img.taopic.com/uploads/allimg/130331/240460-13033106243430.jpg",
                "http://img1.3lian.com/2015/a1/84/d/95.jpg",
                "http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg",
                "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=16f6ff0030292df583cea456d4583615/e1fe9925bc315c60b6b051c087b1cb13495477f3.jpg",


        };
    }


    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        public MyAdapter() {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_imageview, null);
            RecyclerView.ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ImageView itemView = ((ViewHolder) holder).imageView;
            ((ViewHolder) holder).position = position;
            Glide.with(itemView.getContext())
                    .load(datas[position])
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .dontTransform()
//                    .placeholder(R.mipmap.ic_launcher_round)
//                    .crossFade()
                    .apply(new RequestOptions()
                            .override(360)
                            .placeholder(R.mipmap.ic_launcher))
                    .into(itemView);
        }

        @Override
        public int getItemCount() {
            return datas.length;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoViewerActivity.startPhotoViewerActivity(imageView.getContext(), datas, position);
                }
            });
        }

    }
}
