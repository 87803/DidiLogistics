package com.jiuxiang.didilogistics.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.adapter.MessageAdapter;
import com.jiuxiang.didilogistics.databinding.FragmentNotificationsBinding;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;
import com.jiuxiang.didilogistics.utils.App;

//import com.jiuxiang.didilogistics.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            if (msg.what == 1) {
                binding.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
                binding.noDataTextView.setVisibility(notificationsViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
                binding.listview.setVisibility(notificationsViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        App.setNotificationFragmentHandler(handler);
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        View root = binding.getRoot();
        binding.setVariable(BR.notificationViewModel, notificationsViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.noDataTextView.setVisibility(notificationsViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
        binding.listview.setVisibility(notificationsViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);

        binding.setAdapter(new MessageAdapter(getActivity(), notificationsViewModel.getData()));
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("orderID", notificationsViewModel.getData().get(position).getOrderID());
            requireActivity().startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}