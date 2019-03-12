package com.example.mp2_holmes.graphexplorer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private EditText SearchBar;
    private RecyclerView m_recList;
    private JSONObject m_Response;
    private SharedPreferences sharedPref;
    private int NumberOfSearchs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get or create SharedPreferences
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        NumberOfSearchs = sharedPref.getInt("NumberOfSearchs", 0);
        Log.d("NumberOfSearchs",Integer.toString(NumberOfSearchs));

        //initilise searchbar and results recycler
        SearchBar = (EditText) findViewById(R.id.SearchBar);

        m_recList = (RecyclerView) findViewById(R.id.cardList);
        m_recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        m_recList.setLayoutManager(llm);

    }

    public void SearchAPI(View view) {
        String searchParma = SearchBar.getText().toString();
        if(!checkSearchInHistory(searchParma)) {
            NumberOfSearchs++;
            sharedPref.edit().putInt("NumberOfSearchs", NumberOfSearchs).apply();
            sharedPref.edit().putString("Search" + Integer.toString(NumberOfSearchs), searchParma).apply();
        }
        new companiesRequest(this,m_recList,searchParma).execute();
    }

    public boolean checkSearchInHistory(String SearchParma)
    {
        if(!SearchParma.isEmpty()) //catch blank searchs
        {
            for (int i = 0; i < NumberOfSearchs; i++) {

                String PrevSearch = sharedPref.getString("Search" + Integer.toString(i), "def");
                if (SearchParma.matches(PrevSearch)) {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return  true;
        }
    }

    public void ShowHistory(View view) {
        PopupMenu searchHistory = new PopupMenu(this,view);
        searchHistory.setOnMenuItemClickListener(this);
        searchHistory.inflate(R.menu.previous_searchs_pop_up);
        int searchHistoryId =0;
        for(int i =0; i < NumberOfSearchs;i++)
        {
            if(searchHistory.getMenu().findItem(i) == null) {
                String PrevSearch = sharedPref.getString("Search"+Integer.toString(i), "def");
                if(PrevSearch != "def") {
                    searchHistory.getMenu().add(Menu.NONE, searchHistoryId, 1, PrevSearch);
                    searchHistoryId++;
                }
            }
        }
        searchHistory.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        String PrevSearch = (String) item.getTitle();
        SearchBar.setText(PrevSearch);
        return  true;
    }

    public void MoreDetailActivity(String CompanyName,String CompanyNum) {
        Intent intent = new Intent(getBaseContext(), NodeGraph.class);
        intent.putExtra("title", CompanyName);
        intent.putExtra("companyNum",CompanyNum);
        startActivity(intent);
    }
}



