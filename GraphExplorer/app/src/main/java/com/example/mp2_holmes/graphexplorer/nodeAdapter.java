package com.example.mp2_holmes.graphexplorer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class nodeAdapter  extends RecyclerView.Adapter<nodeAdapter.nodeViewHolder> {

    private List<nodeInfo> contactList;
    private Context context;

    public nodeAdapter(List<nodeInfo> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(nodeViewHolder contactViewHolder, final int i) {
        nodeInfo ci = contactList.get(i);
        contactViewHolder.vTitle.setText(nodeInfo.Company_Title + ci.title);
        contactViewHolder.vStatus.setText(nodeInfo.Company_Status + ci.status);
        contactViewHolder.vAddress.setText(nodeInfo.Company_Address + ci.address);
        contactViewHolder.vNumber.setText(nodeInfo.Company_Number + ci.number);

        contactViewHolder.vRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeInfo ci = contactList.get(i);
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.MoreDetailActivity(ci.title,ci.number);
            }
        });
    }

    @Override
    public nodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.node, viewGroup, false);

        return new nodeViewHolder(itemView);
    }

    public static class nodeViewHolder extends RecyclerView.ViewHolder {

        protected TextView vStatus;
        protected TextView vAddress;
        protected TextView vNumber;
        protected TextView vTitle;
        protected RelativeLayout vRelativeLayout;

        public nodeViewHolder(View v) {
            super(v);
            vStatus = (TextView) v.findViewById(R.id.txtStatus);
            vAddress = (TextView) v.findViewById(R.id.txtAddress);
            vTitle = (TextView) v.findViewById(R.id.txtTitle);
            vNumber = (TextView) v.findViewById(R.id.txtCompanyNumber);
            vRelativeLayout = (RelativeLayout) v.findViewById(R.id.more_detail);
        }
    }
}
