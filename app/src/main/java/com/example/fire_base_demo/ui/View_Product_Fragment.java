package com.example.fire_base_demo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fire_base_demo.Adapter.ViewAdapter;
import com.example.fire_base_demo.R;


public class View_Product_Fragment extends Fragment {

ViewAdapter adapter;
RecyclerView recyclerView;


    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);


        recyclerView = view.findViewById(R.id.recycler);



       return view;
    }


}