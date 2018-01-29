package com.zhurui.bunnymall.classify.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Request;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.classify.adapter.ClassifyAdapter;
import com.zhurui.bunnymall.classify.bean.BigClassifyBean;
import com.zhurui.bunnymall.classify.bean.SmallClassifyBean;
import com.zhurui.bunnymall.classify.msg.ClassifyMsg;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.BaseFragment;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.MessageListActivity;
import com.zhurui.bunnymall.home.activity.PersonalCustomizedActivity;
import com.zhurui.bunnymall.home.activity.ProductGridNormalActivity;
import com.zhurui.bunnymall.home.activity.SearchActivity;
import com.zhurui.bunnymall.home.msg.SearchResp;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyFragment extends BaseFragment {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.edit_search)
    TextView edit_search;
    @Bind(R.id.imgbtn_right)
    ImageButton imgbtn_right;

    @Bind(R.id.radiogroup_classify)
    RadioGroup radiogroup_classify;


    @Bind(R.id.grid_classify)
    GridView grid_classify;

    @Bind(R.id.radiobtn_man)
    RadioButton radiobtn_man;
    @Bind(R.id.radiobtn_woman)
    RadioButton radiobtn_woman;
    @Bind(R.id.radiobtn_child)
    RadioButton radiobtn_child;
    @Bind(R.id.radiobtn_sport)
    RadioButton radiobtn_sport;
    @Bind(R.id.radiobtn_design)
    RadioButton radiobtn_design;

    private ClassifyAdapter adapter;

    private String bigtypeid = "0";
    private RadioButton[] rediobuttons = null;
    private List<Integer> bigtypeids = new ArrayList<>();

    private ClassifyMsg classifyMsg1 = null;
    private List<SmallClassifyBean> mans = new ArrayList<>();
    private List<SmallClassifyBean> womans = new ArrayList<>();
    private List<SmallClassifyBean> childs = new ArrayList<>();
    private List<SmallClassifyBean> sports = new ArrayList<>();
    private int position = 0;

    private List<BigClassifyBean> bigClassifyBeanList = null;
    private long fashionid = 0;
    private String normalUrl;
    private String checkUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        initView();
        initData();
    }

    private void initView() {
        Contants.isClassify = false;
        imgbtn_back.setVisibility(View.GONE);
        imgbtn_right.setImageResource(R.drawable.message);
        adapter = new ClassifyAdapter(BaseApplication.getInstance());
        adapter.setOnItemClick(new ClassifyAdapter.OnItemClick() {
            @Override
            public void onItemClick(String keyword) {
                fashionid = Long.parseLong(keyword);
                startActivity(new Intent(BaseApplication.getInstance(), ProductGridNormalActivity.class).putExtra("fashionId", fashionid), false);

            }
        });
        grid_classify.setAdapter(adapter);

        radiogroup_classify.setOnCheckedChangeListener(onCheckedChangeListener);

        Drawable drawable = getResources().getDrawable(R.drawable.radiobutton_design);
        drawable.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.ui_94), getResources().getDimensionPixelOffset(R.dimen.ui_92));
        Drawable bottom = getResources().getDrawable(R.drawable.radiobutton_bottom);
        bottom.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.margin_50), getResources().getDimensionPixelOffset(R.dimen.navigation_bar_edit_margin));

        Drawable drawableMan = getResources().getDrawable(R.drawable.radiobutton_man);
        drawableMan.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.ui_94), getResources().getDimensionPixelOffset(R.dimen.ui_92));

        Drawable manBottom = getResources().getDrawable(R.drawable.radiobutton_bottom);
        manBottom.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.margin_50), getResources().getDimensionPixelOffset(R.dimen.navigation_bar_edit_margin));

        Drawable drawableWonMan = getResources().getDrawable(R.drawable.radiobutton_woman);
        drawableWonMan.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.ui_94), getResources().getDimensionPixelOffset(R.dimen.ui_92));
        Drawable wonManBottom = getResources().getDrawable(R.drawable.radiobutton_bottom);
        wonManBottom.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.margin_50), getResources().getDimensionPixelOffset(R.dimen.navigation_bar_edit_margin));

        Drawable drawableChild = getResources().getDrawable(R.drawable.radiobutton_child);
        drawableChild.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.ui_94), getResources().getDimensionPixelOffset(R.dimen.ui_92));
        Drawable childBottom = getResources().getDrawable(R.drawable.radiobutton_bottom);
        childBottom.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.margin_50), getResources().getDimensionPixelOffset(R.dimen.navigation_bar_edit_margin));


        Drawable drawableSport = getResources().getDrawable(R.drawable.radiobutton_sport);
        drawableSport.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.ui_94), getResources().getDimensionPixelOffset(R.dimen.ui_92));
        Drawable sportBottom = getResources().getDrawable(R.drawable.radiobutton_bottom);
        sportBottom.setBounds(0, 0, getResources().getDimensionPixelOffset(R.dimen.margin_50), getResources().getDimensionPixelOffset(R.dimen.navigation_bar_edit_margin));

        radiobtn_design.setCompoundDrawables(null, drawable, null, bottom);
        radiobtn_man.setCompoundDrawables(null, drawableMan, null, manBottom);
        radiobtn_woman.setCompoundDrawables(null, drawableWonMan, null, wonManBottom);
        radiobtn_child.setCompoundDrawables(null, drawableChild, null, childBottom);
        radiobtn_sport.setCompoundDrawables(null, drawableSport, null, sportBottom);
        radiobtn_design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contants.isClassify = true;
                adapter.smallClassifyBeens = null;
                adapter.notifyDataSetChanged();
                startActivity(new Intent(BaseApplication.getInstance(), PersonalCustomizedActivity.class).putExtra("isClassify", true), false);
            }
        });

    }

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "24");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID() + "");
            jsonObject.put("token", BaseApplication.getInstance().getToken() + "");
            jsonObject.put("bigtypeid", bigtypeid);
            params.put("sendmsg", jsonObject.toString());
            getClassifyData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getClassifyData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<ClassifyMsg>(getActivity()) {
            @Override
            public void onSuccess(Response response, ClassifyMsg classifyMsg) {
                if (classifyMsg.getResult() > 0) {
                    classifyMsg1 = classifyMsg;
                    if (null == bigClassifyBeanList || bigClassifyBeanList.size() < 1) {
                        if (null != classifyMsg.getList().get(0).getBigTypeListInfo() && classifyMsg.getList().get(0).getBigTypeListInfo().size() > 0) {
                            for (int i = 0; i < classifyMsg.getList().get(0).getBigTypeListInfo().size(); i++) {
                                BigClassifyBean bigClassifyBean = classifyMsg.getList().get(0).getBigTypeListInfo().get(i);
                                String smallTypeImage = bigClassifyBean.getSmallTypeImage();
                                if (!TextUtils.isEmpty(smallTypeImage)) {
                                    normalUrl = smallTypeImage.substring(0, smallTypeImage.indexOf(","));
                                    checkUrl = smallTypeImage.substring(smallTypeImage.indexOf(",") + 1);
                                }
//                                new DownloadImageTask().execute(normalUrl, checkUrl, i + "");
                                RadioButton radiobutton = (RadioButton) radiogroup_classify.getChildAt(i);
                                radiobutton.setTag(bigClassifyBean.getSmallTypeID());
                                if (0 == i) {
                                    radiobutton.setChecked(true);
                                } else {
                                    radiobutton.setChecked(false);
                                }
                            }
                            bigClassifyBeanList = classifyMsg.getList().get(0).getBigTypeListInfo();
                        }
                    }

                    switch (position) {
                        case 0:
                            mans = classifyMsg.getList().get(0).getSmallTypeListInfo();
                            adapter.smallClassifyBeens = mans;
                            break;
                        case 1:
                            womans = classifyMsg.getList().get(0).getSmallTypeListInfo();
                            adapter.smallClassifyBeens = womans;
                            break;
                        case 2:
                            childs = classifyMsg.getList().get(0).getSmallTypeListInfo();
                            adapter.smallClassifyBeens = childs;
                            break;
                        case 3:
                            sports = classifyMsg.getList().get(0).getSmallTypeListInfo();
                            adapter.smallClassifyBeens = sports;
                            break;
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(BaseApplication.getInstance(), classifyMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(BaseApplication.getInstance(), getResources().getString(R.string.request_err));
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(BaseApplication.getInstance(), getResources().getString(R.string.request_err));
            }
        });
    }


    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.radiobtn_man:
                    position = 0;
                    if(null == mans || mans.size() < 1){
                        bigtypeid = "455";
                        initData();
                    }else{
                        adapter.smallClassifyBeens = mans;
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.radiobtn_woman:
                    position = 1;
                    if (null == womans || womans.size() < 1) {
                        bigtypeid = "456";
                        initData();
                    } else {
                        adapter.smallClassifyBeens = womans;
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.radiobtn_child:
                    position = 2;
                    if (null == childs || childs.size() < 1) {
                        bigtypeid = "458";
                        initData();
                    } else {
                        adapter.smallClassifyBeens = childs;
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.radiobtn_sport:
                    position = 3;
                    if (null == sports || sports.size() < 1) {
                        bigtypeid = "460";
                        initData();
                    } else {
                        adapter.smallClassifyBeens = sports;
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @OnClick(R.id.edit_search)
    public void toSearch() {
        startActivity(new Intent(BaseApplication.getInstance(), SearchActivity.class).putExtra("state", 2), false);
    }

    @OnClick(R.id.imgbtn_right)
    public void toMessageList() {
        startActivity(new Intent(BaseApplication.getInstance(), MessageListActivity.class), true);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Contants.isClassify) {
            radiobtn_man.setChecked(true);
            position = 0;
            adapter.smallClassifyBeens = mans;
            adapter.notifyDataSetChanged();
        }
    }
}
