package exam.luowuxia.me.android_xamine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import exam.luowuxia.me.android_xamine.R;


public class Joke_Activity extends ActionBarActivity {
    TextView tv_title, tv_time, tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_);
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String content = intent.getStringExtra("content");
        tv_time = (TextView) findViewById(R.id.item_time_joke_activty);
        tv_time.setText(time);
        tv_title = (TextView) findViewById(R.id.item_title_joke_activty);
        tv_title.setText(title);
        tv_content = (TextView) findViewById(R.id.item_text_joke_activty);
        tv_content.setText("    "+content);
    }


}
