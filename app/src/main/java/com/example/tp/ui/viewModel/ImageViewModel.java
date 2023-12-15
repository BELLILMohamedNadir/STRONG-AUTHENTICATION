package com.example.tp.ui.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tp.data.response.FaceRecognizeResponse;
import com.example.tp.data.user.UserEmail;
import com.example.tp.network.ApiCall;
import com.example.tp.object.URL;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ImageViewModel extends ViewModel {

    public static MutableLiveData<ResponseBody> liveEncryptedImage =new MutableLiveData<>();
    public static MutableLiveData<Response<JsonObject>> liveDecryptedImage =new MutableLiveData<>();
    public static MutableLiveData<Integer> liveFaceRecognition =new MutableLiveData<>();

    public void uploadEncryptedImage(MultipartBody.Part image, RequestBody text){
        ApiCall.getInstance()
                .encryptPhoto(image,text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody part) {
                        liveEncryptedImage.setValue(part);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void uploadDecryptedImage(MultipartBody.Part image){
        ApiCall.getInstance().decryptedPhoto(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<JsonObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<JsonObject> responseBody) {
                        if (responseBody!=null)
                            liveDecryptedImage.setValue(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("uploadDecryptedImage eroor ",e.getMessage()+"");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void faceRecognition(MultipartBody.Part image, RequestBody email){
        ApiCall.getInstance().faceRecognition(image,email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        if (responseBody!=null)
                            makeResponse(responseBody.code(), liveFaceRecognition);
                        else
                            liveFaceRecognition.setValue(URL.CODE_404);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("uploadUserPhoto error ",e.getMessage()+"");
                        liveFaceRecognition.setValue(URL.CODE_404);
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
