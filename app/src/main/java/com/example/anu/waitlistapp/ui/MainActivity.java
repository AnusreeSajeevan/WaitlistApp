package com.example.anu.waitlistapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anu.waitlistapp.R;
import com.example.anu.waitlistapp.data.WaitlistContract;
import com.example.anu.waitlistapp.data.WaitlistDbHelper;
import com.example.anu.waitlistapp.ui.adapter.GuestAdapter;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener{

    @Required(order = 1)
    @BindView(R.id.et_guest_name)
    EditText etGuestName;

    @Required(order = 2)
    @BindView(R.id.et_party_size)
    EditText etPartySize;

    @BindView(R.id.btn_add)
    Button btnAdd;

    @BindView(R.id.recycler_view_guests)
    RecyclerView recyclerViewGuests;

    private GuestAdapter mGuestAdapter;
    private SQLiteDatabase mDatabase;
    private WaitlistDbHelper mDbHelper;
    private Validator validator;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /**
         * it will create the database if it is called for the first time
         */
        mDbHelper = new WaitlistDbHelper(this);

        /**
         * keep a reference to the database until paused or killed
         * get a writable database bacause we will be adding new guests to the database
         */
        mDatabase = mDbHelper.getWritableDatabase();
        validator = new Validator(this);
        validator.setValidationListener(this);

        /**
         * we implement ItemTouchHelper with simple callbacks that can handle both left and right swipes
         * we need to ovveride onMove and onSwiped methods to handle moves and swipes
         * first argument to SimpleCallback is related to dragging
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                boolean isRemoved = removeGuest((Long) viewHolder.itemView.getTag());

                if (isRemoved){
                    /**
                     * after removing the guest, notify the adapter
                     */
                    mGuestAdapter.swapCursor(getAllGuests());
                }
                else
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.remove_error), Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewGuests);

        stupRecyclerView();
    }

    /**
     * method to setup the recycle view
     */
    private void stupRecyclerView() {
        recyclerViewGuests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGuests.setHasFixedSize(true);
        fetchGuests();
    }

    private void fetchGuests() {
        /**
         * cursor containing all the guest values will be returned
         */
        Cursor cursor = getAllGuests();
        mGuestAdapter = new GuestAdapter(this, cursor);
        recyclerViewGuests.setAdapter(mGuestAdapter);
    }

    /**
     * method to get all guests from SQLite
     * @return cursor containing the gusts will be returned
     */
    private Cursor getAllGuests() {
        return mDatabase.query(WaitlistContract.WaitlistEntry.KEY_TABLE_NAME,
                null, null, null, null, null, WaitlistContract.WaitlistEntry.KEY_COLUMN_TIMESTAMP);
    }

    @OnClick(R.id.btn_add)
    public void onAddClicked() {
        validator.validate();
    }

    /**
     * method to add new gusts with party size into waitlist table
     * @param guestName name of the guest
     * @param partySize number of persons in the party
     * @return id of the new record inserted, will return -1 if row cannot be inserted
     */
    private long addGuests(String guestName, String partySize) {

        /* create ContentValues instance to insert a new row into the table */
        ContentValues contentValues = new ContentValues();

        /**
         * call put to insert the guestName with the key
         *{@link com.example.anu.waitlistapp.data.WaitlistContract.WaitlistEntry.KEY_COLUMN_GUEST_NAME}
         */
        contentValues.put(WaitlistContract.WaitlistEntry.KEY_COLUMN_GUEST_NAME, guestName);

        /**
         * call put to insert the partySize with the key
         *{@link com.example.anu.waitlistapp.data.WaitlistContract.WaitlistEntry.KEY_COLUMN_PARTY_SIZE}
         */
        contentValues.put(WaitlistContract.WaitlistEntry.KEY_COLUMN_PARTY_SIZE, partySize);

        return mDatabase.insert(WaitlistContract.WaitlistEntry.KEY_TABLE_NAME, null, contentValues);
    }

    @Override
    public void onValidationSucceeded() {
        long insertedId = addGuests(etGuestName.getText().toString(), etPartySize.getText().toString());
        Log.d(TAG, "insertedId : "+insertedId);
        if (insertedId != -1) {
            etGuestName.setText("");
            etPartySize.setText("");
            fetchGuests();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        if (failedView instanceof EditText){
            EditText editText = (EditText) failedView;
            editText.setError(failedRule.getFailureMessage());
        }
    }

    private boolean removeGuest(long id){
        return mDatabase.delete(WaitlistContract.WaitlistEntry.KEY_TABLE_NAME, WaitlistContract.WaitlistEntry._ID + "=" + id,
                null) > 0;
    }
}
