package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.CityModel;
import com.zhurui.bunnymall.mine.bean.DistrictModel;
import com.zhurui.bunnymall.mine.bean.ProvinceModel;

import java.util.List;

/**
 * Created by zhoux on 2017/7/26.
 */

public class ProvinceAdapter  extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private List<Object>objects = null;
    public int state = -1;
    public  List<ProvinceModel> provinceList = null;
    public List<CityModel> cityList  = null;
    public List<DistrictModel> districtList = null;
    public ProvinceAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_province;
        public ViewHolder(View view)
        {
            super(view);
            text_province=(TextView)view.findViewById(R.id.text_province);
        }


    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_province,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(1==state){
            ProvinceModel provinceModel  = (ProvinceModel) provinceList.get(position);
            holder.text_province.setText(provinceModel.getName());
        }else if(2==state){
            CityModel cityModel  = (CityModel) cityList.get(position);
            holder.text_province.setText(cityModel.getName());
        }else if(3==state){
            DistrictModel districtModel  = (DistrictModel) districtList.get(position);
            holder.text_province.setText(districtModel.getName());
        }

        if(null !=mOnItemClickLitener){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(1==state){
            count=provinceList.size();
        }else  if(2==state){
            count=cityList.size();
        }else  if(3==state){
            count=districtList.size();
        }
        return count;
    }
}


