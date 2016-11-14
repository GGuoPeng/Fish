package cn.edu.jnu.fish.interfaces;

import android.graphics.Canvas;

import java.util.List;

import cn.edu.jnu.fish.object.EnemyFish;


public interface IMyFish {
    public float getMiddle_x();

    public void setMiddle_x(float middle_x);

    public float getMiddle_y();

    public void setMiddle_y(float middle_y);

    public void eat(Canvas canvas,List<EnemyFish> planes);
}
