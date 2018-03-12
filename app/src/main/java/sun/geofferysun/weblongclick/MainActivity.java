package sun.geofferysun.weblongclick;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import sun.geofferysun.weblongclick.utils.NoAdWebViewClient;
import sun.geofferysun.weblongclick.utils.SizeUtil;

public class MainActivity extends AppCompatActivity {
    private String mUrl = "http://zhan.renren.com/hdwallpapers?from=template&checked=true";
    private Context mContext;
    private WebView mWebView;
    // 长按查看图片
    private ItemLongClickedPopWindow itemLongClickedPopWindow;
    // 手指触发屏幕的坐标
    private int downX, downY;
    // 需要保存图片的路径
    private String saveImgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        // 设置允许访问文件数据
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationEnabled(true);
        mWebView.setWebViewClient(new NoAdWebViewClient(MainActivity.this,mUrl));

//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);
//        // 是否支持Javascript，默认值false
//         webSettings.setJavaScriptEnabled(true);
//        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setAllowFileAccess(true);
//        // 设置允许访问文件数据
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setGeolocationEnabled(true);

        mWebView.setOnTouchListener(listener);

//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
//                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
//                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
//
//                if (url.toString().contains("sina.cn")){
////                    view.loadUrl("http://ask.csdn.net/questions/178242");
//                    return true;
//                }
//
//                return true;
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
//            {
//                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
//                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (request.getUrl().toString().contains("sina.cn")){
////                        view.loadUrl("http://ask.csdn.net/questions/178242");
//                        return true;
//                    }
//                }
//
//                return false;
//            }
//        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView)v).getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                if (type == WebView.HitTestResult.UNKNOWN_TYPE)
                    return false;
                if (type == WebView.HitTestResult.EDIT_TEXT_TYPE) {

                }

                itemLongClickedPopWindow = new ItemLongClickedPopWindow(MainActivity.this,
                        ItemLongClickedPopWindow.IMAGE_VIEW_POPUPWINDOW,
                        SizeUtil.dp2px(mContext, 120), SizeUtil.dp2px(mContext, 90));

                switch (type) {
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // TODO
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                        break;
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        // 获取图片的路径
                        saveImgUrl = result.getExtra();
                        //通过GestureDetector获取按下的位置，来定位PopWindow显示的位置
                        itemLongClickedPopWindow.showAtLocation(v, Gravity.TOP|Gravity.LEFT, downX, downY + 10);
                        break;
                    default:
                        break;
                }

                itemLongClickedPopWindow.getView(R.id.item_longclicked_viewImage)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemLongClickedPopWindow.dismiss();
                                Intent intent = new Intent(mContext, ShowImgActivity.class);
                                intent.putExtra("info", saveImgUrl);
                                startActivity(intent);
                            }
                        });
                itemLongClickedPopWindow.getView(R.id.item_longclicked_saveImage)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemLongClickedPopWindow.dismiss();
                                new SaveImage().execute(); // Android 4.0以后要使用线程来访问网络
                            }
                        });
                return true;
            }
        });

        // 加载页面
        mWebView.loadUrl(mUrl);
    }

    View.OnTouchListener listener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            downX = (int) arg1.getX();
            downY = (int) arg1.getY();
            return false;
        }
    };

    /***
     * 功能：用线程保存图片
     *
     * @author wangyp
     */
    private class SaveImage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();
                File file = new File(sdcard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }
                int idx = saveImgUrl.lastIndexOf(".");
                String ext = saveImgUrl.substring(idx);
                file = new File(sdcard + "/Download/" + new Date().getTime() + ext);
                InputStream inputStream = null;
                URL url = new URL(saveImgUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                if (conn.getResponseCode() == 200) {
                    inputStream = conn.getInputStream();
                }
                byte[] buffer = new byte[4096];
                int len = 0;
                FileOutputStream outStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                result = "图片已保存至：" + file.getAbsolutePath();

                Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                final Uri localUri = Uri.fromFile(file);
                localIntent.setData(localUri);
                sendBroadcast(localIntent);
            } catch (Exception e) {
                result = "保存失败！" + e.getLocalizedMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            MainActivity.this.finish();
        }
        return true;
    }
}
