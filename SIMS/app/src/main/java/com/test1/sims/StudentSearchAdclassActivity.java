package com.test1.sims;

import studentDBHelper.StudentDBHelper;
import tableContent.TableContent;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class StudentSearchAdclassActivity extends Activity implements OnClickListener {
    private EditText adclassText;
    private Button button;
    private Button reButton;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private StudentDao dao;
    private Button returnButton;
    private LinearLayout layout;

    SyncHorizontalScrollView scrollView1, scrollView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_adclass);
        adclassText = (EditText) findViewById(R.id.et_searchAdclass);
        layout=(LinearLayout) findViewById(R.id.linersearch2);
        button = (Button) findViewById(R.id.bn_sure_search2);
        reButton = (Button) findViewById(R.id.bn_return2);
        listView = (ListView) findViewById(R.id.searchListView2);
        returnButton = (Button) findViewById(R.id.return_id2);
        dao = new StudentDao(new StudentDBHelper(this));

        scrollView1 = findViewById(R.id.hs_12);
        scrollView2 = findViewById(R.id.hs_22);
        scrollView1.setOtherHsv(scrollView2);
        scrollView2.setOtherHsv(scrollView1);

        reButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            reButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            adclassText.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            String adclass = adclassText.getText().toString();
            cursor = dao.findStudentAdclass(adclass);
            if (!cursor.moveToFirst()) {
                Toast.makeText(this, "没有所查行政班级信息！", Toast.LENGTH_SHORT).show();
            } else
                //如果有所查询的信息，则将查询结果显示出来
                adapter = new SimpleCursorAdapter(this, R.layout.student_list_item,
                        cursor, new String[] { TableContent.StudentColumns.ID,
                        TableContent.StudentColumns.ADCALSS,
                        TableContent.StudentColumns.SNO,
                        TableContent.StudentColumns.NAME,
                        TableContent.StudentColumns.SEX,
                        TableContent.StudentColumns.MODIFY_TIME},
                        new int[] {
                                R.id.tv_st_id,
                                R.id.tv_st_adclass,
                                R.id.tv_st_sno,
                                R.id.tv_st_name,
                                R.id.tv_st_sex,
                                R.id.tv_st_modify});
            listView.setAdapter(adapter);
        }else if(v==reButton|v==returnButton){
            finish();
        }
    }
}



