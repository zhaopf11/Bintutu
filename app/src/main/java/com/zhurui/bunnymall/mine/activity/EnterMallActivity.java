package com.zhurui.bunnymall.mine.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.activity.WebViewActivity;
import com.zhurui.bunnymall.mine.bean.EnterMallBean;
import com.zhurui.bunnymall.mine.bean.EnterMallListBean;
import com.zhurui.bunnymall.utils.BitmapUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.FileStorage;
import com.zhurui.bunnymall.utils.FileUtil;
import com.zhurui.bunnymall.utils.ImageCompressUtils.Luban;
import com.zhurui.bunnymall.utils.ImageCompressUtils.OnCompressListener;
import com.zhurui.bunnymall.utils.PermissionsActivity;
import com.zhurui.bunnymall.utils.PermissionsChecker;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.UploadUtil;
import com.zhurui.bunnymall.viewutils.DialogUtil;
import com.zhurui.bunnymall.viewutils.PictureChooseDialog;
import com.zhurui.bunnymall.viewutils.ProvinceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class EnterMallActivity extends BaseActivity implements UploadUtil.UploadListener {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.edit_storename)
    EditText edit_storename;
    @Bind(R.id.edit_contacts)
    EditText edit_contacts;
    @Bind(R.id.edit_contactphone)
    EditText edit_contactphone;
    @Bind(R.id.edit_contactemail)
    EditText edit_contactemail;
    @Bind(R.id.edit_addressdetail)
    EditText edit_addressdetail;


    @Bind(R.id.radiogroup_type)
    RadioGroup radiogroup_type;
    @Bind(R.id.radio_personal)
    RadioButton radio_personal;
    @Bind(R.id.radio_mall)
    RadioButton radio_mall;

    @Bind(R.id.text_logo)
    ImageView text_logo;
    @Bind(R.id.text_identify)
    TextView text_identify;
    @Bind(R.id.text_img1)
    ImageView text_img1;
    @Bind(R.id.text_img2)
    ImageView text_img2;
    @Bind(R.id.text_city)
    TextView text_city;

    @Bind(R.id.shenhe_states)
    TextView shenhe_states;
    @Bind(R.id.shenhe_message)
    TextView shenhe_message;
    @Bind(R.id.text_adavice)
    TextView text_adavice;
    @Bind(R.id.text_iamge_lager)
    TextView text_iamge_lager;
    @Bind(R.id.text_service_agreement)
    TextView text_service_agreement;

    @Bind(R.id.linlay_message)
    LinearLayout linlay_message;
    @Bind(R.id.linlay_states)
    LinearLayout linlay_states;
    @Bind(R.id.linlay_design_ourself)
    LinearLayout linlay_design_ourself;
    @Bind(R.id.lin_infomind)
    LinearLayout lin_infomind;
    @Bind(R.id.rel_choosecity)
    RelativeLayout rel_choosecity;
    @Bind(R.id.btn_submit)
    Button btn_submit;


    private ProvinceDialog provinceDialog;
    private PictureChooseDialog pictureChooseDialog = null;
    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private int state = -1;
    private int type = 2;

    private boolean isClickCamera;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private String imagePath;
    private String fileName;
    private File newFile;
    private File file;

    private String logoFileName;
    private String pictureFileName1;
    private String pictureFileName2;
    Bitmap bitmap = null;
    private ImageLoader imageLoader;
    private Bitmap photoBmp = null;
    private String mProvince;
    private String mCity;
    private String mCounty;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                File nfile = (File) msg.obj;
                dialogUtil.showLoadingDialog(EnterMallActivity.this, "");
                new Thread(new Runnable() {  //开启线程上传文件
                    @Override
                    public void run() {
                        Looper.prepare();
                        UploadUtil uploadUtil = new UploadUtil();
                        uploadUtil.setUploadListener(EnterMallActivity.this);
                        uploadUtil.uploadFile(nfile, Contants.UPLOAD_URL, "supplier");
                    }
                }).start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mall);
        ButterKnife.bind(this);
        imageLoader = BaseApplication.getInstance().getImageLoader();
        initData();
