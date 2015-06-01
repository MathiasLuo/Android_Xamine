package Bean;

/**
 * Created by Administrator on 2015/5/30 0030.
 */
public class JokeBean {
    public String title;
    public String time;
    public String dianzhan;
    public String chaping;
    public String tucao;
    public String content;

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public String text_content;

    public String getChaping() {
        return chaping;
    }

    public void setChaping(String chaping) {
        this.chaping = chaping;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
