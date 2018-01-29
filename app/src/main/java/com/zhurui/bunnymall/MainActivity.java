package com.zhurui.bunnymall;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.cart.fragment.CartFragment;
import com.zhurui.bunnymall.classify.fragment.ClassifyFragment;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.model.bean.Tab;
import com.zhurui.bunnymall.home.bean.ScreenObject;
import com.zhurui.bunnymall.home.bean.VersionBean;
import com.zhurui.bunnymall.home.fragment.HomeFragment;
import com.zhurui.bunnymall.mine.fragment.MineFragment;
import com.zhurui.bunnymall.shoes.fragment.ShoesFragment;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.UserLocalData;
import com.zhurui.bunnymall.viewutils.DialogUtil;
import com.zhurui.bunnymall.viewutils.FragmentTabHost;
import com.zhurui.bunnymall.viewutils.pulltorefresh.pullableview.PullableScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private LayoutInflater mInflater;

    public FragmentTabHost mTabhost;

    private CartFragment cartFragment;

    private MineFragment mineFragment;

    private HomeFragment homeFragment;

    private List<Tab> mTabs = new ArrayList<>(4);
    private long exitTime = 0;
    private int from;
    private Stack<Integer> stack = new Stack<>();
    //栈，存放下标值，模拟fragment返回栈
    ArrayList<Fragment> fragments = new ArrayList<>();
    @Bind(R.id.main_image_center)
    ImageView main_image_center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTab();
        if (Contants.isFirstUpdate) {
            updateVersion();
        }
    }


    private void initTab() {
        stack.clear();
        from = getIntent().getIntExtra("from", 0);
        main_image_center.setImageResource(R.drawable.tab_home_normal);
        Tab tab_home = new Tab(HomeFragment.class, R.string.tab_home, 1);
        Tab tab_classify = new Tab(ClassifyFragment.class, R.string.tab_classify, R.drawable.tab_classify);
        Tab tab_shoe = new Tab(ShoesFragment.class, R.string.tab_shoe, R.drawable.tab_shoe);
        Tab tab_cart = new Tab(CartFragment.class, R.string.tab_cart, R.drawable.tab_cart);
        Tab tab_mine = new Tab(MineFragment.class, R.string.tab_mine, R.drawable.tab_mine);

        mTabs.add(tab_classify);
        mTabs.add(tab_shoe);
        mTabs.add(tab_home);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);


        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.fram_tabcontent);

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabhost.addTab(tabSpec, tab.getFragment(), null);

        }

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (tabId.equals(getString(R.string.tab_cart))) {
                    refData();
                    pushToStack(3);
                }
                if (tabId.equals(getString(R.string.tab_mine))) {
                    minLogin();
                    pushToStack(4);
                }

                if (tabId.equals(getString(R.string.tab_home))) {
                    main_image_center.setImageResource(R.drawable.tab_home_checked);
                    refHomeData();
                    Contants.beforePosition = 2;
                    stack.clear();
                }else{
                    main_image_center.setImageResource(R.drawable.tab_home_normal);
                }

                if (tabId.equals(getString(R.string.tab_classify))) {
                    Contants.beforePosition = 0;
                    pushToStack(0);

                }

                if (tabId.equals(getString(R.string.tab_shoe))) {
                    Contants.beforePosition = 1;
                    pushToStack(1);
                }
            }
        });

        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        if (3 == from) {
            mTabhost.setCurrentTab(3);
        } else {
            mTabhost.setCurrentTab(2);
        }
    }

    private void updateVersion() {
        ScreenObject screenObject = UserLocalData.getScreeObj(BaseApplication.getInstance());
        if (null != screenObject.getVersionlist() && screenObject.getVersionlist().size() > 0) {
            PackageManager pm = MainActivity.this.getPackageManager();
            try {
                PackageInfo pi = pm.getPackageInfo(MainActivity.this.getPackageName(), 0);
                String versionNmae = pi.versionName;
                int versionCode = pi.versionCode;
                VersionBean versionBean = screenObject.getVersionlist().get(0);
                String serverVersion = "";
                String serverVersionCode = "";
                if(versionBean != null){
                    serverVersion = versionBean.getVersion();
                    serverVersionCode = versionBean.getVersionCode();
                }
                if (!TextUtils.isEmpty(serverVersionCode) && versionCode < Integer.parseInt(serverVersionCode)) {
                    boolean isForce = false;
                    if (null != versionBean.getIsforcible() && !"".equals(versionBean.getIsforcible())) {
                        if ("1".equals(versionBean.getIsforcible())) {
                            isForce = true;
                        }
                    }
                    DialogUtil dialogUtil = new DialogUtil();

                    if (isForce) {
                        dialogUtil.updateDialog(MainActivity.this, "检测到新版本,请更新",versionBean.getInfo(), true, false);
                        dialogUtil.dialog.setCancelable(false);
                    } else {
                        dialogUtil.updateDialog(MainActivity.this, "检测到新版本,立即更新吗？",versionBean.getInfo(), true, true);
                        dialogUtil.dialog.setCancelable(true);
                    }

                    dialogUtil.setOnClick(new DialogUtil.OnClick() {
                        @Override
                        public void leftClick() {
                            dialogUtil.dialog.dismiss();
                        }

                        @Override
                        public void rightClick() {
                            Uri uri = Uri.parse(versionBean.getDownloadUrl());
                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(it);
                            dialogUtil.dialog.dismiss();
                        }
                    });
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        Contants.isFirstUpdate = false;
    }

    private void refHomeData() {
        if (null == homeFragment) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab_home));
            if (fragment != null) {
                homeFragment = (HomeFragment) fragment;
                homeFragment.reData();
            }
        } else {
            homeFragment.reData();
        }
    }


    private void refData() {
        if (null == cartFragment) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab_cart));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refData();
            }
        } else {
            cartFragment.refData();
        }

    }


    private void minLogin() {
        if (null == mineFragment) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab_mine));
            if (null != fragment) {
                mineFragment = (MineFragment) fragment;
                mineFragment.getPersonalInfo();
            }
        } else {
            mineFragment.getPersonalInfo();
        }


    }


    private View buildIndicator(Tab tab) {


        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        if((R.string.tab_home == tab.getTitle())){
            text.setVisibility(View.GONE);
        }else{
            img.setBackgroundResource(tab.getIcon());
        }
//        text.setText(tab.getTitle());

        return view;
    }

    @Override
    public void onBackPressed() {
        if (stack.isEmpty()) {
            //返回栈为空，则直接退出
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回键将退出宾兔兔", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();

                System.exit(0);
            }
        } else {
            mTabhost.setCurrentTab(2);
        }
    }

    //压栈
    private void pushToStack(int index) {
        if (stack.isEmpty()) {
            stack.push(index);
        } else {
            int top = stack.peek();
            if (top == index) {

            } else {
                stack.push(index);
            }
        }
    }
}
