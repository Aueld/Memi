package com.example.epmemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ListData> data;

    public ListAdapter(Context context, ArrayList<ListData> data){
        mContext = context;
        this.data = data;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ListData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);

        TextView inText = (TextView)view.findViewById(R.id.inText);

        inText.setText(data.get(position).getText());

        return view;
    }

    public void notify(ArrayList<ListData> list) {
        this.data = list;
        notifyDataSetChanged();
    }
}
