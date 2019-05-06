package io.jiantao.android.sample.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class BlankViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> mTestStringData;

    public BlankViewModel() {
        this.mTestStringData = new MutableLiveData<>();
        mTestStringData.setValue("default value.");
    }

    public LiveData<String> testString(){
        return mTestStringData;
    }

    public void updateData(){
        mTestStringData.setValue(" update Data");
    }
}
