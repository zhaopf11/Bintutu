package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.AddressBean;
import com.zhurui.bunnymall.utils.RegexUtil;

import java.util.List;

/**
 * Created by zhoux on 2017/7/26.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    public List<AddressBean> addressBean;


    public AddressAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_normal;
        TextView text_telnum;
        TextView text_address;
        ImageView img_delete;
        ImageView img_edit;

        public ViewHolder(View view) {
            super(view);
            text_name = (TextView) view.findViewById(R.id.text_name);
            text_normal = (TextView) view.findViewById(R.id.text_normal);
            text_telnum = (TextView) view.findViewById(R.id.text_telnum);
            text_address = (TextView) view.findViewById(R.id.text_address);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            img_edit = (ImageView) view.findViewById(R.id.img_edit);
        }


    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onDelete(int position);
        void onEdit(int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_address,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddressBean add =addressBean.get(position);
        holder.text_name.setText(add.getShouHuoRen());
        holder.text_address.setText(add.getProvinceName() +add.getCityName()+add.getTownName()+add.getAddress());
        holder.text_telnum.setText(RegexUtil.toparsephonenum(add.getMobile()));
        if(!TextUtils.isEmpty(add.getDefaultFlag())){
            if("0".equals(add.getDefaultFlag())){
                holder.text_normal.setVisibility(View.GONE);
            }else if("1".equals(add.getDefaultFlag())){
                holder.text_normal.setVisibility(View.VISIBLE);
            }
        }else {
            holder.text_normal.setVisibility(View.GONE);
        }
        if (null != mOnItemClickLitener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onDelete(position);
                }
            });
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onEdit(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == addressBean?0:addressBean.size();
    }
}

