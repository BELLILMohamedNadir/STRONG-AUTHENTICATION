package com.example.tp.ui.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tp.data.encryption.AffineEncryption;
import com.example.tp.data.encryption.CaesarEncryption;

import com.example.tp.data.encryption.MirrorEncryption;
import com.example.tp.data.encryption.RotationEncryption;
import com.example.tp.network.ApiCall;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageViewModel1 extends ViewModel {

    public static MutableLiveData<String> liveDecryption =new MutableLiveData<>();
    public static MutableLiveData<String> liveAffineError =new MutableLiveData<>();

    public void getCaesarDecryption(CaesarEncryption message){
        ApiCall.getInstance()
                .getCaesarDecryption(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String decryption) {
                        if (decryption!=null)
                            liveDecryption.setValue(decryption);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getCaesarDecryption error ",e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getRotationDecryption(RotationEncryption message){
        ApiCall.getInstance()
                .getRotationDecryption(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String decryption) {
                        if (decryption!=null)
                            liveDecryption.setValue(decryption);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getMirrorDecryption(MirrorEncryption message){
        ApiCall.getInstance()
                .getMirrorDecryption(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String decryption) {
                        if (decryption!=null)
                            liveDecryption.setValue(decryption);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getMirrorDecryption error",e.getMessage()+"");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getAffineDecryption(AffineEncryption message){
        ApiCall.getInstance()
                .getAffineDecryption(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String decryption) {
                        if (decryption!=null)
                            liveDecryption.setValue(decryption);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAffineError.setValue(e.getMessage()+"");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
