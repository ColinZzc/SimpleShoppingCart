package Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colin.internship.MainActivity;
import com.example.colin.internship.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.security.auth.callback.Callback;

public class MyAdapter extends ArrayAdapter  {
    LruCache<String, Bitmap> lruCache = Cache.getLruCache();
    private int resourceID;

    Callback mCallBack;
    public interface Callback {
        public void click(View v);
    }

    public MyAdapter(@NonNull Context context, int resource, @NonNull List objects,Callback callback) {
        super(context, resource, objects);
        resourceID = resource;
        mCallBack = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view ;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent,false);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.image = view.findViewById(R.id.image);
            viewHolder.button = view.findViewById(R.id.button);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        final Products products = (Products) getItem(position);

//        viewHolder.button.setTag(products.getId());

        Log.i("imageURL",products.getImage());
        viewHolder.title.setText(products.getTitle());
        viewHolder.price.setText(products.getPrice());
        //加载图片
        if (lruCache.get(products.getImage()) == null) {
            GetImage getImage = new GetImage();
            getImage.setImageUrl(products.getImage());
            getImage.start();
            try {
                getImage.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = getImage.getResult();
            Log.i("主线程获取的bitmap", bitmap.toString());
            viewHolder.image.setImageBitmap(bitmap);
            lruCache.put(products.getImage(),bitmap);
        }else{
            Bitmap bitmap = lruCache.get(products.getImage());
            viewHolder.image.setImageBitmap(bitmap);
        }

//        viewHolder.button.setOnClickListener(this);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Touched products",products.toString());
                DBDao.add(getContext(),new String[]{products.getId()+""},products.getId()+"",products.getTitle(),products.getPrice(),products.getImage());
                mCallBack.click(v);
            }
        });
        return view;
    }



    class ViewHolder{
        TextView title;
        TextView price;
        ImageView image;
        Button button;
    }

}
