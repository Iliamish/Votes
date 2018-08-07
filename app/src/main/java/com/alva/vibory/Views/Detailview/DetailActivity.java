package com.alva.vibory.Views.Detailview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alva.vibory.Classes.Globals;
import com.alva.vibory.Classes.VolleySingleton;
import com.alva.vibory.Models.Item;
import com.alva.vibory.R;
import com.alva.vibory.Views.Mainveiw.MainActivity;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class DetailActivity extends Activity {
    Item item;

    static class ViewHolder {
        ImageView iconView;
        TextView tvsname;
        TextView tvname;
        TextView party;
        TextView description;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        item = Globals.getInstance().getItem();
        final Button myBtn = (Button)findViewById(R.id.exit);
        final Intent intent = new Intent(this, MainActivity.class);

        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.description = findViewById(R.id.description3);
        viewHolder.iconView = findViewById(R.id.image3);
        viewHolder.party = findViewById(R.id.party3);
        viewHolder.tvname = findViewById(R.id.fname3);
        viewHolder.tvsname = findViewById(R.id.sname3);

        viewHolder.tvsname.setText(item.getSecondname());
        viewHolder.tvname.setText(item.getFirstname() + " " + item.getThirdname());
        viewHolder.description.setText(item.getDescriptions());
        viewHolder.party.setText("Партийность: " + item.getParty());

        String url = "http://adlibtech.ru/elections/upload_images/"+item.getImagelink() ;
        ImageLoader imageLoader = VolleySingleton.getInstance(this).getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {

                    viewHolder.iconView.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        };
        myBtn.setOnClickListener(oclBtnOk);
    }
}
