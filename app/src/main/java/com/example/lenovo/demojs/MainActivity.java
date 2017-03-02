package com.example.lenovo.demojs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    private Button mBtn1, mBtn2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONR = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews() {
        mWebView = (WebView) findViewById(R.id.webview);
        //启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        //从assets目录下面加载html
        mWebView.loadUrl("file:///android_asset/web.html");
        mWebView.addJavascriptInterface(MainActivity.this, "android");

        //Button按钮 无参调用html js方法
        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("javascript:javacalljs()");
            }
        });

        //Button按钮 有参调用html js方法
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("javascript:javacalljswith('要翔')");
            }
        });
    }

    @JavascriptInterface
    public void startFunction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "js调用了android方法", Toast.LENGTH_LONG).show();
            }
        });
    }

    @JavascriptInterface
    public void startFunction(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "js传来的参数是" + text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @JavascriptInterface
    public void call(View view) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONR);
        } else {
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri date = Uri.parse("tel:" + "10086");
        intent.setData(date);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==MY_PERMISSIONS_REQUEST_CALL_PHONR){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                callPhone();
            }else {
                Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @JavascriptInterface
    public void goSendMessage(final String phone){
        Log.v("law","号码是"+phone);
        Intent intent=new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phone));
        startActivity(intent);
    }


}
