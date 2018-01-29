package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.cart.bean.CartProductBean;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.OkHttpHelper;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.ProductCheckAttrBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.mine.activity.FootTypeMessagesActivity;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.UnderLineTextView;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * Created by zhoux on 2017/8/1.
 */

public class MakeOrderAdapter extends RecyclerView.Adapter<MakeOrderAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    public boolean buyNow;
    public ProductDetailBean productDetailBean;
    public String pricetotal;
    private ImageLoader imageLoader;
    public int number;
    public List<CartProductBean> cartProductBeen;
    public List<ProductCheckAttrBean> xuanzhongsxList;
    private CartProductBean cartProduct;
    private boolean isEdit = false;
    @Inject
    public OkHttpHelper mHttpHelper;

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public MakeOrderAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView checkbox_group;
        NetworkImageView img_product;
        TextView text_productintroduce;
        TextView text_count;
        TextView text_price;
        RelativeLayout rel_top;
        TextView text_orderstate;
        TextView productId;
        TextView product_brand;
        ImageView chat_service;
        ImageView edit_foot;
        EditText edit_foot_desc;
        TextView show_foot_desc;
        LinearLayout lin_foot_desc;
        LinearLayout lin_chat_service;

        RelativeLayout lin_check_material;
        TextView check_material_text;
        TextView check_material_text1;
        RelativeLayout lin_personality_request;
        TextView personality_request_text;
        TextView personality_request_text1;
        RelativeLayout lin_customize_size;
        TextView customize_size_text;
        TextView customize_size_text1;
        LinearLayout lin_footdesc;

        public ViewHolder(View view) {
            super(view);
            checkbox_group = (TextView) view.findViewById(R.id.checkbox_group);
            img_product = (NetworkImageView) view.findViewById(R.id.img_product);
            text_productintroduce = (TextView) view.findViewById(R.id.text_productintroduce);
            text_count = (TextView) view.findViewById(R.id.text_count);
            text_price = (TextView) view.findViewById(R.id.text_price);
            rel_top = (RelativeLayout) view.findViewById(R.id.rel_top);
            text_orderstate = (TextView) view.findViewById(R.id.text_orderstate);
            text_orderstate.setVisibility(View.INVISIBLE);
            productId = (TextView) view.findViewById(R.id.productId);
            product_brand = (TextView) view.findViewById(R.id.product_brand);
            chat_service = (ImageView) view.findViewById(R.id.chat_service);
            edit_foot = (ImageView) view.findViewById(R.id.edit_foot);
            edit_foot_desc = (EditText) view.findViewById(R.id.edit_foot_desc);
            show_foot_desc = (TextView) view.findViewById(R.id.show_foot_desc);
            lin_foot_desc = (LinearLayout) view.findViewById(R.id.lin_foot);
            lin_chat_service = (LinearLayout) view.findViewById(R.id.lin_chat_service);

            lin_check_material = (RelativeLayout) view.findViewById(R.id.lin_check_material);
            check_material_text = (TextView) view.findViewById(R.id.check_material_text);
            check_material_text1 = (TextView) view.findViewById(R.id.check_material_text1);
            lin_personality_request = (RelativeLayout) view.findViewById(R.id.lin_personality_request);
            personality_request_text = (TextView) view.findViewById(R.id.personality_request_text);
            personality_request_text1 = (TextView) view.findViewById(R.id.personality_request_text1);
            lin_customize_size = (RelativeLayout) view.findViewById(R.id.lin_customize_size);
            customize_size_text = (TextView) view.findViewById(R.id.customize_size_text);
            customize_size_text1 = (TextView) view.findViewById(R.id.customize_size_text1);
            lin_footdesc = (LinearLayout) view.findViewById(R.id.lin_footdesc);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_identification_order,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.img_product.setDefaultImageResId(R.drawable.list_normal);
        holder.img_product.setErrorImageResId(R.drawable.list_normal);
        if (buyNow) {
            holder.img_product.setImageUrl(Contants.BASE_IMGURL + productDetailBean.getMainImage(), imageLoader);
            holder.checkbox_group.setText(productDetailBean.getSupplierName());
            holder.text_productintroduce.setText(productDetailBean.getName());
            holder.text_count.setText("x" + number);
            holder.text_price.setText("¥" + decimalFormat.format(Float.parseFloat(pricetotal)));
            holder.productId.setText(productDetailBean.getProductID());
            holder.product_brand.setText(productDetailBean.getBrandName());
            //展示选中的定制属性
            showCheckProductAttrbute(holder, productDetailBean.getProductID(),"");
        } else {
            cartProduct = cartProductBeen.get(position);
            holder.img_product.setImageUrl(Contants.BASE_IMGURL + cartProduct.getProductMainImage(), imageLoader);
            if (0 == position) {
                holder.rel_top.setVisibility(View.VISIBLE);
                holder.checkbox_group.setText(cartProduct.getSupplierName());
            } else {
                if (cartProductBeen.get(position - 1).getSupplierID().equals(cartProduct.getSupplierID())) {
                    //同一商店 隐藏商店布局
                    holder.rel_top.setVisibility(View.GONE);
                } else {
                    holder.rel_top.setVisibility(View.VISIBLE);
                    holder.checkbox_group.setText(cartProduct.getSupplierName());
                }
            }

            holder.text_productintroduce.setText(cartProduct.getProductName());
            holder.text_count.setText("x" + cartProduct.getNumber());
            float totalPrice = Float.parseFloat(cartProduct.getPrice()) + Float.parseFloat(cartProduct.getShuxingPrice());
            holder.text_price.setText("¥" + decimalFormat.format(totalPrice));
            holder.productId.setText(cartProduct.getProductID());
            holder.product_brand.setText(cartProduct.getBrandName());
            //展示选中的定制属性
            showCheckProductAttrbute(holder, cartProduct.getProductID(),cartProduct.getCartID());
        }

        if (null != mOnItemClickLitener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

            holder.lin_chat_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //联系客服
                    if (buyNow) {
                        mOnItemClickLitener.onChat(holder.chat_service, productDetailBean.getProductID(), productDetailBean.getName(), productDetailBean.getPrice(),
                                productDetailBean.getMainImage(), productDetailBean.getXnSupplierID());
                    } else {
                        mOnItemClickLitener.onChat(holder.chat_service, cartProductBeen.get(position).getProductID(), cartProductBeen.get(position).getProductName(), cartProductBeen.get(position).getPrice(),
                                cartProductBeen.get(position).getProductMainImage(), cartProductBeen.get(position).getXnsupplierId());
                    }
                }
            });

            holder.lin_foot_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEdit) {
                        isEdit = false;
                        holder.show_foot_desc.setVisibility(View.VISIBLE);
                        holder.edit_foot_desc.setVisibility(View.GONE);
                        holder.edit_foot.setBackground(context.getResources().getDrawable(R.drawable.address_edit));
                        UserFootDataDetailBean footDataDetail = new UserFootDataDetailBean();
                        if (buyNow) {
                            footDataDetail = xuanzhongsxList.get(0).getSxstr().getFootdatadetail().get(0);
                        } else {
                            footDataDetail = cartProductBeen.get(position).getFootDataDetail();
                        }
                        mOnItemClickLitener.setOnEdit(holder.edit_foot_desc.getText().toString(), footDataDetail);
                        if(TextUtils.isEmpty(holder.edit_foot_desc.getText().toString())){
                            holder.show_foot_desc.setText(footDataDetail.getFootDesc());
                        }else{
                            holder.show_foot_desc.setText(holder.edit_foot_desc.getText().toString());
                        }
                    } else {
                        isEdit = true;
                        holder.edit_foot.setBackground(context.getResources().getDrawable(R.drawable.complete_end));
                        holder.show_foot_desc.setVisibility(View.GONE);
                        holder.edit_foot_desc.setVisibility(View.VISIBLE);
                        holder.edit_foot_desc.setText(holder.show_foot_desc.getText().toString());
                        Utils.cursorToEnd(holder.edit_foot_desc, holder.edit_foot_desc.getText().toString());
                    }
                }
            });
        }
    }

    /**
     * 展示选中的定制属性
     * @param holder
     * @param productId
     */
    private void showCheckProductAttrbute(ViewHolder holder, String productId,String cartId) {
        String checkmaterial = "";
        String personalityrequest = "";
        String customizesize = "";
        String yanseImage = "";
        String footdatadesc = "";
        ProductAttrbuteSxstr productAttrbuteSxstr = new ProductAttrbuteSxstr();

        SpannableStringBuilder materialStr = new SpannableStringBuilder("");
        SpannableStringBuilder gexingStr = new SpannableStringBuilder("");
        SpannableStringBuilder chimaStr = new SpannableStringBuilder("");

        if (xuanzhongsxList != null && xuanzhongsxList.size() > 0) {
            if(buyNow){
                productAttrbuteSxstr = xuanzhongsxList.get(0).getSxstr();
            }else{
                for (int i = 0; i < xuanzhongsxList.size(); i++) {
                    ProductAttrbuteSxstr productAttrbuteSxstr1 = xuanzhongsxList.get(i).getSxstr();
                    //根据商品ID和cartId 取出该商品的定制属性
                    if (productAttrbuteSxstr1 != null && productId.equals(productAttrbuteSxstr1.getProductid().get(0)) && cartId.equals(productAttrbuteSxstr1.getCartId().get(0))) {
                        productAttrbuteSxstr = productAttrbuteSxstr1;
                    }
                }
            }

            if (productAttrbuteSxstr != null) {//定制属性
                List<Object> checkMaterialList = BintutuUtils.assembleCheckMaterial(context,productAttrbuteSxstr);
                List<Object> gexingRequireList = BintutuUtils.assembleGexingRequire(context,productAttrbuteSxstr);
                if (productAttrbuteSxstr.getFootdatadetail() != null && productAttrbuteSxstr.getFootdatadetail().size() > 0
                        && productAttrbuteSxstr.getFootdatadetail().get(0) !=null) {//选中的一条足型数据
                    footdatadesc = productAttrbuteSxstr.getFootdatadetail().get(0).getFootDesc();
                    List<Object> checkFootDataList = BintutuUtils.assembleFootData(context,productAttrbuteSxstr,productAttrbuteSxstr.getFootdatadetail().get(0));
                    if(checkFootDataList != null && checkFootDataList.size()>0){
                        customizesize = (String) checkFootDataList.get(0);
                        chimaStr = (SpannableStringBuilder) checkFootDataList.get(1);
                    }
                }

                if(checkMaterialList != null && checkMaterialList.size()>0){
                    checkmaterial = (String) checkMaterialList.get(0);
                    materialStr = (SpannableStringBuilder) checkMaterialList.get(1);
                }
                if(gexingRequireList != null && gexingRequireList.size()>0){
                    personalityrequest = (String) gexingRequireList.get(0);
                    gexingStr = (SpannableStringBuilder) gexingRequireList.get(1);
                }

                if (productAttrbuteSxstr.getYanseList() != null && productAttrbuteSxstr.getYanseList().size() > 0) {//选中颜色的图片
                    yanseImage = productAttrbuteSxstr.getYanseList().get(0).getCustomPropertiesValueImageUrl();
                }
            }
            if (!TextUtils.isEmpty(yanseImage)) {
                holder.img_product.setImageUrl(Contants.BASE_IMGURL + yanseImage, imageLoader);
            }
            if (!TextUtils.isEmpty(checkmaterial)) {
                holder.lin_check_material.setVisibility(View.VISIBLE);
                //需求要求下划线断开，富文本来写。下划线颜色随着字体的颜色改变，
                // 因此在该控件上方放置另一个控件，来实现字体和下划线颜色不一样
                holder.check_material_text.setText(materialStr);
                holder.check_material_text1.setText(checkmaterial);
            }
            if (!TextUtils.isEmpty(personalityrequest)) {
                holder.lin_personality_request.setVisibility(View.VISIBLE);
                holder.personality_request_text.setText(gexingStr);
                holder.personality_request_text1.setText(personalityrequest);
            }
            if (!TextUtils.isEmpty(customizesize)) {
                holder.lin_customize_size.setVisibility(View.VISIBLE);
                holder.customize_size_text.setText(chimaStr);
                holder.customize_size_text1.setText(customizesize);
            }
            if (!TextUtils.isEmpty(footdatadesc)) {
                holder.lin_footdesc.setVisibility(View.VISIBLE);
                holder.show_foot_desc.setText(footdatadesc);
            }
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onChat(View view, String productId, String productName, String price, String productMainImage, String xnSupplierID);

        void setOnEdit(String desc, UserFootDataDetailBean footDataDetail);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemCount() {
        if (buyNow) {
            return 1;
        }
        return null == cartProductBeen ? 0 : cartProductBeen.size();
    }

}
