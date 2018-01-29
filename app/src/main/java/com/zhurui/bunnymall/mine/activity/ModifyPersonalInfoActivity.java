package com.zhurui.bunnymall.mine.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.msg.BaseResp;
import com.zhurui.bunnymall.utils.BitMapUtil;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.FileStorage;
import com.zhurui.bunnymall.utils.PermissionsActivity;
import com.zhurui.bunnymall.utils.PermissionsChecker;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.UploadUtil;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.CircleImageView;
import com.zhurui.bunnymall.viewutils.DateTimePickDialog;
import com.zhurui.bunnymall.viewutils.PictureChooseDialog;
import com.zhurui.bunnymall.viewutils.timepickerview.TimePickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Decoder.BASE64Decoder;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class ModifyPersonalInfoActivity extends BaseActivity implements UploadUtil.UploadListener, View.OnClickListener, TextWatcher {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.edit_nickname)
    EditText edit_nickname;
    @Bind(R.id.edit_name)
    EditText edit_name;
    @Bind(R.id.edit_birthday)
    TextView edit_birthday;
    @Bind(R.id.radiogroup_sex)
    RadioGroup radiogroup_sex;
    @Bind(R.id.radiobtn_man)
    RadioButton radiobtn_man;
    @Bind(R.id.radiobtn_woman)
    RadioButton radiobtn_woman;
    @Bind(R.id.circle_head)
    CircleImageView circle_head;

    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private int sex = 1;
    private boolean isClickCamera;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private String imagePath;
    private String fileName;
    private File newFile;
    private boolean headImage;
    private Bitmap bitmap;
    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        text_title.setText(R.string.personal_info);
        text_right.setText(R.string.salve);
        radiogroup_sex.setOnCheckedChangeListener(onCheckedChangeListener);
        mPermissionsChecker = new PermissionsChecker(this);
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);

        String uri = Contants.IMAGE_HEADURL + BaseApplication.getInstance().getUser().getMainImage();
        Glide.with(this).load(uri)
                .placeholder(R.drawable.head_normal)
                .error(R.drawable.head_normal)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                .into(circle_head);

        if (!TextUtils.isEmpty(BaseApplication.getInstance().getUser().getNick())) {
            edit_nickname.setText(BaseApplication.getInstance().getUser().getNick());
        } else {
            edit_nickname.setText("宾兔兔");
        }
        edit_name.setText(BaseApplication.getInstance().getUser().getName());
        edit_birthday.setText(BaseApplication.getInstance().getUser().getBirthday());

        Utils.cursorToEnd(edit_nickname, edit_nickname.getText().toString());
        if ("1".equals(BaseApplication.getInstance().getUser().getSex())) {
            radiobtn_man.setChecked(true);
            radiobtn_woman.setChecked(false);
        } else if ("2".equals(BaseApplication.getInstance().getUser().getSex())) {
            radiobtn_woman.setChecked(true);
            radiobtn_man.setChecked(false);
        }
        //当点击该控件时显示光标
        edit_name.setOnClickListener(this);
        edit_nickname.setOnClickListener(this);
        edit_nickname.addTextChangedListener(this);
    }

    @OnClick(R.id.circle_head)
    public void chooseHeadImg() {
        PictureChooseDialog pictureChooseDialog = new PictureChooseDialog(ModifyPersonalInfoActivity.this, R.style.CustomDialog);
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

        pictureChooseDialog.show();
    }

    /**
     * 打开系统相机
     */
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(ModifyPersonalInfoActivity.this, getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri
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
        fileName = BaseApplication.getInstance().getUser().getUserID() + "_" + System.currentTimeMillis();
        newFile = new FileStorage().createCropFile(fileName);
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
        if (data != null) {
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

            cropPhoto();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE://从相册选择
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                bitmap = null;
                try {
//                    if (isClickCamera) {
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                    } else {
//                        bitmap = BitmapFactory.decodeFile(imagePath);
//                    }
                    bitmap = BitmapFactory.decodeFile(newFile.getPath());
                    circle_head.setImageBitmap(bitmap);
                    if (null != newFile && null != bitmap) {
                        UploadUtil uploadUtil = new UploadUtil();
                        uploadUtil.setUploadListener(this);
                        new Thread(new Runnable() {  //开启线程上传文件
                            @Override
                            public void run() {
                                Looper.prepare();
                                uploadUtil.uploadFile(newFile, Contants.UPLOAD_URL, "other");

                            }
                        }).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
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

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.radiobtn_man:
                    sex = 1;
                    BaseApplication.getInstance().getUser().setSex(sex + "");
                    break;
                case R.id.radiobtn_woman:
                    sex = 2;
                    BaseApplication.getInstance().getUser().setSex(sex + "");
                    break;
            }
        }
    };


    @OnClick(R.id.text_right)
    public void salveInfo() {
        String nickname = edit_nickname.getText().toString().trim();
        String name = edit_name.getText().toString().trim();
        String birthday = edit_birthday.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            ToastUtils.show(ModifyPersonalInfoActivity.this, "请输入昵称");
        } else if (TextUtils.isEmpty(name)) {
            ToastUtils.show(ModifyPersonalInfoActivity.this, "请输入姓名");
        } else if (TextUtils.isEmpty(birthday)) {
            ToastUtils.show(ModifyPersonalInfoActivity.this, "请选择生日");
        } else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cmd", "21");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                JSONArray jsonArray = new JSONArray();
                JSONObject sexjson = new JSONObject();
                sexjson.put("key", "sex");
                sexjson.put("value", sex);
                jsonArray.put(sexjson);

                JSONObject nickjson = new JSONObject();
                nickjson.put("key", "nick");
                nickjson.put("value", nickname);
                jsonArray.put(nickjson);

                JSONObject namejson = new JSONObject();
                namejson.put("key", "name");
                namejson.put("value", name);
                jsonArray.put(namejson);

                JSONObject birthdayjson = new JSONObject();
                birthdayjson.put("key", "birthday");
                birthdayjson.put("value", birthday);
                jsonArray.put(birthdayjson);
                if (headImage) {
                    JSONObject headimgjson = new JSONObject();
                    headimgjson.put("key", "mainImage");
                    headimgjson.put("value", "other/" + fileName + ".png");
                    jsonArray.put(headimgjson);
                }
                jsonObject.put("msg", jsonArray);
                params.put("sendmsg", jsonObject.toString());
                modifyInfo(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    private void modifyInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg != null){
                    if (baseRespMsg.getResult() > 0) {
                        ToastUtils.show(ModifyPersonalInfoActivity.this, "修改成功");
                        if (bitmap != null) {
                            Contants.headDrawable = new BitmapDrawable(bitmap);
                        }
                        BaseApplication.getInstance().getUser().setNick(edit_nickname.getText().toString().trim());
                        BaseApplication.getInstance().getUser().setName(edit_name.getText().toString().trim());
                        BaseApplication.getInstance().getUser().setBirthday(edit_birthday.getText().toString().trim());
                        BaseApplication.getInstance().getUser().setSex(sex +"");
                        if (!TextUtils.isEmpty(fileName)) {
                            BaseApplication.getInstance().getUser().setMainImage("other/" + fileName + ".png");
                        }
                        BaseApplication.getInstance().putUser(BaseApplication.getInstance().getUser(), BaseApplication.getInstance().getToken());
                        finish();
                    } else {
                        ToastUtils.show(ModifyPersonalInfoActivity.this, baseRespMsg.getRetmsg());
                    }
                }else{
                    ToastUtils.show(BaseApplication.getInstance(), "修改失败，请稍后重试");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ModifyPersonalInfoActivity.this, "修改失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ModifyPersonalInfoActivity.this, "修改失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre() {
        finish();
    }

    @Override
    public void uploadListener(String result) {
        if (null != result && !"".equals(result)) {
            headImage = true;
            ToastUtils.show(ModifyPersonalInfoActivity.this, "头像上传成功");
        } else {
            ToastUtils.show(ModifyPersonalInfoActivity.this, "头像上传失败，请重试");
        }
        Looper.loop();
    }

    /**
     * 日期弹出框
     */
    @OnClick(R.id.lin_birthday)
    public void dataTime() {
        if(!TextUtils.isEmpty(edit_birthday.getText())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null; //初始化date
            try {
                date = sdf.parse(edit_birthday.getText() +""); //Mon Jan 14 00:00:00 CST 2013
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pvTime.setTime(date);
        }
        pvTime.show();

        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(String date) {
                edit_birthday.setText(date);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == edit_name) {
            edit_name.setCursorVisible(true);
        } else if (v == edit_nickname) {
            edit_nickname.setCursorVisible(true);
        }
    }

    /**
     * 控制输入的字符
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String desc = edit_nickname.getText().toString();
        if(desc.length() >= 6){
            ToastUtils.show(mContext, "昵称不能超过6个字");
        }
    }
}
