package pe.edu.upc.pethealth.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.MyTipAdapters;
import pe.edu.upc.pethealth.models.MyTip;
import pe.edu.upc.pethealth.models.Person;
import pe.edu.upc.pethealth.models.User;
import pe.edu.upc.pethealth.network.PetHealthApiService;
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager;

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
        myTipLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        myTipRecyclerView.setAdapter(myTipAdapters);
        myTipRecyclerView.setLayoutManager(myTipLayoutManager);
        User user = SharedPreferencesManager.getInstance(this.getContext()).getUser();
        Person person = SharedPreferencesManager.getInstance(this.getContext()).getPerson();
        Log.d("TESTING Person data:", person.toString());
        Log.d("TESTING User data:", user.toString());
        updateTips();
        return view;
    }

    private void updateTips() {
        AndroidNetworking.get(PetHealthApiService.TIP_URL)
                .addHeaders("access_token", SharedPreferencesManager.getInstance(this.getContext()).getAccessToken())
                .setPriority(Priority.LOW)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            myTips= MyTip.from(data);
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
