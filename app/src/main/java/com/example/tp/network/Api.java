package com.example.tp.network;

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
import com.google.gson.JsonObject;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface Api {
    @POST("tp1/text_decryption/caesar")
    Observable<String> getCaesarDecryption(@Body CaesarEncryption message);

    @POST("tp1/text_decryption/rotation")
    Observable<String> getRotationDecryption(@Body RotationEncryption message);

    @POST("tp1/text_decryption/mirror")
    Observable<String> getMirrorDecryption(@Body MirrorEncryption message);

    @POST("tp1/text_decryption/affine")
    Observable<String> getAffineDecryption(@Body AffineEncryption message);

    @POST("tp1/password_attack")
    Observable<PasswordResponse> getPassword(@Body Password password);

    @Multipart
    @POST("tp1/image_steganography/encryption")
    Observable<ResponseBody> encryptPhoto(@Part MultipartBody.Part image, @Part("text") RequestBody requestBody);

    @Multipart
    @POST("tp1/image_steganography/decryption")
    Observable<Response<JsonObject>> decryptedPhoto(@Part MultipartBody.Part image);

    @Multipart
    @POST("tp3/face-recognition-factor/")
    Observable<Response<ResponseBody>> faceRecognition(@Part MultipartBody.Part image, @Part("email") RequestBody requestBody);

    @POST("tp3/get-rsa-key/")
    Observable<Response<PublicKeyResponse>> requestPublicKey(@Body UserEmail email);

    @POST("tp3/login/")
    Observable<Response<ResponseBody>> authenticateUser(@Body User user);

    @POST("tp3/email-two-factor-authentication/")
    Observable<Response<ResponseBody>> requestTwoFactorAuthentication(@Body UserEmail email);

    @POST("tp3/verify-email-two-factor-authentication/")
    Observable<Response<ResponseBody>> responseTwoFactorAuthentication(@Body TwoFactorRequest request);

    @POST("/tp3/android-id/")
    Observable<Response<ResponseBody>> checkInfo(@Body AndroidID androidID);

}
