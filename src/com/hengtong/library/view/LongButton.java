package com.hengtong.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class LongButton extends Button
{
	private OnBtnLongClickListener mListener;
	private boolean mLongPress;
	
	public LongButton(Context context) {
		super(context);
		init();
	}
	
	public LongButton(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		init();
	}
	public LongButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
	
	private void init()
	{
		setLongClickable(true);
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mLongPress = true;
				mListener.onLongKeyDown(v);
				return false;
			}
		});
	}
	
	public void setOnBtnLongClickListener(OnBtnLongClickListener listener)
	{
		mListener = listener;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			if(mLongPress)
			{
				mListener.onLongKeyUp(this);
				mLongPress = false;
			}
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			super.onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public interface OnBtnLongClickListener 
	{
		public void onLongKeyDown(View v);
		public void onLongKeyUp(View v);
	}
}
