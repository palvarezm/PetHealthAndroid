package pe.edu.upc.pethealth.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.fragments.ChatsFragment;
import pe.edu.upc.pethealth.fragments.HomeFragment;
import pe.edu.upc.pethealth.fragments.MyPetsFragment;
import pe.edu.upc.pethealth.fragments.NoInternetFragment;
import pe.edu.upc.pethealth.fragments.NotificationsFragment;
import pe.edu.upc.pethealth.fragments.ProfileFragment;
import pe.edu.upc.pethealth.fragments.SearchFragment;
import pe.edu.upc.pethealth.models.User;
import pe.edu.upc.pethealth.network.Connection;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User u;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(Connection.isOnline(getApplicationContext()))
                return navigateAccordingTo(item.getItemId());
            else{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content,new NoInternetFragment()).commit();
                return true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        u = User.from(getIntent().getExtras());
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateAccordingTo(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //this adds items to the action bar if present
        getMenuInflater().inflate(R.menu.navigation_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()){
                case R.id.navigation_logout:
                    finish();
                    System.exit(0);
                    return true;
                case R.id.navigation_profile:
                    ProfileFragment newFragment = new ProfileFragment();
                    newFragment.setArguments(u.toBundle());
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.content, newFragment).commit();
                    return true;
                case R.id.navigation_search:
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.content, new SearchFragment()).commit();
                    return true;
            }
        return super.onOptionsItemSelected(item);
    }

    private boolean navigateAccordingTo(int id){
        try{
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content,getFragmentFor(id)).commit();
            return true;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    private Fragment getFragmentFor(int id){
        switch (id){
            case R.id.navigation_home: return new HomeFragment();
            case R.id.navigation_mypets:
                MyPetsFragment newFragment = new MyPetsFragment();
                newFragment.setArguments(u.toBundle());
                return newFragment;
            case R.id.navigation_chat: return new ChatsFragment();
            case R.id.navigation_notifications: return new NotificationsFragment();
        }
        return null;
    }


    public void setFragmentToolbar(String toolbarTitle, boolean backIcon, final FragmentManager fragmentManager){
        toolbar.setTitle(toolbarTitle);
        Integer icon;
        if(backIcon) {
            icon = R.drawable.ic_arrow_back_24dp;
            toolbar.setNavigationIcon(icon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager.popBackStack();
                }
            });
        }else{
            toolbar.setNavigationIcon(null);
        }
    }
}
