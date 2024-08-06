package com.guercif.bookscenter;

import android.annotation.SuppressLint;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String JSON_URL ="https://raw.githubusercontent.com/sadik-fattah/SimpleDataBase/main/BooksSitmap.json";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Books> lsArzone;
    RecyclerView recyclerView;
    RecyclerViewAdapter myadapter;
    SearchView searchView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lsArzone = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewtools);
        searchView = (SearchView)findViewById(R.id.searchview);
        searchView.clearFocus();
        Jsonrequest();
        editableSearch();

    }
    private void editableSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }
    private void filterList(String text){
        List<Books> filteredList = new ArrayList<>();
        for (Books item : lsArzone){
            if (item.getBook_name().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            } else if (item.getBook_type().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "Book not exist in database", Toast.LENGTH_SHORT).show();
        }else {
            myadapter.setFilteredList(filteredList);
        }
    }
    private void Jsonrequest() {
        request = new JsonArrayRequest(JSON_URL.toString().trim(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray responce) {
                JSONObject jsonObject = null;
                for (int i = 0;i< responce.length();i++){
                    try {
                        jsonObject = responce.getJSONObject(i);
                        Books arzone = new Books();
                        arzone.setBook_link(jsonObject.getString("Books_link"));
                        arzone.setBook_type(jsonObject.getString("Books_type"));
                        arzone.setBook_name(jsonObject.getString("Books_name").replace("_"," ").replace("tutorial"," "));
                        arzone.setBook_image(jsonObject.getString("Books_image"));
                        lsArzone.add(arzone);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(lsArzone);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }
    private void setuprecyclerview(List<Books> lsArzone) {
        myadapter = new RecyclerViewAdapter(this,lsArzone);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(myadapter);
    }


}
