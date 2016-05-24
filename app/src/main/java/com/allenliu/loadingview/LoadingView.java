package com.allenliu.loadingview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Allen Liu on 2016/5/20.
 */
public class LoadingView extends SurfaceView {
    private Canvas canvas;
    private Paint centerCirclePaint;
    private Paint leftCirclePaint;
    private Paint rightCirclePaint;
    private int centerColor ;
    private int leftColor;
    private int rightColor ;
    /**
     * 中心点X
     */
    private float centerX;
    /**
     * 中心点Y
     */
    private float centerY;
    /**
     * 开始半径
     */
    private float startRadius;
    private Timer timer;
    /**
     * 最小半径
     */
    private float minRadius;
    /**
     * 小球方向
     */
    private  final int DIRECTION_LEFT=1;
    private final int DIRECTION_RIGHT=2;
    private int leftBallDirection=DIRECTION_LEFT;

    private float delta;
    /**
     * 次数
     */
    private int time=0;
    /**
     * 伸展的次数
     */
    private int expandMaxTime=0;
    /**
     * 小球每次的偏移量
     */
    private float offset=dip2px(1.8f);


    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
   public void setBallOneColor(int color){
       leftColor=color;
       leftCirclePaint.setColor(leftColor);
       invalidate();
   }
    public void setBallTwoColor(int color){
        rightColor=color;
        rightCirclePaint.setColor(rightColor);
        invalidate();
    }
    public void setCenterBallColor(int color){
        centerColor=color;
        invalidate();
    }
    public void setMaxRadius(int radius){
        startRadius=radius;
        invalidate();
    }
    public  void setMinRadius(int radius){
        minRadius=radius;
        invalidate();
    }
    private void init() {
        initPaint();
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                //锁定画布，并循环绘制图形
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            canvas = holder.lockCanvas();
                            if (canvas != null)
                                drawLoadingView();
                            //释放画布
                        } catch (Exception e) {

                        } finally {
                            if (canvas != null)
                                holder.unlockCanvasAndPost(canvas);
                        }


                    }
                };
                if (timer == null)
                    timer = new Timer();
                timer.schedule(task, 0, 10);

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                timer.cancel();
                timer=new Timer();

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        centerX = ((float) (getMeasuredWidth())) / 2f;
        centerY = (float) getMeasuredHeight() / 2f;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        leftColor=Color.parseColor("#FFC45E");
        rightColor=Color.parseColor("#5E9AFF");
        centerColor=Color.parseColor("#FF4081");
        centerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerCirclePaint.setColor(centerColor);
        centerCirclePaint.setStyle(Paint.Style.FILL);
        leftCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        leftCirclePaint.setColor(leftColor);
        leftCirclePaint.setStyle(Paint.Style.FILL);
//        leftCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        rightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rightCirclePaint.setColor(rightColor);
        rightCirclePaint.setStyle(Paint.Style.FILL);
   //     rightCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        startRadius = dip2px(15);
        minRadius=dip2px(5);
        delta=dip2px(0.3f);
    }

    private void drawLoadingView() {
        drawCenterCircle();
    }
    /**
     * 画中心圆
     */
    private void drawCenterCircle() {
        canvas.drawColor(Color.WHITE);
            //小于边界
              if(minRadius+delta*time<=startRadius) {
                  if(leftBallDirection==DIRECTION_LEFT) {
                      //利用默认图像叠加模式实现小球的叠加显示方式
                      canvas.drawCircle(centerX - offset * time, centerY, minRadius + delta * time, leftCirclePaint);
                      canvas.drawCircle(centerX, centerY, startRadius-dip2px(1), centerCirclePaint);
                      canvas.drawCircle(centerX + offset * time, centerY, minRadius + delta * time, rightCirclePaint);
                //从中间到最右边
                  }else{
                      canvas.drawCircle(centerX - offset * time, centerY, minRadius + delta * time, rightCirclePaint);
                      canvas.drawCircle(centerX, centerY, startRadius-dip2px(1), centerCirclePaint);
                      canvas.drawCircle(centerX + offset * time, centerY, minRadius + delta * time, leftCirclePaint);
                  }
                  time=time+1;
                  if(minRadius+delta*time>startRadius){
                      expandMaxTime=time;
                      if(leftBallDirection==DIRECTION_LEFT){
                          leftBallDirection=DIRECTION_RIGHT;;
                      }else{
                          leftBallDirection=DIRECTION_LEFT;
                      }
                  }
            //从最右边到中间
              }else{
                  if(leftBallDirection==DIRECTION_LEFT) {
                      canvas.drawCircle(centerX + expandMaxTime * offset - (time - expandMaxTime) * offset, centerY, startRadius - delta * (time - expandMaxTime), leftCirclePaint);
                      canvas.drawCircle(centerX, centerY, startRadius-dip2px(1), centerCirclePaint);
                      canvas.drawCircle(centerX - expandMaxTime * offset + (time - expandMaxTime) * offset, centerY, startRadius - delta * (time - expandMaxTime), rightCirclePaint);
                 //从左边缩小到中间
                  }else{
                      canvas.drawCircle(centerX + expandMaxTime * offset - (time - expandMaxTime) * offset, centerY, startRadius - delta * (time - expandMaxTime), rightCirclePaint);
                      canvas.drawCircle(centerX, centerY, startRadius-dip2px(1), centerCirclePaint);
                      canvas.drawCircle(centerX - expandMaxTime * offset + (time - expandMaxTime) * offset, centerY, startRadius - delta * (time - expandMaxTime),leftCirclePaint);
                  }
                  time=time+1;
                  if(minRadius+delta*(time-expandMaxTime)>=startRadius) {
                      time=0;
                  }
              }



    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
