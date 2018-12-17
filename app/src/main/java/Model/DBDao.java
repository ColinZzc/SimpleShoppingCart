package Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBDao {
    public static void add(Context context, String[] para, String productID, String title, String price, String imageURL){
        DBHelp dbHelp = new DBHelp(context,"shopping.db",null,1);
        Cursor cursor = dbHelp.getWritableDatabase().query("shopping",null,"product_id = ?",para,null,null,null);
        ContentValues contentValues = new ContentValues();
        if(cursor.moveToFirst()){
            //存在相同商品
            Log.i("数据库","相同商品");
            contentValues.put("num", cursor.getInt(cursor.getColumnIndex("num")) + 1);
            dbHelp.getWritableDatabase().update("shopping", contentValues, "product_id = ? ",para);
        }else{
            contentValues.put("product_id", productID);
            contentValues.put("title", title);
            contentValues.put("price", price);
            contentValues.put("image", imageURL);
            contentValues.put("num", 1);
            dbHelp.getWritableDatabase().insert("shopping", null , contentValues);
            Log.i("数据库","新增商品");
        }
    }

    public static void min(Context context,String[] para){
        DBHelp dbHelp = new DBHelp(context,"shopping.db",null,1);
        Cursor cursor = dbHelp.getWritableDatabase().query("shopping",null,"product_id = ?",para,null,null,null);
        ContentValues contentValues = new ContentValues();
        if (cursor.moveToFirst()){
            Log.i("数据库","商品减1");
            contentValues.put("num",cursor.getInt(cursor.getColumnIndex("num"))-1);
            dbHelp.getWritableDatabase().update("shopping", contentValues, "product_id = ? ",para);
        }
    }

    public static void del(Context context,String[] para){
        DBHelp dbHelp = new DBHelp(context,"shopping.db",null,1);
//        dbHelp.getWritableDatabase().execSQL("delete * from shopping where product_id = "+para[0]);
        dbHelp.getWritableDatabase().delete("shopping","product_id = ? ",para);
    }

    public static List<Products> findAll(Context context){
        List<Products> list = new ArrayList<>();
        DBHelp dbHelp = new DBHelp(context,"shopping.db",null,1);
        Cursor cursor = dbHelp.getWritableDatabase().rawQuery("select * from shopping",null);
        while (cursor.moveToNext()){
            Products product = new Products();
            product.setId(cursor.getInt( cursor.getColumnIndex("product_id")));
            product.setImage(cursor.getString( cursor.getColumnIndex("image")));
            product.setTitle(cursor.getString( cursor.getColumnIndex("title")));
            product.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            product.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            list.add(product);
        }
        return list;
    }
}
