package cn.edu.jnu.fish.factory;

import android.content.res.Resources;

import cn.edu.jnu.fish.object.BigFish;
import cn.edu.jnu.fish.object.GameObject;
import cn.edu.jnu.fish.object.MiddleFish;
import cn.edu.jnu.fish.object.MyFish;
import cn.edu.jnu.fish.object.SmallFish;

/**
 * Created by lenovo on 2016/11/8.
 */

public class GameObjectFactory {
    public GameObject createSmallFish(Resources resources){
        return new SmallFish(resources);
    }

    public GameObject createMiddleFish(Resources resources){
        return new MiddleFish(resources);
    }

    public GameObject createBigFish(Resources resources){
        return new BigFish(resources);
    }

    public GameObject createMyFish(Resources resources){
        return new MyFish(resources);
    }
}
