package com.alva.vibory.Views.Detailview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alva.vibory.Classes.FileUtils;
import com.alva.vibory.Classes.Globals;
import com.alva.vibory.Classes.VolleySingleton;
import com.alva.vibory.Models.Item;
import com.alva.vibory.R;
import com.alva.vibory.Views.Mainveiw.ItemListAdapter;
import com.alva.vibory.Views.Mainveiw.MainActivity;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;

public class DetailActivity extends Activity {
    Item item;
    Context context = this;

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

        new CheckImage(viewHolder.iconView).execute(item.getImagelink());

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        };
        myBtn.setOnClickListener(oclBtnOk);
    }

    private class Thread extends AsyncTask<Object, Void, Bitmap> {


        ImageView iconView;

        public Thread( ImageView  img) {
            iconView = img;
        }


        @Override
        protected Bitmap doInBackground(Object... objects) {

            FileUtils.saveImageFile((Bitmap)objects[0], (String)objects[1], context);
            return (Bitmap) objects[0];
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
            System.out.println("as");
        }
    }

    private class CheckImage extends AsyncTask<Object,Void,Uri>{
        ImageView iconView;

        public CheckImage(ImageView iconView) {
            this.iconView = iconView;
        }

        @Override
        protected Uri doInBackground(Object... objects) {
            if (FileUtils.checkImageFileExist((String)objects[0], context)) {
                File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File imagePath = new File(directory, (String)objects[0]);

                return Uri.fromFile(imagePath);
            } else {
                downloadImage(iconView, (String)objects[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            iconView.setImageURI(uri);
        }
    }

    private void downloadImage(final ImageView iconView, final String link)  {

        String url = "http://adlibtech.ru/elections/upload_images/"+link ;
        ImageLoader imageLoader = VolleySingleton.getInstance(this.context).getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    //FileUtils.saveImageFile(response.getBitmap(), item.getImagelink(), context);


                    new Thread(iconView).execute(response.getBitmap(),link);


                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
