package com.example.coper.rggame.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TabHost;

import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.MyOpenHelper;
import com.example.coper.rggame.tools.RecAdapter;

import java.util.Vector;

/**
 * @author David García Molino
 */

public class ScoresActivity extends AppCompatActivity {

    private RecyclerView recyclerLocal, recyclerFriends;
    private RecyclerView.LayoutManager lManagerLocal, lManagerFriends;
    private TabHost tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        if(savedInstanceState == null) {
            tabs = (TabHost) findViewById(R.id.tabHost);

            MyOpenHelper db = new MyOpenHelper(this);
            Vector<Scoring> data = db.extractAll();
            db.close();
            if (tabs != null) {
                tabs.setup();

                tabs.addTab(tabs.newTabSpec("tabLocal").setIndicator("Local highscores").setContent(R.id.rvLocalScores));
                tabs.addTab(tabs.newTabSpec("tabFriends").setIndicator("Friends highscores").setContent(R.id.rvFriendsScores));

                lManagerLocal = new LinearLayoutManager(this);
                recyclerLocal = (RecyclerView) findViewById(R.id.rvLocalScores);
                recyclerLocal.setAdapter(new RecAdapter(this, data));
                recyclerLocal.setLayoutManager(lManagerLocal);

                lManagerFriends = new LinearLayoutManager(this);
                recyclerFriends = (RecyclerView) findViewById(R.id.rvFriendsScores);
                recyclerFriends.setAdapter(new RecAdapter(this, new Vector<Scoring>()));
                recyclerFriends.setLayoutManager(lManagerFriends);
            }
        }
    }
}
