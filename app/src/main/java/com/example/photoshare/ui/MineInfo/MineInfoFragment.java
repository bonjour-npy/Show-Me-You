package com.example.photoshare.ui.MineInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.photoshare.R;
import com.example.photoshare.activity.AddShareActivity;
import com.example.photoshare.activity.UploadActivity;
import com.example.photoshare.adapter.MinePhotoAdapter;
import com.example.photoshare.databinding.FragmentNotificationsBinding;
import com.example.photoshare.model.caogao.RecordsBean;

import java.util.ArrayList;
import java.util.List;

public class MineInfoFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private List<RecordsBean> list = new ArrayList<>();

    private static final String TAG = "NotificationsFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sh = getActivity().getSharedPreferences("user", 0);
        String user_id = sh.getString("id", "未找到用户ID");
        Log.d(TAG, "onCreateView: " + user_id);

        MineInfoViewModel mineInfoViewModel =
                new ViewModelProvider(this, new MineInfoViewModelFactory(user_id)).get(MineInfoViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Glide.with(this)
                .load(R.drawable.icon)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(binding.avatarIcon);

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadActivity.class);
                startActivity(intent);
            }
        });

        binding.addShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddShareActivity.class);
                startActivity(intent);
            }
        });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        MinePhotoAdapter minePhotoAdapter = new MinePhotoAdapter();
        binding.recyclerView.setAdapter(minePhotoAdapter);
        mineInfoViewModel.getArryList().observe(getViewLifecycleOwner(), new Observer<List<RecordsBean>>() {
            @Override
            public void onChanged(List<RecordsBean> list) {
                minePhotoAdapter.setSharelist(list);
                minePhotoAdapter.notifyDataSetChanged();
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