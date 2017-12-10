package com.example.anu.waitlistapp.ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anu.waitlistapp.R;
import com.example.anu.waitlistapp.data.WaitlistContract;
import com.example.anu.waitlistapp.data.WaitlistDbHelper;
import com.example.anu.waitlistapp.ui.adapter.GuestAdapter;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
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
        stupRecyclerView();
    }

    /**
     * method to setup the recycle view
     */
    private void stupRecyclerView() {
        mGuestAdapter = new GuestAdapter(this);
        recyclerViewGuests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGuests.setHasFixedSize(true);
        recyclerViewGuests.setAdapter(mGuestAdapter);
    }

    @OnClick(R.id.btn_add)
    public void onAddClicked() {
        validator.validate();
    }

    /**
     * method to add new gusts with party size into waitlist table
     * @param guestName name of the guest
     * @param partySize number of persons in the party
     * @return id of the new record inserted
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
        long insertVal = addGuests(etGuestName.getText().toString(), etPartySize.getText().toString());
        Log.d(TAG, "insertVal : "+insertVal);
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        if (failedView instanceof EditText){
            EditText editText = (EditText) failedView;
            editText.setError(failedRule.getFailureMessage());
        }
    }
}
