package com.jiuxiang.didilogistics.ui.home;

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

import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.FragmentHomeBinding;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;
import com.jiuxiang.didilogistics.ui.postDemand.PostDemandActivity;
import com.jiuxiang.didilogistics.utils.App;

//import com.jiuxiang.didilogistics.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            if (msg.what == 1) {
                System.out.println("更新列表");
                binding.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
                binding.noDataTextview.setVisibility(homeViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
                binding.listview.setVisibility(homeViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        App.setHomeFragmentHandler(handler);


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View root = binding.getRoot();

        binding.setVariable(com.jiuxiang.didilogistics.BR.homeViewModel, homeViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.noDataTextview.setVisibility(homeViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
        binding.listview.setVisibility(homeViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);
        //设置适配器方式和以往不同
        binding.setAdapter(new DemandAdapter(getActivity(), homeViewModel.getData()));//
//        binding.listview.setAdapter(binding.getAdp());
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("orderID", homeViewModel.getData().get(position).getOrderID());
            getActivity().startActivity(intent);
            System.out.println("点击了" + position);

            //点击事件
        });

        if (App.getUser().isType()) {
            binding.fab.setVisibility(View.GONE);
        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostDemandActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}