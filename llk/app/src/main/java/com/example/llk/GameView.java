package com.example.llk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;

public class GameView extends View {
    /**
     * 默认模式4*4的地图
     */
    public static final int COUNT = 4;

    /**
     * 对应分值方块的颜色
     */
    private static SparseIntArray colors;
    static {
        colors = new SparseIntArray();
        colors.put(2, Color.parseColor("#EEE4DA"));
        colors.put(4, Color.parseColor("#EDE0C8"));
        colors.put(8, Color.parseColor("#F2B179"));
        colors.put(16, Color.parseColor("#F59563"));
        colors.put(32, Color.parseColor("#F67C5F"));
        colors.put(64, Color.parseColor("#F65E3B"));
        colors.put(128, Color.parseColor("#DCBF65"));
        colors.put(256, Color.parseColor("#EDCC61"));
        colors.put(512, Color.parseColor("#EDC850"));
        colors.put(1024, Color.parseColor("#DBB732"));
        colors.put(2048, Color.parseColor("#EFC329"));
        colors.put(4096, Color.parseColor("#FF3C39"));
    }
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕高度
     */
    private int screenHeight;
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 画笔宽度
     */
    private float paintWidth;
    /**
     * 游戏区域开始点x坐标
     */
    private int areaStartX;
    /**
     * 游戏区域开始点y坐标
     */
    private int areaStartY;
    /**
     * 分值方块的大小
     */
    private int cellSize;
    /**
     * 游戏方块分值保存数组
     */
    private int[][] map;
    /**
     * 手指按下x坐标
     */
    private float xDown;
    /**
     * 手指按下y坐标
     */
    private float yDown;
    /**
     * 手指移动x坐标
     */
    private float xMove;
    /**
     * 手指移动y坐标
     */
    private float yMove;
    /**
     * 手指是否按下
     */
    private boolean isPressed;
    /**
     * 游戏地图是否已经移动过了
     */
    private boolean isMoved;
    /**
     * 随机数生成器
     */
    private Random random;
    /**
     * 当前得分
     */
    private int score;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.paint = new Paint();
        this.paintWidth = 15f;
        this.random = new Random();
        this.map = new int[COUNT][COUNT];

        // 默认初始化3个方块
        for (int i = 0; i < 3; i++) {
            createRandomRect();
        }

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        areaStartY = (screenHeight - screenWidth) >> 1;
        areaStartX = 0;

