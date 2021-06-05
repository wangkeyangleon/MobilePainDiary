package com.example.mobilepaindiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.adapter.RecyclerViewAdapter;
import com.example.mobilepaindiary.databinding.DailyRecordFragmentBinding;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;

import java.util.List;
// display the data in a recycle view
public class DailyRecordFragment extends Fragment {
    private DailyRecordFragmentBinding binding;
    private PainRecordViewModel painRecordViewModel;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public DailyRecordFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DailyRecordFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

//         creating androidViewModel  that accepts the application as a parameter
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(PainRecordViewModel.class);

        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
//                String all = "";
//                for (PainRecord painRecord:painRecords){
//                    String detail = painRecord.toString();
//                    all = all +System.getProperty("line.separator")+detail;
//                }
//                binding.textMessage.setText(all);
            //instantiate RecyclerView in the activity and set an adapter for it
                adapter = new RecyclerViewAdapter(painRecords);
//                creates a line divider between rows
                binding.recycleView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                binding.recycleView.setAdapter(adapter);
//LayoutManager is responsible for measuring and positioning item views within a RecyclerView,
// and determining the policy for recycling items
                layoutManager = new LinearLayoutManager(getContext());
                binding.recycleView.setLayoutManager(layoutManager);

            }
        });


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
