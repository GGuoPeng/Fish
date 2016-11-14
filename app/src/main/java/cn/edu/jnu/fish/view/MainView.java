package cn.edu.jnu.fish.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jnu.fish.R;
import cn.edu.jnu.fish.constant.ConstantUtil;
import cn.edu.jnu.fish.factory.GameObjectFactory;
import cn.edu.jnu.fish.object.BigFish;
import cn.edu.jnu.fish.object.EnemyFish;
import cn.edu.jnu.fish.object.GameObject;
import cn.edu.jnu.fish.object.MiddleFish;
import cn.edu.jnu.fish.object.MyFish;
import cn.edu.jnu.fish.object.SmallFish;
import cn.edu.jnu.fish.sounds.GameSoundPool;

/*��Ϸ���е�������*/
public class MainView extends BaseView {
    private int middlePlaneScore;    // ���͵л��Ļ���
    private int bigPlaneScore;        // ���͵л��Ļ���
    private int sumScore;            // ��Ϸ�ܵ÷�
    private int speedTime;            // ��Ϸ�ٶȵı���

    private float bg_y;                // ͼƬ������
    private float bg_y2;
    private float play_bt_w;
    private float play_bt_h;
    private float missile_bt_y;
    private boolean isPlay;            // �����Ϸ����״̬
    private boolean isTouchPlane;    // �ж�����Ƿ�����Ļ
    private Bitmap background;        // ����ͼƬ
    private Bitmap background2;    // ����ͼƬ
    private Bitmap playButton;        // ��ʼ/��ͣ��Ϸ�İ�ťͼƬ
    private Bitmap missile_bt;        // ������ťͼ��
    private MyFish myFish;        // ��ҵ���
    private List<EnemyFish> enemyFishes;
    private GameObjectFactory factory;

    public MainView(Context context, GameSoundPool sounds) {
        super(context, sounds);
        isPlay = true;
        speedTime = 1;
        factory = new GameObjectFactory();                          //������
        enemyFishes = new ArrayList<EnemyFish>();
        myFish = (MyFish) factory.createMyFish(getResources());//������ҵķɻ�
        myFish.setMainView(this);
        for (int i = 0; i < SmallFish.sumCount; i++) {
            //����С�͵л�
            SmallFish smallFish = (SmallFish) factory.createSmallFish(getResources());
            enemyFishes.add(smallFish);
        }
        for (int i = 0; i < MiddleFish.sumCount; i++) {
            //�������͵л�
            MiddleFish middleFish = (MiddleFish) factory.createMiddleFish(getResources());
            enemyFishes.add(middleFish);
        }
        for (int i = 0; i < BigFish.sumCount; i++) {
            //�������͵л�
            BigFish bigFish = (BigFish) factory.createBigFish(getResources());
            enemyFishes.add(bigFish);
        }
        thread = new Thread(this);
    }

