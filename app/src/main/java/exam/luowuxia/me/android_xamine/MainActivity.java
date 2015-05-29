package exam.luowuxia.me.android_xamine;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        InitImageView();
        setSupportActionBar(mToolbar);
        initViewPager();

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

}
