package com.test1.sims;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import student.Student;
import tableContent.TableContent;
public class ShowStudentActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info);
        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra(TableContent.STUDENT_TABLE);
        ((TextView)findViewById(R.id.tv_info_id)).setText(student.getId()+"");
        ((TextView)findViewById(R.id.tv_info_use)).setText(student.getAdclass()+"");
        ((TextView)findViewById(R.id.tv_info_category)).setText(student.getSno());
        ((TextView)findViewById(R.id.tv_info_money)).setText(student.getName());
        ((TextView)findViewById(R.id.tv_info_date)).setText(student.getSex());
        ((TextView)findViewById(R.id.tv_info_modify)).setText(student.getModify_time());
    }
    public void goBack(View view) {
        finish();
    }
}

