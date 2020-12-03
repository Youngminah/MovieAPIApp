package com.example.hw12;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Actor_info extends LinearLayout {

    TextView textViewName;
    TextView textViewFilm;

    public Actor_info(Context context) {
        super(context);
        init(context);
    }

    public Actor_info(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_actor_info, this, true);

        textViewName = findViewById(R.id.textViewName);
        textViewFilm = findViewById(R.id.textViewFilm);


    }


    public void setName(String name ,String name_en) {

        this.textViewName.setText(name+" ("+name_en+")");
    }

    public void setInfo(String info) {

        String [] movie_arr = info.split("\\|");
        if(movie_arr.length>1){
            this.textViewFilm.setText(movie_arr[0]+" 외 "+(movie_arr.length-1)+"편");
        }
        else {
            this.textViewFilm.setText(info);
        }
    }

}
