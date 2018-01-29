package com.zhurui.bunnymall.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


public class BitMapUtil {

	public static String getPhoto(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 50, out);
		int options = 100;
		while ( out.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			out.reset();//重置out即清空out
			options -= 10;//每次都减少10
			bitmap.compress(CompressFormat.JPEG, options, out);//这里压缩options%，把压缩后的数据存放到baos中
		}
		byte[] results = out.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(results);
	}

	public static Intent getCropImageIntent(Bitmap data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		intent.putExtra("data", data);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 70);
		intent.putExtra("outputY", 70);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		return intent;
	}

	public static void decodeImage(String userPhoto, ImageView view){
		BASE64Decoder decode = new BASE64Decoder();
		byte[] b;
		try {
			b = decode.decodeBuffer(userPhoto);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;// 设置缩放大小
			Bitmap bitmap = getPicFromBytes(b,options);
			view.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static Bitmap decodeImage1(String userPhoto){
		BASE64Decoder decode = new BASE64Decoder();
		Bitmap bitmap = null;
		byte[] b;
		try {
			b = decode.decodeBuffer(userPhoto);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;// 设置缩放大小
			bitmap = getPicFromBytes(b,options);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}
	/**
	 * @param
	 * @param bytes
	 * @param opts
	 * @return Bitmap
	 */
	public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}



}
