package Model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyHttp {

    URL url = null;
    public void Post(final JSONObject para, final Handler handler, final String urlString){

        //创建url对象
        try {
            if (urlString != null) {
                url = new URL(urlString);
            } else {
                url = new URL(HttpHelp.ip+"product");
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("创建url对象异常", e.toString());
        }

        //新建子线程 在子线程中访问服务器
        new Thread(){
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod(HttpHelp.RequestMethod_GET);
                    httpURLConnection.setConnectTimeout(HttpHelp.connectTimeout);
                    httpURLConnection.setReadTimeout(HttpHelp.readTimeout);
                    httpURLConnection.setDoInput(true);//下载
                    httpURLConnection.setDoOutput(true);//上传
                    if (para != null){
                        httpURLConnection.getOutputStream().write(para.toString().getBytes());
                    }
                    Log.i("子线程http请求前",httpURLConnection.toString());
                    httpURLConnection.connect();
                    if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        Log.i("http","请求成功");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                        StringBuilder stringBuilder = new StringBuilder();
                        String inputStr;
                        while ((inputStr = bufferedReader.readLine()) != null){
                            stringBuilder.append(inputStr);
                        }
                        Log.i("子线程收到数据转stringBuilder",stringBuilder.toString());
                        JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                        Log.i("收到的数据转JSONArray",jsonArray.toString());
                        Message responseJSON = new Message();
                        responseJSON.what = 1;
                        responseJSON.obj = jsonArray;
                        handler.sendMessage(responseJSON);
                        }else{
                        Log.i("http请求失败ResponseCode",httpURLConnection.getResponseCode()+"");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.i("子线程IO/JSON报异常",e.toString());
                }
            }
        }.start();
    }
    public void Post(final JSONObject para, final Handler handler){
        Post(para,handler,null);
    }
}
