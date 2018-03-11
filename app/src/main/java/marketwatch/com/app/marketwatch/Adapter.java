package marketwatch.com.app.marketwatch;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pratikb on 07-03-2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
    Context myctx;
    List<NewsData> mydata;

    public Adapter(Context ctx, List<NewsData> data) {
        this.myctx = ctx;
        this.mydata = data;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lists, null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {


        holder.tv_stockname.setText(mydata.get(position).getStorename());
        holder.tv_posteddate.setText(mydata.get(position).getCreate_date());
        holder.tv_buyprice.setText("BUY @" + mydata.get(position).getBuyprice());
        if (mydata.get(position).getStoploss().isEmpty()) {
            holder.tv_stoploss.setText("STOPLOSS " + "-");
        } else {
            holder.tv_stoploss.setText("STOPLOSS " + mydata.get(position).getStoploss());
        }

        if (mydata.get(position).getTarget1().isEmpty()) {
            holder.tv_target_one.setText("-");
        } else {
            holder.tv_target_one.setText("@" + mydata.get(position).getTarget1());
        }

        if (mydata.get(position).getTarget2().isEmpty()) {
            holder.tv_target_two.setText("-");
        } else {
            holder.tv_target_two.setText("@" + mydata.get(position).getTarget2());
        }

        if (mydata.get(position).getTarget3().isEmpty()) {
            holder.tv_target_three.setText("-");
        } else {
            holder.tv_target_three.setText("@" + mydata.get(position).getTarget3());
        }


        if (mydata.get(position).getType().equalsIgnoreCase("buy")) {
            holder.tv_type.setText("BUY CALL");
        }
        holder.tv_duration.setText(mydata.get(position).getDuration());
        if (mydata.get(position).getDuration().contains("Hit")) {
            holder.tv_duration.setBackgroundColor(ContextCompat.getColor(myctx, android.R.color.holo_red_dark));
        } else if (mydata.get(position).getDuration().contains("days")) {
            holder.tv_duration.setBackgroundColor(ContextCompat.getColor(myctx, android.R.color.darker_gray));

        } else {
            holder.tv_duration.setBackgroundColor(ContextCompat.getColor(myctx, android.R.color.holo_green_dark));

        }


    }

    @Override
    public int getItemCount() {
        return mydata.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_stockname, tv_posteddate, tv_buyprice, tv_stoploss, tv_target_one, tv_target_two, tv_target_three, tv_type, tv_duration;
        private ImageView iv_share;

        public Viewholder(final View itemView) {
            super(itemView);
            tv_stockname = (TextView) itemView.findViewById(R.id.tv_stockname);
            tv_posteddate = (TextView) itemView.findViewById(R.id.tv_posteddate);
            tv_buyprice = (TextView) itemView.findViewById(R.id.tv_buyprice);
            tv_stoploss = (TextView) itemView.findViewById(R.id.tv_stoploss);
            tv_target_one = (TextView) itemView.findViewById(R.id.tv_target_one);
            tv_target_two = (TextView) itemView.findViewById(R.id.tv_target_two);
            tv_target_three = (TextView) itemView.findViewById(R.id.tv_target_three);
            tv_type = (TextView) itemView.findViewById(R.id.tv_call);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_status);
            iv_share = (ImageView) itemView.findViewById(R.id.iv_share);
            iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = mydata.get(getAdapterPosition()).getStorename();
                    if (!mydata.get(getAdapterPosition()).getBuyprice().isEmpty()) {
                        msg += " Buy@" + mydata.get(getAdapterPosition()).getBuyprice() + "";
                    }
                    if (!mydata.get(getAdapterPosition()).getStoploss().isEmpty() || mydata.get(getAdapterPosition()).getStoploss().isEmpty()) {
                        msg += " stoploss@" + mydata.get(getAdapterPosition()).getStoploss() + "";
                    }
                    if (!mydata.get(getAdapterPosition()).getTarget1().isEmpty() || !mydata.get(getAdapterPosition()).getTarget1().contains("-")) {
                        msg += " target1@" + mydata.get(getAdapterPosition()).getTarget1() + "";
                    }
                    if (!mydata.get(getAdapterPosition()).getTarget2().isEmpty() || !mydata.get(getAdapterPosition()).getTarget2().contains("-")) {
                        msg += " target2@" + mydata.get(getAdapterPosition()).getTarget2() + "";
                    }
                    if (!mydata.get(getAdapterPosition()).getTarget3().isEmpty() || !mydata.get(getAdapterPosition()).getTarget3().contains("-")) {
                        msg += " target3@" + mydata.get(getAdapterPosition()).getTarget3() + "";
                    }

                    Log.d("msg",msg);


                    getBitmapFromView(msg);
                }
            });
        }
    }

    public void getBitmapFromView(String txt) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, txt);
        sendIntent.setType("text/plain");
        myctx.startActivity(sendIntent);
    }


}
