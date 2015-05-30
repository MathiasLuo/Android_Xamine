package exam.luowuxia.me.android_xamine;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Fragment.Joke_Fragment;
import Fragment.MM_Fragment;
import Fragment.MyShow_Fragment;


public class MainActivity extends ActionBarActivity {
    public final static String APP_NAME = "DuDu";
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ImageView imageView;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TextView textView1, textView2, textView3;
    private List<Fragment> fragments_list;
    private Joke_Fragment joke_fragment;
    private MM_Fragment mm_fragment;
    private MyShow_Fragment myShow_fragment;
    private ViewPagerAdapter mViewPagerAdapter;
    private String httpresult;
    private String apkURL;
    public final static String UL = "http://hongyan.cqupt.edu.cn/app/cyxbsAppUpdate.xml";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent updateIntent =new Intent(MainActivity.this, MyService.class);
                    updateIntent.putExtra("url",apkURL);
                    startService(updateIntent);
                    break;
                case 2:
                    Toast.makeText(MainActivity.this,
                            "网络请求出错,请检查网络设置", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        InitImageView();
        setSupportActionBar(mToolbar);
        initViewPager();
        //初始化标题栏
        SetTeView();
        textView1.setTextColor(Color.parseColor("#FF1616FF"));
        textView1.setTextSize(18);


        Toast.makeText(MainActivity.this, "正在下载图片,请稍等哟！", Toast.LENGTH_LONG).show();

    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        imageView = (ImageView) findViewById(R.id.cursor);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView1.setOnClickListener(new MyOnClickListener(0));
        textView2.setOnClickListener(new MyOnClickListener(1));
        textView3.setOnClickListener(new MyOnClickListener(2));
    }

    private void initViewPager() {
        //设置 fragment  list
        fragments_list = new ArrayList<>();
        joke_fragment = new Joke_Fragment();
        mm_fragment = new MM_Fragment();
        myShow_fragment = new MyShow_Fragment();
        fragments_list.add(mm_fragment);
        fragments_list.add(joke_fragment);
        fragments_list.add(myShow_fragment);
        //绑定adapter
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments_list);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        //绑定监听器
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    private void InitImageView() {
        // 获取图片宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.pic_butt).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 获取分辨率宽度
        int screenW = dm.widthPixels;
        // 计算偏移量
        offset = (screenW / 3 - bmpW) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        // 设置动画初始位置
        imageView.setImageMatrix(matrix);
    }

    private void SetTeView() {
        textView1.setTextColor(Color.parseColor("#ff5facff"));
        textView2.setTextColor(Color.parseColor("#ff5facff"));
        textView3.setTextColor(Color.parseColor("#ff5facff"));
        textView1.setTextSize(16);
        textView2.setTextSize(16);
        textView3.setTextSize(16);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            new Xml_NetThread().start();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        public void onPageScrollStateChanged(int arg0) {


        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        public void onPageSelected(int arg0) {


            Animation animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);//显然这个比较简洁，只有一行代码。
            Log.e("================>>>>>", one + "\n" + two);
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            imageView.startAnimation(animation);
            Toast.makeText(MainActivity.this, "您选择了" + mViewPager.getCurrentItem() + "页卡", Toast.LENGTH_SHORT).show();

            SetTeView();

            switch (arg0) {
                case 0:
                    textView1.setTextColor(Color.parseColor("#FF1616FF"));
                    textView1.setTextSize(18);
                    break;
                case 1:
                    textView2.setTextColor(Color.parseColor("#FF1616FF"));
                    textView2.setTextSize(18);
                    break;

                case 2:
                    textView3.setTextSize(18);
                    textView3.setTextColor(Color.parseColor("#FF1616FF"));
                    break;
            }


        }

    }

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }

    }


    class Xml_NetThread extends Thread {
        @Override
        public void run() {
            super.run();
            //网络请求的初始化
            HttpClient mHttpClient = new DefaultHttpClient();
            HttpGet mHttp = new HttpGet(UL);
            HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 10000);
            HttpConnectionParams.setSoTimeout(mHttpClient.getParams(), 10000);

            try {
                HttpResponse response = mHttpClient.execute(mHttp); // 执行请求，获取响应结果
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
                    httpresult = EntityUtils.toString(response.getEntity(),
                            "UTF-8");
                    //解析xml
                    XmlPull(StringTOInputStream(httpresult));
                    //回调ui线程
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                } else {
                    // 响应未通过
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void XmlPull(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(inputStream, "UTF-8");

            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) // 文档结束
            {
                // 节点名称
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT: // 文档开始

                        break;
                    case XmlPullParser.START_TAG: // 标签开始
                        if ("versionCode".equals(nodeName)) {
                            Log.e("===========>>>>>>>>>>>>", parser.nextText());
                        }
                        if ("versionName".equals(nodeName)) {
                            Log.e("===========>>>>>>>>>>>>", parser.nextText());
                        }
                        if ("updateContent".equals(nodeName)) {
                            Log.e("===========>>>>>>>>>>>>", parser.nextText());
                        }
                        if ("apkURL".equals(nodeName)) {
                           // Log.e("===========>>>>>>>>>>>>", parser.nextText());
                            apkURL = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // 标签结束
                        if ("person".equals(nodeName)) {

                        }
                        break;
                }
                event = parser.next(); // 下一个标签
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InputStream StringTOInputStream(String in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
        return is;
    }

}
