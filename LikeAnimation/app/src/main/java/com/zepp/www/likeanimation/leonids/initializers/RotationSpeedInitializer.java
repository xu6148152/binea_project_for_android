package com.zepp.www.likeanimation.leonids.initializers;

import com.zepp.www.likeanimation.leonids.Particle;
import java.util.Random;

/**
 * Created by xubinggui on 12/27/15.
 */
public class RotationSpeedInitializer implements ParticleInitializer {
    private float mMinRotationSpeed;
    private float mMaxRotationSpeed;

    public RotationSpeedInitializer(float minRotationSpeed,	float maxRotationSpeed) {
        mMinRotationSpeed = minRotationSpeed;
        mMaxRotationSpeed = maxRotationSpeed;
    }

    @Override public void initParticle(Particle p, Random r) {
        p.mRotationSpeed = r.nextFloat() * (mMaxRotationSpeed - mMinRotationSpeed) + mMinRotationSpeed;
    }
}
