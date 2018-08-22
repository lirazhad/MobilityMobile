package website.giffo.mobilitymobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import website.giffo.mobilitymobile.R;
import website.giffo.mobilitymobile.objects.Taxi;

public class TaxiListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Variables

    private Context mContext;
    private ArrayList<Taxi> mTaxis;

    public TaxiListAdapter(Context context, ArrayList<Taxi> taxis){
        this.mContext = context;
        this.mTaxis = taxis;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TaxiListAdapter.TaxiListAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.taxi_item, parent, false));
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        TaxiListAdapter.TaxiListAdapterViewHolder viewHolder = (TaxiListAdapter.TaxiListAdapterViewHolder) holder;

        Taxi taxi = mTaxis.get(position);

        viewHolder.taxi_ic.setImageDrawable(mContext.getResources().getDrawable(taxi.getmTaxiIcon()));
        viewHolder.taxiName.setText(taxi.getmTaxiName());

        int intEta = taxi.getmEta();
        String eta;

        //handle hours
        if (intEta > 59){
            eta = intEta/60 + "h " + intEta%60;
        }else {
            eta = String.valueOf(taxi.getmEta());
        }

        viewHolder.taxiEta.setText(eta+"m");

    }

    @Override
    public int getItemCount() {

        return mTaxis.size();
    }

    private class TaxiListAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView taxi_ic;
        TextView taxiName;
        TextView taxiEta;

        TaxiListAdapterViewHolder(View itemView) {
            super(itemView);

            // get views from layout
            taxi_ic = itemView.findViewById(R.id.taxi_list_taxi_ic_image);
            taxiName = itemView.findViewById(R.id.taxi_list_taxi_name_text_view);
            taxiEta = itemView.findViewById(R.id.taxi_list_eta_text_view);

        }


    }

}
