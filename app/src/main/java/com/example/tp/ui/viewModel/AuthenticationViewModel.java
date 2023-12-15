package com.example.tp.ui.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tp.data.AndroidID;
import com.example.tp.data.request.PublicKeyResponse;
import com.example.tp.data.User;
import com.example.tp.data.request.TwoFactorRequest;
import com.example.tp.data.user.UserEmail;
import com.example.tp.network.ApiCall;
import com.example.tp.object.URL;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AuthenticationViewModel extends ViewModel {

    public static MutableLiveData<String> livePublicKey =new MutableLiveData<>();
    public static MutableLiveData<Integer> liveAuthenticateUser =new MutableLiveData<>();
    public static MutableLiveData<Integer> liveRequestTwoFactorAuth =new MutableLiveData<>();
    public static MutableLiveData<Integer> liveResponseTwoFactorAuth =new MutableLiveData<>();
    public static MutableLiveData<Integer> liveCheckInfo =new MutableLiveData<>();

    // calling api with retrofit and rx java

    public void requestPublicKey(UserEmail email){
        ApiCall.getInstance()
                .requestPublicKey(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<PublicKeyResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<PublicKeyResponse> key) {

                        if (key != null && key.body() != null){
                            if (key.code() == URL.OK_200)
                                livePublicKey.setValue(key.body().getPublic_key());
                            else
                                livePublicKey.setValue(URL.CODE_423+"");
                        }else
                            livePublicKey.setValue(URL.CODE_404+"");
                    }

                    @Override
                    public void onError(Throwable e) {
                        livePublicKey.setValue(URL.CODE_404+"");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void authenticateUser(User user){
        ApiCall.getInstance()
                .authenticateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response!=null){
                            makeResponse(response.code(),liveAuthenticateUser);
                        }else{
                            liveAuthenticateUser.setValue(URL.CODE_404);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAuthenticateUser.setValue(URL.CODE_404);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void requestTwoFactorAuth(UserEmail email){
        ApiCall.getInstance()
                .requestTwoFactorAuthentication(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {

                        if (response != null)
                            makeResponse(response.code(), liveRequestTwoFactorAuth);
                        else
                            liveRequestTwoFactorAuth.setValue(URL.CODE_404);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "onError: "+e.getMessage());
                        liveRequestTwoFactorAuth.setValue(URL.CODE_404);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void responseTwoFactorAuth(TwoFactorRequest request){
        ApiCall.getInstance()
                .responseTwoFactorAuthentication(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response != null)
                            makeResponse(response.code(), liveResponseTwoFactorAuth);
                        else
                            liveResponseTwoFactorAuth.setValue(URL.CODE_404);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveResponseTwoFactorAuth.setValue(URL.CODE_404);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void checkInfo(AndroidID androidID){
        ApiCall.getInstance()
                .checkInfo(androidID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response != null)
                            makeResponse(response.code(), liveCheckInfo);
                        else
                            liveCheckInfo.setValue(URL.CODE_404);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveCheckInfo.setValue(URL.CODE_404);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void makeResponse(int code, MutableLiveData<Integer> liveData){
        switch (code){
            case URL.OK_200: liveData.setValue(URL.OK_200);
                break;
            case URL.CODE_404: liveData.setValue(URL.CODE_404);
                break;
            case URL.CODE_422: liveData.setValue(URL.CODE_422);
                break;
            case URL.CODE_401: liveData.setValue(URL.CODE_401);
                break;
            case URL.CODE_408: liveData.setValue(URL.CODE_408);
                break;
            case URL.CODE_423: liveData.setValue(URL.CODE_423);
                break;
            case URL.CODE_500: liveData.setValue(URL.CODE_500);
        }
    }

}

