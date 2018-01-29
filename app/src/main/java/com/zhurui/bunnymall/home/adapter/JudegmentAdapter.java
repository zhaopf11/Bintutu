package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.CommentBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.viewutils.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhoux on 2017/7/28.
 */

public class JudegmentAdapter extends RecyclerView.Adapter<JudegmentAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    public List<CommentBean> commentBeanList;
    private ImageLoader imageLoader;

    public JudegmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circle_head;
        TextView text_nickname;
        TextView text_date;
        TextView text_judgement;
        LinearLayout lin_img;
        NetworkImageView img_judgement;
        NetworkImageView img_judgement1;
        NetworkImageView img_judgement2;
        NetworkImageView img_judgement3;
        TextView productinfo;
        public ViewHolder(View view) {
            super(view);
            circle_head = (CircleImageView) view.findViewById(R.id.circle_head);
            text_nickname = (TextView) view.findViewById(R.id.text_nickname);
            text_date = (TextView) view.findViewById(R.id.text_date);
            text_judgement = (TextView) view.findViewById(R.id.text_judgement);
            lin_img = (LinearLayout) view.findViewById(R.id.lin_img);
            img_judgement = (NetworkImageView) view.findViewById(R.id.img_judgement);
            img_judgement1 = (NetworkImageView) view.findViewById(R.id.img_judgement1);
            img_judgement2 = (NetworkImageView) view.findViewById(R.id.img_judgement2);
            img_judgement3 = (NetworkImageView) view.findViewById(R.id.img_judgement3);
            productinfo = (TextView) view.findViewById(R.id.productinfo);
        }

    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = mInflater.inflate(R.layout.adapter_judgement,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentBean commentBean = commentBeanList.get(position);
        holder.text_nickname.setText(RegexUtil.nickParse(commentBean.getShowUserName()));
        holder.text_date.setText(commentBean.getPostTime());
        holder.text_judgement.setText(commentBean.getCommentContent());
        try {
            JSONArray jsonArray = new JSONArray(commentBean.getPropName().toString());
            if(null !=jsonArray && jsonArray.length()>0){
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                String info = jsonObject.getString("info");
                String[] infos = info.split(",");
                holder.productinfo.setText("颜色："+ infos[0] + "  尺码："+ infos[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new DownloadImageTask().execute(Contants.BASE_IMGURL + commentBean.getUmainImage(),holder.circle_head);
        if (null != commentBean.getImageNames() && !"".equals(commentBean.getImageNames())) {
            holder.lin_img.setVisibility(View.VISIBLE);
            String[] imageArr = commentBean.getImageNames().split(",");
            int imageCount = imageArr.length;
            imageCount = imageCount > 4 ? 4 : imageCount;
            for (int i = 0; i < imageCount; i++) {
                switch (i) {
                    case 0:
                        holder.img_judgement.setVisibility(View.VISIBLE);
                        holder.img_judgement.setImageUrl(Contants.IMAGE_HEADURL + imageArr[i], imageLoader);
                        holder.img_judgement.setDefaultImageResId(R.drawable.list_normal);
                        holder.img_judgement.setErrorImageResId(R.drawable.list_normal);
                        break;
                    case 1:
                        holder.img_judgement1.setVisibility(View.VISIBLE);
                        holder.img_judgement1.setImageUrl(Contants.IMAGE_HEADURL + imageArr[i], imageLoader);
                        holder.img_judgement1.setDefaultImageResId(R.drawable.list_normal);
                        holder.img_judgement1.setErrorImageResId(R.drawable.list_normal);
                        break;
                    case 2:
                        holder.img_judgement2.setVisibility(View.VISIBLE);
                        holder.img_judgement2.setImageUrl(Contants.IMAGE_HEADURL + imageArr[i], imageLoader);
                        holder.img_judgement2.setDefaultImageResId(R.drawable.list_normal);
                        holder.img_judgement2.setErrorImageResId(R.drawable.list_normal);
                        break;
                    case 3:
                        holder.img_judgement3.setVisibility(View.VISIBLE);
                        holder.img_judgement3.setImageUrl(Contants.IMAGE_HEADURL + imageArr[i], imageLoader);
                        holder.img_judgement3.setDefaultImageResId(R.drawable.list_normal);
                        holder.img_judgement3.setErrorImageResId(R.drawable.list_normal);
                        break;
                }

            }


        } else {
            holder.lin_img.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return null == commentBeanList ? 0 : commentBeanList.size();
    }


    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        return drawable;
    }



     class DownloadImageTask extends AsyncTask<Object, Void, Drawable> {
        CircleImageView circleImageView = null;
        protected Drawable doInBackground(Object... urls) {
            circleImageView = (CircleImageView) urls[1];
            Drawable drawable = loadImageFromNetwork((String) urls[0]);
            return drawable;
        }

        protected void onPostExecute(Drawable result) {
            if(null !=result){
                circleImageView.setImageDrawable(result);
            }else {
                circleImageView.setImageResource(R.drawable.head_normal);
            }


        }

    }
}
