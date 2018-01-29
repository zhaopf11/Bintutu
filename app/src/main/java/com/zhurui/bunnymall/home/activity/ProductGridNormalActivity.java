package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.adapter.ProductGridAdapter;
import com.zhurui.bunnymall.home.bean.Banner;
import com.zhurui.bunnymall.home.bean.FullOffProductBean;
import com.zhurui.bunnymall.home.bean.GuessProductInfo;
import com.zhurui.bunnymall.home.bean.NewProduct;
import com.zhurui.bunnymall.home.bean.Product;
import com.zhurui.bunnymall.home.bean.RecommandProduct;
import com.zhurui.bunnymall.home.msg.ProductListRespMsg;
import com.zhurui.bunnymall.home.msg.SearchResp;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.MyDropDownMenu;
import com.zhurui.bunnymall.viewutils.ScreenDialog;
import com.zhurui.bunnymall.viewutils.pulltorefresh.PullToRefreshLayout;
import com.zhurui.bunnymall.viewutils.pulltorefresh.pullableview.PullableScrollView;

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

public class ProductGridNormalActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, View.OnClickListener,PullableScrollView.StopCall {

    @Bind(R.id.imgbtn_right)
    ImageButton imgbtn_right;
    @Bind(R.id.edit_search)
    EditText edit_search;
    @Bind(R.id.myDropDownMenu1)
    MyDropDownMenu myDropDownMenu1;
    @Bind(R.id.myDropDownMenu2)
    MyDropDownMenu myDropDownMenu2;
    @Bind(R.id.grid_product)
    GridView grid_product;
    @Bind(R.id.img_banner)
    ImageView img_banner;
    private ProductGridAdapter productGridAdapter;
    //1:新品2：满减3：抢购4：团购0:普通
    private int state;
    //2:新品推荐3:人气推荐 4:猜你喜欢5:店铺6:店铺的分类7:店铺首页传过来
    private int from;
    private List<Product> products = null;
    private List<GuessProductInfo> guessProductInfos = null;
    private List<NewProduct> newProductList = null;
    private List<RecommandProduct> recommandProducts = null;
    private List<FullOffProductBean> fullOffProductBeen = null;
    private int smalltypeId = 0;
    private int sortBy = 0;
    private int sellnumber = 0;
    private int priceOder = 0;
    private JSONArray msg = new JSONArray();
    private long fashionid = 0;

