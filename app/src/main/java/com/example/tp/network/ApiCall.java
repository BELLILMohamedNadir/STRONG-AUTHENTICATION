package com.example.tp.network;

import android.util.Log;

import com.example.tp.data.AndroidID;
import com.example.tp.data.Password;
import com.example.tp.data.PasswordResponse;
import com.example.tp.data.User;
import com.example.tp.data.encryption.AffineEncryption;
import com.example.tp.data.encryption.CaesarEncryption;
import com.example.tp.data.encryption.MirrorEncryption;
import com.example.tp.data.encryption.RotationEncryption;
import com.example.tp.data.request.TwoFactorRequest;
import com.example.tp.data.request.PublicKeyResponse;
import com.example.tp.data.response.FaceRecognizeResponse;
import com.example.tp.data.user.UserEmail;
import com.example.tp.object.URL;
import com.example.tp.ui.viewModel.MessageViewModel1;
import com.google.gson.JsonObject;
//import com.example.tp.ui.viewModel.MessageViewModel1;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    private static volatile ApiCall INSTANCE = null;
    Api api;

    public ApiCall() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Adjust the timeout as needed
                .readTimeout(30, TimeUnit.SECONDS)    // Adjust the timeout as needed
                .writeTimeout(30, TimeUnit.SECONDS)   // Adjust the timeout as needed
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        api = retrofit.create(Api.class);
    }

    public static ApiCall getInstance() {
        if (INSTANCE == null) {
            synchronized (MessageViewModel1.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApiCall();
                }
            }
        }
        return INSTANCE;
    }
    public Observable<String> getCaesarDecryption(CaesarEncryption message){
        return api.getCaesarDecryption(message);
    }
    public Observable<String> getRotationDecryption(RotationEncryption message){
        return api.getRotationDecryption(message);
    }
    public Observable<String> getMirrorDecryption(MirrorEncryption message){
        return api.getMirrorDecryption(message);
    }
    public Observable<String> getAffineDecryption(AffineEncryption message){
        return api.getAffineDecryption(message);
    }
    public Observable<PasswordResponse> getPassword(Password password){
        return api.getPassword(password);
    }

    public Observable<Response<PublicKeyResponse>> requestPublicKey(UserEmail email){
        return api.requestPublicKey(email);
    }

    public Observable<Response<ResponseBody>> authenticateUser(User user){
        return api.authenticateUser(user);
    }
    public Observable<Response<ResponseBody>> requestTwoFactorAuthentication(UserEmail email){
        return api.requestTwoFactorAuthentication(email);
    }
    public Observable<Response<ResponseBody>> responseTwoFactorAuthentication(TwoFactorRequest request){
        return api.responseTwoFactorAuthentication(request);
    }

    public Observable<Response<ResponseBody>> checkInfo(AndroidID androidID){
        return api.checkInfo(androidID);
    }

    public Observable<ResponseBody> encryptPhoto(MultipartBody.Part image, RequestBody text){return api.encryptPhoto(image,text);}

    public Observable<Response<JsonObject>> decryptedPhoto(MultipartBody.Part image){return api.decryptedPhoto(image);}

    public Observable<Response<ResponseBody>> faceRecognition(MultipartBody.Part image, RequestBody email){
        return api.faceRecognition(image,email);
    }
}