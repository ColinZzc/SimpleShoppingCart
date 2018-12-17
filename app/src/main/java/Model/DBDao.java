package Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBDao {
    public static void add(Context context, String[] para, String productID, String title, String price, String imageURL){
        DBHelp dbHelp = new DBHelp(context,DBString.DBName,null,1);
        Cursor cursor = dbHelp.getWritableDatabase().query(DBString.tableName,null,DBString.product_id+" = ?",para,null,null,null);
        ContentValues contentValues = new ContentValues();
        if(cursor.moveToFirst()){
            //存在相同商品
            Log.i("数据库","相同商品");
            contentValues.put(DBString.num, cursor.getInt(cursor.getColumnIndex(DBString.num)) + 1);
            dbHelp.getWritableDatabase().update(DBString.tableName, contentValues, DBString.product_id+" = ? ",para);
        }else{
            contentValues.put(DBString.product_id, productID);
            contentValues.put(DBString.title, title);
            contentValues.put(DBString.price, price);
            contentValues.put(DBString.imageURL, imageURL);
            contentValues.put(DBString.num, 1);
            dbHelp.getWritableDatabase().insert(DBString.tableName, null , contentValues);
            Log.i("数据库","新增商品");
        }
    }

    public static void min(Context context,String[] para){
        DBHelp dbHelp = new DBHelp(context,DBString.DBName,null,1);
        Cursor cursor = dbHelp.getWritableDatabase().query(DBString.tableName,null,DBString.product_id+" = ?",para,null,null,null);
        ContentValues contentValues = new ContentValues();
        if (cursor.moveToFirst()){
            Log.i("数据库","商品减1");
            contentValues.put(DBString.num,cursor.getInt(cursor.getColumnIndex(DBString.num))-1);
            dbHelp.getWritableDatabase().update(DBString.tableName, contentValues, DBString.product_id+" = ? ",para);
        }
    }

    public static void del(Context context,String[] para){
        DBHelp dbHelp = new DBHelp(context,DBString.DBName,null,1);
        dbHelp.getWritableDatabase().delete(DBString.tableName,DBString.product_id+" = ? ",para);
    }

    public static List<Products> findAll(Context context){
        List<Products> list = new ArrayList<>();
        DBHelp dbHelp = new DBHelp(context,DBString.DBName,null,1);
        Cursor cursor = dbHelp.getWritableDatabase().rawQuery("select * from "+DBString.tableName,null);
        while (cursor.moveToNext()){
            Products product = new Products();
            product.setId(cursor.getInt( cursor.getColumnIndex(DBString.product_id)));
            product.setImage(cursor.getString( cursor.getColumnIndex(DBString.imageURL)));
            product.setTitle(cursor.getString( cursor.getColumnIndex(DBString.title)));
            product.setPrice(cursor.getString(cursor.getColumnIndex(DBString.price)));
            product.setNum(cursor.getInt(cursor.getColumnIndex(DBString.num)));
            list.add(product);
        }
        return list;
    }
}
