package com.saiyu.foreground.https;


import android.util.Log;

import com.saiyu.foreground.https.response.RetData;
import com.saiyu.foreground.https.response.RetSecret;
import com.saiyu.foreground.https.utils.AESUtils;
import com.saiyu.foreground.https.utils.CJsonSecret;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class RequestEncryptInterceptor implements Interceptor {

//    public String sKey = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String sKey = "";
        try {
            sKey = CJsonSecret.getKey(request.url().toString());
            Log.i("解密", "url======================" + request.url().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = request.body();

        if (null != body) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                //add by wangsk 表单文件不能往下走
                if (contentType.toString().startsWith("multipart/")) {
                    return chain.proceed(request);
                }//end
                charset = contentType.charset(charset);
            }
            String paramsStr = buffer.readString(charset);
            try {
                paramsStr = AESUtils.Encrypt(paramsStr, sKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramsStr);
            request = request.newBuilder().post(requestBody).build();
        }

        Response response = chain.proceed(request);
        response = decrypt(response, sKey);
        return response;
    }

    private Response decrypt(Response response, String sKey) throws IOException {
        if (response.isSuccessful()) { //the response data
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String strResult = buffer.clone().readString(charset);
            RetSecret retSecret = RetSecret.parse(strResult);
            if (retSecret != null && retSecret.isNeedDecrypt()) {
//            KLog.i("解密", "sKey======================" + requestEncryptInterceptor.sKey);
                String data = AESUtils.Decrypt(retSecret.data, sKey);
                strResult = RetData.toJson(200, retSecret.getMsg(), data);
//                KLog.i("解密", "解密后=" + strResult);
            }

            ResponseBody responseBody = ResponseBody.create(contentType, strResult);
            response = response.newBuilder().body(responseBody).build();
            Log.i("解密", "解密后=" + strResult);
        }
        return response;
    }

}
