package com.zhurui.bunnymall.shoes.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseFragment;
import com.zhurui.bunnymall.shoes.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.shihao.library.XRadioGroup;


/**
 * 鞋趣
 */
public class ShoesFragment extends BaseFragment implements XRadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.radio_shoes_interset)
    RadioButton radio_shoes_interset;
    @Bind(R.id.radio_craftsman_interview)
    RadioButton radio_craftsman_interview;
    @Bind(R.id.radiogroup_shoes)
    XRadioGroup radiogroup_shoes;

    private ArrayList<Object> items=new ArrayList<Object>();
    private ViewPagerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoes, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        initView();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        items.add(new OneFragment());
        items.add(new TwoFragment());

        adapter=new ViewPagerAdapter(getActivity().getSupportFragmentManager(), items);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setOffscreenPageLimit(items.size());
        viewPager.setOnPageChangeListener(this);
        radiogroup_shoes.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(XRadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radio_shoes_interset:
                viewPager.setCurrentItem(0);
                break;
            case R.id.radio_craftsman_interview:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radio_shoes_interset.setChecked(true);
                break;
            case 1:
                radio_craftsman_interview.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
