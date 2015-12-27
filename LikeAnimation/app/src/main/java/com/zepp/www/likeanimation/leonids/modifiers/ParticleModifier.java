package com.zepp.www.likeanimation.leonids.modifiers;

import com.zepp.www.likeanimation.leonids.Particle;

/**
 * Created by xubinggui on 12/27/15.
 */
public interface ParticleModifier {
    /**
     * modifies the specific value of a particle given the current miliseconds
     * @param particle
     * @param miliseconds
     */
    void apply(Particle particle, long miliseconds);
}
