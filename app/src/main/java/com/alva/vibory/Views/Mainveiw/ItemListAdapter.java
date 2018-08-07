package com.alva.vibory.Views.Mainveiw;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alva.vibory.Classes.FileUtils;
import com.alva.vibory.Classes.Globals;
import com.alva.vibory.Classes.VolleySingleton;
import com.alva.vibory.Models.Item;
import com.alva.vibory.R;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.util.ArrayList;

public class ItemListAdapter extends BaseAdapter{

    Context context;
    LayoutInflater inflater;
    ArrayList<Item> itemsArray;
    MainActivity mainActivity;

    static class ViewHolder {
        ImageView iconView;
        TextView tvsname;
        TextView tvname;
        TextView tvtname;
        TextView tvvotes;
        TextView tvvotesprosent;
        ImageView imageView;

    }
    public void setItemsArray(ArrayList<Item> itemsArray) {
        this.itemsArray = itemsArray;
    }

    public ItemListAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.context = mainActivity.getApplicationContext();
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemsArray = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return itemsArray.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final Item item = (Item) getItem(i);

        view = inflater.inflate(R.layout.itemlistadapter, null);

        viewHolder = new ViewHolder();

        viewHolder.tvsname = (TextView) view.findViewById(R.id.tvsname);
        viewHolder.tvname = (TextView) view.findViewById(R.id.tvname);
        viewHolder.tvtname = (TextView) view.findViewById(R.id.tvtname);
        viewHolder.tvvotesprosent = (TextView)  view.findViewById(R.id.tvvotesprosent);
        viewHolder.tvvotes = (TextView)  view.findViewById(R.id.tvvotes);
        viewHolder.iconView = (ImageView)view.findViewById(R.id.Candidateimg);
        viewHolder.imageView = (ImageView)view.findViewById(R.id.square);

        viewHolder.tvsname.setText(item.getSecondname());
        viewHolder.tvname.setText(item.getFirstname());
        viewHolder.tvtname.setText(item.getThirdname());
        viewHolder.tvvotes.setText("Голосов: " + item.getVotes());


        viewHolder.tvvotesprosent.setText("Проценты: " + (Integer.valueOf(item.getVotes())*100/Globals.getInstance().getTotal()) + "%");
        if (Integer.valueOf(item.getId())==Globals.getInstance().getCheckcand())
        viewHolder.imageView.setImageResource(R.drawable.squaretick);

        if (FileUtils.checkImageFileExist(item.getImagelink(), context)) {
            File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imagePath = new File(directory, item.getImagelink());
            viewHolder.iconView.setImageURI(Uri.fromFile(imagePath));

        } else {
            downloadImage(viewHolder.iconView, item);
        }



        final int k =i;
        //viewHolder.imageView = null;
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewHolder.imageView.setImageResource(R.drawable.squaretick);

                sendVoice(item);
                notifyDataSetChanged();
            }
        });

        view.setTag(viewHolder);

        return view;
    }

    private void sendVoice(Item item) {
        //запрос на отправку голоса
        Globals.getInstance().setPrevcand(Globals.getInstance().getCheckcand());
        Globals.getInstance().setCheckcand(Integer.valueOf( item.getId()));
        System.out.println(Globals.getInstance().getPrevcand());

        mainActivity.sendVoice(item);


    }

    private void downloadImage(final ImageView iconView, final Item item) {

        String url = "http://adlibtech.ru/elections/upload_images/"+item.getImagelink() ;
        ImageLoader imageLoader = VolleySingleton.getInstance(this.context).getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    FileUtils.saveImageFile(response.getBitmap(), item.getImagelink(), context);
                    iconView.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
