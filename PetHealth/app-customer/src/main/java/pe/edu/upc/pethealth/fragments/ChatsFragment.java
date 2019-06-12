package pe.edu.upc.pethealth.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.ChatAdapters;
import pe.edu.upc.pethealth.repositories.ChatsRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    RecyclerView chatRecyclerView;
    ChatAdapters chatAdapters;
    RecyclerView.LayoutManager chatLayoutManager;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Chat",false,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        chatRecyclerView = (RecyclerView) view.findViewById(R.id.chatRecyclerView);
        chatAdapters = new ChatAdapters(ChatsRepository.getChats());
        chatLayoutManager = new LinearLayoutManager(view.getContext());
        chatRecyclerView.setAdapter(chatAdapters);
        chatRecyclerView.setLayoutManager(chatLayoutManager);
        return view;
    }

}
