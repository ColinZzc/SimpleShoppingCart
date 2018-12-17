package Model;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;

public class GetImage extends Thread {
    public void setResult(Bitmap result) {
        this.result = result;
    }

    public Bitmap getResult() {
        return result;
    }

    private Bitmap result;

    private String imageUrl;
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public void run() {
        //http请求
        try {
            URL url = new URL("http://172.20.10.2:8080/shopping/"+imageUrl);
            HttpURLConnection httpURLConnection  = (HttpURLConnection) url.openConnection();
            InputStream is = httpURLConnection.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(is, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuffer result = new StringBuffer();
//            String temp ;
//            while((temp = bufferedReader.readLine()) != null){
//                result.append(temp);
//            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            setResult(bitmap);
            Log.i("getimag",result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}