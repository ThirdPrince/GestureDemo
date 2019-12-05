package com.android.gesture.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.android.gesture.R;


public class LockIndicator extends View {
	private int numRow = 3;	// ��
	private int numColum = 3; // ��
	private int patternWidth = 40;
	private int patternHeight = 40;
	private int f = 5;
	private int g = 5;
	private int strokeWidth = 3;
	private Paint paint = null;
	private Drawable patternNoraml = null;
	private Drawable patternPressed = null;
	private String lockPassStr; // ��������

	public LockIndicator(Context paramContext) {
		super(paramContext);
	}

	public LockIndicator(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet, 0);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.STROKE);
		patternNoraml = getResources().getDrawable(R.drawable.more_ss_small);
		patternPressed = getResources().getDrawable(R.drawable.more_ss_small_on);
		if (patternPressed != null) {
			patternWidth = patternPressed.getIntrinsicWidth();
			patternHeight = patternPressed.getIntrinsicHeight();
			this.f = (patternWidth / 4);
			this.g = (patternHeight / 4);
			patternPressed.setBounds(0, 0, patternWidth, patternHeight);
			patternNoraml.setBounds(0, 0, patternWidth, patternHeight);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if ((patternPressed == null) || (patternNoraml == null)) {
			return;
		}
		for (int i = 0; i < numRow; i++) {
			for (int j = 0; j < numColum; j++) {
				paint.setColor(-16777216);
				int i1 = j * patternHeight + j * this.g;
				int i2 = i * patternWidth + i * this.f;
				canvas.save();
				canvas.translate(i1, i2);
				String curNum = String.valueOf(numColum * i + (j + 1));
				if (!TextUtils.isEmpty(lockPassStr)) {
					if (lockPassStr.indexOf(curNum) == -1) {
						patternNoraml.draw(canvas);
					} else {
						patternPressed.draw(canvas);
					}
				} else {
					patternNoraml.draw(canvas);
				}
				canvas.restore();
			}
		}
	}

	@Override
	protected void onMeasure(int paramInt1, int paramInt2) {
		if (patternPressed != null)
			setMeasuredDimension(numColum * patternHeight + this.g
					* (-1 + numColum), numRow * patternWidth + this.f
					* (-1 + numRow));
	}

	/**
	 * �������»���
	 * @param paramString ���������ַ�����
	 */
	public void setPath(String paramString) {
		lockPassStr = paramString;
		invalidate();
	}
}