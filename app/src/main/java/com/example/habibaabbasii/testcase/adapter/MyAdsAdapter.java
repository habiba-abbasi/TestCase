package com.example.habibaabbasii.testcase.adapter;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habibaabbasii.testcase.R;
import com.example.habibaabbasii.testcase.databinding.ItemBinding;
import com.example.habibaabbasii.testcase.model.UserModel;

import java.util.List;

/**
 * Created by Roshaann 2.7 gpa on 15/04/2018.
 */

public class MyAdsAdapter extends RecyclerView.Adapter<MyAdsAdapter.MyAdsAdapterViewHolder>{

List<UserModel> list;
ItemBinding binding;
    @Override
    public MyAdsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item,parent,false);

        return new MyAdsAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyAdsAdapterViewHolder holder, int position) {

        binding.avatar.setImageURI(Uri.parse(list.get(position).getImage()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyAdsAdapterViewHolder extends RecyclerView.ViewHolder{

        public MyAdsAdapterViewHolder(ItemBinding binding) {
            super(binding.getRoot());
        }
    }
}
