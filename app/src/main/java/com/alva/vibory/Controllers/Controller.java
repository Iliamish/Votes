package com.alva.vibory.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.alva.vibory.Classes.Globals;
import com.alva.vibory.Interface.MyInterface;
import com.alva.vibory.Models.Item;
import com.alva.vibory.R;
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

public class Controller {

    public Controller(MyInterface myInterface) {
        this.myInterface = myInterface;
    }

    ArrayList<Item> itemsArray = new ArrayList<>();
    MyInterface myInterface;
    Context cxt;

    public void updateItems(Context context) {
        cxt = context;

        String url = "http://adlibtech.ru/elections/api/getcandidates.php";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        itemsLoadingComplete(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("device_id","101010012");
                params.put("device_name", "Ilya");

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void itemsLoadingComplete(String response) {
        itemsArray = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            Globals.getInstance().setTotal(0);

            JSONObject jsonObject0 = jsonArray.getJSONObject(0);

            for (int i = 0; i < jsonArray.length()-2; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Item item = new Item();
                item.setImagelink(jsonObject.get("image").toString());
                item.setFirstname(jsonObject.getString("firstname"));
                item.setSecondname(jsonObject.getString("secondname"));
                item.setDescriptions(jsonObject.getString("description"));
                item.setThirdname(jsonObject.getString("thirdname"));
                item.setParty(jsonObject.getString("party"));
                item.setVotes(jsonObject.getString("votes"));
                item.setId(jsonObject.getString("id"));
                Globals.getInstance().setTotal(Globals.getInstance().getTotal() + Integer.valueOf(jsonObject.getString("votes")));

                itemsArray.add(item);


            }



        } catch (JSONException e) {
            e.printStackTrace();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.get("result").toString().equalsIgnoreCase("error")) {

                    return;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }


        myInterface.Update(itemsArray);

    }

    public void sendVoice(final Item item){

        RequestQueue queue = Volley.newRequestQueue(cxt);
        String url ="http://adlibtech.ru/elections/api/addvote.php";
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        updateItems(cxt);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("device_id", "101");
                params.put("device_name", "Ilya");
                params.put("candidate_id", item.getId());
                if (Globals.getInstance().getPrevcand() != -1)
                    params.put("last_id",String.valueOf( Globals.getInstance().getPrevcand()));
                return params;
            }
        };
        queue.add(strRequest);



    }
}
