package com.hengtong.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;


public class VerticalScrollView extends ScrollView
{
	private boolean canScroll;
	private GestureDetector mGestureDetector;
	
	@SuppressWarnings("deprecation")
	public VerticalScrollView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		mGestureDetector = new GestureDetector(new YScrollDetector());
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) 
	{
		if(ev.getAction() == MotionEvent.ACTION_UP)
		{
			canScroll = true;
		}
		//return super.onInterceptTouchEvent(ev);
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}
	class YScrollDetector extends SimpleOnGestureListener 
	{
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) 
		{
			if(canScroll)
			{
				if (Math.abs(distanceY) >= Math.abs(distanceX))
				{
					canScroll = true;
				}
				else
				{
					canScroll = false;
				}
			}
			return canScroll;
			//return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}
}
