package com.example.mobilepaindiary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.ReportsFragmentBinding;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReportsFragment extends Fragment implements View.OnClickListener {
    private ReportsFragmentBinding binding;
    private static String TAG = ReportsFragment.class.getName();
    private LinearLayout lin_sub1, lin_sub2, lin_sub3;

    private Fragment subFragment1;
    private Fragment subFragment2;
    private Fragment subFragment3;
    private ImageView mTabline;
    private int mScreen1_3;


    public ReportsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ReportsFragmentBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.reports_fragment, null);
        initView(view);
        setLinstener();
        initData();
        return view;
    }

    private void initView(View view) {
        lin_sub1 = (LinearLayout) view.findViewById(R.id.lin_sub1);
        lin_sub2 = (LinearLayout) view.findViewById(R.id.lin_sub2);
        lin_sub3 = (LinearLayout) view.findViewById(R.id.lin_sub3);
        mTabline = (ImageView) view.findViewById(R.id.imv_tabline);

    }

    protected void initData() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mScreen1_3 = displayMetrics.widthPixels / 3;
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)mTabline.getLayoutParams();
        layoutParams.width = mScreen1_3;
        mTabline.setLayoutParams(layoutParams);

//        first display
        setSubFragment(0);
        setmTabline(0);
    }

    protected void setLinstener() {
        lin_sub1.setOnClickListener(this);
        lin_sub2.setOnClickListener(this);
        lin_sub3.setOnClickListener(this);

    }

    public void setmTabline(int i) {

        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
                .getLayoutParams();
        lp.leftMargin = i * mScreen1_3;
        mTabline.setLayoutParams(lp);

    }

//    set each sub-fragment content
    public void setSubFragment(int i) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (0 == i) {
            subFragment1 = (subFragment1 == null ? new SubFragment1() : subFragment1);
            transaction.replace(R.id.id_content, subFragment1);
            //	transaction.addToBackStack(null);
            transaction.commit();

        } else if (1 == i) {
            subFragment2 = (subFragment2 == null ? new SubFragment2() : subFragment2);
            transaction.replace(R.id.id_content, subFragment2);
            //	transaction.addToBackStack(null);
            transaction.commit();
        } else if (2 == i) {
            subFragment3 = (subFragment3 == null ? new SubFragment3() : subFragment3);
            transaction.replace(R.id.id_content, subFragment3);
            //	transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_sub1:
                setSubFragment(0);
                setmTabline(0);
                break;
            case R.id.lin_sub2:
                setSubFragment(1);
                setmTabline(1);
                break;
            case R.id.lin_sub3:
                setSubFragment(2);
                setmTabline(2);
                break;
            default:
                break;
        }

    }
}
