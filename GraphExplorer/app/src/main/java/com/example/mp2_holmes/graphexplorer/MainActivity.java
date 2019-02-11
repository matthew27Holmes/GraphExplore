package com.example.mp2_holmes.graphexplorer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView DiagramTitle;
    private EditText SearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        nodeAdapter ca = new nodeAdapter(createList(30));
        recList.setAdapter(ca);

    }

    public void SearchAPI(View view) {
        DiagramTitle = (TextView) findViewById(R.id.DiagarmTitle);
        SearchBar = (EditText) findViewById(R.id.SearchBar);
        String searchParma = SearchBar.getText().toString();
        new ApiRequest(this,DiagramTitle,searchParma).execute();
        //DiagramTitle.setText(searchParma);
    }


    private List<nodeInfo> createList(int size) {

        List<nodeInfo> result = new ArrayList<nodeInfo>();
        for (int i=1; i <= size; i++) {
            nodeInfo ci = new nodeInfo();
            ci.name = nodeInfo.NAME_PREFIX + i;
            ci.surname = nodeInfo.SURNAME_PREFIX + i;
            ci.email = nodeInfo.EMAIL_PREFIX + i + "@test.com";

            result.add(ci);

        }
        return result;
    }

    public void ShareResults(View view) {
    }

    public void ShowHistory(View view) {
    }
}



