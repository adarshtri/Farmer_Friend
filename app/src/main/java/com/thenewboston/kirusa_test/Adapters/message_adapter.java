package com.thenewboston.kirusa_test.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.thenewboston.kirusa_test.ApplicationContext.MyApplication;
import com.thenewboston.kirusa_test.PojoClasses.message_object;
import com.thenewboston.kirusa_test.R;
import com.thenewboston.kirusa_test.Volley.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by adarsh on 22/9/16.
 */

public class message_adapter extends RecyclerView.Adapter<message_adapter.ViewHolderFarmer>{

    private LayoutInflater layoutInflater = null;
    private ArrayList<message_object> messages = new ArrayList<>();
    VolleySingleton volleySingleton;
    RequestQueue requestQueue ;
    MyApplication myApplication;
    Context context;

    public message_adapter (Context context, ArrayList<message_object> messages){
        this.context=context;
        volleySingleton = VolleySingleton.getInstance();
        layoutInflater = LayoutInflater.from(context);
        requestQueue = volleySingleton.getRequestQueue();
        myApplication = MyApplication.getInstance();
        this.messages = messages;
    }
    public void setFeed(ArrayList<message_object> mo) {
        this.messages = mo;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolderFarmer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.farmer_row_layout, parent, false);
        message_adapter.ViewHolderFarmer viewHolderFarmer = new message_adapter.ViewHolderFarmer(view);
        return viewHolderFarmer;
    }

    @Override
    public void onBindViewHolder(ViewHolderFarmer holder, int position) {
            holder.message.setText(messages.get(position).getMessage());
            holder.group_id.setText(messages.get(position).getGroup_id());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolderFarmer extends RecyclerView.ViewHolder{
        TextView message,group_id;
        public ViewHolderFarmer(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.farmer_group_message);
            group_id = (TextView) itemView.findViewById(R.id.farmer_group_id);
        }
    }
}
