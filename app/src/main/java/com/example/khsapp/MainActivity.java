package com.example.khsapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout frameLayout;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToolBar Code
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Drawer Code
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
       ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.nav_in,R.string.nav_out);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
      /*  mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
*/

        frameLayout=findViewById(R.id.main_framelayout);
        setFragment(new MyHomeFragment());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //Header tab menu button customization
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       int id=item.getItemId();

       //noinspection SimplifiableIfStatement
        if(id==R.id.main_search_icon){
            //todo:search
            return true;
        }else if(id==R.id.main_notification_icon){
            //todo:notification System
            return true;
        }else if(id==R.id.main_cart_icon){
            //todo:Cart
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



 /*   @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    */


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Handle navigation view item clicks here
        int id=menuItem.getItemId();

        if(id==R.id.nav_my_mall){

        }else if(id==R.id.nav_my_orders){

        }else if(id==R.id.nav_my_rewards){

        }else if(id==R.id.nav_my_cart){

        }else if(id==R.id.nav_my_wishlist){

        }else if(id==R.id.nav_my_account){

        }else if(id==R.id.nav_my_sign_out){

        }
        return false;
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

}