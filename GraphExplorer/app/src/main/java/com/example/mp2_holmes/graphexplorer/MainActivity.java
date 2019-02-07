package com.example.mp2_holmes.graphexplorer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView DiagramTitle;
    private EditText SearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void SearchAPI(View view) {
        DiagramTitle = (TextView) findViewById(R.id.DiagarmTitle);
        SearchBar = (EditText) findViewById(R.id.SearchBar);
        String searchParma = SearchBar.getText().toString();
        new ApiRequest(this,DiagramTitle,searchParma).execute();
        //DiagramTitle.setText(searchParma);
    }





    public void ShareResults(View view) {
    }

    public void ShowHistory(View view) {
    }
}



