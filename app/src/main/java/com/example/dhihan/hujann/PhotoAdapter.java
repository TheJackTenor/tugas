package com.example.dhihan.hujann;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhihan on 10/01/18.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    private List<HashMap<String, String>> photos;
    Context context;

    public PhotoAdapter(List<HashMap<String, String>> photos, Context context)
    {
        this.photos = photos;
        this.context=context;
    }

    @Override
    public PhotoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_rows,viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.MyViewHolder viewHolder, final int position){
        final PhotoAdapter.MyViewHolder holder = viewHolder;
        holder.photoText.setText(photos.get(position).get(MainActivity.TAG_TITLE).toString());
        Picasso.with(context).load(photos.get(position).get(MainActivity.TAG_THUMBANIL_URL).toString()).into(holder.photo);
    }

    @Override
    public  int getItemCount(){
        return photos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.photo_text) TextView photoText;
        @BindView(R.id.photo) ImageView photo;

        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
