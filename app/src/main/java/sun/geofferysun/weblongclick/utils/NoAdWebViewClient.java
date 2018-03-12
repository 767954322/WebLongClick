package sun.geofferysun.weblongclick.utils;

/**
 * Created by gumenghao on 18/3/12.
 */

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NoAdWebViewClient extends WebViewClient {

    private String homeurl;
    private Context context;

    public NoAdWebViewClient(Context context, String homeurl) {
        this.context = context;
        this.homeurl = homeurl;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        url = url.toLowerCase();
        if (!url.contains(homeurl)) {
            if (!ADFilterTool.hasAd(context, url)) {
                return super.shouldInterceptRequest(view, url);
            } else {
                return new WebResourceResponse(null, null, null);
            }
        } else {
            return super.shouldInterceptRequest(view, url);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
        //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
        //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
        //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

        return true;
    }

}
