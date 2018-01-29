package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.home.activity.StoreHomeActivity;
import com.zhurui.bunnymall.mine.adapter.ProductCollectionsAdapter;
import com.zhurui.bunnymall.mine.adapter.StoreCollectionsAdapter;
import com.zhurui.bunnymall.mine.bean.CollectionsProductBean;
import com.zhurui.bunnymall.mine.bean.CollectionsProductListMessageBean;
import com.zhurui.bunnymall.mine.bean.CollectionsStoreBean;
import com.zhurui.bunnymall.mine.bean.CollectionsStoreListMessageBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class MyCollectionsActivity extends BaseActivity {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.radiogroup_collection)
    RadioGroup radiogroup_collection;
    @Bind(R.id.recycler_collection)
    RecyclerView recycler_collection;
    private ProductCollectionsAdapter productCollectionsAdapter;
    private StoreCollectionsAdapter storeCollectionsAdapter;
    private boolean isStore;
    private boolean isFirstIn;
    private List<CollectionsStoreBean> mCollectionsStoreList;
    private List<CollectionsProductBean> mCollectionsProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collections);
        ButterKnife.bind(this);
        initView();
        initData();
        getData();
    }

    private void initView(){
        text_title.setText(R.string.my_collections);
        text_right.setVisibility(View.GONE);
    }

    private void initData(){
        isStore = getIntent().getBooleanExtra("isStore",false);
        isFirstIn = true;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_collection.setLayoutManager(linearLayoutManager);
        radiogroup_collection.setOnCheckedChangeListener(changeListener);
        storeCollectionsAdapter = new StoreCollectionsAdapter(BaseApplication.getInstance());
        productCollectionsAdapter = new ProductCollectionsAdapter(BaseApplication.getInstance());

        if(isStore){
            radiogroup_collection.check(R.id.radio_store);
//            recycler_collection.addItemDecoration(new FullOffDecoration(20));
            recycler_collection.setAdapter(storeCollectionsAdapter);
        }else {
//            recycler_collection.addItemDecoration(new FullOffDecoration(2));
            recycler_collection.setAdapter(productCollectionsAdapter);
        }
        storeCollectionsAdapter.setOnItemClickLitener(new StoreCollectionsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(BaseApplication.getInstance(), position+"", Toast.LENGTH_SHORT)
//                        .show();
                CollectionsStoreBean collectionsStoreBean = storeCollectionsAdapter.mCollectionsStoreList.get(position);
                startActivity(new Intent(BaseApplication.getInstance(), StoreHomeActivity.class).putExtra("supplierid",collectionsStoreBean.getSupplierID()),false);
            }
        });

        productCollectionsAdapter.setOnItemClickLitener(new ProductCollectionsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(BaseApplication.getInstance(), position+"", Toast.LENGTH_SHORT)
//                        .show();
                Intent intent = new Intent(BaseApplication.getInstance(),ProductNewDetailActivity.class);
                intent.putExtra("productType",mCollectionsProductList.get(position).getFlag());
                intent.putExtra("productId",Long.parseLong(mCollectionsProductList.get(position).getProductID()));
                startActivity(intent);
            }
            @Override
            public void onItemSalimilarity(int position) {
                CollectionsProductBean collectionsProductBean = mCollectionsProductList.get(position);
                startActivity((new Intent(BaseApplication.getInstance(),
                        FindSimilarityActivity.class)).putExtra("collectionsProductBean",collectionsProductBean),false);
            }
        });

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }

    RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.radio_product:
                    recycler_collection.removeAllViews();
//                    recycler_collection.addItemDecoration(new FullOffDecoration(2));
                    recycler_collection.setAdapter(productCollectionsAdapter);
                    if(!isFirstIn){
                        if(mCollectionsProductList == null || mCollectionsProductList.size()==0){
                            isStore = false;
                            getData();
                        }
                    }
                    break;
                case R.id.radio_store:
                    recycler_collection.removeAllViews();
//                    recycler_collection.addItemDecoration(new FullOffDecoration(20));
                    recycler_collection.setAdapter(storeCollectionsAdapter);
                    if(!isFirstIn){
                        if(mCollectionsStoreList == null || mCollectionsStoreList.size()==0){
                            isStore = true;
                            getData();
                        }
                    }
                    break;
            }
        }
    };

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if(isStore){
                jsonObject.put("cmd", "61");
            }else{
                jsonObject.put("cmd", "60");
            }
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno","1");
            jsonObject.put("pagesize","100");
            params.put("sendmsg", jsonObject.toString());
            getCollectionsListMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCollectionsListMessage(Map<String, Object> params) {
        if(isStore){
            mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<CollectionsStoreListMessageBean>(this) {
                @Override
                public void onSuccess(Response response, CollectionsStoreListMessageBean collectionsStoreListMessageBean) {
                    if(collectionsStoreListMessageBean != null){
                        if (collectionsStoreListMessageBean.getResult() > 0) {
                            mCollectionsStoreList = collectionsStoreListMessageBean.getList();
                            storeCollectionsAdapter.mCollectionsStoreList = mCollectionsStoreList;
                            storeCollectionsAdapter.notifyDataSetChanged();
                            isFirstIn = false;
                        } else {
                            ToastUtils.show(BaseApplication.getInstance(), collectionsStoreListMessageBean.getRetmsg());
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
        }else{
            mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<CollectionsProductListMessageBean>(this) {
                @Override
                public void onSuccess(Response response, CollectionsProductListMessageBean collectionsProductListMessageBean) {
                    if(collectionsProductListMessageBean != null){
                        if (collectionsProductListMessageBean.getResult() > 0) {
                            mCollectionsProductList = collectionsProductListMessageBean.getList();
                            productCollectionsAdapter.mCollectionsProducList = mCollectionsProductList;
                            productCollectionsAdapter.notifyDataSetChanged();
                            isFirstIn = false;
                        } else {
                            ToastUtils.show(BaseApplication.getInstance(), collectionsProductListMessageBean.getRetmsg());
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

}
