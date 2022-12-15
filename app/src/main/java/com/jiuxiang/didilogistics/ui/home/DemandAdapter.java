package com.jiuxiang.didilogistics.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.databinding.DemandItemBinding;

import java.util.List;
import java.util.Objects;

public class DemandAdapter extends BaseAdapter {
    Context context;
    public List<Demand> data;
    private DemandItemBinding binding;

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
//        @SuppressLint("ViewHolder") ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.demand_item, parent, false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.demand_item, parent, false);
        binding.setVariable(BR.demandBean, Objects.requireNonNull(data).get(position));
        if (data.get(position).getState().equals("已取消")) {
            TextView textView = binding.state;
            textView.setTextColor(context.getResources().getColor(R.color.grey_order_cancel));
        } else if (data.get(position).getState().equals("已完成")) {
            TextView textView = binding.state;
            textView.setTextColor(context.getResources().getColor(R.color.green_success));
        }

        return binding.getRoot();
    }
}
