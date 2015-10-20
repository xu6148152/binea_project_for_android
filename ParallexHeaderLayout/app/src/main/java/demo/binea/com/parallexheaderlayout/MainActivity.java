package demo.binea.com.parallexheaderlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();

    private BounceScroller scroller;

    private View mHeaderView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        scroller = (BounceScroller) findViewById(R.id.bounce_scroll);
        scroller.enableHeader(true);
        mHeaderView = findViewById(R.id.iv_header);
        scroller.setParallexHeaderView(mHeaderView);
        //scroller.setCanBounce(false);
        GridLayoutManager llm = new GridLayoutManager(this, 2);
        llm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                if (position == RecyclerAdapter.TYPE_HEADER) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MarginDecoration(this));
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int headerTop = 0;
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                headerTop += dy;
                mHeaderView.setTranslationY(-headerTop / 2);
            }
        });
    }

    static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_HEADER){
                //HEADER
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_header_layout, null));
            }else{
                //ITEM
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, null));
            }
        }

        @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(position > 0){
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.mTextView.setText(String.valueOf(position - 1));
            }else{

            }
        }

        @Override public int getItemCount() {
            return 50;
        }

        @Override public int getItemViewType(int position) {
            if(position == 0){
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView mTextView;
            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.tv_number);
            }
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder{

            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
