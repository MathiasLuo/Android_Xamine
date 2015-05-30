package Fragment;


import android.content.Intent;
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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Bean.JokeBean;
import exam.luowuxia.me.android_xamine.BitmapActivity;
import exam.luowuxia.me.android_xamine.Joke_Activity;
import exam.luowuxia.me.android_xamine.MyRecyclerViewAdapter_Joke;
import exam.luowuxia.me.android_xamine.R;

public class Joke_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String httpresult;
    private View mView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private int mlastVisibleItem;
    public int current_page = 1;
    public List<JokeBean> list_jokes = new ArrayList<>();
    private MyRecyclerViewAdapter_Joke myRecyclerViewAdapter_joke;
    public String url = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments&page=";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    myRecyclerViewAdapter_joke = new MyRecyclerViewAdapter_Joke(list_jokes, current_page);
                    mRecyclerView.setAdapter(myRecyclerViewAdapter_joke);
                    mSwipeRefreshWidget.setRefreshing(false);
                    Log.e("===================>>>>>>>>", list_jokes.size() + "");
                    myRecyclerViewAdapter_joke.setOnItemClickListener(new MyRecyclerViewAdapter_Joke.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, JokeBean data) {
                            Intent intent = new Intent(getActivity(), Joke_Activity.class);
                            intent.putExtra("title", data.title);
                            intent.putExtra("time", data.time);
                            intent.putExtra("content", data.text_content);
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    Toast.makeText(getActivity(),
                            "网络请求出错,请检查网络设置", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Joke_NetThread().start();
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
                        && mlastVisibleItem + 1 == myRecyclerViewAdapter_joke.getItemCount()) {
                    mSwipeRefreshWidget.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    Log.e("=============id", mlastVisibleItem + "mlastVisibleItem");
                    current_page += 1;
                    new Joke_NetThread().start();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_mm_, container, false);
        initView();
        return mView;
    }

    @Override
    public void onRefresh() {
        if (current_page == 1) {
            Toast.makeText(getActivity(),
                    "当前是第一页,返回不了。", Toast.LENGTH_LONG).show();
            mSwipeRefreshWidget.setRefreshing(false);
            //滚动到列首部--->这是一个很方便的api，可以滑动到指定位置
            mRecyclerView.scrollToPosition(0);
        } else {

            current_page = current_page - 1;
            new Joke_NetThread().start();
        }
    }


    class Joke_NetThread extends Thread {
        @Override
        public void run() {
            super.run();
            //网络请求的初始化
            HttpClient mHttpClient = new DefaultHttpClient();
            HttpGet mHttp = new HttpGet(url + current_page);
            HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(), 10000);
            HttpConnectionParams.setSoTimeout(mHttpClient.getParams(), 10000);

            try {
                HttpResponse response = mHttpClient.execute(mHttp); // 执行请求，获取响应结果
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
                    httpresult = EntityUtils.toString(response.getEntity(),
                            "UTF-8");
                    Parse_httpresult(httpresult);

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
            }
        }
    }


    private void Parse_httpresult(String string) {

        JSONTokener jsonParser = new JSONTokener(string);

        try {
            JSONObject content = (JSONObject) jsonParser.nextValue();
            current_page = content.getInt("current_page");
            JSONArray jsonObjs = content.getJSONArray("comments");

            list_jokes.clear();

            for (int i = 0; i < jsonObjs.length(); i++) {
                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                String title = jsonObj.getString("comment_author");
                String time = jsonObj.getString("comment_date_gmt");
                String dianzhan = jsonObj.getString("vote_positive");
                String chaping = jsonObj.getString("vote_negative");
                String tucao = jsonObj.getString("comment_approved");
                String content_c = jsonObj.getString("comment_content");
                String text_content = jsonObj.getString("text_content");
                Log.e("======================>>>>>>>>>>",
                        "\n" + title + "\n" + time + "\n" + text_content + "\n" + dianzhan + "\n" + chaping + "\n" + tucao + "\n" + content_c + "\n");

                JokeBean mJokeBean = new JokeBean();
                mJokeBean.setChaping(chaping);
                mJokeBean.setDianzhan(dianzhan);
                mJokeBean.setTucao(tucao);
                mJokeBean.setTime(time);
                mJokeBean.setTitle(title);
                mJokeBean.setContent(content_c);
                mJokeBean.setText_content(text_content);


                list_jokes.add(mJokeBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

