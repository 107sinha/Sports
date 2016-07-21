package com.example.deepanshu.sportscafe.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.deepanshu.sportscafe.App;
import com.example.deepanshu.sportscafe.R;
import com.example.deepanshu.sportscafe.activity.AddStudent;
import com.example.deepanshu.sportscafe.model.Students;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerView.OnItemTouchListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private ArrayList<Students> mStudentList;


    private OnItemClickListener mListener;

    public MyAdapter(Context context, final RecyclerView mRecyclerView, OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(child, mRecyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);
    }

    private GestureDetector mGestureDetector;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public MyAdapter(Context context, ArrayList<Students> mStudentList) {
        this.mStudentList = mStudentList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.student_header, parent, false);

            return new HeaderHolder(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.student_list, parent, false);

            return new ListHolder(itemView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private Students getItem(int position) {
        return mStudentList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {

        } else if (holder instanceof ListHolder) {

            Students students = getItem(position - 1);
            ListHolder viewholder = (ListHolder) holder;

            viewholder.name.setText(students.getName());

            viewholder.addr.setText(students.getAddress());

            viewholder.phone.setText(students.getNumber());

            viewholder.email.setText(students.getEmail());

            Log.d("please", students.getImage());

            if (viewholder.imageView != null) {
                Glide.with(mContext)
                        .load(students.getImage())
                        .centerCrop()
                        .crossFade()
                        .thumbnail(0.5f)
                        .transform(new AddStudent.CircleTransform(mContext))
                        .placeholder(R.drawable.profile_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewholder.imageView);


            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mStudentList.size() + 1;
    }

    // List View
    private static class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView addr;
        TextView email;
        TextView phone;
        ImageView imageView;

        Context myContext;


        ListHolder(View itemView) {
            super(itemView);
            this.myContext = itemView.getContext();
            this.name = (TextView) itemView.findViewById(R.id.studName);
            this.addr = (TextView) itemView.findViewById(R.id.studAddr);
            this.email = (TextView) itemView.findViewById(R.id.studEmail);
            this.phone = (TextView) itemView.findViewById(R.id.studPhone);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);

            name.setOnClickListener(this);
            addr.setOnClickListener(this);
            email.setOnClickListener(this);
            phone.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            switch (view.getId()) {

                case R.id.studName:
                    Intent intent = new Intent(myContext, AddStudent.class);
                    intent.putExtra(App.ID, position);
                    intent.putExtra(App.NAME, name.getText());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    myContext.startActivity(intent);
                    break;

                case R.id.studEmail:
                    Intent gmail = new Intent(Intent.ACTION_SEND);
                    gmail.putExtra(Intent.EXTRA_EMAIL  , new String[]{email.getText().toString()});
                    gmail.setType("message/rfc822");
                    if (gmail.resolveActivity(myContext.getPackageManager()) != null) {
                        myContext.startActivity(Intent.createChooser(gmail, "Choose an Email client :"));
                    }
                    break;

                case R.id.studAddr:
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Uri.encode(addr.getText().toString()));

                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");

                    // Attempt to start an activity that can handle the Intent
                    if (mapIntent.resolveActivity(myContext.getPackageManager()) != null) {
                        myContext.startActivity(mapIntent);
                    }
                    break;

                case R.id.studPhone:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone.getText()));
                    if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (callIntent.resolveActivity(myContext.getPackageManager()) != null) {
                        myContext.startActivity(callIntent);
                    }
                    break;

                default:
                    break;
            }

        }

    }

    // Header View
    private static class HeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView header;
        Context headContext;

        HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);
            headContext = itemView.getContext();

            header.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.header) {
                Intent i = new Intent(headContext, AddStudent.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                headContext.startActivity(i);
            }
        }
    }
}
