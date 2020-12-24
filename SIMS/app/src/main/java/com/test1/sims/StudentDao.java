package com.test1.sims;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import student.Student;
import studentDBHelper.StudentDBHelper;
import tableContent.TableContent;

public class StudentDao {
    private StudentDBHelper dbHelper;
    private Cursor cursor;

    public StudentDao(StudentDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // 添加一个Student对象数据到数据库表
    public long addStudent(Student r, String a) {
        ContentValues values = new ContentValues();
        values.put(TableContent.StudentColumns.ADCALSS, r.getAdclass());
        values.put(TableContent.StudentColumns.SNO, r.getSno());
        values.put(TableContent.StudentColumns.NAME, r.getName());
        values.put(TableContent.StudentColumns.SEX, r.getSex());
        values.put(TableContent.StudentColumns.MODIFY_TIME, r.getModify_time());
        values.put(TableContent.StudentColumns.BIHUA, Float.parseFloat(a));
        return dbHelper.getWritableDatabase().insert(TableContent.STUDENT_TABLE, null, values);
    }

    // 删除一个id所对应的数据库表Student的记录
    public int deleteStudentById(long id) {
        return dbHelper.getWritableDatabase().delete(TableContent.STUDENT_TABLE,
                TableContent.StudentColumns.ID + "=?", new String[]{id + ""});
    }

    // 更新一个id所对应数据库表Student的记录
    public int updateStudent(Student r, String a){
        ContentValues values = new ContentValues();
        values.put(TableContent.StudentColumns.ADCALSS, r.getAdclass());
        values.put(TableContent.StudentColumns.SNO, r.getSno());
        values.put(TableContent.StudentColumns.NAME, r.getName());
        values.put(TableContent.StudentColumns.SEX, r.getSex());
        values.put(TableContent.StudentColumns.MODIFY_TIME, r.getModify_time());
        values.put(TableContent.StudentColumns.BIHUA, Float.parseFloat(a));
        return dbHelper.getWritableDatabase().update(TableContent.STUDENT_TABLE, values,
                TableContent.StudentColumns.ID + "=?", new String[]{Long.toString(r.getId())});
    }

    // 查询所有的记录
    public List<Map<String, Object>> getAllStudents() {
        //modify_time desc
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, null, null,
                null, null, TableContent.StudentColumns.MODIFY_TIME + " desc");
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>(8);
            long id = cursor.getInt(cursor.getColumnIndex(TableContent.StudentColumns.ID));
            map.put(TableContent.StudentColumns.ID, id);
            String adclass = cursor.getString(cursor.getColumnIndex(TableContent.StudentColumns.ADCALSS));
            map.put(TableContent.StudentColumns.ADCALSS, adclass);
            String sno = cursor.getString(cursor.getColumnIndex(TableContent.StudentColumns.SNO));
            map.put(TableContent.StudentColumns.SNO, sno);
            String name = cursor.getString(cursor.getColumnIndex(TableContent.StudentColumns.NAME));
            map.put(TableContent.StudentColumns.NAME, name);
            String sex = cursor.getString(cursor.getColumnIndex(TableContent.StudentColumns.SEX));
            map.put(TableContent.StudentColumns.SEX, sex);
            String modify_time = cursor.getString(cursor.getColumnIndex(TableContent.StudentColumns.MODIFY_TIME));
            map.put(TableContent.StudentColumns.MODIFY_TIME, modify_time);
            data.add(map);
        }
        return data;
    }

    //由学号模糊查询一条记录
    public Cursor findStudentSno(String sno) {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, "sno like ?",
                new String[]{"%" + sno + "%"}, null, null, null, null);
        return cursor;
    }

    //由班级查询一条记录
    public Cursor findStudentAdclass(String adclass) {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, "adclass like ?",
                new String[]{"%" + adclass + "%"}, null, null, null, null);
        return cursor;
    }

    //由姓名查询一条记录
    public Cursor findStudentName(String name) {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, "name like ?",
                new String[]{"%" + name + "%"}, null, null, null, null);
        return cursor;
    }

    //由性别查询一条记录
    public Cursor findStudentSex(String sex) {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, "sex like ?",
                new String[]{"%" + sex + "%"}, null, null, null, null);
        return cursor;
    }

//    //由金额范围查询一条记录
//    public Cursor findStudentMoney(float money1, float money2) {
//        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE,null,"money>=? and money<=?",
//                new String[]{Float.toString(money1), Float.toString(money2)},null,null,null);
//        //多条件查询记得String是数组
//        return cursor;
//    }
//
//    //由时间范围查询一条记录
//    public Cursor findStudentDate(String date1, String date2) {
//        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE,null,"date>=? and date<=?",
//                new String[]{date1, date2},null,null,null);
//        //多条件查询记得String是数组
//        return cursor;
//    }

    //按编号进行排序
    public Cursor sortByID() {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, null,
                null, null, null, TableContent.StudentColumns.ID);
        return cursor;
    }

    //按班级进行排序
    public Cursor sortByMoney() {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, null,
                null, null, null, TableContent.StudentColumns.ADCALSS);
        return cursor;
    }

    //按学号进行排序
    public Cursor sortByUse() {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, null,
                null, null, null, TableContent.StudentColumns.SNO);
        return cursor;
    }

    //按姓名进行排序
    public Cursor sortByDate() {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, null,
                null, null, null, TableContent.StudentColumns.BIHUA);
        while(cursor.moveToNext()){
            String n = cursor.getString(cursor.getColumnIndex("name"));
            Float b = cursor.getFloat(cursor.getColumnIndex("bihua"));

            System.out.print(n);
            System.out.println(b);
        }
        return cursor;
    }

    //按性别进行排序
    public Cursor sortByCategory() {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, null,
                null, null, null, TableContent.StudentColumns.SEX);
        return cursor;
    }

    //按最后修改时间进行排序
    public Cursor sortByModify() {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, null,
                null, null, null, TableContent.StudentColumns.MODIFY_TIME);
        return cursor;
    }

    // 自定义的方法通过View和Id得到一个Student对象
    public Student getStudentFromView(View view, long id) {
        TextView adclassView = (TextView) view.findViewById(R.id.tv_st_adclass);
        TextView snoView = (TextView) view.findViewById(R.id.tv_st_sno);
        TextView nameView = (TextView) view.findViewById(R.id.tv_st_name);
        TextView sexView = (TextView) view.findViewById(R.id.tv_st_sex);

        String adclass = adclassView.getText().toString();
        String sno = snoView.getText().toString();
        String name = nameView.getText().toString();
        String sex = sexView.getText().toString();

        Student student = new Student(id, adclass, sno, name, sex, null);
        return student;
    }

    // 查询一条ID的记录
    public Student getStudentByID(long id) {
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContent.STUDENT_TABLE, null, "_id = ?",
                new String[]{Long.toString(id)}, null, null, null);
        cursor.moveToFirst();
        String adclass = cursor.getString(cursor.getColumnIndex("adclass"));
        String sno = cursor.getString(cursor.getColumnIndex("sno"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String sex = cursor.getString(cursor.getColumnIndex("sex"));
        String modify_time = cursor.getString(cursor.getColumnIndex("modify_time"));
        return new Student(id, adclass, sno, name, sex, modify_time);
    }

    public void closeDB() {
        dbHelper.close();
    }


}