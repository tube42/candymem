
package se.tube42.kidsmem.item;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.item.*;
import se.tube42.lib.util.*;
import se.tube42.lib.ks.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.service.*;

public class FireworkItem
extends ParticleItem
{
    private MessageListener listener;
    private int msg, data0;

    public FireworkItem()
    {
    }


    public void emit(MessageListener listener, int msg, int data0)
    {
        if(getState() != 0) {
            stop();
        }

        this.listener = listener;
        this.msg = msg;
        this.data0 = data0;

        emit1();
    }

    private void emit1()
    {
        final float ya = -20;
        final float xa = ServiceProvider.getRandom(0, UI.sw);
        final float xb = Math.max(0, Math.min(UI.sw,
                  xa + ServiceProvider.getRandom(0, UI.sw / 3)));
        final float yb = UI.sh * 0.3f + ServiceProvider.getRandom(0, UI.sh * 0.7f);
        final float t = ServiceProvider.getRandom(0.6f, 1.0f);

        setState(0);
        setPosition(xa, ya);
        set(BaseItem.ITEM_X, xb).configure(t, null);
        set(BaseItem.ITEM_Y, yb).configure(t, null);
        emit(Assets.pep_trail);

    }

    private void emit2()
    {
        setState(1);
        emit(Assets.pep_firework);
    }


    public void onParticleEnded()
    {
        super.onParticleEnded();

        switch(getState()) {
        case 0:
            emit2();
            break;
        default:
            setState(0);
            if(listener != null)
                listener.onMessage(msg, data0, this, this);
        }
    }
}
