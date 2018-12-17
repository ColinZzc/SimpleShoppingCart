package Model;


import android.content.Context;
import android.graphics.Bitmap;
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

import com.example.colin.internship.R;

import java.util.List;

public class CartAdapter extends ArrayAdapter {
    private LruCache<String, Bitmap> lruCache = Cache.getLruCache();
    private int resourceID;

    private Callback mCallBack;
    public interface Callback {
        void click(View v);
    }
    public CartAdapter(@NonNull Context context, int resource, @NonNull List objects, Callback callback) {
        super(context, resource, objects);
        resourceID = resource;
        mCallBack = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view ;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent,false);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.image = view.findViewById(R.id.image);
            viewHolder.button = view.findViewById(R.id.button);
            viewHolder.num = view.findViewById(R.id.num);
            viewHolder.bPlus = view.findViewById(R.id.but_pl);
            viewHolder.bMinus = view.findViewById(R.id.but_mi);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (CartAdapter.ViewHolder) view.getTag();
        }

        final Products product = (Products) getItem(position);
        viewHolder.title.setText(product.getTitle());
        viewHolder.price.setText(product.getPrice());
        viewHolder.num.setText(product.getNum()+"");
        viewHolder.num.setTag(product.getId());
        String imageURL = product.getImage();
        if (lruCache.get(imageURL) == null) {
            GetImage getImage = new GetImage();
            getImage.setImageUrl(imageURL);
            getImage.start();
            try {
                getImage.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = getImage.getResult();
            Log.i("主线程获取的bitmap", bitmap.toString());
            viewHolder.image.setImageBitmap(bitmap);
            lruCache.put(imageURL,bitmap);
        }else{
            Bitmap bitmap = lruCache.get(imageURL);
            viewHolder.image.setImageBitmap(bitmap);
        }

        viewHolder.bPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBDao.add(getContext(),new String[]{product.getId()+""},product.getId()+"",product.getTitle(),product.getPrice(),product.getImage());
                mCallBack.click(v);
            }
        });

        viewHolder.bMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getNum()>1){
                    DBDao.min(getContext(),new String[]{product.getId()+""});
                    mCallBack.click(v);
                }else{
                    Toast.makeText(getContext(),"不能再少啦～",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



    class ViewHolder{
        TextView title;
        TextView price;
        ImageView image;
        Button button;
        TextView num;
        Button bPlus;
        Button bMinus;
    }
}
