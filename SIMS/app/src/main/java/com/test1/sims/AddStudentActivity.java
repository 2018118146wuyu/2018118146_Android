package com.test1.sims;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import studentDBHelper.StudentDBHelper;
import student.Student;
import tableContent.TableContent;
import tool.Bihuatest;
import tool.Getbihua;

public class AddStudentActivity extends Activity implements OnClickListener {
    private static final String TAG = "AddStudentActivity";
    private final static int DATE_DIALOG = 1;
    private static final int DATE_PICKER_ID = 1;
    private TextView idText;
    private EditText moneyText;
    private EditText useText;
    private EditText sexText;
    private EditText dataText;


    private RadioGroup group;
    private RadioButton button1;
    private RadioButton button2;

    private Button restoreButton;
    private String sex;
    private Button resetButton;
    private Button cancelButton;
    private Long student_id;
    private String date = "";
    private StudentDao dao;
    private boolean isAdd = true;
    private Bihuatest b;
    private Getbihua g;
    private String bihua = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);

        b = new Bihuatest();
        g = new Getbihua();
        idText = (TextView) findViewById(R.id.tv_re_id);
        moneyText = (EditText) findViewById(R.id.et_money);
        useText = (EditText) findViewById(R.id.et_use);
        button1 = (RadioButton) findViewById(R.id.rb_sex_nan);
        button2 = (RadioButton) findViewById(R.id.rb_sex_nv);

        dataText = (EditText) findViewById(R.id.et_date);
        group = (RadioGroup) findViewById(R.id.rg_sex);

        restoreButton = (Button) findViewById(R.id.btn_save);
        resetButton = (Button) findViewById(R.id.btn_clear);
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        dao = new StudentDao(new StudentDBHelper(this)); // 设置监听 78

        restoreButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        dataText.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        try {
            readCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkIsAddStudent();
    }
    // 检查此时Activity是否用于添加学员信息
    private void checkIsAddStudent() {
        Intent intent = getIntent();
        Serializable serial = intent.getSerializableExtra(TableContent.STUDENT_TABLE);
        if (serial == null) {
            isAdd = true;
        } else {
            isAdd = false;
            Student s = (Student) serial;
            showEditUI(s);
        }
    }
    //显示学员信息更新的UI104
    private void showEditUI(Student student) {
        // 先将Student携带的数据还原到Student的每一个属性中去
        student_id = student.getId();
        String adclass = student.getAdclass();
        String sno = student.getSno();
        String name = student.getName();

        String sex = student.getSex();

        if (sex.toString().equals("男")) {
            button1.setChecked(true);
        } else if (sex.toString().equals("女")) {
            button2.setChecked(true);
        }

        // 还原数据
        idText.setText(student_id + "");
        moneyText.setText(adclass + "");
        useText.setText(sno + "");
        dataText.setText(name + "");

        setTitle("学员信息更新");
        restoreButton.setText("更新");
    }
    public void onClick(View v) {
        // 收集数据
        if (v == restoreButton) {
            if (!checkUIInput()) {// 界面输入验证
                return;
            }
            Student student = getStudentFromUI();
            date = student.getName();
            if (isAdd) {
                long id = 0;
                try {
                    id = dao.addStudent(student, g2());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dao.closeDB();
                if (id > 0) {
                    Toast.makeText(this, "保存成功， ID=" + id,Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "保存失败，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            } else if (!isAdd) {
                long id = 0;
                try {
                    id = dao.updateStudent(student, g2());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // long id = dao.addStudent(student);
                dao.closeDB();
                if (id > 0) {
                    Toast.makeText(this, "更新成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "更新失败，请重新输入！",Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == resetButton) {
            clearUIData();
        } else if (v == dataText) {
            showDialog(DATE_PICKER_ID);
        } else if(v == cancelButton){
            finish();
        }
    }
    //       清空界面的数据176
    private void clearUIData() {
        moneyText.setText("");
        useText.setText("");
        sexText.setText("");
        dataText.setText("");
        group.clearCheck();
    }
    //      收集界面输入的数据，并将封装成Student对象
    private Student getStudentFromUI(){
        String money = moneyText.getText().toString();
        String use = useText.getText().toString();

        String category = ((RadioButton) findViewById(group
                .getCheckedRadioButtonId())).getText().toString();
        date = dataText.getText().toString();
        String modifyDateTime = getCurrentDateTime();

        Student s=new Student(money, use, date, category, modifyDateTime);
        if (!isAdd) {
            s.setId(Integer.parseInt(idText.getText().toString()));
        }
        return s;
    }
    private void g() throws UnsupportedEncodingException {
        double a = b.getStrokeCount(date.charAt(0))/1.0;
        double c = b.getStrokeCount(date.charAt(1))/20.0;

        bihua = Double.toString(a + c);
    }
    //      * 得到当前的日期时间
    private String getCurrentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }

    //验证用户是否按要求输入了数据
    private boolean checkUIInput() { // name, age, sex
        String money = moneyText.getText().toString();
        String use = useText.getText().toString();
        String name = dataText.getText().toString();

        int id = group.getCheckedRadioButtonId();
        String message = null;
        View invadView = null;
        if (use.trim().length() == 0) {
            message = "请输入学号！";
            invadView = useText;
        } else if (id == -1) {
            message = "请选择性别！";
        } else if (money.trim().length() == 0) {
            message = "请输入行政班级";
            invadView = moneyText;
        } else if (name.trim().length() == 0) {
            message = "请输入姓名";
            invadView = dataText;
        }
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (invadView != null)
                invadView.requestFocus();
            return false;
        }
        return true;
    }

    public void readCSV() throws IOException{
        InputStream fiStream;
        Scanner scanner;
        try {
            fiStream=getAssets().open("学生名单.csv");
            scanner=new Scanner(fiStream,"utf-8");
            scanner.nextLine();//读下一行,把表头越过。不注释的话第一行数据就越过去了
            while (scanner.hasNextLine()) {
                String sourceString = scanner.nextLine();
                String[] lines=sourceString.split(",");

                Student bean = new Student(lines[1],lines[2],lines[3],lines[4],getCurrentDateTime());
                final String[] bi = {""};
                date = lines[3];
                dao.addStudent(bean, g2());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String g2() throws IOException {
        final double[] a = {b.getStrokeCount(date.charAt(0)) / 1.0};
        final double[] c = {b.getStrokeCount(date.charAt(1)) / 50.0};
        // Android 4.0 之后不能在主线程中请求HTTP请求
        if(a[0] < 0){
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        a[0] = g.getbihua(String.valueOf(date.charAt(0)))/1.0;
                        c[0] = g.getbihua(String.valueOf(date.charAt(1)))/50.0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return Double.toString(a[0] + c[0]);
        }else{
            return Double.toString(a[0] + c[0]);
        }

    }

}

