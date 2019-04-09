package pe.edu.upc.pethealth.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.MyTipAdapters;
import pe.edu.upc.pethealth.models.MyTip;
import pe.edu.upc.pethealth.network.PetHealthApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView myTipRecyclerView;
    MyTipAdapters myTipAdapters;
    RecyclerView.LayoutManager myTipLayoutManager;
    List<MyTip> myTips;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Home",false,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        myTipRecyclerView = (RecyclerView) view.findViewById(R.id.myTipRecyclerView);
        myTips = new ArrayList<>();
        myTipAdapters = new MyTipAdapters(myTips);
        myTipLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        myTipRecyclerView.setAdapter(myTipAdapters);
        myTipRecyclerView.setLayoutManager(myTipLayoutManager);
        updateTips();
        return view;
    }

    private void updateTips() {
        AndroidNetworking.get(PetHealthApiService.TIP_URL)
                .setPriority(Priority.LOW)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            myTips= MyTip.from(response.getJSONArray("content"));
                            myTipAdapters.setMyTips(myTips);
                            myTipAdapters.notifyDataSetChanged();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(getString(R.string.app_name),anError.getLocalizedMessage());
                    }
                });
    }

}
