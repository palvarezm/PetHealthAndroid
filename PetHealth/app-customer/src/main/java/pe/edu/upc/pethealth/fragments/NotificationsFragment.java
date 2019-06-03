package pe.edu.upc.pethealth.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.NotificationAdapters;
import pe.edu.upc.pethealth.models.Notification;
import pe.edu.upc.pethealth.repositories.NotificationsRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    private RecyclerView notificationsRecyclerView;
    private NotificationAdapters notificationAdapters;
    private RecyclerView.LayoutManager notificationLayoutManager;
    List<Notification> notifications;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Notifications",false,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationsRecyclerView = (RecyclerView) view.findViewById(R.id.notificationRecyclerView);
        notificationAdapters = new NotificationAdapters(NotificationsRepository.getNotifications());
        notificationLayoutManager = new LinearLayoutManager(view.getContext());
        notificationsRecyclerView.setAdapter(notificationAdapters);
        notificationsRecyclerView.setLayoutManager(notificationLayoutManager);
        return view;
    }

}
