package com.example.parking.ui.personal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonalViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PersonalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is personal fragment");
        //webview 하면 쉬울것
        //webView 활용하면 화면 디자인 쉬울거같긴한데 그 내가 실질적으로 활용하는 chatting 기능 있잖아
    }

    public LiveData<String> getText() {
        return mText;
    }
}
