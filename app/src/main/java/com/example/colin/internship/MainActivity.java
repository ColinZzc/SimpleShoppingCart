package com.example.colin.internship;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Model.HttpHelp;
import Model.MyAdapter;
import Model.MyHttp;
import Model.Products;

public class MainActivity extends AppCompatActivity implements MyAdapter.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProducts();

    }

    private void showProducts() {
        MyHttp myHttp = new MyHttp();
        Handler myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        JSONArray jsonArray = (JSONArray) msg.obj;
                        List<Products> list = new ArrayList<>();
                        Log.i("主线程接收handler",jsonArray.toString());
                        for (int i = 0; i< jsonArray.length();i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Products products = new Products();
                                products.setId(jsonObject.getInt(HttpHelp.id));
                                products.setPrice(jsonObject.getString(HttpHelp.price));
                                products.setTitle(jsonObject.getString(HttpHelp.title));
                                products.setImage(jsonObject.getString(HttpHelp.image));
                                list.add(products);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("jsonarray list",list.toString());
                            }
                        }
                        Log.i("主线程hander收到的数据",list.toString());
                        ListView listView = findViewById(R.id.myListView);
                        MyAdapter myAdapter = new MyAdapter(MainActivity.this,R.layout.single_item,list,MainActivity.this);
                        listView.setAdapter(myAdapter);
                        break;
                }
                return false;
            }
        });
        myHttp.Post(null,myHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_post:
                Intent intent = new Intent(MainActivity.this,ShoppingCart.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void click(View v) {
        Toast.makeText(MainActivity.this,R.string.toast_add_item_success,Toast.LENGTH_SHORT).show();
    }
}
