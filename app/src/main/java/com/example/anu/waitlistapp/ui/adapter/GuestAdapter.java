package com.example.anu.waitlistapp.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anu.waitlistapp.R;
import com.example.anu.waitlistapp.data.WaitlistContract;

import butterknife.BindView;

/**
 * Created by Design on 07-12-2017.
 */

public class GuestAdapter extends RecyclerView.Adapter<GuestHolder> {

    private Context mContext;
    private Cursor mCursor;

    public GuestAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public GuestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_guest_item, parent, false);
        return new GuestHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestHolder holder, int position) {
        /**
         * move the cursor to the position to be displayed
         */
        if (!mCursor.moveToPosition(position))
            return;

        /**
         * get guest name and party size from the cursor
         */
        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.KEY_COLUMN_GUEST_NAME));
        int size = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.KEY_COLUMN_PARTY_SIZE));

        /**
         * update ui basd on the values
         */
        holder.txtGuestName.setText(name);
        holder.txtPartySize.setText(String.valueOf(size));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
