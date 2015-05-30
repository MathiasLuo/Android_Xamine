package exam.luowuxia.me.android_xamine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BitmapActivity extends Activity {
    private ImageView imageView;
    private Bitmap bitmap;
    private String path;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            imageView.setImageBitmap(bitmap);

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_layout,
                            (ViewGroup) findViewById(R.id.dialog));

                    Button save_bitmap = (Button) layout.findViewById(R.id.save_bitmap);
                    Button share_bitmap = (Button) layout.findViewById(R.id.share_bitmap);

                    save_bitmap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SaveBitmap(bitmap);
                            Toast.makeText(BitmapActivity.this, "已经成功保存至/sdcard/MeiZi，不老实哟！", Toast.LENGTH_SHORT).show();
                        }
                    });

                    share_bitmap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SaveBitmap(bitmap);
                            SharePhoto(path, BitmapActivity.this);
                        }
                    });

                    new AlertDialog.Builder(BitmapActivity.this).setTitle("图片操作").setView(layout)
                            .show();
                    return false;
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
        imageView = (ImageView) findViewById(R.id.bitmap_imagview);

        new Thread() {
            @Override
            public void run() {
                super.run();
                bitmap = GetBitmapForUrl(url);
                Message message = new Message();
                handler.sendMessage(message);
            }
        }.start();
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


    //保存到本地
    public void SaveBitmap(Bitmap bmp) {
        //存储路径
        File file = new File("/sdcard/MeiZi");
        if (!file.exists())
            file.mkdirs();
        try {
            path = file.getPath() + "/meizi" + file.listFiles().length + ".jpg";
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SharePhoto(String photoUri, final Activity activity) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(photoUri);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, activity.getTitle()));
    }
}
