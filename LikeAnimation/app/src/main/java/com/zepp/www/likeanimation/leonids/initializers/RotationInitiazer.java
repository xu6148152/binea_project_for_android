package com.zepp.www.likeanimation.leonids.initializers;

import com.zepp.www.likeanimation.leonids.Particle;
import java.util.Random;

/**
 * Created by xubinggui on 12/27/15.
 */
public class RotationInitiazer implements ParticleInitializer {
    private int mMinAngle;
    private int mMaxAngle;

    public RotationInitiazer(int minAngle, int maxAngle) {
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override public void initParticle(Particle p, Random r) {
        p.mRotationSpeed = r.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
    }
}
