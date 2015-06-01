package Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Bean.MeiZiBean;
import exam.luowuxia.me.android_xamine.BitmapActivity;
import exam.luowuxia.me.android_xamine.MyRecyclerViewAdapter_MeiZi;
import exam.luowuxia.me.android_xamine.R;

public class MM_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private int count = 0;
    private int mlastVisibleItem;
    private RecyclerView.LayoutManager layoutManager;
    public int current_page = 1;
    public String url = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=";
    public final List<MeiZiBean> list_meizis = new ArrayList<>();
    private String httpresult;
    private View mView;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter_MeiZi mMyRecyclerViewAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mMyRecyclerViewAdapter = new MyRecyclerViewAdapter_MeiZi(list_meizis, current_page);
                    mRecyclerView.setAdapter(mMyRecyclerViewAdapter);
                    mSwipeRefreshWidget.setRefreshing(false);
                    if (current_page == 1) {
                        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView,
                                                             int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == RecyclerView.SCROLL_STATE_IDLE
                                        && mlastVisibleItem + 1 == mMyRecyclerViewAdapter.getItemCount()) {
                                    mSwipeRefreshWidget.setRefreshing(true);
                                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                                    Log.d("=============id", mlastVisibleItem + "mlastVisibleItem");
                                    current_page += 1;
                                    new MeiZi_NetThread().start();
                                }
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                mlastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

                            }
                        });
                    }

                    mMyRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter_MeiZi.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, MeiZiBean data) {
                            Intent intent = new Intent(getActivity(), BitmapActivity.class);
                            intent.putExtra("url", data.bitmap_url);
                            startActivity(intent);
                        }
                    });
                    Log.d("===================>>>>>>>>", list_meizis.size() + "");
                    break;
                case 2:
                    Toast.makeText(getActivity(),
                            "妹子网络请求出错,请检查网络设置", Toast.LENGTH_LONG).show();
                    mSwipeRefreshWidget.setRefreshing(false);
                    break;
            }

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MeiZi_NetThread().start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mm_, container, false);
        initView();
        return mView;
    }

    private void initView() {

        mSwipeRefreshWidget = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mRecyclerView = (RecyclerView) mView.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mlastVisibleItem + 1 == mMyRecyclerViewAdapter.getItemCount()) {
                    mSwipeRefreshWidget.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    Log.d("=============id", mlastVisibleItem + "mlastVisibleItem");
                    current_page += 1;
                    new MeiZi_NetThread().start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mlastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            }
        });


    }

    @Override
    public void onRefresh() {
        if (current_page == 1) {
            Toast.makeText(getActivity(),
                    "正在刷新", Toast.LENGTH_SHORT).show();
            new MeiZi_NetThread().start();
            //滚动到列首部--->这是一个很方便的api，可以滑动到指定位置
            mRecyclerView.scrollToPosition(0);
        } else {

            current_page = current_page - 1;
            new MeiZi_NetThread().start();
        }

    }

    private void Parse_httpresult(String string) {

        JSONTokener jsonParser = new JSONTokener(string);

        try {


            JSONObject content = (JSONObject) jsonParser.nextValue();
            current_page = content.getInt("current_page");
            JSONArray jsonObjs = content.getJSONArray("comments");

            List<MeiZiBean> count_lists = new ArrayList<>();

            for (int i = 0; i < 11; i++) {
                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                String title = jsonObj.getString("comment_author");
                String time = jsonObj.getString("comment_date_gmt");
                String dianzhan = jsonObj.getString("vote_positive");
                String chaping = jsonObj.getString("vote_negative");
                JSONArray bitmap_urls = jsonObj.getJSONArray("pics");
                String bitmap_url = bitmap_urls.getString(0);
                String tucao = jsonObj.getString("comment_approved");
                Log.d("======================>>>>>>>>>>",
                        "\n" + title + "\n" + time + "\n" + bitmap_url + "\n" + dianzhan + "\n" + chaping + "\n" + tucao + "\n");

                MeiZiBean meiZiBean = new MeiZiBean();
                meiZiBean.setBitmap_url(bitmap_url);
                meiZiBean.setChaping(chaping);
                meiZiBean.setDianzhan(dianzhan);
                meiZiBean.setTucao(tucao);
                meiZiBean.setTime(time);
                meiZiBean.setTitle(title);
                meiZiBean.setBitmap_meizi(GetBitmapForUrl(bitmap_url));
                // list_meizis.add(meiZiBean);
                count_lists.add(meiZiBean);

            }
            list_meizis.clear();
            list_meizis.addAll(count_lists);
            count_lists.clear();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap GetBitmapForUrl(String url_string) {
        URL url = null;
        try {
            url = new URL(url_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.connect();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            byte[] dataImage = bos.toByteArray();
            bos.close();
            in.close();
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    class MeiZi_NetThread extends Thread {
        @Override
        public void run() {
            /*//网络请求的初始化
            HttpClient mHttpClient = new DefaultHttpClient();
            HttpGet mHttp = new HttpGet(url + current_page);
            HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 10000);
            HttpConnectionParams.setSoTimeout(mHttpClient.getParams(), 10000);

            try {
                HttpResponse response = mHttpClient.execute(mHttp); // 执行请求，获取响应结果
                Log.e("响应未通过", "响应未通过" + response.getStatusLine().getStatusCode() + "\n" + url + current_page);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
                    httpresult = EntityUtils.toString(response.getEntity(),
                            "UTF-8");
                    Parse_httpresult(httpresult);

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                } else {
                    Log.e("响应未通过", "响应未通过");
                    // 响应未通过
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
            HttpURLConnection connection = null;
            try {
                URL url_con = new URL(url + current_page);
                connection = (HttpURLConnection) url_con.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                httpresult = response.toString();
                Parse_httpresult(httpresult);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                // 响应未通过
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // 响应未通过
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            } catch (ProtocolException e) {
                e.printStackTrace();
                // 响应未通过
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                // 响应未通过
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }


        }


    }
}
