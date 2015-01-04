package se.tube42.kidsmem.scene;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.service.*;
import se.tube42.kidsmem.logic.*;
import se.tube42.kidsmem.item.*;

import static se.tube42.kidsmem.data.Constants.*;

public class BackgroundScene
extends Scene
implements TweenListener
{
    private static final int
          COUNT = 12,
          PCOUNT = 6
          ;

    private Layer player;
    private ParticleItem [] pis;
    private int pis_cnt;

    private SpriteItem [] candies;
    private int sw, sh, size;

    public BackgroundScene()
    {
    	super("bg");

        // candies
    	candies = new SpriteItem[COUNT];
    	for(int i = 0; i < candies.length; i++) {
            final float r = ServiceProvider.getRandom(0, 359);
            final float s = ServiceProvider.getRandom(0.2f, 1.4f);
            candies[i] = new SpriteItem(Assets.reg_candy2, i % Assets.reg_candy2.length);
            candies[i].setRotation(r);
            candies[i].setScale(s);
    	}
    	positionAll();

    	getLayer(0).add(candies);

        // top
        Items.top = new TopBar();
        Items.top.hide();

        getLayer(1).add(Items.top);

        // particles
        pis_cnt = 0;
        pis = new ParticleItem[PCOUNT];
    	for(int i = 0; i < pis.length; i++)
            pis[i] = new ParticleItem();

        player = getLayer(2);
        player.add(pis);



    }

    public void resize(int sw, int sh)
    {
    	super.resize(sw, sh);
    	this.sw = sw;
    	this.sh = sh;

        size = Math.min(sw, sh) / 8;


        for(int i = 0; i < COUNT; i++)
            candies[i].setSize(size, size);
    	positionAll();
    }

    // --------------------------------------------------

    private void show_explosion(BaseItem si)
    {
        ParticleItem pi = pis[pis_cnt];
        pis_cnt = (pis_cnt + 1) % pis.length;


        pi.setPosition(si.getX() + si.getW() / 2,
                  si.getY() + si.getH() / 2 );

        pi.setPosition(0.2f, pi.getX(), pi.getY() + si.getH() / 3 );

        pi.emit(Assets.pep_explode);
    }

    // --------------------------------------------------


    private void positionAll()
    {
        for(int i = 0; i < COUNT; i++)
            position(candies[i]);
    }

    private void position(SpriteItem si)
    {
        final float y0 = - size * 4;
        final float y1 = sh + size * 2;
        final float p = ServiceProvider.getRandom(0, 1.5f);
    	final float tr = ServiceProvider.getRandom(0.8f, 1.2f);
    	final float t = 0.07f * sh / (0.1f + si.getScale() * tr);
        final float a = ServiceProvider.getRandom(0.1f, 0.4f);

        si.set(BaseItem.ITEM_A, a);

    	si.pause(BaseItem.ITEM_Y, y0, p)
              .tail(y1).configure(t, null).finish(this, 0);


        final float x0 = size / 2;
        final float x1 = sw - size / 2;
    	final float x = ServiceProvider.getRandom(x0, x1);
    	si.setImmediate(BaseItem.ITEM_X, x);


    	final float r0 = ServiceProvider.getRandom(0, 180);
    	final float r1 = ServiceProvider.getRandom(120, 180) + r0;

    	si.pause(BaseItem.ITEM_R, r0, p).tail(r1).configure(t, null);
    }

    private SpriteItem get_at(int x, int y)
    {
        for(int i = 0; i < COUNT; i++)
            if(candies[i].hit(x, y))
                return candies[i];

        return null;
    }

    public boolean touch(int x, int y, boolean down, boolean drag)
    {
        if(down && !drag) {
            final SpriteItem si = get_at(x, y);

            if(si != null) {
                show_explosion(si);
                si.removeTween(BaseItem.ITEM_A, false);
                si.set(BaseItem.ITEM_A, 0f).configure(0.3f, null).finish(this, 0);
            }
        }

        return false;
    }

    public void onFinish(Item item, int index, int msg)
    {
    	position( (SpriteItem) item);
    }

}

