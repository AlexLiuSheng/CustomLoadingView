# CustomLoadingView
a loadingview that extends a surfaceview
  
  公司项目的一个需求，一个比较简单的自定义动画加载，试用surfaceview绘制，surfaceview的双缓存机制绘制效率非常高。
  
  效果图，真机上运行更流畅
  
 ![image](https://github.com/AlexLiuSheng/CustomLoadingView/blob/master/s.gif)
 
 使用：
 
         view.setMinRadius(20);
         view.setMaxRadius(50);
         view.setBallOneColor(getResources().getColor(R.color.colorAccent));
         view.setCenterBallColor(getResources().getColor(R.color.colorAccent));
         view.setBallTwoColor(getResources().getColor(R.color.colorAccent));
        
  核心代码:
   
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