//        initView();

    }

    private void initView() {
        text_title.setText(R.string.enter_check);
        text_right.setVisibility(View.GONE);
        radiogroup_type.setOnCheckedChangeListener(onCheckedChangeListener);
        mPermissionsChecker = new PermissionsChecker(this);
        pictureChooseDialog = new PictureChooseDialog(EnterMallActivity.this, R.style.CustomDialog);
        pictureChooseDialog.setOnItemClick(new PictureChooseDialog.OnItemClick() {
            @Override
            public void takeOnClick() {
                //检查权限(6.0以上做权限判断)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        startPermissionsActivity();
                    } else {
                        openCamera();
                    }
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

    private void initData() {

        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "68");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            getData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<EnterMallListBean>(this) {

            @Override
            public void onSuccess(Response response, EnterMallListBean enterMallListBean) {
                if (enterMallListBean.getResult() > 0) {
                    List<EnterMallBean> enterList = enterMallListBean.getList();
                    if (enterList != null && enterList.size() > 0) {
                        EnterMallBean enterMallBean = enterList.get(0);
                        if (enterMallBean != null) {
                            upToUi(enterMallBean);
                        }
                    } else {
                        initView();
                    }
                } else {
                    ToastUtils.show(BaseApplication.getInstance(), enterMallListBean.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(EnterMallActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(EnterMallActivity.this, "请求失败，请稍后重试");
            }
        });
    }

    /**
     * 更新界面
     */
    private void upToUi(EnterMallBean enterMallBean) {
        text_title.setText("审核结果");
        if ("90".equals(enterMallBean.getStatus())) {
            initView();
            linlay_states.setVisibility(View.VISIBLE);
            linlay_message.setVisibility(View.VISIBLE);
            shenhe_states.setText("审核不通过");
            btn_submit.setText("重新提交");
            edit_storename.setText(enterMallBean.getName());
            edit_contacts.setText(enterMallBean.getLinkman());
            edit_contactphone.setText(enterMallBean.getLinkphone());
            edit_contactemail.setText(enterMallBean.getLinkemail());
            text_city.setText(enterMallBean.getProvincename() + enterMallBean.getCityname() + enterMallBean.getTownname());
            edit_addressdetail.setText(enterMallBean.getAddress());
            mProvince = enterMallBean.getProvincename();
            mCity = enterMallBean.getCityname();
            mCounty = enterMallBean.getTownname();
            logoFileName = enterMallBean.getLogo();
            new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getLogo(), text_logo);
            if (!TextUtils.isEmpty(enterMallBean.getSupplierTypeID())) {
                if ("1".equals(enterMallBean.getSupplierTypeID())) {
                    type = 1;
                    radio_personal.setText("企业");
                    text_identify.setText("营业执照");
                    pictureFileName1 = enterMallBean.getBusinesslicenseimage();
                    new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getBusinesslicenseimage(), text_img1);
                    text_img2.setVisibility(View.GONE);
                } else if ("2".equals(enterMallBean.getSupplierTypeID())) {
                    pictureFileName1 = enterMallBean.getIdcardimage1();
                    pictureFileName2 = enterMallBean.getIdcardimage2();
                    new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getIdcardimage1(), text_img1);
                    new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getIdcardimage2(), text_img2);
                }
            }
        } else {
            text_right.setVisibility(View.GONE);
            linlay_design_ourself.setVisibility(View.GONE);
            text_adavice.setVisibility(View.GONE);
            text_iamge_lager.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            radio_mall.setVisibility(View.GONE);
            linlay_states.setVisibility(View.VISIBLE);
            lin_infomind.setVisibility(View.GONE);
            edit_storename.setText(enterMallBean.getName());
            edit_contacts.setText(enterMallBean.getLinkman());
            edit_contactphone.setText(enterMallBean.getLinkphone());
            edit_contactemail.setText(enterMallBean.getLinkemail());
            text_city.setText(enterMallBean.getProvincename() + enterMallBean.getCityname() + enterMallBean.getTownname());
            text_city.setCompoundDrawables(null, null, null, null);
            edit_addressdetail.setText(enterMallBean.getAddress());
            edit_storename.setEnabled(false);
            edit_contacts.setEnabled(false);
            edit_contactphone.setEnabled(false);
            edit_contactemail.setEnabled(false);
            edit_addressdetail.setEnabled(false);
            rel_choosecity.setEnabled(false);
            text_city.setEnabled(false);
            text_logo.setEnabled(false);
            text_img1.setEnabled(false);
            text_img2.setEnabled(false);
            new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getLogo(), text_logo);
            new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getIdcardimage1(), text_img1);
            if (!TextUtils.isEmpty(enterMallBean.getSupplierTypeID())) {
                if ("1".equals(enterMallBean.getSupplierTypeID())) {
                    radio_personal.setText("企业");
                    text_identify.setText("营业执照");
                    text_img2.setVisibility(View.GONE);
                    new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getBusinesslicenseimage(), text_img1);
                } else if ("2".equals(enterMallBean.getSupplierTypeID())) {
                    radio_personal.setText("个人");
                    text_identify.setText("身份证件照");
                    new DownloadImageTask().execute(Contants.IMAGE_HEADURL + enterMallBean.getIdcardimage2(), text_img2);
                }
            }
            if ("10".equals(enterMallBean.getStatus())) {
                shenhe_states.setText("待审核");
            } else if ("20".equals(enterMallBean.getStatus())) {
                shenhe_states.setText("已上线");
            } else if ("30".equals(enterMallBean.getStatus())) {
                shenhe_states.setText("已下线");
            }
            radio_personal.setChecked(false);
        }
        shenhe_message.setText(enterMallBean.getAuditNote());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE://从相册选择
                if (data != null) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        handleImageOnKitKat(data);
                        file = new File(imagePath);
                        compressImage(file);
                    }
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == RESULT_OK) {
                    compressImage(file);
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

    /**
     * 打开系统相机
     */
    private void openCamera() {
        fileName = BaseApplication.getInstance().getUser().getUserID() + "_" + System.currentTimeMillis();
        file = new FileStorage().createCropFile(fileName);
//        file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(EnterMallActivity.this, getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    /**
     * 裁剪
     */
    private void cropPhoto() {

        Uri outputUri = Uri.fromFile(newFile);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
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

//        cropPhoto();
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
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


    @OnClick(R.id.text_logo)
    public void getLogo() {
        if (pictureChooseDialog != null) {
            pictureChooseDialog.show();
            state = 1;
        }
    }

    @OnClick(R.id.text_img1)
    public void getImage1() {
        if (pictureChooseDialog != null) {
            pictureChooseDialog.show();
            state = 2;
        }
    }

    @OnClick(R.id.text_img2)
    public void getImage2() {
        if (pictureChooseDialog != null) {
            pictureChooseDialog.show();
            state = 3;
        }
    }


    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.radio_personal:
                    text_identify.setText(R.string.identify);
                    text_img1.setImageResource(R.drawable.identify_up);
                    text_img2.setVisibility(View.VISIBLE);
                    type = 2;
                    break;
                case R.id.radio_mall:
                    text_identify.setText("营业执照");
                    text_img2.setVisibility(View.GONE);
                    text_img1.setImageResource(R.drawable.add_business);
                    type = 1;
                    break;
            }
        }
    };

    @OnClick(R.id.text_city)
    public void chooseCity() {
        provinceDialog = new ProvinceDialog(EnterMallActivity.this, R.style.CustomDialog);
        provinceDialog.setBtnOnClice(new ProvinceDialog.BtnOnClick() {
            @Override
            public void btnOnClick(String province, String city, String county) {
                mProvince = province;
                mCity = city;
                mCounty = county;
                text_city.setText(province + city + county);
            }
        });
        provinceDialog.show();
    }

    @OnClick(R.id.btn_submit)
    public void updateInfo() {
        String storename = edit_storename.getText().toString().trim();
        String contactname = edit_contacts.getText().toString().trim();
        String contactphone = edit_contactphone.getText().toString().trim();
        String contactemaill = edit_contactemail.getText().toString().trim();
        String address = text_city.getText().toString().trim();
        String addressdetail = edit_addressdetail.getText().toString().trim();

        if (TextUtils.isEmpty(storename)) {
            ToastUtils.show(EnterMallActivity.this, "请输入商户名称");
        } else if (TextUtils.isEmpty(logoFileName)) {
            ToastUtils.show(EnterMallActivity.this, "请上传商家Logo");
        } else if (TextUtils.isEmpty(pictureFileName1)) {
            if (1 == type) {
                ToastUtils.show(EnterMallActivity.this, "请上传营业执照");
            } else if (2 == type) {
                ToastUtils.show(EnterMallActivity.this, "请上传身份证正面照片");
            }
        } else if (2 == type && TextUtils.isEmpty(pictureFileName2)) {
            ToastUtils.show(EnterMallActivity.this, "请上传身份证反面照片");
        } else if (TextUtils.isEmpty(contactname)) {
            ToastUtils.show(EnterMallActivity.this, "请输入联系人");
        } else if (TextUtils.isEmpty(contactphone)) {
            ToastUtils.show(EnterMallActivity.this, "请输入联系电话");
        } else if (!RegexUtil.isMobileNO(contactphone)) {
            ToastUtils.show(EnterMallActivity.this, "请输入正确的手机号码");
        } else if (TextUtils.isEmpty(contactemaill)) {
            ToastUtils.show(EnterMallActivity.this, "请输入联系邮箱");
        } else if (!RegexUtil.isEmail(contactemaill)) {
            ToastUtils.show(EnterMallActivity.this, "请输入正确的邮箱");
        } else if (TextUtils.isEmpty(address)) {
            ToastUtils.show(EnterMallActivity.this, "请选择所在城市");
        } else if (TextUtils.isEmpty(addressdetail)) {
            ToastUtils.show(EnterMallActivity.this, "请输入详细地址");
        } else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cmd", "19");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                jsonObject.put("logo", "supplier/" + logoFileName);
                if (2 == type) {
                    jsonObject.put("idcardimage1", "supplier/" + pictureFileName1);
                    jsonObject.put("idcardimage2", "supplier/" + pictureFileName2);
                    jsonObject.put("businesslicenseimage", "");
                } else if (1 == type) {
                    jsonObject.put("idcardimage1", "");
                    jsonObject.put("idcardimage2", "");
                    jsonObject.put("businesslicenseimage", "supplier/" + pictureFileName1);
                }

                jsonObject.put("suppliertypeid", type + "");
                jsonObject.put("name", storename);
                jsonObject.put("linkman", contactname);
                jsonObject.put("linkphone", contactphone);
                jsonObject.put("linkemail", contactemaill);
                jsonObject.put("provincename", mProvince);
                jsonObject.put("cityname", mCity);
                jsonObject.put("townname", mCounty);
                jsonObject.put("address", addressdetail);
                params.put("sendmsg", jsonObject.toString());
                updateData(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    private void updateData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(EnterMallActivity.this, "提交成功");
                    DialogUtil dialogUtil = new DialogUtil();
                    dialogUtil.infoDialog(EnterMallActivity.this, "成功提交，验证结果将以短信形式发至您的手机，注意查收", true, false);
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
                    ToastUtils.show(EnterMallActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(EnterMallActivity.this, "提交失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(EnterMallActivity.this, "提交失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.img_help)
    public void showInfo() {
        alertDialog();
    }


    private void alertDialog() {

        Dialog dialog = new Dialog(EnterMallActivity.this, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.layout_infoshow, null);
        TextView text_sure = (TextView) dv.findViewById(R.id.text_sure);
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();


    }


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    /**
     * 电商平台服务协议
     *
     * @param view
     */
    @OnClick(R.id.text_service_agreement)
    public void serviceAgreement(View view) {
        startActivity(new Intent(BaseApplication.getInstance(), WebViewActivity.class).putExtra("url", "file:///android_asset/business_ervice_agreement.htm"));
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
                        ToastUtils.show(EnterMallActivity.this, "上传失败，请稍后重试");
                    }
                });
    }

    /**
     * 展示图片
     *
     * @param file
     */
    private void showImageAndUpload(File file) {
        try {
            //将图片文件转成bitmap，并展示
            InputStream inputStream = new FileInputStream(file);
            Bitmap pb = BitmapFactory.decodeStream(inputStream);
            bitmap = BitmapUtils.scaleImage(pb, 800, 480);
            if (null != bitmap) {
                if (1 == state) {
                    text_logo.setImageBitmap(bitmap);
                    logoFileName = file.getName();
                } else if (2 == state) {
                    text_img1.setImageBitmap(bitmap);
                    pictureFileName1 = file.getName();
                } else if (3 == state) {
                    text_img2.setImageBitmap(bitmap);
                    pictureFileName2 = file.getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadListener(String result) {
        dialogUtil.dismiss();
        if (null != result && !"".equals(result)) {
            ToastUtils.show(EnterMallActivity.this, "上传成功");
        } else {
            ToastUtils.show(EnterMallActivity.this, "上传失败，请稍后重试");
        }
        Looper.loop();
    }

    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }


    class DownloadImageTask extends AsyncTask<Object, Void, Drawable> {
        ImageView imageView = null;

        protected Drawable doInBackground(Object... urls) {
            imageView = (ImageView) urls[1];
            Drawable drawable = loadImageFromNetwork((String) urls[0]);
            return drawable;
        }

        protected void onPostExecute(Drawable result) {
            if (null != result) {
                imageView.setImageDrawable(BitmapUtils.zoomDrawable(result, 800, 480));
            }
        }
    }
}
