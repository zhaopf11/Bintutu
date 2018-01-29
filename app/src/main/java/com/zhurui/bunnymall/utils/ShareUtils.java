package com.zhurui.bunnymall.utils;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.viewutils.ShareDialog;

/**
 * Author     wildma
 * DATE       2017/07/16
 * Des	      ${友盟分享工具类}
 */
public class ShareUtils {
    static PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 分享链接
     */
    public static void shareWeb(final Activity activity, String WebUrl, String title, String description,String imageUrl, SHARE_MEDIA platform) {
        //检查权限(6.0以上做权限判断)
        mPermissionsChecker = new PermissionsChecker(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                PermissionsActivity.startActivityForResult(activity, 4, PERMISSIONS);
            }
        }
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if(!TextUtils.isEmpty(imageUrl)){
            web.setThumb(new UMImage(activity, Contants.BASE_IMGURL + imageUrl));  //网络缩略图
        }else {
            //分享本地的图片
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.sharelogo);
            web.setThumb(new UMImage(activity, bitmap));
        }
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                    Toast.makeText(activity, share_media + " 收藏成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, share_media + " 分享成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "throw:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, share_media + " 分享失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, share_media + " 分享取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .share();

        //新浪微博中图文+链接
        /*new ShareAction(activity)
                .setPlatform(platform)
                .withText(description + " " + WebUrl)
                .withMedia(new UMImage(activity,imageID))
                .share();*/
    }

    public static void openShare(final Activity activity,String productName, String shareUrl,String productImage,String productInfo){
        ShareDialog shareDialog = new ShareDialog(activity, R.style.CustomDialog);
        shareDialog.setOnItemClick(new ShareDialog.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 1:
                        ShareUtils.shareWeb(activity, shareUrl, productName, productInfo,  productImage, SHARE_MEDIA.QQ);
                        break;
                    case 2:
                        ShareUtils.shareWeb(activity, shareUrl, productName, productInfo,  productImage, SHARE_MEDIA.WEIXIN);
                        break;
                    case 3:
                        ShareUtils.shareWeb(activity, shareUrl, productName, productInfo,  productImage, SHARE_MEDIA.WEIXIN_CIRCLE);
                        break;
                    case 4:
                        ShareUtils.shareWeb(activity, shareUrl, productName, productInfo,  productImage, SHARE_MEDIA.SINA);
                        break;
                }
            }
        });
        shareDialog.show();
    }
}
