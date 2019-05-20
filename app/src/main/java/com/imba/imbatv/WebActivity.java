package com.imba.imbatv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;


import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;

import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.Toast;


import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;

import java.io.IOException;


public class WebActivity extends Activity {
    private WebView webView;
    private WebSettings settings;
    private Dialog dialog;

    private String url = "http://202.99.114.74:58316/imbaepg/template/template_version4_1/main_rmtj.html?bizCode=biz_522074538354&bizParentCode=biz_453447401015&imbaUserID=cutv201711272010101";
    private KSYTextureView videoView;
    private View parentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       parentView=View.inflate(this,R.layout.activity_web,null);
        setContentView(parentView);
        initView();
    }


    @SuppressLint("JavascriptInterface")
    private void initView() {


        int dZoom = 100;
        webView = findViewById(R.id.webview);
        settings = webView.getSettings();
        webView.setInitialScale(dZoom);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocus();
        webView.addJavascriptInterface(new JavaScriptInterface(), "imbatv");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);





    }

    class JavaScriptInterface {

        @JavascriptInterface
        public void play(final String url) {
            Log.e("url", url);
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      creatDialog(url);
                      webView.goBackOrForward(-2);
                  }
              });





        }
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 1:
                  Toast.makeText(WebActivity.this,"提醒",Toast.LENGTH_SHORT).show();
                   break;
               case 2:
                   screenDialog();
                   break;
               case 3:
                   windowsDialog();
                   break;
               case 4:
                  playPause();
                   break;
               case 5:
                   playResume();
                   break;

           }
        }
    };
    private void creatDialog(final String url) {
        dialog = new Dialog(WebActivity.this, R.style.dialog_orders);
        final View view = View.inflate(WebActivity.this, R.layout.activity_ks_player, null);
        videoView = view.findViewById(R.id.player);
        videoView.setBufferTimeMax(10.0f);
        videoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_NOSCALE_TO_FIT);

        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                videoView.start();
                Log.e("播放时长",videoView.getDuration()+"");

            }
        });
        try {
            videoView.setDataSource(url);
            videoView.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = 510;
        lp.height = 287;
        lp.x = 385;
        lp.y = 161;
        window.setAttributes(lp);
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what=2;
                handler.sendMessage(message);

            }
        },10000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what=1;
                handler.sendMessage(message);
            }
        },15000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Message message = handler.obtainMessage();
                message.what=3;
                handler.sendMessage(message);


            }
        },20000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what=4;
                handler.sendMessage(message);
            }
        },30000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what=5;
                handler.sendMessage(message);
            }
        },35000);
     videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
         @Override
         public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
             if (i==104){
                 if (videoView!=null){
                     videoView.reload(url,false);
                 }

             }
             return false;
         }
     });
   videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
       @Override
       public void onCompletion(IMediaPlayer iMediaPlayer) {
           if (videoView!=null){
               videoView.reset();
               try {
                   videoView.setBufferTimeMax(10.0f);
                   videoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_NOSCALE_TO_FIT);
                   videoView.setDataSource(url);
                   videoView.prepareAsync();
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       }
   });
    }

    private void playResume(){
        if (videoView!=null){
            videoView.start();
        }
    }
    private void  playPause(){
        if (videoView!=null){
            if (videoView.isPlaying()){
                videoView.pause();
            }

        }
    }

    private void playDestroy(){
        if (videoView!=null){
            videoView.stop();
            videoView.release();
            videoView=null;
        }
        if (dialog!=null){
            dialog.dismiss();

        }
    }
    private void nextVideo(String url){
        if (videoView!=null){
            videoView.reload(url,true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_FAST);
        }
    }
private void screenDialog(){
    Window window = dialog.getWindow();
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
    lp.gravity = Gravity.CENTER;
    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setAttributes(lp);

}

    @Override
    protected void onDestroy() {
    if (videoView!=null){
        videoView.stop();
        videoView.release();
        videoView=null;
    }
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();

        }
        super.onDestroy();

    }

    private void windowsDialog(){
    Window window = dialog.getWindow();
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.gravity =Gravity.LEFT | Gravity.TOP;
    lp.width = 510;//宽高可设置具体大小
    lp.height = 287;
    dialog.getWindow().setAttributes(lp);
}
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density; //当前屏幕密度因子
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    try {
        Log.e("keyCode: ", keyCode + "");
        if (keyCode == 67 || keyCode == 4 || keyCode == KeyEvent.KEYCODE_BACK) {//返回
         if (dialog.getWindow().getDecorView().getWidth()==1280){
             windowsDialog();
             return false;
         }else {
             dialog.dismiss();
         }


        }
        if (keyCode == 66 || keyCode == 23 || keyCode == KeyEvent.KEYCODE_ENTER) {//确定
            Log.e("dialog宽度1",dialog.getWindow().getDecorView().getWidth()+"");

        }
    }catch (Exception e){
        e.printStackTrace();
    }
        return super.onKeyDown(keyCode, event);
    }


}
