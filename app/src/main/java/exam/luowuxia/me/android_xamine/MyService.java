package exam.luowuxia.me.android_xamine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyService extends Service {
    private Context context;
    private Notification notification;
    private NotificationManager nManager;
    private PendingIntent pendingIntent;
    private String appURL;
    private String filepath;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appURL = intent.getStringExtra("url");
        Log.e("================>>>>>>>>>", appURL);
        CreateInform();

        return super.onStartCommand(intent, flags, startId);
    }


    //创建通知
    public void CreateInform() {
        //定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
        Intent intent = new Intent();
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        //创建一个通知
        notification = new Notification(R.drawable.abc_ic_go_search_api_mtrl_alpha, "开始下载~~", System.currentTimeMillis());
        notification.setLatestEventInfo(context, "正在下载掌上重邮~", "点击查看详细内容", pendingIntent);
        nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(100, notification);
        new Thread(new updateRunnable()).start();
    }


    class updateRunnable implements Runnable {
        int downnum = 0;//已下载的大小
        int downcount = 0;//下载百分比

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Log.e("================>>>>>>>>>", appURL);
                DownLoadApp(appURL);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("================>>>>>>>>>Exception", appURL);
            }
        }

        public void DownLoadApp(String urlString) throws Exception {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int length = urlConnection.getContentLength();
            InputStream inputStream = urlConnection.getInputStream();
            OutputStream outputStream = new FileOutputStream(getFile());
            byte buffer[] = new byte[1024 * 3];
            int readsize = 0;
            while ((readsize = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readsize);
                downnum += readsize;
                if ((downcount == 0) || (int) (downnum * 100 / length) - 1 > downcount) {
                    downcount += 1;
                    notification.setLatestEventInfo(context, "正在下载掌上重邮~", "已下载了" + (int) downnum * 100 / length + "%", pendingIntent);
                    nManager.notify(100, notification);
                }
                if (downnum == length) {
                    notification.setLatestEventInfo(context, "已下载完成掌上重邮~", "点击安装", pendingIntent);
                    nManager.notify(100, notification);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(new File(filepath)),
                            "application/vnd.android.package-archive");
                    context.startActivity(intent);
                    nManager.cancelAll();
                }
            }
            inputStream.close();
            outputStream.close();
        }
    }

    //获取文件的保存路径
    public File getFile() throws Exception {
        String SavePath = getSDCardPath() + "/App";
        File path = new File(SavePath);
        File file = new File(SavePath + "/CQUPT.apk");
        filepath = getSDCardPath() + "/App" + "/CQUPT.apk";
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    //获取SDCard的目录路径功能
    private String getSDCardPath() {
        File sdcardDir = null;
        // 判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

}
