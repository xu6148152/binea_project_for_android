package demo.binea.com.parallexheaderlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "can vertical scroll " + recyclerView.canScrollVertically(1));
    }

    static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0){
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
            }
        }

        @Override public int getItemCount() {
            return 50;
        }

        @Override public int getItemViewType(int position) {
            if(position == 0){
                return 0;
            }
            return 1;
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
