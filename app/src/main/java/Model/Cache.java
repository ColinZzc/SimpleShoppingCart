package Model;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class Cache {
    static LruCache<String, Bitmap> lruCache;
    static Boolean init = false;

    public static LruCache<String, Bitmap> getLruCache() {

        if (lruCache == null){
            long maxMemory = Runtime.getRuntime().maxMemory();
            int cacheSize = (int) (maxMemory / 8);
            lruCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }

        return lruCache;
    }
}
