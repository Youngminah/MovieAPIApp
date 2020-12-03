package com.example.hw12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static RequestQueue requestQueue;
    EditText editText;
    String urlStr="http://www.kobis.or.kr/kobisopenapi/webservice/rest/people/searchPeopleList.json?key=430156241533f1d058c603178cc3ca0e&peopleNm=";
    RecyclerView recyclerView;
    MovieAdapter adapter=new MovieAdapter();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.buttonSearch);
        editText = findViewById(R.id.editTextPersonName);
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressTask task = new progressTask();
                String name = editText.getText().toString();
                if(!name.equals("")){
                    progressBar.setVisibility(View.VISIBLE);
                    String str = urlStr+name;
                    makeRequest(str);
                }

            }

        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



    }

    class progressTask extends AsyncTask<Integer,Integer, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            return null;
        }
    }

    void makeRequest(String url){
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processResponse(response);
                progressBar.setVisibility(View.GONE);            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }
        ){

        };
        request.setShouldCache(false);
        requestQueue.add(request);

    }


    public void processResponse(String response) {
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);


        int count=0;
        for (int i = 0; i < movieList.peopleListResult.peopleList.size(); i++) {
            if((movieList.peopleListResult.peopleList.get(i).repRoleNm).equals("배우")){
                Movie movie = movieList.peopleListResult.peopleList.get(i);
                adapter.addItem(movie);
                count++;
            }
        }
        if(count==0){
            String name = editText.getText().toString();
            Movie movie = new Movie();
            movie.peopleNm= name;
            movie.filmoNames="검색결과없음";
            movie.peopleNmEn="";
            adapter.addItem(movie);
        }
        adapter.notifyDataSetChanged();
    }




}

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    ArrayList<Movie> items = new ArrayList<Movie>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.activity_actor_info, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Movie item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Movie item){

        items.add(0,item);
    }

    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }

    public Movie getItem(int position) {
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewFilm;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewFilm = itemView.findViewById(R.id.textViewFilm);
        }

        public void setItem(Movie item) {
            if(item.peopleNmEn.equals("")){
                textViewName.setText(item.peopleNm);
            }
            else{
                textViewName.setText(item.peopleNm+" ("+item.peopleNmEn+")");
            }

            String str = item.filmoNames;
            String [] movie_arr = str.split("\\|");
            if(movie_arr.length>1){
                textViewFilm.setText(movie_arr[0]+" 외 "+(movie_arr.length-1)+"편");
            }
            else {
                textViewFilm.setText(str);
            }
        }



    }

}


