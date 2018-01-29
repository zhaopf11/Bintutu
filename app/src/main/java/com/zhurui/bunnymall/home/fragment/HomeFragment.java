package com.zhurui.bunnymall.home.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.BaseFragment;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.BuyLimitActivity;
import com.zhurui.bunnymall.home.activity.BuyTeamActivity;
import com.zhurui.bunnymall.home.activity.FullOffActivity;
import com.zhurui.bunnymall.home.activity.MessageListActivity;
import com.zhurui.bunnymall.home.activity.NoticeWebViewActivity;
import com.zhurui.bunnymall.home.activity.PersonalCustomizedActivity;
import com.zhurui.bunnymall.home.activity.ProductGridNormalActivity;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.home.activity.ScanActivity;
import com.zhurui.bunnymall.home.activity.SearchActivity;
import com.zhurui.bunnymall.home.activity.WebViewActivity;
import com.zhurui.bunnymall.home.adapter.GuessRecommandAdapter;
import com.zhurui.bunnymall.home.adapter.HomeBannerAdapter;
import com.zhurui.bunnymall.home.adapter.HotRecommandAdapter;
import com.zhurui.bunnymall.home.adapter.RecommandAdapter;
import com.zhurui.bunnymall.home.adapter.TestNormalAdapter;
import com.zhurui.bunnymall.home.bean.BannerInfo;
import com.zhurui.bunnymall.home.bean.GuessProductInfo;
import com.zhurui.bunnymall.home.bean.ListObject;
import com.zhurui.bunnymall.home.bean.NewProduct;
import com.zhurui.bunnymall.home.bean.RecommandProduct;
import com.zhurui.bunnymall.home.msg.HomeResp;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.GridViewInScrollView;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;
import com.zhurui.bunnymall.viewutils.MyDropDownMenu;
import com.zhurui.bunnymall.viewutils.ScreenDialog;
import com.zhurui.bunnymall.viewutils.SpaceItemDecoration;
import com.zhurui.bunnymall.viewutils.pulltorefresh.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {

    private static final String TAG = "HomeFragment";

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.edit_search)
    TextView edit_search;
    @Bind(R.id.imgbtn_right)
    ImageButton imgbtn_right;
    @Bind(R.id.sliderLayout)
    RollPagerView sliderLayout;
    @Bind(R.id.recycler_banner)
    RecyclerView recycler_banner;
    @Bind(R.id.recycler_newrecommand)
    RecyclerView recyclerView;
    private RecommandAdapter adapter;
    private HomeBannerAdapter homeBannerAdapter;
    @Bind(R.id.grid_guessrecommand)
    GridViewInScrollView guessrecommand;
    private GuessRecommandAdapter gridAdapter;
    @Bind(R.id.rel_newproduct)
    RelativeLayout rel_newproduct;
    @Bind(R.id.rel_guess)
    RelativeLayout rel_guess;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout mPullToRefreshLayout;
    @Bind(R.id.loadmore_view)
    RelativeLayout loadmore_view;
    private List<GuessProductInfo> guessProductInfos = null;
    private List<NewProduct> newProductList = null;
    private List<RecommandProduct> recommandProducts = null;
    private List<ListObject> listObjects = null;
    public List<BannerInfo> bannerInfo;
    public List<BannerInfo> bannerBehindInfo;

    boolean refresh = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        Contants.shopOrderID = null;
        initView();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        //清除列表界面选中的筛选条件
        ScreenDialog.checkMap = new HashMap<Integer, String>();
        ScreenDialog.childCheckMap = new HashMap<Integer, Integer>();
        ScreenDialog.strings = null;
        ScreenDialog.radioString = "";
        MyDropDownMenu.checkInfo = "";
        MyDropDownMenu.isPrice = false;
        MyDropDownMenu.priceoder = 0;
        MyDropDownMenu.price = -1;
        SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isSeletceNum", 0);
        editor.commit();
    }

    public void reData() {
        if (null == listObjects || listObjects.size() < 1) {
            initData();
        }
    }

    private void initView() {
        mPullToRefreshLayout.setOnRefreshListener(this);
        loadmore_view.setVisibility(View.GONE);
        imgbtn_back.setImageResource(R.drawable.sweep);
        imgbtn_back.setVisibility(View.GONE);
        imgbtn_right.setImageResource(R.drawable.message);
        recyclerView.setFocusable(false);

    }

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "10");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID() + "");
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            getHomeData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getHomeData(Map<String, Object> params) {
        if (mHttpHelper != null) {
            mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<HomeResp>(getActivity(),"home") {
                @Override
                public void onSuccess(Response response, HomeResp homeResp) {
                    if (homeResp != null) {
                        if (homeResp.getResult() > 0) {
                            listObjects = homeResp.getList();
                            initSlider(homeResp.getList().get(0).getBannerInfo());
                            initStoreView(homeResp.getList().get(0).getBannerBehindInfo());
                            initRecyclerView(homeResp.getList().get(0).getNewProduct());
                            initGridView(homeResp.getList().get(0).getGuessProduct());
                            bannerInfo = homeResp.getList().get(0).getBannerInfo();
                            bannerBehindInfo = homeResp.getList().get(0).getBannerBehindInfo();
                            guessProductInfos = homeResp.getList().get(0).getGuessProduct();
                            newProductList = homeResp.getList().get(0).getNewProduct();
                            recommandProducts = homeResp.getList().get(0).getRecommendProducts();
                        } else {
                            ToastUtils.show(BaseApplication.getInstance(), homeResp.getRetmsg());
                        }
                        if (refresh) {
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    } else {
                        if (refresh) {
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e) {
                    if (refresh) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
                }

                @Override
                public void onServerError(Response response, int code, String errmsg) {
                    if (refresh) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    super.onFailure(request, e);
                    if (refresh) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                }
            });
        }
    }


    private void initSlider(List<BannerInfo> bannerInfos) {
        if(sliderLayout != null) {
            sliderLayout.setPlayDelay(3000);
            //设置透明度
            sliderLayout.setAnimationDurtion(500);
            //设置适配器
            sliderLayout.setAdapter(new TestNormalAdapter(bannerInfos));
            if (null != bannerInfos && bannerInfos.size() > 1) {
                sliderLayout.setHintView(new ColorPointHintView(BaseApplication.getInstance(), getResources().getColor(R.color.color_d2ba91), getResources().getColor(R.color.gray)));
            } else {
                sliderLayout.setHintView(null);
            }
            sliderLayout.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (bannerInfo != null && bannerInfos.size() > 0 && !TextUtils.isEmpty(bannerInfo.get(position).getLink())) {
                        Intent intent = new Intent(BaseApplication.getInstance(), NoticeWebViewActivity.class);
                        intent.putExtra("url", bannerInfo.get(position).getLink());
                        intent.putExtra("title", bannerInfo.get(position).getTitle());
                        startActivity(intent, false);
                    }
                }
            });
        }
    }

    /**
     * 广告banner
     *
     * @param bannerInfos
     */
    private void initStoreView(List<BannerInfo> bannerInfos) {
        if (recycler_banner != null && null != bannerInfos && bannerInfos.size() > 0) {
            recycler_banner.setVisibility(View.VISIBLE);
            //解决recyclerview 卡顿的问题
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            recycler_banner.setLayoutManager(linearLayoutManager);
            recycler_banner.addItemDecoration(new SpaceItemDecoration(0));
            //设置适配器
            homeBannerAdapter = new HomeBannerAdapter(BaseApplication.getInstance(), false, bannerInfos);
            homeBannerAdapter.setOnItemClickLitener(new HomeBannerAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(BaseApplication.getInstance(), NoticeWebViewActivity.class);
                    if (bannerBehindInfo != null && bannerBehindInfo.size() > 0 && !TextUtils.isEmpty(bannerBehindInfo.get(position).getLink())) {
                        intent.putExtra("url", Contants.PRODUCT_DETAIL_URL + bannerBehindInfo.get(position).getBannerurl());
                        intent.putExtra("title", bannerBehindInfo.get(position).getTitle());
                        startActivity(intent, false);
                    }
                }
            });
            recycler_banner.setAdapter(homeBannerAdapter);
        }

    }

    /**
     * 精品推荐
     *
     * @param newProducts
     */
    private void initRecyclerView(List<NewProduct> newProducts) {
        if(rel_newproduct != null && recyclerView != null) {
            if (null != newProducts && newProducts.size() > 0) {
                rel_newproduct.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance()) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
//            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                //设置适配器
                adapter = new RecommandAdapter(BaseApplication.getInstance(), false, newProducts);
                adapter.setOnItemClickLitener(new RecommandAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(newProductList.get(position).getProductID())), false);
                    }
                });
                recyclerView.setAdapter(adapter);
            } else {
                rel_newproduct.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 热卖推荐
     *
     * @param guessProductInfos
     */
    private void initGridView(List<GuessProductInfo> guessProductInfos) {
        if(rel_guess != null && guessrecommand != null) {
            if (null != guessProductInfos && guessProductInfos.size() > 0) {
                rel_guess.setVisibility(View.VISIBLE);
                guessrecommand.setVisibility(View.VISIBLE);
                gridAdapter = new GuessRecommandAdapter(BaseApplication.getInstance(), guessProductInfos, false);
                gridAdapter.setOnItemClickLitener(new GuessRecommandAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(guessProductInfos.get(position).getProductID())), false);
                    }
                });
                guessrecommand.setAdapter(gridAdapter);
            } else {
                rel_guess.setVisibility(View.GONE);
                guessrecommand.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.edit_search)
    public void toSearch() {
        startActivity(new Intent(BaseApplication.getInstance(), SearchActivity.class), false);

    }

    @OnClick(R.id.imgbtn_right)
    public void toMessageList() {
        startActivity(new Intent(BaseApplication.getInstance(), MessageListActivity.class), true);

    }

    @OnClick(R.id.imgbtn_back)
    public void toScan() {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //先判断有没有权限 ，没有就在这里进行权限的申请
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CAMERA}, 0);

        } else {
            //说明已经获取到摄像头权限了 想干嘛干嘛
            startActivity(new Intent(BaseApplication.getInstance(), ScanActivity.class), false);

        }
    }

//    @OnClick(R.id.lin_newrecommand)
//    public void toNewRecommand() {
//        Intent intent = new Intent(BaseApplication.getInstance(), ProductGridNormalActivity.class);
//        intent.putExtra("productlist", (Serializable) newProductList);
//        intent.putExtra("from", 2);
//        intent.putExtra("state", 1);
//        startActivity(intent, false);
//    }

//    @OnClick(R.id.lin_guessrecommand)
//    public void toGuessRecommand() {
//        Intent intent = new Intent(BaseApplication.getInstance(), ProductGridNormalActivity.class);
//        intent.putExtra("productlist", (Serializable) guessProductInfos);
//        intent.putExtra("from", 4);
//        startActivity(intent, false);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh = true;
        initData();
        mPullToRefreshLayout = pullToRefreshLayout;
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPullToRefreshLayout = pullToRefreshLayout;
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }
}
