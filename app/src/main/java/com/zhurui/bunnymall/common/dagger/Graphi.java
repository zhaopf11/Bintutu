package com.zhurui.bunnymall.common.dagger;


import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseFragment;
import com.zhurui.bunnymall.common.BasePresenter;
import com.zhurui.bunnymall.utils.Pager;

/**
 * Dagger2的图接口
 * <p/>
 * Created by stefan on 16/11/2.
 */
public interface Graphi {

    void inject(BaseActivity mainActivity); // 注入BaseActivity

    void inject(BaseFragment baseFragment); // 注入BaseFragment

    void inject(Pager pager); // 注入Pager

    void inject(BasePresenter pager); // 注入BasePresenter
}