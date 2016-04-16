package edu.uw.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {

    private static final String TAG = "MainActivity";

    private boolean dualPane;

    private String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View right = findViewById(R.id.container_right);
        dualPane = (right != null); //are we a dual view?

        if(savedInstanceState != null) {
            searchTerm = savedInstanceState.getString("searchTerm");
        }

        Fragment fragment  = MoviesFragment.newInstance(searchTerm);

        if(!dualPane) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, "MoviesFragment");
            ft.commit();
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_left, fragment, "MoviesFragment");
            ft.commit();
        }

    }


    //respond to search button clicking
    public void handleSearchClick(View v){
        Log.v(TAG, "Button handled");

        EditText text = (EditText)findViewById(R.id.txtSearch);
        searchTerm = text.getText().toString();

        MoviesFragment fragment = (MoviesFragment)getSupportFragmentManager().findFragmentByTag("MoviesFragment");
        if(fragment == null){
            fragment = new MoviesFragment();
        }

        if(!dualPane) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, "MoviesFragment");
            ft.commit();
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_left, fragment, "MoviesFragment");
            ft.commit();
        }


        fragment.fetchData(searchTerm);
    }

    @Override
    public void movieSelected(Movie movie) {


        Bundle bundle = new Bundle();
        bundle.putString("title", movie.toString());
        bundle.putString("imdb", movie.imdbId);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        if(!dualPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment, "DetailFragment")
                    .addToBackStack(null)
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_right, detailFragment, "DetailFragment")
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(searchTerm != null) {
            outState.putString("searchTerm", searchTerm);
        }

        super.onSaveInstanceState(outState);
    }
}
