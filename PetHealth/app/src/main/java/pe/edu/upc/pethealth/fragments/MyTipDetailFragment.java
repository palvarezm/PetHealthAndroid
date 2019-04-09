package pe.edu.upc.pethealth.fragments;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.models.MyTip;

public class MyTipDetailFragment extends Fragment {

    ImageView tipDetailANImageView;
    TextView tittleDetailTextView;
    TextView descriptionDetailTextView;
    AVLoadingIndicatorView loadingIndicatorView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tip_detail, container, false);
        ((MainActivity)getActivity()).setFragmentToolbar("Tip",true,getFragmentManager());

        MyTip myTip = MyTip.from(getArguments());
        tipDetailANImageView = (ImageView) view.findViewById(R.id.detailImageView);
        tittleDetailTextView =(TextView) view.findViewById(R.id.tittleDetailTextView);
        descriptionDetailTextView = (TextView) view.findViewById(R.id.descriptionDetailTextView);
        loadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        loadingIndicatorView.setVisibility(View.VISIBLE);
        tipDetailANImageView.setVisibility(View.INVISIBLE);
        loadImage(myTip.getImage());
        tittleDetailTextView.setText(myTip.getTittle());
        descriptionDetailTextView.setText(myTip.getDescription());
        return  view;
    }

    private void loadImage(String url){
        AndroidNetworking.get(url)
                .setTag("imageRequest")
                .setPriority(Priority.MEDIUM)
                .setBitmapMaxHeight(240)
                .setBitmapMaxWidth(368)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        tipDetailANImageView.setImageBitmap(response);
                        tipDetailANImageView.setVisibility(View.VISIBLE);
                        loadingIndicatorView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.toString());
                    }
                });
    }
}
