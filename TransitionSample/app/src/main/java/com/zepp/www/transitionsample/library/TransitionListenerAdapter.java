package com.zepp.www.transitionsample.library;

import android.transition.Transition;

/**
 * Utility adapter class to avoid having to override all three methods
 * whenever someone just wants to listen for a single event.
 *
 * @hide
 */
public class TransitionListenerAdapter implements Transition.TransitionListener {
    @Override public void onTransitionStart(Transition transition) {
    }

    @Override public void onTransitionEnd(Transition transition) {
    }

    @Override public void onTransitionCancel(Transition transition) {
    }

    @Override public void onTransitionPause(Transition transition) {
    }

    @Override public void onTransitionResume(Transition transition) {
    }
}