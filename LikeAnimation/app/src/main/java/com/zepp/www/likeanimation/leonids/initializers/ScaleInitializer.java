package com.zepp.www.likeanimation.leonids.initializers;

import com.zepp.www.likeanimation.leonids.Particle;
import java.util.Random;

/**
 * Created by xubinggui on 12/27/15.
 */
public class ScaleInitializer implements ParticleInitializer {
    private float mMaxScale;
    private float mMinScale;

    public ScaleInitializer(float minScale, float maxScale) {
        mMinScale = minScale;
        mMaxScale = maxScale;
    }

    @Override public void initParticle(Particle p, Random r) {
        p.mScale = r.nextFloat() * (mMaxScale - mMinScale) + mMinScale;
    }
}
