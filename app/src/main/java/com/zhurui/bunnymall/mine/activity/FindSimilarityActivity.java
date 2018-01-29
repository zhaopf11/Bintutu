package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.home.adapter.ProductGridAdapter;
import com.zhurui.bunnymall.home.bean.Product;
import com.zhurui.bunnymall.mine.bean.CollectionsProductBean;
import com.zhurui.bunnymall.mine.bean.FootPrintBean;
import com.zhurui.bunnymall.mine.bean.SimilarityProductListMessageBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class FindSimilarityActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.img_product)
    NetworkImageView img_product;
    @Bind(R.id.text_productintroduce)
    TextView text_productintroduce;
    @Bind(R.id.text_productprice)
    TextView text_productprice;

    @Bind(R.id.text_findsimilarity)
    TextView text_findsimilarity;
    @Bind(R.id.grid_similarity)
    GridView grid_similarity;
    private ProductGridAdapter productGridAdapter;
    private CollectionsProductBean collectionsProductBean;
    private Boolean isFootPrint;
    private FootPrintBean footPrintBean;
    private String fashionID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_similarity);
        ButterKnife.bind(this);
        initView();
        initData();
        getData();
    }

    private void initView(){
        ImageLoader imageLoader = BaseApplication.getInstance().getImageLoader();
        footPrintBean = (FootPrintBean)getIntent().getSerializableExtra("footprintbean");
        if (footPrintBean != null){
            fashionID = footPrintBean.getFashionID();
            img_product.setImageUrl(Contants.BASE_IMGURL + footPrintBean.getMainImage(),imageLoader);
            text_productintroduce.setText(footPrintBean == null ? "" :footPrintBean.getName());
            text_productprice.setText(footPrintBean == null ? "" :"¥" + footPrintBean.getPrice());
        }

        collectionsProductBean = (CollectionsProductBean)getIntent().getSerializableExtra("collectionsProductBean");
        if(collectionsProductBean != null){
            fashionID = collectionsProductBean.getFashionID();
            img_product.setImageUrl(Contants.BASE_IMGURL + collectionsProductBean.getMainImage(),imageLoader);
            text_productintroduce.setText(collectionsProductBean == null ? "" :collectionsProductBean.getName());
            text_productprice.setText(collectionsProductBean == null ? "" :"¥" + collectionsProductBean.getPrice());
        }
        text_title.setText(R.string.find_similarity);
        text_right.setVisibility(View.GONE);
        text_findsimilarity.setVisibility(View.GONE);
    }

    private void initData(){
        productGridAdapter = new ProductGridAdapter(BaseApplication.getInstance(),0,null);
        grid_similarity.setAdapter(productGridAdapter);
        grid_similarity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(BaseApplication.getInstance(),
                            ProductNewDetailActivity.class).putExtra("productId",Long.parseLong(productGridAdapter.products.get(position).getProductID())),false);
            }
        });
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "63");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno","1");
            jsonObject.put("pagesize","100");
            if(TextUtils.isEmpty(fashionID)){
                jsonObject.put("fashionid",0);
            }else{
                jsonObject.put("fashionid",fashionID);
            }
            params.put("sendmsg", jsonObject.toString());
            getCollectionsListMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCollectionsListMessage(Map<String, Object> params) {
            mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<SimilarityProductListMessageBean>(this) {
                @Override
                public void onSuccess(Response response, SimilarityProductListMessageBean mSimilarityProductListMessageBean) {
                    if (mSimilarityProductListMessageBean != null) {
                        if (mSimilarityProductListMessageBean.getResult() > 0) {
                            List<Product> mSimilarityProductList = mSimilarityProductListMessageBean.getList();
                            productGridAdapter.setDataChanged(mSimilarityProductList);
                            productGridAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.show(BaseApplication.getInstance(), mSimilarityProductListMessageBean.getRetmsg());
                        }
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e) {
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
                }

                @Override
                public void onServerError(Response response, int code, String errmsg) {
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");

                }
            });
    }
}
