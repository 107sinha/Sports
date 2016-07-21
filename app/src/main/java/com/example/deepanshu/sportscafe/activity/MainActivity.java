package com.example.deepanshu.sportscafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.deepanshu.sportscafe.R;
import com.example.deepanshu.sportscafe.SharedPreference;
import com.example.deepanshu.sportscafe.adapter.DividerItemDecoration;
import com.example.deepanshu.sportscafe.adapter.MyAdapter;
import com.example.deepanshu.sportscafe.model.Students;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Students> mStudentList = new ArrayList<>();

    private SharedPreference sharedP;
    List mStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        sharedP = new SharedPreference();
        mStudent = sharedP.getFavorites(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, mStudentList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new MyAdapter(this, mRecyclerView, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(getApplicationContext(), "" + view.getId() + "pos" + position, Toast.LENGTH_LONG).show();
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

        if (mStudent != null) {
            studentLists(mStudent);
        }

    }

    private void studentLists(List mStudent) {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(mStudent.toString());

        for (int i = 0; i < jsonArray.size(); i++) {

            JsonElement jsonObject = jsonArray.get(i);
            JsonObject jObject = jsonObject.getAsJsonObject();
            JsonArray jsonObj = jObject.getAsJsonArray("data");
            jsonObject = jsonObj.get(0);
            jObject = jsonObject.getAsJsonObject();

            Students students = new Students();
            students.setName(jObject.get("name").getAsString());
            students.setAddress(jObject.get("address").getAsString());
            students.setEmail(jObject.get("email").getAsString());
            students.setNumber(jObject.get("phone").getAsString());
            students.setImage(jObject.get("image").getAsString());


            mStudentList.add(students);

            Log.d(TAG + "whats", jObject.get("image").getAsString());
        }

        mAdapter.notifyDataSetChanged();
        Log.d(TAG + "abey", jsonArray.toString());

    }
}
