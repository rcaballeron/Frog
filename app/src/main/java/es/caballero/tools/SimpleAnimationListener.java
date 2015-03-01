package es.caballero.tools;

import android.view.animation.Animation;

/**
 * Created by rcaballeron on 01.03.2015.
 */
public abstract class SimpleAnimationListener implements Animation.AnimationListener {
    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {}

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
