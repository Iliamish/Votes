package com.alva.vibory.Views.Mainveiw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alva.vibory.Classes.Globals;
import com.alva.vibory.Controllers.Controller;
import com.alva.vibory.Interface.MyInterface;
import com.alva.vibory.Models.Item;
import com.alva.vibory.R;
import com.alva.vibory.Views.Detailview.DetailActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements MyInterface{

    ListView listView;
    ItemListAdapter itemListAdapter;
    ArrayList<Item> itemsArray = new ArrayList<>();
    Controller controller = new Controller(this);
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("fssfs");

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("p");
            }
        }).start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        listView = (ListView)findViewById(R.id.lvSimple);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = itemsArray.get(i);
                Globals.getInstance().setItem(item);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DetailActivity.class);
                startActivity(intent);
            }
        });

        itemListAdapter = new ItemListAdapter( this);
        listView.setAdapter(itemListAdapter);

        final SwipeRefreshLayout refreshControl = findViewById(R.id.refreshControl);
        refreshControl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //updateItems();
                controller.updateItems(context);
                refreshControl.setRefreshing(false);
            }
        });



        controller.updateItems(this);



    }




    public void sendVoice(final Item item){
        controller.sendVoice(item);
    }

    @Override
    public void Update(ArrayList<Item> itemsArray) {
        this.itemsArray = itemsArray;
        ((TextView)findViewById(R.id.allvotes)).setText("Всего: " + Globals.getInstance().getTotal());
        itemListAdapter.setItemsArray(itemsArray);
        itemListAdapter.notifyDataSetChanged();

    }
}
