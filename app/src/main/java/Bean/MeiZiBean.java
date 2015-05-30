package Bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2015/5/30 0030.
 */
public class MeiZiBean {

    public String title;
    public String time;
    public String dianzhan;
    public String chaping;
    public String tucao;
    public Bitmap bitmap_meizi;

    public Bitmap getBitmap_meizi() {
        return bitmap_meizi;
    }

    public String getBitmap_url() {
        return bitmap_url;
    }

    public void setBitmap_url(String bitmap_url) {
        this.bitmap_url = bitmap_url;
    }

    public String bitmap_url;

    public Bitmap getBitmap_meizi(Bitmap bitmap) {
        return bitmap_meizi;
    }

    public void setBitmap_meizi(Bitmap bitmap_meizi) {
        this.bitmap_meizi = bitmap_meizi;
    }

    public String getChaping() {
        return chaping;
    }

    public void setChaping(String chaping) {
        this.chaping = chaping;
    }

    public String getDianzhan() {
        return dianzhan;
    }

    public void setDianzhan(String dianzhan) {
        this.dianzhan = dianzhan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTucao() {
        return tucao;
    }

    public void setTucao(String tucao) {
        this.tucao = tucao;
    }


}
