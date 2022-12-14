package com.jiuxiang.didilogistics.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.beans.Demand;

import java.util.List;
import java.util.Objects;

public class DemandAdapter extends BaseAdapter {
    Context context;
    public List<Demand> data;

    public DemandAdapter(Context context, List<Demand> objects) {
        super();
        this.context = context;
        data = objects;
    }

    @Override
    public int getCount() {
        return Objects.requireNonNull(data).size();
    }

    @Override
    public Object getItem(int position) {
        return Objects.requireNonNull(data).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.demand_item, parent, false);
        binding.setVariable(BR.demandBean, Objects.requireNonNull(data).get(position));
        return binding.getRoot();
    }
}
