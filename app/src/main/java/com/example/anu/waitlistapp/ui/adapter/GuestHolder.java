package com.example.anu.waitlistapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anu.waitlistapp.R;

import butterknife.BindView;

/**
 * Created by Design on 07-12-2017.
 */

public class GuestHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_guest_name)
    TextView txtGuestName;
    @BindView(R.id.txt_party_size)
    TextView txtPartySize;

    public GuestHolder(View itemView) {
        super(itemView);
    }
}
