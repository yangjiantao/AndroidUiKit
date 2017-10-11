package io.jiantao.android.uikit.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/10/10.
 */
public class BundleHelper {

    private final Bundle b;

    public BundleHelper() {
        this(new Bundle());
    }

    public BundleHelper(Bundle src) {
        this.b = src;
    }

    public BundleHelper putInt(String key, int val){
        b.putInt(key,val);
        return this;
    }
    public BundleHelper putString(String key,String val){
        b.putString(key, val);
        return this;
    }
    public BundleHelper putFloat(String key,float val){
        b.putFloat(key, val);
        return this;
    }
    public BundleHelper putLong(String key,long val){
        b.putLong(key, val);
        return this;
    }
    public BundleHelper putDouble(String key,double val){
        b.putDouble(key, val);
        return this;
    }

    public BundleHelper putSerializable(String key,Serializable data){
        b.putSerializable(key, data);
        return this;
    }
    public BundleHelper putParcelable(String key,Parcelable data){
        b.putParcelable(key, data);
        return this;
    }
    public <T extends Parcelable> BundleHelper putParcelableArrayList(String key, ArrayList<T> data){
        b.putParcelableArrayList(key, data);
        return this;
    }
    public <T extends Parcelable> BundleHelper putParcelableList(String key, List<T> data){
        b.putParcelableArrayList(key, new ArrayList<Parcelable>(data));
        return this;
    }

    public BundleHelper putParcelableArray(String key, Parcelable[] array){
        b.putParcelableArray(key, array);
        return this;
    }

    public Bundle getBundle(){
        return b;
    }

    public <T extends Fragment>T into(T fragment){
        fragment.setArguments(getBundle());
        return fragment;
    }
    public <T extends android.app.Fragment>T into(T fragment){
        fragment.setArguments(getBundle());
        return fragment;
    }

    public BundleHelper putStringArrayList(String key,List<String> imageUrls) {
        b.putStringArrayList(key, imageUrls != null && imageUrls.size() > 0
                ? new ArrayList<>(imageUrls) : null);
        return this;
    }

    public BundleHelper putBoolean(String key , boolean value) {
        b.putBoolean(key ,value);
        return this;
    }
    public BundleHelper putAll(Bundle b) {
        this.b.putAll(b);
        return this;
    }
}
