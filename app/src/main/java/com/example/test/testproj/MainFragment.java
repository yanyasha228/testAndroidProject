package com.example.test.testproj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.adapters.ShowsListAdapter;
import com.example.test.testproj.helpers.ConnectivityHelper;
import com.example.test.testproj.helpers.ShowBuilder;
import com.example.test.testproj.models.Show;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Fragment that uses the REST API of www.tvmaze.com to searching data
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */

public class MainFragment extends Fragment implements ShowsListAdapter.ShowClickListener {
    private RecyclerView recyclerView;
    private EditText search;
    private DBAdapter dbAdapter;
    private ShowsListAdapter showListAdapter;
    private static List<Show> showMainList;
    private static List<Show> showListSearch;
    private ConnectivityHelper connectivityHelper;
    private TextView noDataResults;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        search = (EditText) layout.findViewById(R.id.searchMain);

        noDataResults = (TextView) layout.findViewById(R.id.noResultsMain);

        dbAdapter = new DBAdapter(getActivity());

        connectivityHelper = new ConnectivityHelper(getActivity());

        getAllFavorites();

        recyclerView = layout.findViewById(R.id.showListMain);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        showListAdapter = new ShowsListAdapter(getActivity(), showMainList);

        showListAdapter.setShowClickListener(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(showListAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If checking connection is successful - > lets search
                if (connectivityHelper.isConnected()) {
                    if (s.length() == 0) {
                        showListAdapter.setFilter(showMainList);
                        showListSearch = showMainList;
                    } else getShowServerData(s.toString().toLowerCase());

                } else
                    Toast.makeText(getActivity(), "Waiting for internet connection...", Toast.LENGTH_SHORT).show();
            }
        });
//If press ENTER -> hide keyboard
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            KeyboardMain.hide(search);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return layout;
    }
    //Class with hideKeyboard func
    public static class KeyboardMain {
        public static void hide(View view) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
//Loading data from server Async
    private void getShowServerData(final String searchStr) {
        AsyncTask<String, Void, Void> searchTask = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://api.tvmaze.com/search/shows?q=" + searchStr).build();
                try {
                    Response response = client.newCall(request).execute();
                    showListSearch = new ShowBuilder(response.body().string()).getShowListWithFavoritesValidation(showMainList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (showListSearch.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noDataResults.setVisibility(View.VISIBLE);
                } else {
                    noDataResults.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                showListAdapter.setFilter(showListSearch);
            }
        };
        searchTask.execute(searchStr);
    }
//If Show doesn't exist put Show into database
    private void addFavoriteShow(int position) {
        dbAdapter.open();
        Show addToFavoritesShow = showListSearch.get(position);
        if (!dbAdapter.showAlreadyExists(addToFavoritesShow)) {
            addToFavoritesShow.setFav(1);
            dbAdapter.insert(addToFavoritesShow);
            addToFavoritesShow.setId(dbAdapter.getShowByName(addToFavoritesShow.getName()).getId());
        }
        dbAdapter.close();
        getAllFavorites();

    }
//Deleting Show
    private void deleteFavoriteShow(Show show) {
        dbAdapter.open();
        dbAdapter.delete(show.getId());
        dbAdapter.close();
        getAllFavorites();

    }
    /*
*Implementing interface which is in the custom RecyclerView.Adapter   {@link ShowsListAdapter}
*/
    @Override
    public void showClicked(View view, int position) {
        if (view.getId() == R.id.showItemLayout) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((showListSearch.get(position)).getUrl()));
            getActivity().startActivity(intent);
        } else if (showListSearch.get(position).getFav() == 0) {
            addFavoriteShow(position);
            ((ImageButton) view).setImageResource(R.drawable.heart_like);
            showListSearch.get(position).setFav(1);
        } else {
            deleteFavoriteShow(showListSearch.get(position));
            ((ImageButton) view).setImageResource(R.drawable.heart_unlike);
            showListSearch.get(position).setFav(0);
        }
    }

//Get favorites list
    public void getAllFavorites() {
        dbAdapter.open();
        showMainList = dbAdapter.getShows();
        dbAdapter.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
