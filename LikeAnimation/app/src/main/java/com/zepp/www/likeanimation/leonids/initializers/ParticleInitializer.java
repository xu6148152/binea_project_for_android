package com.zepp.www.likeanimation.leonids.initializers;

import com.zepp.www.likeanimation.leonids.Particle;
import java.util.Random;

/**
 * Created by xubinggui on 12/27/15.
 */
public interface ParticleInitializer {
    void initParticle(Particle p, Random r);
}
