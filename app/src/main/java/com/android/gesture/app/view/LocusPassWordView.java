package com.android.gesture.app.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.android.gesture.R;
import com.android.gesture.app.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocusPassWordView extends View {

	private Context context ;

	private float width = 0;
	private float height = 0;

	//
	private boolean isCache = false;
	//
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	//
	private Point[][] mPoints = new Point[3][3];
	//
	private float dotRadius = 0;
	//
	private List<Point> sPoints = new ArrayList<Point>();
	private boolean checking = false;
	private long CLEAR_TIME = 1000;
	private int pwdMaxLen = 9;
	private int pwdMinLen = 4;
	private boolean isTouch = true;
	
	private Paint arrowPaint;
	private Paint linePaint;
	private Paint selectedPaint;
	private Paint errorPaint;
	private Paint normalPaint;
	private Paint pointPaint;
	private int errorColor = 0xffff695e;
	//选择颜色值
	private int selectedColor = 0xFF0090ff;
	//图案颜色值
	private int outterSelectedColor = 0xFF0090ff;
	private int outterErrorColor = 0xffff695e;
	private int dotColor = 0xff9097b3;
	private int outterDotColor = 0xff9097b3;
//	private int dotColor = 0xffffffff;
//	private int outterDotColor = 0xffffffff;

	public LocusPassWordView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public LocusPassWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public LocusPassWordView(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (!isCache) {
			initCache();
		}
		drawToCanvas(canvas);
	}

	private void drawToCanvas(Canvas canvas) {
		boolean inErrorState = false;
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (p.state == Point.STATE_CHECK) {
					selectedPaint.setColor(outterSelectedColor);  
					canvas.drawCircle(p.x, p.y, dotRadius, selectedPaint);
					pointPaint.setColor(selectedColor);
					canvas.drawCircle(p.x, p.y, dotRadius/4, pointPaint);
//					canvas.drawPoint(p.x, p.y, pointPaint);
				} else if (p.state == Point.STATE_CHECK_ERROR) {
					inErrorState = true;
					errorPaint.setColor(outterErrorColor);
					canvas.drawCircle(p.x, p.y, dotRadius, errorPaint);
					pointPaint.setColor(errorColor);
					canvas.drawCircle(p.x, p.y, dotRadius/4, pointPaint);
//					canvas.drawPoint(p.x, p.y, pointPaint);
				} else {
					normalPaint.setColor(dotColor);
					canvas.drawCircle(p.x, p.y, dotRadius, normalPaint);
					normalPaint.setColor(outterDotColor);
//					canvas.drawCircle(p.x, p.y, dotRadius/4, normalPaint);
				}
			}
		}
		
		if (inErrorState) {
			arrowPaint.setColor(errorColor);
			linePaint.setColor(errorColor);
		} else {
			arrowPaint.setColor(selectedColor);
			linePaint.setColor(selectedColor);
		}

		if (sPoints.size() > 0) {
			int tmpAlpha = mPaint.getAlpha();
			Point tp = sPoints.get(0);
			for (int i = 1; i < sPoints.size(); i++) {
				Point p = sPoints.get(i);
				drawLine(tp, p, canvas, linePaint);
				drawArrow(canvas, arrowPaint, tp, p, dotRadius/2, 38);
				tp = p;
			}
			if (this.movingNoPoint) {
				drawLine(tp, new Point(moveingX, moveingY, -1), canvas, linePaint);
			}
			mPaint.setAlpha(tmpAlpha);
		}

	}
	
	private void drawLine(Point start, Point end, Canvas canvas, Paint paint) {
		double d = MathUtil.distance(start.x, start.y, end.x, end.y,0);
		//线长，线不经过园内
		float rx = (float) ((end.x-start.x) * dotRadius / d);
		float ry = (float) ((end.y-start.y) * dotRadius / d);
		canvas.drawLine(start.x+rx, start.y+ry, end.x-rx, end.y-ry, paint);
	}
	
	private void drawArrow(Canvas canvas, Paint paint, Point start, Point end, float arrowHeight, int angle) {
		double d = MathUtil.distance(start.x, start.y, end.x, end.y);
		float sin_B = (float) ((end.x - start.x) / d);
		float cos_B = (float) ((end.y - start.y) / d);
		float tan_A = (float) Math.tan(Math.toRadians(angle));
		float h = (float) (d - arrowHeight - dotRadius * 1.1);
		float l = arrowHeight * tan_A;
		float a = l * sin_B;
		float b = l * cos_B;
		float x0 = h * sin_B;
		float y0 = h * cos_B;
		float x1 = start.x + (arrowHeight*3/2) * sin_B;
		float y1 = start.y + (arrowHeight*3/2) * cos_B;
		//调节箭头大小
		float x2 = (float) (start.x + x0 - b - sin_B*(d - arrowHeight*2 - dotRadius));
		float y2 = (float) (start.y + y0 + a - cos_B*(d - arrowHeight*2 - dotRadius));
		float x3 = (float) (start.x + x0 + b - sin_B*(d - arrowHeight*2 - dotRadius));
		float y3 = (float) (start.y + y0 - a - cos_B*(d - arrowHeight*2 - dotRadius));
		Path path = new Path();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void initCache() {
		width = this.getWidth();
		height = this.getHeight();
		float x = 0;
		float y = 0;

		if (width > height) {
			x = (width - height) / 2;
			width = height;
		} else {
			y = (height - width) / 2;
			height = width;
		}
		
		int leftPadding = 34;
		float dotPadding = width / 3 - leftPadding;
		float middleX = width / 2;
		float middleY = height / 2;

		mPoints[0][0] = new Point(x + middleX - dotPadding, y + middleY - dotPadding, 1);
		mPoints[0][1] = new Point(x + middleX, y + middleY - dotPadding, 2);
		mPoints[0][2] = new Point(x + middleX + dotPadding, y + middleY - dotPadding, 3);
		mPoints[1][0] = new Point(x + middleX - dotPadding, y + middleY, 4);
		mPoints[1][1] = new Point(x + middleX, y + middleY, 5);
		mPoints[1][2] = new Point(x + middleX + dotPadding, y + middleY, 6);
		mPoints[2][0] = new Point(x + middleX - dotPadding, y + middleY + dotPadding, 7);
		mPoints[2][1] = new Point(x + middleX, y + middleY + dotPadding, 8);
		mPoints[2][2] = new Point(x + middleX + dotPadding, y + middleY + dotPadding, 9);
		
		Log.d("jerome", "canvas width:"+width);
		//圆圈的大小
		dotRadius = width / 10;
 		isCache = true;
 		
 		initPaints();
	}
	
	private void initPaints() {
		arrowPaint = new Paint();
 		arrowPaint.setColor(selectedColor);
 		arrowPaint.setStyle(Style.FILL);
 		arrowPaint.setAntiAlias(true);
 		
 		linePaint = new Paint();
 		linePaint.setColor(selectedColor);
		linePaint.setStyle(Style.STROKE);
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(dotRadius / 18);
		
		selectedPaint = new Paint();
		selectedPaint.setStyle(Style.STROKE);
		selectedPaint.setAntiAlias(true); 
		selectedPaint.setStrokeWidth(dotRadius / 18);
		
		errorPaint = new Paint();
		errorPaint.setStyle(Style.STROKE);
		errorPaint.setAntiAlias(true); 
		errorPaint.setStrokeWidth(dotRadius / 18);
		
		normalPaint = new Paint();
		normalPaint.setStyle(Style.STROKE);
		normalPaint.setAntiAlias(true); 
		normalPaint.setStrokeWidth(dotRadius / 18); 
		
		pointPaint = new Paint();
		pointPaint.setStyle(Style.FILL);
		pointPaint.setAntiAlias(true); 
		pointPaint.setStrokeWidth(dotRadius / 4); 
	}

	/**
	 *
	 * 
	 * @param index
	 * @return
	 */
	public int[] getArrayIndex(int index) {
		int[] ai = new int[2];
		ai[0] = index / 3;
		ai[1] = index % 3;
		return ai;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	private Point checkSelectPoint(float x, float y) {
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (p == null) {
					return null;
				}
				if (MathUtil.checkInRound(p.x, p.y, dotRadius, (int) x, (int) y)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 *
	 */
	private void reset() {
		for (Point p : sPoints) {
			p.state = Point.STATE_NORMAL;
		}
		sPoints.clear();
		this.enableTouch();
	}

	/**
	 *
	 * 
	 * @param p
	 * @return
	 */
	private int crossPoint(Point p) {
		// reset
		if (sPoints.contains(p)) {
			if (sPoints.size() > 2) {
				//
				if (sPoints.get(sPoints.size() - 1).index != p.index) {
					return 2;
				}
			}
			return 1; //
		} else {
			return 0; //
		}
	}

	/**
	 *
	 * 
	 * @param point
	 */
	private void addPoint(Point point) {
		if (sPoints.size() > 0) {
			Point lastPoint = sPoints.get(sPoints.size() - 1);
			int dx = Math.abs(lastPoint.getColNum() - point.getColNum());
			int dy = Math.abs(lastPoint.getRowNum() - point.getRowNum());
			if ((dx > 1 || dy > 1) && (dx == 0 || dy == 0 || dx == dy)) {
//			if ((dx > 1 || dy > 1) && (dx != 2 * dy) && (dy != 2 * dx)) {
				int middleIndex = (point.index + lastPoint.index) / 2 - 1;
				Point middlePoint = mPoints[middleIndex / 3][middleIndex % 3];
				if (middlePoint.state != Point.STATE_CHECK) {
					middlePoint.state = Point.STATE_CHECK;
					sPoints.add(middlePoint);
				}
			}
		}
		this.sPoints.add(point);
	}

	/**
	 *
	 * 
	 * @param points
	 * @return
	 */
	private String toPointString() {
		if (sPoints.size() >= pwdMinLen && sPoints.size() <= pwdMaxLen) {
			StringBuffer sf = new StringBuffer();
			for (Point p : sPoints) {
				sf.append(p.index);
			}
			return sf.toString();
		} else {
			return "";
		}
	}

	boolean movingNoPoint = false;
	float moveingX, moveingY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//
		if (!isTouch) {
			return false;
		}

		movingNoPoint = false;

		float ex = event.getX();
		float ey = event.getY();
		boolean isFinish = false;
		boolean redraw = false;
		Point p = null;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: //
			//
			if (task != null) {
				task.cancel();
				task = null;
				Log.d("task", "touch cancel()");
			}
			//
			reset();
			p = checkSelectPoint(ex, ey);
			if (p != null) {
				checking = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (checking) {
				p = checkSelectPoint(ex, ey);
				if (p == null) {
					movingNoPoint = true;
					moveingX = ex;
					moveingY = ey;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			p = checkSelectPoint(ex, ey);
			checking = false;
			isFinish = true;
			break;
		}
		if (!isFinish && checking && p != null) {

			int rk = crossPoint(p);
			if (rk == 2) //
			{
				// reset();
				// checking = false;

				movingNoPoint = true;
				moveingX = ex;
				moveingY = ey;

				redraw = true;
			} else if (rk == 0) //
			{
				p.state = Point.STATE_CHECK;
				addPoint(p);
				redraw = true;
			}
			// rk == 1

		}

		//
		if (redraw) {

		}
		if (isFinish) {
			/*if (WatchDogMySelfActivity.OPENGESTRUE) {
				if (this.sPoints.size() == 0 ) {
					this.reset();
				}else if (mCompleteListener != null) {
					this.disableTouch();
					mCompleteListener.onComplete(toPointString());
				}
			}else*/{
				if (this.sPoints.size() == 1 || this.sPoints.size() == 0 ) {
					this.reset();
				} else if (sPoints.size() < pwdMinLen || sPoints.size() > pwdMaxLen) {
					// mCompleteListener.onPasswordTooMin(sPoints.size());
					error();
					clearPassword();
					Toast.makeText(this.getContext(), context.getString(R.string.not_enough_length_pwd),
							Toast.LENGTH_SHORT).show();
				} else if (mCompleteListener != null) {
					this.disableTouch();
					mCompleteListener.onComplete(toPointString());
				}
			}
		}
		this.postInvalidate();
		return true;
	}

	/**
	 *
	 */
	private void error() {
		for (Point p : sPoints) {
			p.state = Point.STATE_CHECK_ERROR;
		}
	}

	public void markError() {
		markError(CLEAR_TIME);
	}

	public void markError(final long time) {
		for (Point p : sPoints) {
			p.state = Point.STATE_CHECK_ERROR;
		}
		this.clearPassword(time);
	}

	public void enableTouch() {
		isTouch = true;
	}

	public void disableTouch() {
		isTouch = false;
	}

	private Timer timer = new Timer();
	private TimerTask task = null;


	public void clearPassword() {
		clearPassword(CLEAR_TIME);
	}

	public void clearPassword(final long time) {
		if (time > 1) {
			if (task != null) {
				task.cancel();
				Log.d("task", "clearPassword cancel()");
			}
			postInvalidate();
			task = new TimerTask() {
				public void run() {
					reset();
					postInvalidate();
				}
			};
			Log.d("task", "clearPassword schedule(" + time + ")");
			timer.schedule(task, time);
		} else {
			reset();
			postInvalidate();
		}

	}

	//
	private OnCompleteListener mCompleteListener;

	/**
	 * @param mCompleteListener
	 */
	public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
		this.mCompleteListener = mCompleteListener;
	}

	public interface OnCompleteListener {
		
		public void onComplete(String password);
	}
}
