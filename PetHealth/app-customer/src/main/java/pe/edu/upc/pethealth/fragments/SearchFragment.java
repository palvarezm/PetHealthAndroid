package pe.edu.upc.pethealth.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.SearchAdapters;
import pe.edu.upc.pethealth.models.Veterinary;
import pe.edu.upc.pethealth.network.LoggerCallback;
import pe.edu.upc.pethealth.network.RestClient;
import pe.edu.upc.pethealth.network.RestView;
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private RecyclerView searchRecyclerView;
    private SearchAdapters searchAdapters;
    private RecyclerView.LayoutManager searchLayoutManager;

    private List<pe.edu.upc.lib.Veterinary> veterinaries;
    private SharedPreferencesManager sharedPreferencesManager;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Search",true,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.getContext());
        searchRecyclerView = (RecyclerView) view.findViewById(R.id.searchRecyclerView);
        veterinaries = new ArrayList<>();
        updateSearch(view);
        return view;
    }

    private void updateSearch(View view){
        Call<RestView<JsonArray>> call = new RestClient().getWebServices().getCloseVeterinaries(sharedPreferencesManager.getAccessToken(), -12.0874509, -77.0499422);
        call.enqueue(new LoggerCallback<RestView<JsonArray>>(){
            @Override
            public void onResponse(Call<RestView<JsonArray>> call, Response<RestView<JsonArray>> response) {
                super.onResponse(call, response);
                parseResponse(response.body().getData());
                searchAdapters = new SearchAdapters(veterinaries);
                searchLayoutManager = new LinearLayoutManager(view.getContext());
                searchRecyclerView.setAdapter(searchAdapters);
                searchRecyclerView.setLayoutManager(searchLayoutManager);
            }

            @Override
            public void onFailure(Call<RestView<JsonArray>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("FAILURE", t.toString());
            }
        });
    }

    private void parseResponse(JsonArray data) {
        for (JsonElement dataElement: data) {
            Gson gson = new Gson();
            pe.edu.upc.lib.Veterinary veterinary = gson.fromJson(dataElement.getAsJsonObject().get("veterinary").toString(), pe.edu.upc.lib.Veterinary.class);
            veterinary.setDistance(gson.fromJson(dataElement.getAsJsonObject().get("distance").toString(), Double.class));
            Log.d("Veterinary", veterinary.toString());
            veterinaries.add(veterinary);
        }
    }
}
