package com.example.colin.internship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import Model.CartAdapter;
import Model.CartAdapter.Callback;
import Model.DBDao;
import Model.Products;

public class ShoppingCart extends AppCompatActivity implements Callback {

    ListView cart;
    CartAdapter cartAdapter;
    List<Products> products;
    TextView allNum;
    TextView allPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.black_back);
        }

        allNum = findViewById(R.id.allnum);
        allPrice = findViewById(R.id.allprice);
        cart = findViewById(R.id.cartListView);
        products = DBDao.findAll(ShoppingCart.this);
        cartAdapter = new CartAdapter(ShoppingCart.this,R.layout.single_cart_item,products,ShoppingCart.this);
        cart.setAdapter(cartAdapter);
        cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDelDialog(position);

            }
        });
        count();
    }

    private void count() {
        int countNum = 0;
        double countPrice = 0;
        for (Products p : products) {
            countNum += p.getNum();
            double n = Double.valueOf(p.getPrice().substring(1,p.getPrice().length()));
            countPrice += p.getNum()*n;
        }
        allNum.setText(countNum+"");
        allPrice.setText(countPrice+"元");
    }

    private void showDelDialog(final int position) {
        AlertDialog.Builder delDia = new AlertDialog.Builder(ShoppingCart.this);
        delDia.setTitle("确定要将此宝贝从购物车中丢弃嘛～");
        delDia.setMessage(products.get(position).getTitle());
        delDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBDao.del(ShoppingCart.this,new String[]{products.get(position).getId()+""});
                products.remove(position);
                cartAdapter.notifyDataSetChanged();
                count();
            }
        });
        delDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        delDia.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ShoppingCart.this.finish();
                break;
        }
        return true;
    }

    @Override
    public void click(View v) {
        products.clear();
        products.addAll(DBDao.findAll(ShoppingCart.this));
        cartAdapter.notifyDataSetChanged();
        count();
    }
}
