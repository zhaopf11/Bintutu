package com.zhurui.bunnymall.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.adapter.CarouselAdapter;
import com.zhurui.bunnymall.home.adapter.GuessRecommandAdapter;
import com.zhurui.bunnymall.home.adapter.HotRecommandAdapter;
import com.zhurui.bunnymall.home.adapter.RecommandAdapter;
import com.zhurui.bunnymall.home.bean.BannerInfo;
import com.zhurui.bunnymall.home.bean.CarouselBean;
import com.zhurui.bunnymall.home.bean.StoreListProductBean;
import com.zhurui.bunnymall.home.bean.StoreObject;
import com.zhurui.bunnymall.home.msg.SearchResp;
import com.zhurui.bunnymall.home.msg.StoreListProductRespMsg;
import com.zhurui.bunnymall.home.msg.StoreRespMsg;
import com.zhurui.bunnymall.home.msg.TeamBuyResp;
import com.zhurui.bunnymall.mine.activity.MyWalletActivity;
import com.zhurui.bunnymall.mine.msg.WalletRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.GridViewInScrollView;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;
import com.zhurui.bunnymall.viewutils.MyDropDownMenu;
import com.zhurui.bunnymall.viewutils.SpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

public class StoreHomeActivity extends BaseActivity {


    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.edit_search)
    TextView edit_search;
    @Bind(R.id.imgbtn_right)
    ImageButton imgbtn_right;
    @Bind(R.id.img_classify)
    ImageView img_classify;

    @Bind(R.id.img_store)
    NetworkImageView img_store;
    @Bind(R.id.text_storename)
    TextView text_storename;
    @Bind(R.id.text_followcount)
    TextView text_followcount;
    @Bind(R.id.text_follow)
    TextView text_follow;

    @Bind(R.id.lin_diamond)
    LinearLayout lin_diamond;
    @Bind(R.id.img_diamond1)
    ImageView img_diamond1;
    @Bind(R.id.img_diamond2)
    ImageView img_diamond2;
    @Bind(R.id.img_diamond3)
    ImageView img_diamond3;
    @Bind(R.id.img_diamond4)
    ImageView img_diamond4;
    @Bind(R.id.img_diamond5)
    ImageView img_diamond5;
    @Bind(R.id.radiogroup_store)
    RadioGroup radiogroup_store;
    @Bind(R.id.lin_storehome)
    ScrollView lin_storehome;
    @Bind(R.id.img_storehome)
    RollPagerView img_storehome;
    @Bind(R.id.lin_specialprice)
    LinearLayout lin_specialprice;
    @Bind(R.id.lin_hotsales)
    LinearLayout lin_hotsales;
    @Bind(R.id.lin_newrecommand)
    LinearLayout lin_newrecommand;
    @Bind(R.id.recycler_newrecommand)
    RecyclerView recycler_newrecommand;
    private RecommandAdapter adapter;
    @Bind(R.id.lin_hotrecommand)
    LinearLayout lin_hotrecommand;
    @Bind(R.id.recycler_hotrecommand)
    GridView recycler_hotrecommand;
    private HotRecommandAdapter hotadapter;

    @Bind(R.id.myDropDownMenu)
    MyDropDownMenu myDropDownMenu;
    @Bind(R.id.grid_allproduct)
    GridView grid_allproduct;

    @Bind(R.id.rel_product)
    RelativeLayout rel_product;
    @Bind(R.id.rel_hotproduct)
    RelativeLayout rel_hotproduct;

    private GuessRecommandAdapter guessRecommandAdapter;

    private String supplierid;
    private ImageLoader imageLoader;
    private List<StoreListProductBean> storeListProductBeen = null;
    private StoreObject storeObject;
    private boolean isFollow = false;
    private CarouselAdapter carouselAdapter = null;
    private List<CarouselBean> carouselBeens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_home);
        ButterKnife.bind(this);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        initView();
        initData();
    }

    private void initView() {
        imgbtn_right.setImageResource(R.drawable.message);
        lin_storehome.setVisibility(View.VISIBLE);
        img_classify.setVisibility(View.VISIBLE);
        myDropDownMenu.btn_screen.setText("新品");
        myDropDownMenu.setVisibility(View.GONE);
        grid_allproduct.setVisibility(View.GONE);
        radiogroup_store.setOnCheckedChangeListener(checkedChangeListener);
        recycler_newrecommand.setFocusable(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_newrecommand.setLayoutManager(linearLayoutManager);
        recycler_newrecommand.addItemDecoration(new SpaceItemDecoration(20));
        //设置适配器
        adapter = new RecommandAdapter(BaseApplication.getInstance(), true, null);
        adapter.setOnItemClickLitener(new RecommandAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null != storeObject.getHostProduct() && storeObject.getHostProduct().size() > 0) {
                    startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(storeObject.getHostProduct().get(position).getProductID())), false);
                }
            }
        });
        recycler_newrecommand.setAdapter(adapter);


        //设置适配器
        hotadapter = new HotRecommandAdapter(BaseApplication.getInstance(), null, true);
        recycler_hotrecommand.setAdapter(hotadapter);
        hotadapter.setOnItemClickLitener(new HotRecommandAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null != storeObject.getBlowProduct() && storeObject.getBlowProduct().size() > 0) {
                    startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(storeObject.getBlowProduct().get(position).getProductID())), false);

                }
            }
        });


        guessRecommandAdapter = new GuessRecommandAdapter(StoreHomeActivity.this, null, true);
        grid_allproduct.setAdapter(guessRecommandAdapter);
        guessRecommandAdapter.setOnItemClickLitener(new GuessRecommandAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null != storeListProductBeen && storeListProductBeen.size() > 0) {
                    startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(storeListProductBeen.get(position).getProductID())), false);
                }
            }
        });
        myDropDownMenu.setSubMitInfo(new MyDropDownMenu.SubMitInfo() {
            @Override
            public void subMitInfo(int smalltypeid, int sortby, int sellNumber, int priceoder, JSONArray jsonArray, boolean isNew) {

                initStoreList(smalltypeid, sortby, sellNumber, priceoder, isNew);


            }
        });

        //设置轮播
        carouselAdapter = new CarouselAdapter();
        img_storehome.setPlayDelay(3000);
        //设置透明度
        img_storehome.setAnimationDurtion(500);
        img_storehome.setHintView(new ColorPointHintView(BaseApplication.getInstance(), getResources().getColor(R.color.color_d2ba91), getResources().getColor(R.color.gray)));
        img_storehome.setAdapter(carouselAdapter);

    }

    private void initData() {
        supplierid = getIntent().getStringExtra("supplierid");
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "34");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("supplierid", supplierid);
            params.put("sendmsg", jsonObject.toString());
            getStoreInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getStoreInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<StoreRespMsg>(this) {
            @Override
            public void onSuccess(Response response, StoreRespMsg storeRespMsg) {
                if (storeRespMsg.getResult() > 0) {
                    storeObject = storeRespMsg.getList().get(0);
                    if (null != storeObject.getSupplierDetail() && storeObject.getSupplierDetail().size() > 0) {
                        img_store.setImageUrl(Contants.BASE_IMGURL + storeObject.getSupplierDetail().get(0).getLogo(), imageLoader);
                        img_store.setDefaultImageResId(R.drawable.store_normal);
                        img_store.setErrorImageResId(R.drawable.store_normal);
                        text_storename.setText(storeRespMsg.getList().get(0).getSupplierDetail().get(0).getName());
                        String count = storeRespMsg.getList().get(0).getCountNumber().get(0).getFollowCount();
                        if (TextUtils.isEmpty(count)) {
                            text_followcount.setText("0");
                        } else {
                            text_followcount.setText(count);
                        }
                        setSupplierLevel(storeRespMsg.getList().get(0).getSupplierDetail().get(0).getLevel());
                    }

                    if (null != storeRespMsg.getList().get(0).getBlowProduct() && storeRespMsg.getList().get(0).getBlowProduct().size() > 0) {
                        hotadapter.storeProductBeen = storeRespMsg.getList().get(0).getBlowProduct();
                        hotadapter.notifyDataSetChanged();
                    } else {
                        rel_hotproduct.setVisibility(View.GONE);
                    }

                    if (null != storeRespMsg.getList().get(0).getHostProduct() && storeRespMsg.getList().get(0).getHostProduct().size() > 0) {
                        adapter.storeProductBeen = storeRespMsg.getList().get(0).getHostProduct();
                        adapter.notifyDataSetChanged();
                    } else {
                        rel_product.setVisibility(View.GONE);
                    }
                    if (storeObject != null && storeObject.getSupplierDetail() != null && storeObject.getSupplierDetail().size() > 0
                            && storeObject.getSupplierDetail().get(0) != null) {
                        if ("1".equals(storeObject.getSupplierDetail().get(0).getIsFav())) {
                            text_follow.setText("已关注");
                            text_follow.setTextColor(getResources().getColor(R.color.gray));
                            text_follow.setBackgroundResource(R.drawable.gray_corner_bg1);
                            isFollow = true;
                        } else {
                            text_follow.setText("关注");
                            text_follow.setTextColor(getResources().getColor(R.color.color_d2ba91));
                            text_follow.setBackgroundResource(R.drawable.orange_corner_bg);
                            isFollow = false;
                        }
                    }

                    if (storeRespMsg != null && null != storeRespMsg.getList() && storeRespMsg.getList().size() > 0 && storeRespMsg.getList().get(0) != null
                            && null != storeRespMsg.getList().get(0).getTopBannerList() && storeRespMsg.getList().get(0).getTopBannerList().size() > 0) {
                        for (BannerInfo bannerInfo : storeRespMsg.getList().get(0).getTopBannerList()) {
                            CarouselBean carouselBean = new CarouselBean();
                            carouselBean.setBannerImage(bannerInfo.getBannerImage());
                            carouselBeens.add(carouselBean);
                        }

                        if (null != carouselBeens && carouselBeens.size() > 0) {
                            carouselAdapter.carouselBeen = carouselBeens;
                            carouselAdapter.notifyDataSetChanged();
                            if (carouselBeens.size() < 2) {
                                img_storehome.setHintView(null);
                            }
                        } else {
                            img_storehome.setBackgroundResource(R.drawable.detail_normal);
                        }
                    } else {
                        img_storehome.setBackgroundResource(R.drawable.banner_normal);
                    }

                } else {
                    ToastUtils.show(StoreHomeActivity.this, storeRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(StoreHomeActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(StoreHomeActivity.this, "请求失败，请稍后重试");

            }
        });
    }

    private void initStoreList(int smallTypeId, int sortby, int sellNumber, int priceoder, boolean isNew) {
        if (isNew) {
            smallTypeId = 1;
            sortby = 4;
        }
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "69");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("smalltypeid", smallTypeId);
            jsonObject.put("fashionid", "0");
            jsonObject.put("supplierid", supplierid);
            jsonObject.put("primaryid", "0");
            jsonObject.put("category", "0");
            jsonObject.put("sortby", sortby);
            jsonObject.put("sellnumber", sellNumber);
            jsonObject.put("priceoder", priceoder);
            jsonObject.put("pagesize", "200");
            jsonObject.put("pageno", "1");
            jsonObject.put("keywords", "");
            jsonObject.put("msg", new JSONArray());
            params.put("sendmsg", jsonObject.toString());
            getStoreList(params);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getStoreList(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<StoreListProductRespMsg>(this) {
            @Override
            public void onSuccess(Response response, StoreListProductRespMsg storeRespMsg) {
                if (storeRespMsg.getResult() > 0) {
                    storeListProductBeen = storeRespMsg.getList();
                    guessRecommandAdapter.storeListProductBeen = storeListProductBeen;
                    guessRecommandAdapter.notifyDataSetChanged();

                } else {
                    ToastUtils.show(StoreHomeActivity.this, storeRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(StoreHomeActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(StoreHomeActivity.this, "请求失败，请稍后重试");

            }
        });

    }

    private void setSupplierLevel(String level) {
        if ("".equals(level)) {
            lin_diamond.setVisibility(View.INVISIBLE);
        } else {
            int supplierLevel = Integer.parseInt(level);
            switch (supplierLevel) {
                case 1:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.GONE);
                    img_diamond3.setVisibility(View.GONE);
                    img_diamond4.setVisibility(View.GONE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 2:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.GONE);
                    img_diamond4.setVisibility(View.GONE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 3:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.VISIBLE);
                    img_diamond4.setVisibility(View.GONE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 4:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.VISIBLE);
                    img_diamond4.setVisibility(View.VISIBLE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 5:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.VISIBLE);
                    img_diamond4.setVisibility(View.VISIBLE);
                    img_diamond5.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @OnClick(R.id.imgbtn_right)
    public void toMessageList() {
        startActivity(new Intent(StoreHomeActivity.this, MessageListActivity.class), true);
    }

    @OnClick(R.id.img_classify)
    public void toStoreClassify() {
        startActivity(new Intent(BaseApplication.getInstance(), StoreProductClassifyActivity.class).putExtra("supplierid", supplierid), false);
    }

    @OnClick(R.id.edit_search)
    public void toSearch() {
        startActivity(new Intent(BaseApplication.getInstance(), SearchActivity.class).putExtra("supplierid", supplierid).putExtra("state", 3), false);

    }


    // 当Tab发生变化时，改变tab的标签的显示图片
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.radio_storehome:
                    lin_storehome.setVisibility(View.VISIBLE);
                    myDropDownMenu.setVisibility(View.GONE);
                    grid_allproduct.setVisibility(View.GONE);
                    break;
                case R.id.radio_allproduct:
                    lin_storehome.setVisibility(View.GONE);
                    myDropDownMenu.setVisibility(View.VISIBLE);
                    grid_allproduct.setVisibility(View.VISIBLE);
                    SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("isSeletceNum", 0);
                    editor.commit();
                    if (null == storeListProductBeen) {
                        initStoreList(0, 0, 0, 0, false);
                    }
                    break;
//                case R.id.radio_newproductgrounding:
//                    break;
                default:
                    break;
            }
        }
    };


    @OnClick(R.id.text_follow)
    public void Follow() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "62");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("typeid", "2");
            jsonObject.put("id", supplierid);
            params.put("sendmsg", jsonObject.toString());
            storeFollow(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void storeFollow(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    if (isFollow) {
                        text_follow.setText("关注");
                        text_follow.setTextColor(getResources().getColor(R.color.color_d2ba91));
                        text_follow.setBackgroundResource(R.drawable.orange_corner_bg);
                        text_followcount.setText((Integer.parseInt(text_followcount.getText().toString().trim()) - 1) + "");
                        ToastUtils.show(StoreHomeActivity.this, "取消关注");
                    } else {
                        text_follow.setText("已关注");
                        text_follow.setBackgroundResource(R.drawable.gray_corner_bg1);
                        text_follow.setTextColor(getResources().getColor(R.color.gray));
                        text_followcount.setText((Integer.parseInt(text_followcount.getText().toString().trim()) + 1) + "");
                        ToastUtils.show(StoreHomeActivity.this, "关注成功");

                    }

                    isFollow = !isFollow;

                } else {
                    ToastUtils.show(StoreHomeActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(StoreHomeActivity.this, "处理失败，请稍后重试");
                dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(StoreHomeActivity.this, "处理失败，请稍后重试");
                dismissDialog();

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                dismissDialog();
            }
        });
    }

    @OnClick(R.id.lin_newrecommand)
    public void getMoreHot() {

        Intent intent = new Intent(BaseApplication.getInstance(), ProductGridNormalActivity.class);
        intent.putExtra("supplierid", supplierid);
        intent.putExtra("sortby", 5);
        intent.putExtra("smalltypeid", 1);
        intent.putExtra("from", 7);
        SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isSeletceNum", 0);
        editor.commit();
        startActivity(intent, false);

    }

    @OnClick(R.id.lin_hotrecommand)
    public void getMoreLike() {
        Intent intent = new Intent(BaseApplication.getInstance(), ProductGridNormalActivity.class);
        intent.putExtra("supplierid", supplierid);
        intent.putExtra("sortby", 6);
        intent.putExtra("smalltypeid", 1);
        intent.putExtra("from", 7);
        SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isSeletceNum", 0);
        editor.commit();
        startActivity(intent, false);

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

}
