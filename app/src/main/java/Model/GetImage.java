package Model;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
        try {
            URL url = new URL(HttpHelp.ip+imageUrl);
            HttpURLConnection httpURLConnection  = (HttpURLConnection) url.openConnection();
            InputStream is = httpURLConnection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            setResult(bitmap);
            Log.i("getimag",result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}