package com.example.demo.component;

import com.example.demo.voz.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class ActionImageButton extends ImageButton {

	public ActionImageButton(Context context) {
		super(context);
		this.setOnTouchListener(new OnTouchListenerCahngeBackground());
		this.setOnLongClickListener(new OnLongClickListenerShowContent());
	}

	public ActionImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnTouchListener(new OnTouchListenerCahngeBackground());
		this.setOnLongClickListener(new OnLongClickListenerShowContent());
	}

	public ActionImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnTouchListener(new OnTouchListenerCahngeBackground());
		this.setOnLongClickListener(new OnLongClickListenerShowContent());
	}

	class OnLongClickListenerShowContent implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			Toast.makeText(ActionImageButton.this.getContext(),
					ActionImageButton.this.getContentDescription(),
					Toast.LENGTH_SHORT).show();
			return true;
		}

	}

	class OnTouchListenerCahngeBackground implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				ActionImageButton.this
						.setBackgroundResource(com.example.demo.voz.R.color.url_color);
				return false;
			}
			case MotionEvent.ACTION_UP: {
				ActionImageButton.this
						.setBackgroundResource(R.color.black_overlay);
				return false;
			}
			}
			return false;
		}

	}

}
