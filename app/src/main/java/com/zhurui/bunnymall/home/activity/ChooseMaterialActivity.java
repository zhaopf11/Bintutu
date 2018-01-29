package com.zhurui.bunnymall.home.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.adapter.ChooseMaterialAdapter;
import com.zhurui.bunnymall.home.bean.CustomGroupProperty;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.home.bean.FootTypeBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.home.msg.AddCartRespMsg;
import com.zhurui.bunnymall.home.msg.CustomPropertyRespMsg;
import com.zhurui.bunnymall.mine.activity.EnterMallActivity;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.FileStorage;
import com.zhurui.bunnymall.utils.PermissionsActivity;
import com.zhurui.bunnymall.utils.PermissionsChecker;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.UploadUtil;
import com.zhurui.bunnymall.viewutils.FlowRadioGroup;
import com.zhurui.bunnymall.viewutils.MyExpandableListView;
import com.zhurui.bunnymall.viewutils.PictureChooseDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseMaterialActivity extends BaseActivity implements UploadUtil.UploadListener {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_designepicture)
    TextView text_designepicture;

    @Bind(R.id.lin_designepicture)
    LinearLayout lin_designepicture;

    @Bind(R.id.checkbox_footmessage)
    CheckBox checkbox_footmessage;
    @Bind(R.id.flowRadioGroupFoot)
    FlowRadioGroup flowRadioGroupFoot;

    @Bind(R.id.expandable)
    MyExpandableListView expandable;
    @Bind(R.id.btn_reset)
    Button btn_reset;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    private ChooseMaterialAdapter materialAdapter;
    private Map<Integer, String> checkMap = new HashMap<Integer, String>();
    public static Map<Integer, Integer> childCheckMap = new HashMap<Integer, Integer>();
    private Map<String, List<CustomeChildPropertyBean>> propertyMap = new HashMap<>();
    private String customPropertiesInfo;

    private PictureChooseDialog pictureChooseDialog = null;
    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private String imagePath;
    private String fileName;
    private File newFile;

    private Map<String, String> propertyinfo = new HashMap<>();
    private Map<String, String> propertyname = new HashMap<>();
    private boolean isNeedBack = false;
    private ProductDetailBean productDetailBean;
    private List<FootTypeBean> footTypeBeen;
    private String foottypeid;
    private boolean toCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_material);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.choose_material);
        text_right.setVisibility(View.GONE);
        materialAdapter = new ChooseMaterialAdapter(ChooseMaterialActivity.this);
        materialAdapter.setOnItemClick(new ChooseMaterialAdapter.OnItemClick() {
            @Override
            public void groupClick(boolean isChecked, int groupposition) {
                if (isChecked) {
                    expandable.expandGroup(groupposition);
                } else {
                    expandable.collapseGroup(groupposition);
                }
            }

            @Override
            public void childClick(int groupPosition, String string, int childPosition, String propertyInfo, String propertyName) {
                checkMap.put(groupPosition, string);
                materialAdapter.checkMap = checkMap;
                childCheckMap.put(groupPosition, childPosition);
                propertyinfo.put(materialAdapter.propertyGroupBeen.get(groupPosition).getCustomPropertiesID(), propertyInfo);
                propertyname.put(materialAdapter.propertyGroupBeen.get(groupPosition).getCustomPropertiesID(), propertyName);
                materialAdapter.notifyDataSetChanged();
            }
        });
        expandable.setAdapter(materialAdapter);
        mPermissionsChecker = new PermissionsChecker(this);
        isNeedBack = getIntent().getBooleanExtra("isNeedBack", false);
        toCart = getIntent().getBooleanExtra("toCart", false);
        footTypeBeen = (List<FootTypeBean>) getIntent().getSerializableExtra("foottypeid");
        if (null != footTypeBeen && footTypeBeen.size() > 0) {
            for (int i = 0; i < footTypeBeen.size(); i++) {
                RadioButton button = (RadioButton) LayoutInflater.from(ChooseMaterialActivity.this).inflate(R.layout.radiobutton_material, null);
                button.setHeight((int) getResources().getDimension(R.dimen.margin_50));
                button.setText(footTypeBeen.get(i).getName() + "");
                button.setTag(footTypeBeen.get(i).getUserFootTypeDataID());
                button.setPadding((int) getResources().getDimension(R.dimen.navigation_bar_margin), 0, (int) getResources().getDimension(R.dimen.navigation_bar_margin), 0);
                flowRadioGroupFoot.addView(button, i);
            }

        }
        flowRadioGroupFoot.setOnCheckedChangeListener(onCheckedChangeListener);
        productDetailBean = (ProductDetailBean) getIntent().getSerializableExtra("product");
        //普通定制不上传设计图，自设计定制才可以上传设计图纸
        if("1".equals(productDetailBean.getCustomFlag())){
            lin_designepicture.setVisibility(View.GONE);
        }else{
            lin_designepicture.setVisibility(View.VISIBLE);
        }
        checkbox_footmessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flowRadioGroupFoot.setVisibility(View.VISIBLE);
                } else {
                    flowRadioGroupFoot.setVisibility(View.GONE);
                }
            }
        });
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (group.getId()) {
                case R.id.flowRadioGroupFoot:
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) group.getChildAt(i);
                        if (radioButton.getId() == checkedId) {
                            foottypeid = (String) radioButton.getTag();
                            checkbox_footmessage.setText(radioButton.getText());
                        }
                    }
                    break;
            }
        }
    };


    private void initData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "30");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            getMaterial(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMaterial(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<CustomPropertyRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CustomPropertyRespMsg propertyResp) {
                if (propertyResp.getResult() > 0) {
                    if (null != propertyResp.getList().get(0).getCustompropertieslist() && propertyResp.getList().get(0).getCustompropertieslist().size() > 0) {
                        String[] propertyInfoList = productDetailBean.getCustomPropertiesInfo().split(",");
                        List<CustomGroupProperty> customGroupProperties = RegexUtil.getCustomPropertyGroup(ChooseMaterialActivity.this, propertyInfoList, propertyResp.getList().get(0));
                        Map<String, List<CustomeChildPropertyBean>> map = RegexUtil.getCustomPropertyChild(ChooseMaterialActivity.this, propertyInfoList, propertyResp.getList().get(0));
                        materialAdapter.propertyGroupBeen = customGroupProperties;
                        materialAdapter.propertyMap = map;
                        if(childCheckMap != null){
                            childCheckMap.clear();
                        }
                        materialAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.show(ChooseMaterialActivity.this, propertyResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ChooseMaterialActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ChooseMaterialActivity.this, "请求失败，请稍后重试");

            }
        });
    }


    private void dealData() {

    }

    @OnClick(R.id.text_designepicture)
    public void choosePicture() {
        PictureChooseDialog pictureChooseDialog = new PictureChooseDialog(ChooseMaterialActivity.this, R.style.CustomDialog);
        pictureChooseDialog.text_take.setVisibility(View.GONE);
        pictureChooseDialog.setOnItemClick(new PictureChooseDialog.OnItemClick() {
            @Override
            public void takeOnClick() {

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
            }
        });

        pictureChooseDialog.show();
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
        fileName = BaseApplication.getInstance().getUser().getUserID() + "" + System.currentTimeMillis();
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
            case REQUEST_PICTURE_CUT://裁剪完成
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeFile(imagePath);
                    text_designepicture.setText("");
                    text_designepicture.setBackground(new BitmapDrawable(bitmap));
                    if (null != newFile && null != bitmap) {
                        UploadUtil uploadUtil = new UploadUtil();
                        uploadUtil.setUploadListener(this);
                        dialogUtil.showLoadingDialog(ChooseMaterialActivity.this, "");
                        new Thread(new Runnable() {  //开启线程上传文件
                            @Override
                            public void run() {
                                uploadUtil.uploadFile(newFile, Contants.UPLOAD_URL,"");
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
                    selectFromAlbum();
                }
                break;
        }
    }

    @OnClick(R.id.btn_reset)
    public void reSet() {
        checkMap = new HashMap<>();
        childCheckMap = new HashMap<>();
        materialAdapter.checkMap = checkMap;
        materialAdapter.notifyDataSetChanged();
        propertyinfo.clear();
    }

    @OnClick(R.id.btn_submit)
    public void subMit() {
        String propertyvalue = "";
        String propertynames = "";
        if(!TextUtils.isEmpty(foottypeid)){
            for (String string : propertyinfo.values()) {
                propertyvalue = propertyvalue + string + ",";
            }
            for (String string : propertyname.values()) {
                propertynames = propertynames + string + " ";
            }
            if (isNeedBack) {
                if (toCart) {
                    addProductToCart(propertyvalue, propertynames, fileName);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("propertyInfo", propertyvalue);
                    intent.putExtra("propertyName", propertynames);
                    intent.putExtra("imageName", fileName);
                    intent.putExtra("foottypeid", foottypeid);
                    setResult(RESULT_OK, intent);
                }
            } else {
                Intent intent = new Intent(BaseApplication.getInstance(), MakeOrdersActivity.class);
                intent.putExtra("propertyInfo", propertyvalue);
                intent.putExtra("propertyName", propertynames);
                intent.putExtra("product", productDetailBean);
                intent.putExtra("actionid", "0");
                intent.putExtra("buyNow", true);
                if("2".equals(productDetailBean.getCustomFlag())){
                    intent.putExtra("isCustomized", true);
                }else{
                    intent.putExtra("isCustomized", false);
                }
                intent.putExtra("foottypeid", foottypeid);

                if (null != fileName && !"".equals(fileName)) {
                    intent.putExtra("imageName", fileName + ".png");
                } else {
                    intent.putExtra("imageName", "");
                }
                startActivity(intent, true);
            }
        } else{
            ToastUtils.show(BaseApplication.getInstance(),"请选择足型数据");
        }
    }


    private void addProductToCart(String propertyvalue, String propertynames, String fileName) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "44");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("actionid", "1");
            JSONArray jsonArray = new JSONArray();
            JSONObject productObject = new JSONObject();
            productObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            productObject.put("productid", productDetailBean.getProductID() + "");
            productObject.put("name", productDetailBean.getName());
            productObject.put("suppliername", productDetailBean.getSupplierName());
            productObject.put("colorpropertiesname", "");
            productObject.put("sizepropertiesname", "");
            productObject.put("deliveryfee", productDetailBean.getDeliveryFee());
            productObject.put("deliveryname", productDetailBean.getDeliveryFeeName());
            productObject.put("supplierid", productDetailBean.getSupplierID() + "");
            productObject.put("price", productDetailBean.getPrice() + "");
            productObject.put("number", "1");
            productObject.put("colorid", "");
            productObject.put("sizeid", "");
            productObject.put("mainimage", productDetailBean.getMainImage());
            productObject.put("typeid", productDetailBean.getFlag() + "");
            productObject.put("groupBuyID", 0);
            productObject.put("skuID", "");
            if (null != foottypeid && !"".equals(foottypeid)) {
                productObject.put("userfoottypedataid", foottypeid);
            } else {
                productObject.put("userfoottypedataid", "0");
            }
            productObject.put("custompropertiesinfo", propertyvalue);
            productObject.put("custompropertiesinfoname", propertynames);
            productObject.put("orderproductattachimage", fileName);
            jsonArray.put(productObject);
            jsonObject.put("productlist", jsonArray);
            params.put("sendmsg", jsonObject.toString());
            addInCart(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void addInCart(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<AddCartRespMsg>(this) {
            @Override
            public void onSuccess(Response response, AddCartRespMsg addCartRespMsg) {
                if (addCartRespMsg.getResult() > 0) {
                    if ("1".equals(addCartRespMsg.getList().get(0).getIssuccess())) {
//                        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
//                        intent.putExtra("from", 1);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent, true);
                        ToastUtils.show(ChooseMaterialActivity.this, "成功加入购物车");
                        finish();
                    } else {
                        ToastUtils.show(ChooseMaterialActivity.this, "添加失败");
                    }
                } else {
                    ToastUtils.show(ChooseMaterialActivity.this, addCartRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ChooseMaterialActivity.this, "添加失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ChooseMaterialActivity.this, "添加失败，请稍后重试");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
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
            ToastUtils.show(ChooseMaterialActivity.this, "上传成功");
        } else {
            ToastUtils.show(ChooseMaterialActivity.this, "上传失败，请稍后重试");

        }
    }
}
