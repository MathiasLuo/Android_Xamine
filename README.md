# Android_Xamine 

###不知道如何展示sql  就贴贴代码吧。
###为了方便手机上的测试，我就每次都添加一下数据

##第一个SQL的辅助类，里面创建了数据表。

    import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Administrator on 2015/5/30 0030.
 */
public class SQLHelper extends SQLiteOpenHelper {

    //数据库版本
    public static final int VERSION = 1;
    //数据库名称
    public static final String MYDATANAME = "SQLdata";
    //创建Student表
    String sql1 = "create table  IF NOT  EXISTS Student  (_id integer,SNO varchar(20),Name varchar(10),Age integer,College varchar(30))";
    //创建Course表
    String sql2 = "create table  IF NOT  EXISTS  Course  (_id integer,CourseID varchar(15),CourseName varchar(30),CourseBeforeID varchar(15))";
    //创建Choose表
    String sql3 = "create table  IF NOT  EXISTS  Choose  (_id integer,SNO varchar(20),CourseID varchar(30),Score decimal(2,5))";

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



    ##然后就是操作的类了,由于写在了Fragment里面,所以我就只贴重要代码了。（对sql语句并不是很熟悉，所以就用android中的）。