    // ��ͼ�ı�ķ���
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        super.surfaceChanged(arg0, arg1, arg2, arg3);
    }

    // ��ͼ�����ķ���
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        super.surfaceCreated(arg0);
        initBitmap(); // ��ʼ��ͼƬ��Դ
        for (GameObject obj : enemyFishes) {
            obj.setScreenWH(screen_width, screen_height);
        }
        myFish.setScreenWH(screen_width, screen_height);
        myFish.setAlive(true);
        if (thread.isAlive()) {
            thread.start();
        } else {
            thread = new Thread(this);
            thread.start();
        }
    }

    // ��ͼ���ٵķ���
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        super.surfaceDestroyed(arg0);
        release();
    }

    // ��Ӧ�����¼��ķ���
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isTouchPlane = false;
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h) {
                if (isPlay) {
                    isPlay = false;
                } else {
                    isPlay = true;
                    synchronized (thread) {
                        thread.notify();
                    }
                }
                return true;
            }
            //�ж���ҷɻ��Ƿ񱻰���
            else if (x > myFish.getObject_x() && x < myFish.getObject_x() + myFish.getObject_width()
                    && y > myFish.getObject_y() && y < myFish.getObject_y() + myFish.getObject_height()) {
                if (isPlay) {
                    isTouchPlane = true;
                }
                return true;
            }
        }
        //��Ӧ��ָ����Ļ�ƶ����¼�
        else if (event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1) {
            //�жϴ������Ƿ�Ϊ��ҵķɻ�
            if (isTouchPlane) {
                float x = event.getX();
                float y = event.getY();
                if (x > myFish.getMiddle_x() + 20) {
                    if (myFish.getMiddle_x() + myFish.getSpeed() <= screen_width) {
                        myFish.setMiddle_x(myFish.getMiddle_x() + myFish.getSpeed());
                    }
                } else if (x < myFish.getMiddle_x() - 20) {
                    if (myFish.getMiddle_x() - myFish.getSpeed() >= 0) {
                        myFish.setMiddle_x(myFish.getMiddle_x() - myFish.getSpeed());
                    }
                }
                if (y > myFish.getMiddle_y() + 20) {
                    if (myFish.getMiddle_y() + myFish.getSpeed() <= screen_height) {
                        myFish.setMiddle_y(myFish.getMiddle_y() + myFish.getSpeed());
                    }
                } else if (y < myFish.getMiddle_y() - 20) {
                    if (myFish.getMiddle_y() - myFish.getSpeed() >= 0) {
                        myFish.setMiddle_y(myFish.getMiddle_y() - myFish.getSpeed());
                    }
                }
                return true;
            }
        }
        return false;
    }

    // ��ʼ��ͼƬ��Դ����
    @Override
    public void initBitmap() {
        playButton = BitmapFactory.decodeResource(getResources(), R.drawable.play);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
        background2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_02);
        scalex = screen_width / background.getWidth();
        scaley = screen_height / background.getHeight();
        play_bt_w = playButton.getWidth();
        play_bt_h = playButton.getHeight() / 2;
        bg_y = 0;
        bg_y2 = bg_y - screen_height;
    }

    //��ʼ����Ϸ����
    public void initObject() {
        for (EnemyFish obj : enemyFishes) {
            //��ʼ��С�͵л�
            if (obj instanceof SmallFish) {
                if (!obj.isAlive()) {
                    obj.initial(speedTime, 0, 0);
                    break;
                }
            }
            //��ʼ�����͵л�
            else if (obj instanceof MiddleFish) {
                if (middlePlaneScore > 10000) {
                    if (!obj.isAlive()) {
                        obj.initial(speedTime, 0, 0);
                        break;
                    }
                }
            }
            //��ʼ�����͵л�
            else if (obj instanceof BigFish) {
                if (bigPlaneScore >= 25000) {
                    if (!obj.isAlive()) {
                        obj.initial(speedTime, 0, 0);
                        break;
                    }
                }
            }
        }

        //�����ȼ�
        if (sumScore >= speedTime * 100000 && speedTime < 6) {
            speedTime++;
        }
    }

    // �ͷ�ͼƬ��Դ�ķ���
    @Override
    public void release() {
        for (GameObject obj : enemyFishes) {
            obj.release();
        }
        myFish.release();
        if (!playButton.isRecycled()) {
            playButton.recycle();
        }
        if (!background.isRecycled()) {
            background.recycle();
        }
        if (!background2.isRecycled()) {
            background2.recycle();
        }
        if (!missile_bt.isRecycled()) {
            missile_bt.recycle();
        }
    }

    // ��ͼ����
    @Override
    public void drawSelf() {
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.BLACK); // ���Ʊ���ɫ
            canvas.save();
            // ���㱳��ͼƬ����Ļ�ı���
            canvas.scale(scalex, scaley, 0, 0);
            canvas.drawBitmap(background, 0, bg_y, paint);   // ���Ʊ���ͼ
            canvas.drawBitmap(background2, 0, bg_y2, paint); // ���Ʊ���ͼ
            canvas.restore();
            //���ư�ť
            canvas.save();
            canvas.clipRect(10, 10, 10 + play_bt_w, 10 + play_bt_h);
            if (isPlay) {
                canvas.drawBitmap(playButton, 10, 10, paint);
            } else {
                canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
            }
            canvas.restore();
            //���Ƶл�
            for (EnemyFish obj : enemyFishes) {
                if (obj.isAlive()) {
                    obj.drawSelf(canvas);
                    //���л��Ƿ�����ҵķɻ���ײ
                    //����д�жϺ���
                    //�ж����������߳���
                    if (obj.isCanCollide() && myFish.isAlive()) {
                        if (obj.isCollide(myFish)) {
                            if (sumScore <= 500){
                                if(obj instanceof SmallFish){
                                    obj.setAlive(false);
                                }else{
                                    myFish.setAlive(false);
                                }
                            }else if(sumScore<=2000){
                                if(obj instanceof BigFish){
                                    myFish.setAlive(false);
                                }else{
                                    obj.setAlive(false);
                                }
                            }else{
                                obj.setAlive(false);
                            }
                        }
                    }
                }
            }
            if (!myFish.isAlive()) {
                threadFlag = false;
            }
            myFish.drawSelf(canvas);    //������ҵķɻ�
            Log.d("MainView","eat");
            myFish.eat(canvas,enemyFishes);
            //���ƻ�������
            paint.setTextSize(30);
            paint.setColor(Color.rgb(235, 161, 1));
            canvas.drawText("����:" + String.valueOf(sumScore), 30 + play_bt_w, 40, paint);        //��������
            canvas.drawText("�ȼ� X " + String.valueOf(speedTime), screen_width - 150, 40, paint); //��������
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }

    // �����ƶ����߼�����
    public void viewLogic() {
        if (bg_y > bg_y2) {
            bg_y += 10;
            bg_y2 = bg_y - background.getHeight();
        } else {
            bg_y2 += 10;
            bg_y = bg_y2 - background.getHeight();
        }
        if (bg_y >= background.getHeight()) {
            bg_y = bg_y2 - background.getHeight();
        } else if (bg_y2 >= background.getHeight()) {
            bg_y2 = bg_y - background.getHeight();
        }
    }

    // ������Ϸ�����ķ���
    public void addGameScore(int score) {
        middlePlaneScore += score;    // ���͵л��Ļ���
        bigPlaneScore += score;        // ���͵л��Ļ���
        sumScore += score;            // ��Ϸ�ܵ÷�
    }

    // �߳����еķ���
    @Override
    public void run() {
        while (threadFlag) {
            long startTime = System.currentTimeMillis();
            initObject();
            drawSelf();
            viewLogic();        //�����ƶ����߼�
            long endTime = System.currentTimeMillis();
            if (!isPlay) {
                synchronized (thread) {
                    try {
                        thread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                if (endTime - startTime < 100)
                    Thread.sleep(100 - (endTime - startTime));
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message message = new Message();
        message.what = ConstantUtil.TO_END_VIEW;
        message.arg1 = Integer.valueOf(sumScore);
        mainActivity.getHandler().sendMessage(message);
    }

    public void playSound(int key) {
        sounds.playSound(key, 0);
    }

    public int getSumScore(){return sumScore;}
}
