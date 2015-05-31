# Android_Xamine 

###不知道如何展示sql  就贴贴代码吧。
###为了方便手机上的测试，我就每次都添加一下数据

###第一个SQL的辅助类，里面创建了数据表。

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

     然后就是操作的类了,由于写在了Fragment里面,所以我就只贴重要代码了。（对sql语句并不是很熟悉，所以就用android中的）。
     首先在`onCreate()`中生产`mSqLiteDatabase`,然后向表中加入数据。

     mSqlHelper = new SQLHelper(getActivity(), SQLHelper.MYDATANAME, null, SQLHelper.VERSION);
            mSqLiteDatabase = mSqlHelper.getWritableDatabase();
            insert_students();
            insert_Course();
            insert_Choose();

     加入数据的方式也采用的android里面的`insert()`,向表中假数据的方式也都差不多,所以就贴一下。

         ContentValues value_student_1 = new ContentValues();
                value_student_1.put("SNO", "S00001");
                value_student_1.put("Name", "张三");
                value_student_1.put("Age", 20);
                value_student_1.put("College", "计算机学院");
                mSqLiteDatabase.insert("Student", null, value_student_1);

     接着就是数据的查询了：

           String stuname = mEditText.getText().toString();
           Cursor cursor_student = mSqLiteDatabase.query("Student", null, "Name=?", new String[]{stuname}, null, null, null);
           if (cursor_student.moveToNext()) {
                String SNO = cursor_student.getString(cursor_student.getColumnIndex("SNO"));
                String name = cursor_student.getString(cursor_student.getColumnIndex("Name"));
                int age = cursor_student.getInt(cursor_student.getColumnIndex("Age"));
                String College = cursor_student.getString(cursor_student.getColumnIndex("College"));
                mTextView.setText("name: " + name + "\n" + "SNO: " + SNO + "\nage: " + age + "\nCollege: " + College);

     删除数据,因为需要删除该学生的所有资料所以我采取镶嵌的删除：

            Cursor cursor_student = mSqLiteDatabase.query("Student", null, "Name=?", new String[]{string}, null, null, null);
                   if (cursor_student.moveToNext()) {
                       String SNO = cursor_student.getString(cursor_student.getColumnIndex("SNO"));
                       String name = cursor_student.getString(cursor_student.getColumnIndex("Name"));
                       int age = cursor_student.getInt(cursor_student.getColumnIndex("Age"));
                       String College = cursor_student.getString(cursor_student.getColumnIndex("College"));

                       Cursor cursor_Choose = mSqLiteDatabase.query("Choose", null, "SNO=?", new String[]{SNO}, null, null, null);
                       if (cursor_Choose.moveToNext()) {
                           String CourseID = cursor_Choose.getString(cursor_Choose.getColumnIndex("CourseID"));
                           int Score = cursor_Choose.getInt(cursor_Choose.getColumnIndex("Score"));

                           Cursor cursor_Course = mSqLiteDatabase.query("Course", null, "CourseID=?", new String[]{CourseID}, null, null, null);
                           if (cursor_Course.moveToNext()) {
                               String CourseName = cursor_Course.getString(cursor_Course.getColumnIndex("CourseName"));
                               String CourseBeforeID = cursor_Course.getString(cursor_Course.getColumnIndex("CourseBeforeID"));

                               DeteStuByName(name);
                               DeteCourseByCourseName(CourseName);
                               DeteChooseByName(CourseID);

                           }
                       }
                   }

     其中:
             public void DeteStuByName(String name) {
                    mSqLiteDatabase.delete("Student", "Name=?", new String[]{name});
                }

                public void DeteCourseByCourseName(String CourseName) {
                    mSqLiteDatabase.delete("Course", "CourseName=?", new String[]{CourseName});
                }

                public void DeteChooseByName(String Choosename) {
                    mSqLiteDatabase.delete("Choose", "CourseID=?", new String[]{Choosename});
                }

     对于把计算机学院的同学,年龄加2,我是先查询Student表中的,有计算机的,就把他的年龄加2：

             public void ex_jisuanji() {
                     Cursor cursor_student = mSqLiteDatabase.query("Student", null, "College=?", new String[]{"计算机学院"}, null, null, null);
                     while (cursor_student.moveToNext()) {
                         String name = cursor_student.getString(cursor_student.getColumnIndex("Name"));
                         int age = cursor_student.getInt(cursor_student.getColumnIndex("Age"));
                         re_jisuanji(name, age);
                     }

                 }

                 public void re_jisuanji(String name, int age) {
                     ContentValues values = new ContentValues();
                     values.put("Age", age + 2);
                     mSqLiteDatabase.update("Student", values, "Name=?", new String[]{name});
                 }
