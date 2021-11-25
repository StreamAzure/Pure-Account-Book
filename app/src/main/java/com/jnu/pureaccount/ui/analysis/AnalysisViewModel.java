package com.jnu.pureaccount.ui.analysis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnalysisViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnalysisViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}