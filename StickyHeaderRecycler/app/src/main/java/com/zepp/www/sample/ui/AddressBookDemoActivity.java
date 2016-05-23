package com.zepp.www.sample.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import com.zepp.www.sample.R;
import com.zepp.www.sample.StickyHeadersDemoApp;
import com.zepp.www.sample.adapters.AddressBookAdapter;
import com.zepp.www.sample.api.RandomUserLoader;
import com.zepp.www.sample.model.Person;
import com.zepp.www.stickyheaderrecyclerview.StickyHeaderLayoutManager;
import java.util.List;

public class AddressBookDemoActivity extends DemoActivity implements RandomUserLoader.OnLoadCallback {

    private static final String TAG = AddressBookDemoActivity.class.getSimpleName();
    AddressBookAdapter adapter = new AddressBookAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new StickyHeaderLayoutManager());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        getRandomUserLoader().load(this);
    }

    @Override
    public void onRandomUsersDidLoad(List<Person> randomUsers) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setPeople(randomUsers);
    }

    @Override
    public void onRandomUserLoadFailure(final Throwable t) {
        Log.e(TAG, "onRandomUserLoadFailure: Unable to load people, e:" + t.getLocalizedMessage() );

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        Snackbar snackbar = Snackbar.make(recyclerView, "Unable to load addressbook", Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.demo_addressbook_load_error_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomUserLoadError(t.getLocalizedMessage());
            }
        });
        snackbar.show();
    }

    private void showRandomUserLoadError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.demo_addressbook_load_error_dialog_title)
               .setMessage(message)
               .setPositiveButton(android.R.string.ok, null)
               .show();
    }

    private RandomUserLoader getRandomUserLoader() {
        return ((StickyHeadersDemoApp) getApplicationContext()).getRandomUserLoader();
    }
}
