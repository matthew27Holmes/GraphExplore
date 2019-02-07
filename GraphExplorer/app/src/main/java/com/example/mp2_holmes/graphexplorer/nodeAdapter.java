package com.example.mp2_holmes.graphexplorer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class nodeAdapter  extends RecyclerView.Adapter<nodeAdapter.nodeViewHolder> {

        private List<nodeInfo> contactList;

        public nodeAdapter(List<nodeInfo> contactList) {
            this.contactList = contactList;
        }


        @Override
        public int getItemCount() {
            return contactList.size();
        }

        @Override
        public void onBindViewHolder(nodeViewHolder contactViewHolder, int i) {
            nodeInfo ci = contactList.get(i);
            contactViewHolder.vName.setText(ci.name);
            contactViewHolder.vSurname.setText(ci.surname);
            contactViewHolder.vEmail.setText(ci.email);
            contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);
        }


        @Override
        public nodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.node, viewGroup, false);

            return new nodeViewHolder(itemView);
        }

        public static class nodeViewHolder extends RecyclerView.ViewHolder {

            protected TextView vName;
            protected TextView vSurname;
            protected TextView vEmail;
            protected TextView vTitle;

            public nodeViewHolder(View v) {
                super(v);
                vName =  (TextView) v.findViewById(R.id.txtName);
                vSurname = (TextView)  v.findViewById(R.id.txtSurname);
                vEmail = (TextView)  v.findViewById(R.id.txtEmail);
                vTitle = (TextView) v.findViewById(R.id.title);
            }
        }
    }
}
