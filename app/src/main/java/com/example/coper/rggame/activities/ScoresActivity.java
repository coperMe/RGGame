package com.example.coper.rggame.activities;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TabHost;

import com.example.coper.rggame.POJO.RecAdapter;
import com.example.coper.rggame.POJO.Scores;
import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.R;

import java.util.Vector;

public class ScoresActivity extends AppCompatActivity {

    private RecyclerView recyclerLocal, recyclerFriends;
    private RecyclerView.LayoutManager lManagerLocal, lManagerFriends;
    private TabHost tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();

        tabs.addTab(tabs.newTabSpec("tabLocal").setIndicator("Local highscores").setContent(R.id.rvLocalScores));
        tabs.addTab(tabs.newTabSpec("tabFriends").setIndicator("Friends highscores").setContent(R.id.rvFriendsScores));

        lManagerLocal = new LinearLayoutManager(this);
        recyclerLocal = (RecyclerView) findViewById(R.id.rvLocalScores);
        recyclerLocal.setAdapter(new RecAdapter(this, new Vector<Scoring>()));
        recyclerLocal.setLayoutManager(lManagerLocal);

        lManagerFriends = new LinearLayoutManager(this);
        recyclerFriends = (RecyclerView) findViewById(R.id.rvFriendsScores);
        recyclerFriends.setAdapter(new RecAdapter(this, new Vector<Scoring>()));
        recyclerFriends.setLayoutManager(lManagerFriends);
    }


}
