package marketwatch.com.app.marketwatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pratikb on 07-03-2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
    Context myctx;
    List<NewsData>mydata;

    public Adapter(Context ctx, List<NewsData>data){
        this.myctx=ctx;
        this.mydata=data;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class  Viewholder extends RecyclerView.ViewHolder{

        public Viewholder(View itemView) {
            super(itemView);
        }
    }



}
