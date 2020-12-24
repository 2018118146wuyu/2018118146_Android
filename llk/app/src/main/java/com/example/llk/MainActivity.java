package com.example.llk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends Activity {

    private GameView gameView;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        setContentView(gameView);

        dialog = new AlertDialog.Builder(this).setTitle("确认消息").setMessage("你确定要退出游戏吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameView.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        dialog.show();
    }

}