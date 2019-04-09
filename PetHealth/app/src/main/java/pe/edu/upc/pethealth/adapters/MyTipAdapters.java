package pe.edu.upc.pethealth.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.fragments.MyTipDetailFragment;
import pe.edu.upc.pethealth.models.MyTip;

/**
 * Created by genob on 28/09/2017.
 */

public class MyTipAdapters extends RecyclerView.Adapter<MyTipAdapters.ViewHolder> {

    private List<MyTip> myTips;

    public MyTipAdapters() {
    }

    public MyTipAdapters(List<MyTip> myTips) {
        this.myTips = myTips;
    }

    public List<MyTip> getMyTips() {
        return myTips;
    }

    public MyTipAdapters setMyTips(List<MyTip> myTips) {
        this.myTips = myTips;
        return this;
    }

    @Override
    public MyTipAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tips,parent,false));
    }

    @Override
    public void onBindViewHolder(MyTipAdapters.ViewHolder holder, int position) {
        holder.loadingIndicatorView.setVisibility(View.VISIBLE);
        holder.tipANImageView.setVisibility(View.INVISIBLE);
        final MyTip myTip = myTips.get(position);
        holder.tittleTextView.setText(myTip.getTittle());
        holder.descriptionTextView.setText(myTip.getDescription());
        loadImage(myTip.getImage(),holder.tipANImageView,holder.loadingIndicatorView);
        holder.tipANImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity context = (MainActivity) view.getContext();
                MyTipDetailFragment newFragment = new MyTipDetailFragment();
                newFragment.setArguments(myTip.toBundle());
                context.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void loadImage(String url, final ImageView imageView, final AVLoadingIndicatorView loadingIndicatorView){
        AndroidNetworking.get(url)
                .setTag("imageRequest")
                .setPriority(Priority.MEDIUM)
                .setBitmapMaxHeight(240)
                .setBitmapMaxWidth(240)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setVisibility(View.VISIBLE);
                        loadingIndicatorView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.toString());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return myTips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tipANImageView;
        TextView tittleTextView;
        TextView descriptionTextView;
        AVLoadingIndicatorView loadingIndicatorView;
        public ViewHolder(View itemView) {
            super(itemView);
            loadingIndicatorView = (AVLoadingIndicatorView) itemView.findViewById(R.id.avi);
            tipANImageView = (ImageView) itemView.findViewById(R.id.tipImageView);
            tittleTextView = (TextView) itemView.findViewById(R.id.tipTittleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.tipDescriptionTextView);
        }
    }
}
