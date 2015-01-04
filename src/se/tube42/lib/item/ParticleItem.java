package se.tube42.lib.item;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;


import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;

public class ParticleItem extends BaseItem
{
    protected PooledEffect effect;

    public ParticleItem()
    {
        this.effect = null;
    }


    public PooledEffect emit(ParticleEffectPool pool)
    {
        if(effect != null) {
            stop();
        }

        flags &= ~FLAG_DEAD;

        effect = pool.obtain();
        effect.setPosition(getX(), getY());
        effect.start();
        return effect;
    }

    // -----------------------------------------

    public void draw(SpriteBatch sb)
    {
        if( (flags & FLAG_VISIBLE) == 0) return;

        if(effect != null) {
            final float dt = Gdx.graphics.getDeltaTime();
            effect.setPosition(getX(), getY());
            onUpdate(dt);
            effect.draw(sb);

            if(effect.isComplete()) {
                onParticleEnded();
            }
        }
    }

    /*
    public void release()
    {
        stop();
        super.release();
    }
       */
    public void stop()
    {
        if(effect != null) {
            effect.free();
            effect = null;
            flags |= FLAG_DEAD;
        }
    }

    // --------------------------------------------

    public void onParticleEnded()
    {
        stop();
    }

    public void onUpdate(float dt)
    {
        effect.update(dt);
    }


}
