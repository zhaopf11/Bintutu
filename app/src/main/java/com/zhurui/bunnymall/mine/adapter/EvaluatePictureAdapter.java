package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;

import java.util.List;

/**
 * Created by zhoux on 2017/8/10.
 */

public class EvaluatePictureAdapter extends RecyclerView.Adapter<EvaluatePictureAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private List<Bitmap> bitmaps;

    public EvaluatePictureAdapter(Context context,List<Bitmap> bitmaps) {
        mInflater = LayoutInflater.from(context);
        this.bitmaps = bitmaps;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_judgement;
        ImageView img_delete;
        public ViewHolder(View view) {
            super(view);
            img_judgement=(ImageView)view.findViewById(R.id.img_judgement);
            img_delete = (ImageView)view.findViewById(R.id.img_delete);
        }


    }

    public interface OnItemClickLitener {
        void delete(int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_evaluate_picture,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.img_judgement.setImageBitmap(bitmaps.get(position));
        if (null != mOnItemClickLitener) {
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.delete(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }
}
