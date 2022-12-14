package com.jiuxiang.didilogistics.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.beans.Message;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends BaseAdapter {
    Context context;
    public List<Message> data;

    public MessageAdapter(Context context, List<Message> objects) {
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
        @SuppressLint("ViewHolder") ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.message_item, parent, false);
        binding.setVariable(BR.message, Objects.requireNonNull(data).get(position));
        return binding.getRoot();
    }
}
