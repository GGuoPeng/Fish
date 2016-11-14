package cn.edu.jnu.fish.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jnu.fish.R;
import cn.edu.jnu.fish.factory.GameObjectFactory;
import cn.edu.jnu.fish.interfaces.IMyFish;
import cn.edu.jnu.fish.view.MainView;

/**
 * Created by lenovo on 2016/11/8.
 */

public class MyFish extends GameObject implements IMyFish {
    private float middle_x;             // 鱼中心坐标
    private float middle_y;
    private long startTime;             // 开始的时间
    private long endTime;             // 结束的时间
    private Bitmap myfish;             // 鱼游动时的图片
    private MainView mainView;
    private GameObjectFactory factory;

    public MyFish(Resources resources) {
        super(resources);
        initBitmap();
        this.speed = 8;
        factory = new GameObjectFactory();
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    // 设置屏幕宽度和高度
    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);
        object_x = screen_width / 2 - object_width / 2;
        object_y = screen_height - object_height;
        middle_x = object_x + object_width / 2;
        middle_y = object_y + object_height / 2;
    }

    // 初始化图片资源的
    @Override
    public void initBitmap() {
        myfish = BitmapFactory.decodeResource(resources, R.drawable.myfish);
        object_width = myfish.getWidth() / 2; // 获得每一帧位图的宽
        object_height = myfish.getHeight();    // 获得每一帧位图的高
    }

    // 对象的绘图方法
    @Override
    public void drawSelf(Canvas canvas) {
        if (isAlive) {
            int x = (int) (currentFrame * object_width); // 获得当前帧相对于位图的X坐标
            canvas.save();
            int change = mainView.getSumScore() / 100;

            canvas.clipRect(object_x, object_y, object_x + object_width + change, object_y + object_height + change);
            canvas.drawBitmap(myfish, object_x - x, object_y, paint);
            canvas.restore();
            currentFrame++;
            if (currentFrame >= 2) {
                currentFrame = 0;
            }
        } else {
            currentFrame++;
            if (currentFrame >= 2) {
                currentFrame = 1;
            }
        }
    }

    // 释放资源的方法
    @Override
    public void release() {
        if (!myfish.isRecycled()) {
            myfish.recycle();
        }
    }

    //发射子弹
    @Override
    public void eat(Canvas canvas, List<EnemyFish> planes) {
        Log.d("MyFish", "eat");
        for (EnemyFish obj : planes) { //遍历敌机对象
            // 判断敌机是否被检测碰撞
            if (obj.isCanCollide()) {
                if (obj.isCollide((GameObject) obj)) {                       // 检查碰撞
                    if () {
                        mainView.addGameScore(obj.getScore());// 获得分数
                        if (obj instanceof SmallFish) {
                            mainView.playSound(2);
                        } else if (obj instanceof MiddleFish) {
                            mainView.playSound(3);
                        } else if (obj instanceof BigFish) {
                            mainView.playSound(4);
                        } else {
                            mainView.playSound(5);
                        }
                        break;
                    }
                }
            }
        }
    }

    //getter和setter方法
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public float getMiddle_x() {
        return middle_x;
    }

    @Override
    public void setMiddle_x(float middle_x) {
        this.middle_x = middle_x;
        this.object_x = middle_x - object_width / 2;
    }

    @Override
    public float getMiddle_y() {
        return middle_y;
    }

    @Override
    public void setMiddle_y(float middle_y) {
        this.middle_y = middle_y;
        this.object_y = middle_y - object_height / 2;
    }
}
