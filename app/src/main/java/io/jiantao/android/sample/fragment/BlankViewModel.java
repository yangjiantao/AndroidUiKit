package io.jiantao.android.sample.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