    private List<Banner> banners;
    private String supplierid;
    private String fashionID = "0";
    private String primaryID = "0";
    private int pageNo = 1;
    private boolean refresh = false;
    // 455：男款   456：女款  458：童款  460：运动款
    private String smalltypeid2 = "0";
    private String keyWord = "";
    @Bind(R.id.refresh_view)
    PullToRefreshLayout mPullToRefreshLayout;
    @Bind(R.id.scrollview)
    PullableScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_grid_normal);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        keyWord = getIntent().getStringExtra("keyWord");
        smalltypeid2 = getIntent().getStringExtra("smalltypeid2");
        edit_search.setText(keyWord);
        mPullToRefreshLayout.setOnRefreshListener(this);
        mPullToRefreshLayout.autoRefresh();
        scrollview.setCallback(this);
        scrollview.initData(myDropDownMenu2);
        edit_search.setOnClickListener(this);

        myDropDownMenu1.setSubMitInfo(new MyDropDownMenu.SubMitInfo() {
            @Override
            public void subMitInfo(int smalltypeid, int sortby, int sellNumber, int priceoder, JSONArray jsonArray, boolean isNew) {
                queryContent(smalltypeid,sortby,sellNumber, priceoder, jsonArray,isNew);
            }
        });
        myDropDownMenu2.setSubMitInfo(new MyDropDownMenu.SubMitInfo() {
            @Override
            public void subMitInfo(int smalltypeid, int sortby, int sellNumber, int priceoder, JSONArray jsonArray, boolean isNew) {
                queryContent(smalltypeid,sortby,sellNumber, priceoder, jsonArray,isNew);
            }
        });

    }

    /**
     * 根据选中条件查找相关的鞋品
     * @param smalltypeid
     * @param sortby
     * @param sellNumber
     * @param priceoder
     * @param jsonArray
     * @param isNew
     */
    private void queryContent(int smalltypeid, int sortby, int sellNumber, int priceoder, JSONArray jsonArray, boolean isNew) {
        smalltypeId = smalltypeid;
        sortBy = sortby;
        sellnumber = sellNumber;
        priceOder = priceoder;
        msg = jsonArray;
        pageNo = 1;
        refresh = true;
        if (5 == from || 6 == from || 7 == from) {
            initSearchStoreInfo(fashionID, primaryID);
        } else {
            initSearchInfo();

        }
    }

    /**
     * 查询列表接口
     */
    private void initSearchInfo() {
        String searchwords = edit_search.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "40");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("smalltypeid", smalltypeId);
            jsonObject.put("fashionid", fashionid + "");
            jsonObject.put("category", from + "");
            jsonObject.put("sortby", sortBy);
            jsonObject.put("sellnumber", sellnumber);
            jsonObject.put("priceoder", priceOder);
            jsonObject.put("pagesize", "20");
            jsonObject.put("pageno", pageNo);
            jsonObject.put("keywords", searchwords);
            jsonObject.put("msg", msg);
            jsonObject.put("smalltypeid2", smalltypeid2);
            params.put("sendmsg", jsonObject.toString());
            getListData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 店铺里面相关列表的查询接口
     * @param fashionID
     * @param primaryID
     */
    private void initSearchStoreInfo(String fashionID, String primaryID) {
        String searchwords = edit_search.getText().toString().trim();

        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "69");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("smalltypeid", smalltypeId);
            jsonObject.put("fashionid", fashionID);
            jsonObject.put("supplierid", supplierid);
            jsonObject.put("primaryid", primaryID);
            jsonObject.put("category", "0");
            jsonObject.put("sortby", sortBy);
            jsonObject.put("sellnumber", sellnumber);
            jsonObject.put("priceoder", priceOder);
            jsonObject.put("pagesize", "20");
            jsonObject.put("pageno", pageNo);
            jsonObject.put("keywords", searchwords);
            jsonObject.put("msg", msg);
            params.put("sendmsg", jsonObject.toString());
            getListData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getListData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<ProductListRespMsg>(this,"list") {
            @Override
            public void onSuccess(Response response, ProductListRespMsg productListRespMsg) {
                if (productListRespMsg.getResult() > 0) {
                    products = productListRespMsg.getList();
                    banners = productListRespMsg.getListbanner();
                    if(banners != null && banners.size() > 0){
                        //展示广告banner
                        String bannerImage = banners.get(0).getBannerImage();
                        img_banner.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(Contants.BASE_IMGURL+bannerImage)
                                .placeholder(R.drawable.list_normal)
                                .error(R.drawable.list_normal)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                                .into(img_banner);
                    }
                    if(products != null && products.size() > 0){
                        if(refresh){
                            pageNo = 2;
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            if(productGridAdapter.products != null){
                                productGridAdapter.products.clear();
                            }
                            productGridAdapter.products = productListRespMsg.getList();
                        }else{
                            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            pageNo ++ ;
                            if(productGridAdapter.products == null ){
                                productGridAdapter.products = new ArrayList<Product>();
                            }
                            productGridAdapter.products.addAll(productListRespMsg.getList());
                        }
                        productGridAdapter.notifyDataSetChanged();
                    }else{
                        if(!TextUtils.isEmpty(keyWord)){
                            ToastUtils.show(BaseApplication.getInstance(),"没有搜索到相关商品");
                        }
                        if(refresh) {
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            productGridAdapter.products = productListRespMsg.getList();
                            productGridAdapter.notifyDataSetChanged();
                        }else{
                            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                } else {
                    if(refresh) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }else{
                        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                    ToastUtils.show(ProductGridNormalActivity.this, productListRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ProductGridNormalActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ProductGridNormalActivity.this, "请求失败，请稍后重试");

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                if(refresh) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }else{
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }
        });
    }

    private void initData() {
        state = getIntent().getIntExtra("state", 0);
        from = getIntent().getIntExtra("from", 0);
        //初始化第一次数据
        smalltypeId = 1;
        sortBy = 1;
        switch (from) {
            case 0:
                products = (List<Product>) getIntent().getSerializableExtra("productlist");
                fashionid = getIntent().getLongExtra("fashionId", 0);
                break;
            case 5:
                supplierid = getIntent().getStringExtra("supplierid");
                products = (List<Product>) getIntent().getSerializableExtra("productlist");
                break;
            case 6:
                supplierid = getIntent().getStringExtra("supplierid");
//                products = (List<Product>) getIntent().getSerializableExtra("productlist");
                fashionID = getIntent().getStringExtra("fashionid");
                sortBy = getIntent().getIntExtra("sortby", 0);
                primaryID = getIntent().getStringExtra("primaryid");
                break;
            case 7:
                supplierid = getIntent().getStringExtra("supplierid");
                sortBy = getIntent().getIntExtra("sortby", 0);
                smalltypeId = getIntent().getIntExtra("smalltypeid", 0);
                break;
        }

        productGridAdapter = new ProductGridAdapter(BaseApplication.getInstance(), state, products);
        grid_product.setAdapter(productGridAdapter);
        grid_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(productGridAdapter.products.get(position).getProductID())), false);
            }
        });
    }

    /**
     * 跳转购物车界面
     */
    @OnClick(R.id.imgbtn_right)
    public void toCart() {
        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
        intent.putExtra("from", 3);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, false);
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }


    private void toRefresh(){
        if (5 == from || 6 == from || 7 == from) {
            initSearchStoreInfo(fashionID, primaryID);
        } else {
            initSearchInfo();
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh = true;
        pageNo = 1;
        toRefresh();
        mPullToRefreshLayout = pullToRefreshLayout;
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        refresh = false;
        toRefresh();
        mPullToRefreshLayout = pullToRefreshLayout;
    }

    /**
     * 点击搜索框跳转到搜索界面
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ProductGridNormalActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    /**
     * 根据上拉或下滑的距离，来判断显示或者隐藏myDropDownMenu1，myDropDownMenu2
     * @param isStop
     */
    @Override
    public void stopSlide(boolean isStop) {
        if(isStop){
            myDropDownMenu1.setVisibility(View.VISIBLE);
            myDropDownMenu2.setVisibility(View.GONE);
            myDropDownMenu1.refreshDataUi();
        }else {
            myDropDownMenu2.setVisibility(View.VISIBLE);
            myDropDownMenu1.setVisibility(View.GONE);
            myDropDownMenu2.refreshDataUi();
        }
    }
}
