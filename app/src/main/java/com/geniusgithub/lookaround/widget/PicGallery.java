/*
package com.geniusgithub.lookaround.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;


public class PicGallery extends Gallery {

	private GestureDetector gestureScanner;
	private ImageViewEx imageView;

	public PicGallery(Context context) {
		super(context);

	}

	public PicGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setDetector(GestureDetector dectector) {
		gestureScanner = dectector;
	}

	public PicGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnTouchListener(new OnTouchListener() {

			float baseValue;
			float originalScale;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				View view = PicGallery.this.getSelectedView();
				if (view instanceof ImageViewEx) {
					imageView = (ImageViewEx) view;

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						baseValue = 0;
						originalScale = imageView.getScale();
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						if (event.getPointerCount() == 2) {
							float x = event.getX(0) - event.getX(1);
							float y = event.getY(0) - event.getY(1);
							float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
							// System.out.println("value:" + value);
							if (baseValue == 0) {
								baseValue = value;
							} else {
								float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
								// scale the image
								imageView.zoomTo(originalScale * scale, x
										+ event.getX(1), y + event.getY(1));

							}
						}
					}
				}
				return false;
			}

		});
	}

	float v[] = new float[9];
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		View view = PicGallery.this.getSelectedView();
		if (view instanceof ImageViewEx) {
			
			float xdistance = calXdistance(e1, e2);
			float min_distance = PictureBrowseFragment.screenWidth / 4f;
		
			if (isScrollingLeft(e1, e2) && xdistance > min_distance) {
				kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
			} else if (!isScrollingLeft(e1, e2) && xdistance > min_distance) {
				kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
			}
			
			imageView = (ImageViewEx) view;

			Matrix m = imageView.getImageMatrix();
			m.getValues(v);
			// 图片实时的上下左右坐标
			float left, right;
			// 图片的实时宽，高
			float width = imageView.getScale() * imageView.getImageWidth();
			float height = imageView.getScale() * imageView.getImageHeight();
			
			if ((int) width <= PictureBrowseFragment.screenWidth
					&& (int) height <= PictureBrowseFragment.screenHeight)// 如果图片当前大小<屏幕大小，直接处理滑屏事件
			{
				super.onScroll(e1, e2, distanceX, distanceY);
			} else {
				left = v[Matrix.MTRANS_X];
				right = left + width;
				Rect r = new Rect();
				imageView.getGlobalVisibleRect(r);

				if (distanceX > 0)// 向左滑动
				{
					if (r.left > 0 || right < PictureBrowseFragment.screenWidth) {// 判断当前ImageView是否显示完全
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						imageView.postTranslate(-distanceX, -distanceY);
					}
				} else if (distanceX < 0)// 向右滑动
				{
					if (r.right < PictureBrowseFragment.screenWidth || left > 0) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						imageView.postTranslate(-distanceX, -distanceY);
					}
				}

			}

		} else {
			super.onScroll(e1, e2, distanceX, distanceY);
		}
		return false;
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	private float calXdistance(MotionEvent e1, MotionEvent e2) {
		return Math.abs(e2.getX() - e1.getX());
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Logger.d(DEBUG,"[PicGallery.onTouchEvent]"+"PicGallery.onTouchEvent");
		if (gestureScanner != null) {
			gestureScanner.onTouchEvent(event);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// 判断边界是否越界
			View view = PicGallery.this.getSelectedView();
			if (view instanceof ImageViewEx) {
				
				if(kEvent != KEY_INVALID) { // 是否切换上一页或下一页
					onKeyDown(kEvent, null);
					kEvent = KEY_INVALID;
				}
				
				imageView = (ImageViewEx) view;
				float width = imageView.getScale() * imageView.getImageWidth();
				float height = imageView.getScale()
						* imageView.getImageHeight();
				// Logger.LOG("onTouchEvent", "width=" + width + ",height="
				// + height + ",screenWidth="
				// + PictureViewActivity.screenWidth + ",screenHeight="
				// + PictureViewActivity.screenHeight);
				if ((int) width <= PictureBrowseFragment.screenWidth
						&& (int) height <= PictureBrowseFragment.screenHeight)// 如果图片当前大小<屏幕大小，判断边界
				{
					break;
				}
				float v[] = new float[9];
				Matrix m = imageView.getImageMatrix();
				m.getValues(v);
				float top = v[Matrix.MTRANS_Y];
				float bottom = top + height;
				if (top < 0 && bottom < PictureBrowseFragment.screenHeight) {
//					imageView.postTranslateDur(-top, 200f);
					imageView.postTranslateDur(PictureBrowseFragment.screenHeight
							- bottom, 200f);
				}
				if (top > 0 && bottom > PictureBrowseFragment.screenHeight) {
//					imageView.postTranslateDur(PictureViewActivity.screenHeight
//							- bottom, 200f);
					imageView.postTranslateDur(-top, 200f);
				}
				
				float left =v[Matrix.MTRANS_X];
				float right = left + width;
				if(left<0 && right< PictureBrowseFragment.screenWidth){
//					imageView.postTranslateXDur(-left, 200f);
					imageView.postTranslateXDur(PictureBrowseFragment.screenWidth
							- right, 200f);
				}
				if(left>0 && right>PictureBrowseFragment.screenWidth){
//					imageView.postTranslateXDur(PictureViewActivity.screenWidth
//							- right, 200f);
					imageView.postTranslateXDur(-left, 200f);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}
	
	int kEvent = KEY_INVALID; //invalid
	public static final int KEY_INVALID = -1;
}
*/
