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

>是不是很简单没啥技术含量呢，如果喷，请轻喷
