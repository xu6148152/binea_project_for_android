package com.zepp.www.stickyrecyclerview.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import com.zepp.www.stickyrecyclerview.adapters.SimpleDemoAdapter;

public class SectioningAdapterDemoActivity extends DemoActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SimpleDemoAdapter(5, 5));
    }
}
