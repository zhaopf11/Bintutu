package com.zhurui.bunnymall.home.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.utils.camera.CameraManager;
import com.zhurui.bunnymall.utils.decoding.CaptureActivityHandler;
import com.zhurui.bunnymall.utils.decoding.InactivityTimer;
import com.zhurui.bunnymall.viewutils.ViewfinderView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanActivity extends BaseActivity implements SurfaceHolder.Callback {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.preview_view)
    SurfaceView preview_view;
    @Bind(R.id.viewfinder_view)
    ViewfinderView viewfinder_view;

    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        initView();
        CameraManager.init(getApplication());
        initData();

    }

    private void initView() {
        text_title.setText(R.string.scan);
        text_right.setText("");
        Drawable drawable = getResources().getDrawable(R.drawable.help);
         // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        text_right.setCompoundDrawables(drawable, null, null, null);
    }

    private void initData() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = preview_view.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(ScanActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            ScanActivity.this.finish();
        } else {
//            Intent resultIntent = new Intent();
//            resultIntent.setClass(Sao1SaoActivity.this, Sao1SaoResultActivity.class);
//            resultIntent.putExtra("result", resultString);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            barcode.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] bitmapByte = baos.toByteArray();
//            resultIntent.putExtra("bitmap", bitmapByte);
//            startActivity(resultIntent);
//            overridePendingTransition(R.anim.activity_in_from_rigth, R.anim.activity_out_to_scale);
//            ScanActivity.this.finish();
            Toast.makeText(ScanActivity.this, resultString, Toast.LENGTH_SHORT).show();

        }

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }


    public ViewfinderView getViewfinderView() {
        return viewfinder_view;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinder_view.drawViewfinder();

    }

    /**
     * 初始化声音资源
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);//设置音量
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    /**
     * 响铃和震动
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }


    @OnClick(R.id.text_right)
    public void toScanHelp() {
        startActivity(new Intent(BaseApplication.getInstance(), ScanHelpActivity.class), false);
    }

}
