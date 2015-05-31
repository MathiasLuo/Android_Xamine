package Fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import SQL.SQLHelper;
import exam.luowuxia.me.android_xamine.R;


public class MyShow_Fragment extends android.support.v4.app.Fragment {
    private View mView;
    private SQLHelper mSqlHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private EditText mEditText;
    private Button mButton_req, mButton_dete;
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSqlHelper = new SQLHelper(getActivity(), SQLHelper.MYDATANAME, null, SQLHelper.VERSION);
        mSqLiteDatabase = mSqlHelper.getWritableDatabase();
        insert_students();
        insert_Course();
        insert_Choose();
        ex_jisuanji();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_show_, container, false);
        mEditText = (EditText) mView.findViewById(R.id.edit_my);
        mButton_req = (Button) mView.findViewById(R.id.button_my);
        mButton_dete = (Button) mView.findViewById(R.id.button_mymy);
        mTextView = (TextView) mView.findViewById(R.id.tv_my);

        mButton_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stuname = mEditText.getText().toString();

                Cursor cursor_student = mSqLiteDatabase.query("Student", null, "Name=?", new String[]{stuname}, null, null, null);
                if (cursor_student.moveToNext()) {
                    String SNO = cursor_student.getString(cursor_student.getColumnIndex("SNO"));
                    String name = cursor_student.getString(cursor_student.getColumnIndex("Name"));
                    int age = cursor_student.getInt(cursor_student.getColumnIndex("Age"));
                    String College = cursor_student.getString(cursor_student.getColumnIndex("College"));
                    mTextView.setText("name: " + name + "\n" + "SNO: " + SNO + "\nage: " + age + "\nCollege: " + College);

                    Cursor cursor_Choose = mSqLiteDatabase.query("Choose", null, "SNO=?", new String[]{SNO}, null, null, null);
                    if (cursor_Choose.moveToNext()) {
                        String CourseID = cursor_Choose.getString(cursor_Choose.getColumnIndex("CourseID"));
                        int Score = cursor_Choose.getInt(cursor_Choose.getColumnIndex("Score"));
                        mTextView.setText(mTextView.getText() + "\n\nCourseID: " + CourseID + "\nScore: " + Score);

                        Cursor cursor_Course = mSqLiteDatabase.query("Course", null, "CourseID=?", new String[]{CourseID}, null, null, null);
                        if (cursor_Course.moveToNext()) {

                            String CourseName = cursor_Course.getString(cursor_Course.getColumnIndex("CourseName"));
                            String CourseBeforeID = cursor_Course.getString(cursor_Course.getColumnIndex("CourseBeforeID"));
                            mTextView.setText(mTextView.getText() + "\n\nCourseName:" + CourseName + "\nCourseBeforeID:" + CourseBeforeID);
                        }
                    }
                } else {
                    mTextView.setText("没有该名学生");
                }
            }
        });

        mButton_dete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stuname = mEditText.getText().toString();
                DeStuByName(stuname);

            }
        });
        return mView;
    }

    public void DeteStuByName(String name) {
        mSqLiteDatabase.delete("Student", "Name=?", new String[]{name});
    }

    public void DeteCourseByCourseName(String CourseName) {
        mSqLiteDatabase.delete("Course", "CourseName=?", new String[]{CourseName});
    }

    public void DeteChooseByName(String Choosename) {
        mSqLiteDatabase.delete("Choose", "CourseID=?", new String[]{Choosename});
    }


    private void insert_students() {
        ContentValues value_student_1 = new ContentValues();
        value_student_1.put("SNO", "S00001");
        value_student_1.put("Name", "张三");
        value_student_1.put("Age", 20);
        value_student_1.put("College", "计算机学院");
        mSqLiteDatabase.insert("Student", null, value_student_1);

        ContentValues value_student_2 = new ContentValues();
        value_student_2.put("SNO", "S00002");
        value_student_2.put("Name", "李四");
        value_student_2.put("Age", 19);
        value_student_2.put("College", "通信学院");
        mSqLiteDatabase.insert("Student", null, value_student_2);

        ContentValues value_student_3 = new ContentValues();
        value_student_3.put("SNO", "S00003");
        value_student_3.put("Name", "王五");
        value_student_3.put("Age", 21);
        value_student_3.put("College", "计算机学院");
        mSqLiteDatabase.insert("Student", null, value_student_3);
    }

    private void insert_Course() {
        ContentValues value_Course_1 = new ContentValues();
        value_Course_1.put("CourseID", "C1");
        value_Course_1.put("CourseName", "计算机引论");
        value_Course_1.put("CourseBeforeID", "C2");
        mSqLiteDatabase.insert("Course", null, value_Course_1);

        ContentValues value_Course_2 = new ContentValues();
        value_Course_2.put("CourseID", "C2");
        value_Course_2.put("CourseName", "c语言");
        value_Course_2.put("CourseBeforeID", "C1");
        mSqLiteDatabase.insert("Course", null, value_Course_2);

        ContentValues value_Course_3 = new ContentValues();
        value_Course_3.put("CourseID", "C3");
        value_Course_3.put("CourseName", "数据结构");
        value_Course_3.put("CourseBeforeID", "C2");
        mSqLiteDatabase.insert("Course", null, value_Course_3);
    }

    private void insert_Choose() {
        ContentValues value_Choose_1 = new ContentValues();
        value_Choose_1.put("SNO", "S00001");
        value_Choose_1.put("CourseID", "C1");
        value_Choose_1.put("Score", "95");
        mSqLiteDatabase.insert("Choose", null, value_Choose_1);

        ContentValues value_Choose_2 = new ContentValues();
        value_Choose_2.put("SNO", "S00001");
        value_Choose_2.put("CourseID", "C2");
        value_Choose_2.put("Score", "80");
        mSqLiteDatabase.insert("Choose", null, value_Choose_2);


        ContentValues value_Choose_3 = new ContentValues();
        value_Choose_3.put("SNO", "S00001");
        value_Choose_3.put("CourseID", "C3");
        value_Choose_3.put("Score", "84");
        mSqLiteDatabase.insert("Choose", null, value_Choose_3);


        ContentValues value_Choose_4 = new ContentValues();
        value_Choose_4.put("SNO", "S00002");
        value_Choose_4.put("CourseID", "C1");
        value_Choose_4.put("Score", "80");
        mSqLiteDatabase.insert("Choose", null, value_Choose_4);


        ContentValues value_Choose_5 = new ContentValues();
        value_Choose_5.put("SNO", "S00002");
        value_Choose_5.put("CourseID", "C2");
        value_Choose_5.put("Score", "85");
        mSqLiteDatabase.insert("Choose", null, value_Choose_5);

        ContentValues value_Choose_6 = new ContentValues();
        value_Choose_6.put("SNO", "S00003");
        value_Choose_6.put("CourseID", "C1");
        value_Choose_6.put("Score", "78");
        mSqLiteDatabase.insert("Choose", null, value_Choose_6);

        ContentValues value_Choose_7 = new ContentValues();
        value_Choose_7.put("SNO", "S00003");
        value_Choose_7.put("CourseID", "C3");
        value_Choose_7.put("Score", "70");
        mSqLiteDatabase.insert("Choose", null, value_Choose_7);

    }

    public void DeStuByName(String string) {
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
        } else {
            mTextView.setText("没有该名学生");
        }

    }


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


}
