#项目迁移 项目迁移 项目迁移 重要的事情说三遍，这边的代码不会更新请到以下地址。
[完整代码 GitHub](https://github.com/samwangds/SamAndroidLibrary/blob/master/lib/src/main/java/com/sam/lib/widget/listener/OnClickEffectTouchListener.java)

![效果展示](http://upload-images.jianshu.io/upload_images/1181400-de634b59356a54f5.gif?imageMogr2/auto-orient/strip)



#思路
通过监听`onTouch`方法
* 在`MotionEvent.ACTION_DOWN`执行view变小的动画
`v.animate().scaleX(scale).scaleY(scale).setDuration(duration).setInterpolator(interpolator);`
* 在`MotionEvent.ACTION_CANCEL`和`MotionEvent.ACTION_UP`时执行还原的动画
`v.animate().scaleX(1).scaleY(1).setInterpolator(interpolator);`
* 同时需要注意在`onTouch`方法 `return true;`及处理VIEW的`pressed state`

#核心代码
```
 @Override
 public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.animate().scaleX(scale).scaleY(scale).setDuration(duration).setInterpolator(interpolator);
                v.setPressed(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < v.getWidth() && y > 0 && y < v.getHeight());
                if (v.isPressed() != isInside) {
                    v.setPressed(isInside);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                v.setPressed(false);
                v.animate().scaleX(1).scaleY(1).setInterpolator(interpolator);
                break;
            case MotionEvent.ACTION_UP:
                v.animate().scaleX(1).scaleY(1).setInterpolator(interpolator);
                if (v.isPressed()) {
                    v.performClick();
                    v.setPressed(false);
                }
                break;
        }
        return true;
  }
```

#调用方法
```
  ImageView iv  = (ImageView) findViewById(R.id.iv_test);
  TextView tv  = (TextView) findViewById(R.id.tv_test);

  OnClickAnimTouchListener clickAnim = new OnClickAnimTouchListener();
  iv.setOnTouchListener(clickAnim);
  tv.setOnTouchListener(clickAnim);
```

#update: 2016.01.15 做了下重构，顺便把代码迁移到我自己的Lib工程，并上传Jcenter以便以后使用
* 把效果部分抽取成一个接口
 ```
public interface ViewClickEffect {  
  /**     * 按下去的效果     */    
void onPressedEffect(View view);    
/**     * 释放的效果     * @param view     */   
 void onUnPressedEffect(View view);}
```
* 这样我们之前实现的放大缩小效果就变成了 [完整代码](https://github.com/samwangds/SamAndroidLibrary/blob/master/lib/src/main/java/com/sam/lib/impl/DefaultClickEffectScaleAnimate.java)
```
@Override
public void onPressedEffect(View view) {
    view.animate().scaleX(scale).scaleY(scale).setDuration(duration).setInterpolator(interpolator);
}
@Overridepublic void onUnPressedEffect(View view) {
    view.animate().scaleX(1).scaleY(1).setInterpolator(interpolator);
}
```
再顺手写一个点击时改变透明度的 [完整代码](https://github.com/samwangds/SamAndroidLibrary/blob/master/lib/src/main/java/com/sam/lib/impl/DefaultClickEffectTranslucence.java)
```
@Override
public void onPressedEffect(View view) {
    view.animate().alpha(scale).setDuration(duration).setInterpolator(interpolator);
}
@Override
public void onUnPressedEffect(View view) {
    view.animate().alpha(1).setDuration(duration).setInterpolator(interpolator);
}
```
* 这时候我们在`OnClickEffectTouchListener`：)改个了名捏 里面增加一个变量，外部可以传`ViewClickEffect`接口的实现，或者以上两个默认实现 
```
private ViewClickEffect mViewClickEffect = new DefaultClickEffectScaleAnimate();
public void setViewClickEffect(ViewClickEffect viewClickEffect) {    
  mViewClickEffect = viewClickEffect;
}

 @Override
 public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mViewClickEffect.onPressedEffect(v);
                v.setPressed(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < v.getWidth() && y > 0 && y < v.getHeight());
                if (v.isPressed() != isInside) {
                    v.setPressed(isInside);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mViewClickEffect.onUnPressedEffect(v);
                v.setPressed(false);
                break;
            case MotionEvent.ACTION_UP:
                mViewClickEffect.onUnPressedEffect(v);
                if (v.isPressed()) {
                    v.performClick();
                    v.setPressed(false);
                }
                break;
        }
        return true;
  }
```

* 好了，收工。
 
[完整代码 GitHub](https://github.com/samwangds/SamAndroidLibrary/blob/master/lib/src/main/java/com/sam/lib/widget/listener/OnClickEffectTouchListener.java)