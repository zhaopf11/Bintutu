package com.zhurui.bunnymall.mine.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.adapter.EvaluateOrdersAdapter;
import com.zhurui.bunnymall.mine.adapter.EvaluatePictureAdapter;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.utils.BitmapUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.FileStorage;
import com.zhurui.bunnymall.utils.FileUtil;
import com.zhurui.bunnymall.utils.ImageCompressUtils.Luban;
import com.zhurui.bunnymall.utils.ImageCompressUtils.OnCompressListener;
import com.zhurui.bunnymall.utils.PermissionsActivity;
import com.zhurui.bunnymall.utils.PermissionsChecker;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.UploadUtil;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.DialogUtil;
import com.zhurui.bunnymall.viewutils.EvaluateHeartLin;
import com.zhurui.bunnymall.viewutils.PictureChooseDialog;
import com.zhurui.bunnymall.viewutils.SpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class EvaluateActivity extends BaseActivity implements UploadUtil.UploadListener {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.edit_mind)
    EditText edit_mind;
    @Bind(R.id.checkbox_anonymous)
    CheckBox checkbox_anonymous;


    @Bind(R.id.text_addpicture)
    TextView text_addpicture;
    @Bind(R.id.recycler_picture)
    RecyclerView recycler_picture;
    @Bind(R.id.recycler_product)
    RecyclerView recycler_product;
    private EvaluatePictureAdapter evaluatePictureAdapter;


    @Bind(R.id.heartlin1)
    EvaluateHeartLin heartlin1;
    @Bind(R.id.heartlin2)
    EvaluateHeartLin heartlin2;
    @Bind(R.id.heartlin3)
    EvaluateHeartLin heartlin3;
    private PictureChooseDialog pictureChooseDialog = null;
    private List<Bitmap> bitmaps = null;
    private Bitmap newPhoto = null;
    private Uri imageUri;
    private Uri outputUri;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private String shopOrderId;
    private List<OrderBean> orderBeanList;
    private int productevaluationstar = 0;
    private int logisticsevaluationstar = 0;
    private int serviceevaluationstar = 0;

    private String fileName;
    private String imagePath;
    private File newFile;
    public Map<Object, String> fileNameMap = new HashMap<>();
    public List<String> fileNameList = new ArrayList<>();
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private File file;
    Bitmap photoBmp = null;
    private boolean isClickCamera;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                File nfile = (File) msg.obj;
                dialogUtil.showLoadingDialog(EvaluateActivity.this, "");
                new Thread(new Runnable() {  //开启线程上传文件
                    @Override
                    public void run() {
                        Looper.prepare();
                        UploadUtil uploadUtil = new UploadUtil();
                        uploadUtil.setUploadListener(EvaluateActivity.this);
                        uploadUtil.uploadFile(nfile, Contants.UPLOAD_URL, "comment");
                    }
                }).start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.evaluate_publish);
        text_right.setText(R.string.publish);
        mPermissionsChecker = new PermissionsChecker(this);
        text_right.setTextColor(getResources().getColor(R.color.gray));
    }

    private void initData() {
        orderBeanList = (List<OrderBean>) getIntent().getSerializableExtra("orderBeanList");
        shopOrderId = orderBeanList.get(0).getShopOrderID();
        EvaluateOrdersAdapter evaluateOrdersAdapter = new EvaluateOrdersAdapter(this, orderBeanList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_product.setLayoutManager(linearLayoutManager1);
        recycler_product.setAdapter(evaluateOrdersAdapter);

        bitmaps = new ArrayList<Bitmap>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_picture.setLayoutManager(linearLayoutManager);
        recycler_picture.addItemDecoration(new SpaceItemDecoration(10));
        evaluatePictureAdapter = new EvaluatePictureAdapter(BaseApplication.getInstance(), bitmaps);
        evaluatePictureAdapter.setOnItemClickLitener(new EvaluatePictureAdapter.OnItemClickLitener() {
            @Override
            public void delete(int position) {
                String imageName = fileNameMap.get(bitmaps.get(position));
                if (fileNameList.contains(imageName)) {
                    fileNameList.remove(imageName);
                }
                bitmaps.remove(position);
                evaluatePictureAdapter.notifyDataSetChanged();
                if (bitmaps.size() < 4) {
                    text_addpicture.setVisibility(View.VISIBLE);
                }
            }
        });
        recycler_picture.setAdapter(evaluatePictureAdapter);
        heartlin1.setOnClick(new EvaluateHeartLin.OnClick() {
            @Override
            public void onItemClice(int heart) {
                productevaluationstar = heart;
            }
        });

        heartlin2.setOnClick(new EvaluateHeartLin.OnClick() {
            @Override
            public void onItemClice(int heart) {
                logisticsevaluationstar = heart;
            }
        });

        heartlin3.setOnClick(new EvaluateHeartLin.OnClick() {
            @Override
            public void onItemClice(int heart) {
                serviceevaluationstar = heart;
            }
        });

        pictureChooseDialog = new PictureChooseDialog(EvaluateActivity.this, R.style.CustomDialog);
        pictureChooseDialog.setOnItemClick(new PictureChooseDialog.OnItemClick() {
            @Override
            public void takeOnClick() {
                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                    startPermissionsActivity();
                } else {
                    openCamera();
                }
                isClickCamera = true;
            }

            @Override
            public void chooseOnClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        startPermissionsActivity();
                    } else {
                        selectFromAlbum();
                    }
                } else {
                    selectFromAlbum();
                }
                isClickCamera = false;
            }
        });

    }

    /**
     * 拍照
     */
    private void openCamera() {
//        file = new FileStorage().createIconFile();
        fileName = BaseApplication.getInstance().getUser().getUserID() + "_" + System.currentTimeMillis();
        file = new FileStorage().createCropFile(fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(EvaluateActivity.this, getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
    }

    /**
     * 从相册选择照片
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //此处调用了图片选择器
        //如果直接写intent.setDataAndType("image/*");
        //调用的是系统图库
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                compressImage(file);
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                if (data != null) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        handleImageOnKitKat(data);//获取相册选中图片的路径
//                        fileName = BaseApplication.getInstance().getUser().getUserID() + "_" + System.currentTimeMillis();
//                        file = new FileStorage().createCropFile(fileName);
//                        FileUtil.copyFile(imagePath, file);//将选中照片复制到新的路径下
                        file = new File(imagePath);
                        compressImage(file);
                    }
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.show(this, "如果拒绝访问权限，部分功能可能无法使用");
                    finish();
                } else {
                    if (isClickCamera) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
        }
    }


    @OnClick(R.id.text_addpicture)
    public void addPicture() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity();
            } else {
                pictureChooseDialog.show();
            }
        } else {
            pictureChooseDialog.show();
        }

    }

    @OnClick(R.id.text_right)
    public void poblish() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        String str = edit_mind.getText().toString();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.show(EvaluateActivity.this, "请输入评价内容");
        } else if (productevaluationstar > 0) {
            try {
                jsonObject.put("cmd", "48");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                jsonObject.put("shoporderid", shopOrderId);
                jsonObject.put("commentcontent", edit_mind.getText());
                jsonObject.put("imagenames", Utils.listToString(fileNameList));
                if (checkbox_anonymous.isChecked()) {
                    jsonObject.put("type", "0");
                } else {
                    jsonObject.put("type", "1");
                }
                jsonObject.put("productevaluationstar", productevaluationstar);
                jsonObject.put("logisticsevaluationstar", logisticsevaluationstar);
                jsonObject.put("serviceevaluationstar", serviceevaluationstar);
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject1 = new JSONObject();
                if (orderBeanList != null && orderBeanList.size() > 0) {
                    for (int i = 0; i < orderBeanList.size(); i++) {
                        List<String> strList = new ArrayList<>();
                        jsonObject1.put("productid", orderBeanList.get(i).getProductID());
                        strList.add(orderBeanList.get(i).getColorPropertiesName());
                        strList.add(orderBeanList.get(i).getSizePropertiesName());
                        jsonObject1.put("info", Utils.listToString(strList));
                    }
                }
                jsonArray.put(jsonObject1);
                jsonObject.put("propname", jsonArray);
                params.put("sendmsg", jsonObject.toString());
                getInfo(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.show(EvaluateActivity.this, "请对商品描述进行评分");
        }
    }

    public void getInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseResp) {
                if (baseResp.getResult() > 0) {
//                    ToastUtils.show(EvaluateActivity.this, "评价成功");
                    DialogUtil dialogUtil = new DialogUtil();
                    dialogUtil.infoDialog(EvaluateActivity.this, "评价成功", true, false);
                    dialogUtil.setOnClick(new DialogUtil.OnClick() {
                        @Override
                        public void leftClick() {

                        }

                        @Override
                        public void rightClick() {
                            dialogUtil.dialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    ToastUtils.show(EvaluateActivity.this, getResources().getString(R.string.evaluate_err));
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(EvaluateActivity.this, getResources().getString(R.string.request_err));
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(EvaluateActivity.this, getResources().getString(R.string.request_err));

            }
        });
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    @Override
    public void uploadListener(String result) {
        dialogUtil.dismiss();
        if (null != result && !"".equals(result)) {
            ToastUtils.show(EvaluateActivity.this, "上传成功");
        } else {
            ToastUtils.show(EvaluateActivity.this, "上传失败，请稍后重试");
        }
        Looper.loop();
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 压缩图片
     * @param file
     */
    public void compressImage(final File file) {
        Log.e("原图file", "===========" + file.getAbsolutePath().toString());
        try {
            long a = FileUtil.getFileSize(file);
            Log.e("原图file大小：", "===========" + FileUtil.FormetFileSize(a));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Luban.get(this).load(file) //传要压缩的图片
                .putGear(Luban.THIRD_GEAR)//设定压缩档次，默认三挡
                .setFilename(BaseApplication.getInstance().getUser().getUserID() + "_" + System.currentTimeMillis() + ".png")//设置压缩后图片的名字
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File newfile) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        Log.e("newfile", "===========" + newfile.getAbsolutePath().toString());
                        try {
                            long a = FileUtil.getFileSize(newfile);
                            Log.e("newfile大小：", "===========" + FileUtil.FormetFileSize(a));
                            showImageAndUpload(newfile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.obj = newfile;
                        message.what = 100;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(EvaluateActivity.this, "上传失败，请稍后重试");
                    }
                });
    }

    /**
     * 展示照片并上传照片
     */
    public void showImageAndUpload(File newfile) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Bitmap pb = BitmapFactory.decodeStream(inputStream);
            newPhoto = BitmapUtils.scaleImage(pb, 480, 800);
            bitmaps.add(newPhoto);
            fileNameList.add("commentImage/" +newfile.getName());
            evaluatePictureAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmaps.size() == 4) {
            text_addpicture.setVisibility(View.GONE);
        }
    }
}
