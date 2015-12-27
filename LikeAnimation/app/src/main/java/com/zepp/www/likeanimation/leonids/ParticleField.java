package com.zepp.www.likeanimation.leonids;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class ParticleField extends View {

	private List<Particle> mParticles;

	private final AtomicBoolean atomicBoolean = new AtomicBoolean(true);

	public ParticleField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ParticleField(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ParticleField(Context context) {
		super(context);
	}

	public void setParticles(List<Particle> particles) {
		mParticles = particles;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw all the particles
		synchronized (atomicBoolean) {
			for (int i = 0; i < mParticles.size(); i++) {
				mParticles.get(i).draw(canvas);
			}
		}
	}
}