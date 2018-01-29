package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by zhoux on 2017/7/19.
 */

public class HomeMessageAdapter extends RecyclerView.Adapter<HomeMessageAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private List<Map<String, Object>> settinginfolist;
    private Map<String, Object> settinginfo;
    private ImageLoader imageLoader;
    private SimpleDateFormat dateFormat;
    public HomeMessageAdapter(Context context, List<Map<String, Object>> settinginfolist) {
        mInflater = LayoutInflater.from(context);
        this.settinginfolist = settinginfolist;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_storename;
        TextView text_infotime;
        TextView text_storeinfo;
        NetworkImageView img_store;

        public ViewHolder(View view) {
            super(view);
            text_storename = (TextView) view.findViewById(R.id.text_storename);
            text_infotime = (TextView) view.findViewById(R.id.text_infotime);
            text_storeinfo = (TextView) view.findViewById(R.id.text_storeinfo);
            img_store = (NetworkImageView) view.findViewById(R.id.img_store);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_home_message,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (settinginfolist != null && settinginfolist.size() > 0) {
            settinginfo = settinginfolist.get(position);
            if (settinginfo != null) {
                String settingid = (String) settinginfo.get("settingid");
                String uname = (String)settinginfo.get("uname");
                if(TextUtils.isEmpty(uname)){
                    holder.text_storename.setText("在线客户");
                }else{
                    holder.text_storename.setText(uname);
                }
                String uicon = (String) settinginfo.get("uicon");
                if (uicon != null) {
                    holder.img_store.setImageUrl(uicon,imageLoader);
                }
                Object msgtime = settinginfo.get("msgtime");
                if(!TextUtils.isEmpty(msgtime+"")){
                    String time = TimeStamp2Date(msgtime+"");
                    holder.text_infotime.setText(time);
                }
                holder.text_storeinfo.setText((String) settinginfo.get("textmsg"));
            }
        }
        if (null != mOnItemClickLitener) {
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
        return settinginfolist == null ? 0 : settinginfolist.size();
    }


    private String TimeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString);
        String dateString = new SimpleDateFormat("MM-dd HH:mm").format(timestamp);
        String data1 = dateString.substring(0, 6);
        try {
            String time = getTime();
//            long mdates = TimeUtils.getCurrentTimeMillis();
//            String dateString2 = new SimpleDateFormat("MM-dd HH:mm").format(mdates);//yy/mm/dd
            String data2 = time.substring(0, 6);
            if (data1.equals(data2)) {
                dateString = dateFormat.format(new Date(timestamp));
            } else {
                SimpleDateFormat dateformat = new SimpleDateFormat("yy/MM/dd");// 可以方便地修改日期格式
                dateString = dateformat.format(new Date(timestamp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public String getTime() throws java.text.ParseException {
        TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
        TimeZone.setDefault(tz);
        Date now = new Date();
        dateFormat = new SimpleDateFormat("MM-dd HH:mm");// 可以方便地修改日期格式
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String time = dateFormat.format(now);
        return time;
    }
}
