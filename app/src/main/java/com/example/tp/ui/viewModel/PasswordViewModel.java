package com.example.tp.ui.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tp.data.Password;
import com.example.tp.data.PasswordResponse;
import com.example.tp.network.ApiCall;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PasswordViewModel extends ViewModel {
    public static MutableLiveData<PasswordResponse> livePassword=new MutableLiveData<>();
    public static MutableLiveData<Boolean> liveError=new MutableLiveData<>();

    public void getPassword(Password password){
        ApiCall.getInstance()
                .getPassword(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PasswordResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PasswordResponse passwordResponse) {
                        if (passwordResponse!=null)
                            livePassword.setValue(passwordResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveError.setValue(true);
                        Log.e("PasswordViewModel Error",e.getMessage()+"");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
