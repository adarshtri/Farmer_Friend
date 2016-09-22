package com.thenewboston.kirusa_test.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.thenewboston.kirusa_test.Activities.Group_Update;
import com.thenewboston.kirusa_test.ApplicationContext.MyApplication;
import com.thenewboston.kirusa_test.PojoClasses.officers_group;
import com.thenewboston.kirusa_test.R;
import com.thenewboston.kirusa_test.Volley.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by adarsh on 21/9/16.
 */

public class office_adapter extends RecyclerView.Adapter<office_adapter.ViewHolderOfficer> {

    private LayoutInflater layoutInflater = null;
    private ArrayList<officers_group>  groups = new ArrayList<>();
    VolleySingleton volleySingleton;
    RequestQueue requestQueue ;
    MyApplication myApplication;
    Context context;

    public office_adapter(Context context,ArrayList<officers_group> groups){
        this.context=context;
        volleySingleton = VolleySingleton.getInstance();
        layoutInflater = LayoutInflater.from(context);
        requestQueue = volleySingleton.getRequestQueue();
        myApplication = MyApplication.getInstance();
        this.groups=groups;
    }
    public void setFeed(ArrayList<officers_group> gr) {
        this.groups = gr;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderOfficer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.officer_row_layout, parent, false);
        ViewHolderOfficer viewHolderOfficer = new ViewHolderOfficer(view);
        return viewHolderOfficer;
    }

    @Override
    public void onBindViewHolder(final ViewHolderOfficer holder, final int position) {
            holder.group_name.setText(groups.get(position).getGroup_name()+":"+groups.get(position).getGroup_id());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,Group_Update.class);
                    intent.putExtra("group_name",groups.get(position).getGroup_name());
                    intent.putExtra("group_id",groups.get(position).getGroup_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
    public static class ViewHolderOfficer extends RecyclerView.ViewHolder {


        TextView group_name;
        CardView cardView;

        public ViewHolderOfficer(View itemView) {
            super(itemView);
            group_name = (TextView) itemView.findViewById(R.id.row_layout_group_name_officer);
            cardView = (CardView) itemView.findViewById(R.id.officer_card);
        }
    }
}
