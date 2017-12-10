package com.example.anu.waitlistapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anu.waitlistapp.R;

import butterknife.BindView;

/**
 * Created by Design on 07-12-2017.
 */

public class GuestAdapter extends RecyclerView.Adapter<GuestHolder> {

    private Context mContext;

    public GuestAdapter(Context context) {
        mContext = context;
    }

    @Override
    public GuestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_guest_item, parent, false);
        return new GuestHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