        cellSize = screenWidth / COUNT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGameArea(canvas, areaStartX, areaStartY);
        drawGameRect(canvas, map);
        drawScore(canvas);
    }

    /**
     * 从屏幕的(x,y)点开始画出游戏区域
     *
     * @param canvas
     * @param x
     * @param y
     */
    private void drawGameArea(Canvas canvas, int x, int y) {
        // 画游戏区域背景
        paint.reset();
        paint.setColor(Color.parseColor("#CDC7BB"));
        canvas.drawRect(x, y, screenWidth, y + screenWidth, paint);

        // 画出分割线
        paint.reset();
        paint.setColor(Color.parseColor("#CDB599"));
        paint.setStrokeWidth(paintWidth);
        for (int i = 0; i <= COUNT; i++) {
            canvas.drawLine(x + i, cellSize * i + y, screenWidth, cellSize * i + y, paint);
        }
        for (int i = 0; i <= COUNT; i++) {
            canvas.drawLine(cellSize * i, i + y, cellSize * i, screenWidth + y, paint);
        }
    }

    /**
     * 画出游戏方块
     *
     * @param canvas
     * @param map
     */
    private void drawGameRect(Canvas canvas, int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0) {
                    drawEachRect(canvas, i, j);
                }
            }
        }
    }

    /**
     * 画出每一个小方块
     *
     * @param canvas
     * @param i
     * @param j
     */
    private void drawEachRect(Canvas canvas, int i, int j) {
        // 画游戏方块背景
        paint.reset();
        paint.setColor(colors.get(map[i][j], colors.get(4096)));
        canvas.drawRect(cellSize * j + areaStartX + paintWidth / 2, cellSize * i + areaStartY
                + paintWidth / 2, cellSize * j + areaStartX - paintWidth / 2 + cellSize, cellSize
                * i + areaStartY - paintWidth / 2 + cellSize, paint);

        // 画出方块分值
        paint.reset();
        if (map[i][j] <= 4) {
            paint.setColor(Color.BLACK);
        } else {
            paint.setColor(Color.WHITE);
        }
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(paintWidth);
        paint.setTextSize(cellSize * 5 / 12);
        canvas.drawText(String.valueOf(map[i][j]), cellSize * j + areaStartX + cellSize / 2,
                cellSize * i + areaStartY + cellSize * 2 / 3, paint);
    }

    /**
     * 显示得分
     *
     * @param canvas
     */
    private void drawScore(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.GRAY);
        paint.setTextSize(cellSize * 5 / 12);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(paintWidth);
        canvas.drawText("得分：" + score, screenWidth / 2, areaStartY / 2, paint);
    }

    /**
     * 处理触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();

                // 移动距离
                int distanceX = (int) (xMove - xDown);
                int distanceY = (int) (yMove - yDown);

                // 移动距离绝对值
                int absX = Math.abs(distanceX);
                int absY = Math.abs(distanceY);

                // 如果移动距离大于屏幕宽度的1/4就算作有效移动
                if (absX > (screenWidth >> 2) || absY > (screenWidth >> 2)) {
                    if (absX > absY) {
                        if (distanceX < 0) {
                            moveToLeft();
                        } else {
                            moveToRight();
                        }
                    } else {
                        if (distanceY < 0) {
                            moveToUp();
                        } else {
                            moveToDown();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                isMoved = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 不能移动？
     *
     * @return
     */
    private boolean canNotMove() {
        // 手指按下状态，并且已经掉用过移动方法了，就不继续调用了，防止按下多次调用移动方法
        return isPressed && isMoved;
    }

    /**
     * 上移
     */
    private void moveToUp() {
        if (canNotMove()) {
            return;
        }
        // 保存移动前状态
        int[][] mapTemp = copyMap(map);

        // 先消除中间空白方块
        int k;
        int temp;
        for (int j = 0; j < COUNT; j++) {
            for (int i = 0; i < COUNT - 1; i++) {
                k = i;
                while (k < COUNT - 1 && map[k][j] == 0) {
                    temp = map[k][j];
                    map[k][j] = map[k + 1][j];
                    map[k + 1][j] = temp;
                    k++;
                }
            }
        }

        // 合并能够相加的方块
        for (int j = 0; j < COUNT; j++) {
            for (int i = 0; i < COUNT - 1; i++) {
                if (map[i][j] == map[i + 1][j]) {
                    map[i][j] *= 2;
                    score += map[i + 1][j];
                    map[i + 1][j] = 0;
                }
            }
        }

        // 再消除一遍中间空白方块
        for (int j = 0; j < COUNT; j++) {
            for (int i = 0; i < COUNT - 1; i++) {
                k = i;
                while (k < COUNT - 1 && map[k][j] == 0) {
                    temp = map[k][j];
                    map[k][j] = map[k + 1][j];
                    map[k + 1][j] = temp;
                    k++;
                }
            }
        }

        // 如果移动过后方块数据不相等，说明移动成功，应该创建一个新的方块
        if (!isEquals(mapTemp, map)) {
            createRandomRect();
        }

        // 刷新画面
        invalidate();
        isMoved = true;
    }

    /**
     * 下移
     */
    private void moveToDown() {
        if (canNotMove()) {
            return;
        }

        int[][] mapTemp = copyMap(map);

        int k;
        int temp;
        for (int j = 0; j < COUNT; j++) {
            for (int i = COUNT - 1; i > 0; i--) {
                k = i;
                while (k > 0 && map[k][j] == 0) {
                    temp = map[k][j];
                    map[k][j] = map[k - 1][j];
                    map[k - 1][j] = temp;
                    k--;
                }
            }
        }

        for (int j = 0; j < COUNT; j++) {
            for (int i = COUNT - 1; i > 0; i--) {
                if (map[i][j] == map[i - 1][j]) {
                    map[i][j] *= 2;
                    score += map[i - 1][j];
                    map[i - 1][j] = 0;
                }
            }
        }

        for (int j = 0; j < COUNT; j++) {
            for (int i = COUNT - 1; i > 0; i--) {
                k = i;
                while (k > 0 && map[k][j] == 0) {
                    temp = map[k][j];
                    map[k][j] = map[k - 1][j];
                    map[k - 1][j] = temp;
                    k--;
                }
            }
        }
        if (!isEquals(mapTemp, map)) {
            createRandomRect();
        }
        invalidate();
        isMoved = true;
    }

    /**
     * 左移
     */
    private void moveToLeft() {
        if (canNotMove()) {
            return;
        }
        int[][] mapTemp = copyMap(map);

        int k;
        int temp;
        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < map[i].length - 1; j++) {
                k = j;
                while (k < COUNT - 1 && map[i][k] == 0) {
                    temp = map[i][k];
                    map[i][k] = map[i][k + 1];
                    map[i][k + 1] = temp;
                    k++;
                }
            }
        }

        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < map[i].length - 1; j++) {
                if (map[i][j] == map[i][j + 1]) {
                    map[i][j] *= 2;
                    score += map[i][j + 1];
                    map[i][j + 1] = 0;
                }
            }
        }

        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < map[i].length - 1; j++) {
                k = j;
                while (k < COUNT - 1 && map[i][k] == 0) {
                    temp = map[i][k];
                    map[i][k] = map[i][k + 1];
                    map[i][k + 1] = temp;
                    k++;
                }
            }
        }
        if (!isEquals(mapTemp, map)) {
            createRandomRect();
        }
        invalidate();
        isMoved = true;
    }

    /**
     * 右移
     */
    private void moveToRight() {
        if (canNotMove()) {
            return;
        }
        int[][] mapTemp = copyMap(map);

        int k;
        int temp;
        for (int i = 0; i < COUNT; i++) {
            for (int j = map[i].length - 1; j > 0; j--) {
                k = j;
                while (k > 0 && map[i][k] == 0) {
                    temp = map[i][k];
                    map[i][k] = map[i][k - 1];
                    map[i][k - 1] = temp;
                    k--;
                }
            }
        }

        for (int i = 0; i < COUNT; i++) {
            for (int j = map[i].length - 1; j > 0; j--) {
                if (map[i][j] == map[i][j - 1]) {
                    map[i][j] *= 2;
                    score += map[i][j - 1];
                    map[i][j - 1] = 0;
                }
            }
        }

        for (int i = 0; i < COUNT; i++) {
            for (int j = map[i].length - 1; j > 0; j--) {
                k = j;
                while (k > 0 && map[i][k] == 0) {
                    temp = map[i][k];
                    map[i][k] = map[i][k - 1];
                    map[i][k - 1] = temp;
                    k--;
                }
            }
        }
        if (!isEquals(mapTemp, map)) {
            createRandomRect();
        }
        invalidate();
        isMoved = true;
    }

    /**
     * 创建随机方块
     */
    private void createRandomRect() {
        int num = Math.random() < 0.9 ? 2 : 4; // 随机出现一个2或者4

        int x;
        int y;
        do {
            x = random.nextInt(COUNT);
            y = random.nextInt(COUNT);
        } while (map[x][y] != 0); // 随机找一点，直到该点没有方块为止

        map[x][y] = num; // 把新生成的数加到地图里面
    }

    /**
     * 判断两份方块数据是否一致
     *
     * @param tempMap
     * @param map
     * @return
     */
    private boolean isEquals(int[][] tempMap, int[][] map) {
        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < COUNT; j++) {
                if (tempMap[i][j] != map[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 复制一份游戏方块数据
     *
     * @param map
     * @return
     */
    private int[][] copyMap(int[][] map) {
        int[][] tempMap = new int[COUNT][COUNT];
        for (int i = 0; i < COUNT; i++) {
            tempMap[i] = map[i].clone(); // 数组克隆
        }
        return tempMap;
    }

}
