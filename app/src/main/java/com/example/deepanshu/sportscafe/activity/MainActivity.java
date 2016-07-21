package com.example.deepanshu.sportscafe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.deepanshu.sportscafe.App;
import com.example.deepanshu.sportscafe.R;
import com.example.deepanshu.sportscafe.adapter.DividerItemDecoration;
import com.example.deepanshu.sportscafe.adapter.MyAdapter;
import com.example.deepanshu.sportscafe.model.Students;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView.Adapter mAdapter;
    private ArrayList<Students> mStudentList = new ArrayList<>();

    private SharedPreferences sharedArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        sharedArrayList = getSharedPreferences(App.STUDENTLIST, Context.MODE_PRIVATE);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, mStudentList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new MyAdapter(this, mRecyclerView, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {

                    case R.id.header:
                        startActivity(new Intent(MainActivity.this, AddStudent.class));
                        break;

                    case R.id.studName:
                        break;

                    case R.id.studEmail:
                        break;

                    case R.id.studAddr:
                        break;

                    case R.id.studPhone:
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        if (mStudentList != null) {
            new DownloadFilesTask().execute();
        }

    }

    private void studentLists() {

        int i = 1;

        mStudentList.clear();

        while (true) {
            if (!sharedArrayList.getString(App.ID+i, "").equals("")) {

                Students stud = new Students();
                stud.setId(sharedArrayList.getString(App.ID+i, ""));
                stud.setName(sharedArrayList.getString(App.NAME+i, ""));
                stud.setAddress(sharedArrayList.getString(App.ADDR+i, ""));
                stud.setEmail(sharedArrayList.getString(App.EMAIL+i, ""));
                stud.setImage(sharedArrayList.getString(App.IMAGE+i, ""));
                stud.setNumber(sharedArrayList.getString(App.PHONE+i, ""));

                mStudentList.add(stud);
                i++;
            } else {
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            studentLists();
            return null;
        }
    }
}
