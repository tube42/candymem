package se.tube42.kidsmem.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Input.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.item.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.control.*;

import static se.tube42.kidsmem.data.Constants.*;

public class Button extends BaseItem
{
    public static final int
          ITEM_S2 = 5
          ;

    private TextureRegion [] icons, shadows;
    private int shadow1, shadow2;
    private int index1, index2;

    private Item tweener;
    private boolean down;

    public Button(TextureRegion []icons, TextureRegion []shadows, int shadow1, int shadow2)
    {
        this.down = false;
        this.icons = icons;
        this.shadows = shadows;
        this.shadow1 = shadow1;
        this.shadow2 = shadow2;
        this.index1 = -1;
        this.index2 = -1;
        this.tweener = new Item(2);

        this.w = icons[0].getRegionWidth();
        this.h = icons[0].getRegionHeight();

        tweener.set(0, 0);
        tweener.set(1, 0);
        setImmediate(ITEM_S2, 1); // initial value for S2
    }

    public void press()
    {
        if(!down) {
            down = true;
            tweener.set(0, 1).configure(0.28f, TweenEquation.CUBE_OUT);
        }
    }

    public void release()
    {
        if(down) {
            down = false;
            tweener.set(0, 0).configure(0.2f, TweenEquation.CUBE_OUT);
        }
    }

    public int getIndex()
    {
        return index1;
    }

    public void animateIndex(int new_index)
    {
        if(new_index != index1) {
            index2 = index1;
            index1 = new_index;
            tweener.set(1, 1, 0).configure(1f, null);
        }
    }

    public void setIndex(int new_index)
    {
        index2 = index1;
        index1 = new_index;
        tweener.setImmediate(1, 0);
    }

    // ------------------------------------------------------

    public void show(float pause, float duration)
    {
        final float p = pause + RandomService.get(0, 0.2f);
        final float t = duration + RandomService.get(0f, 0.1f);

        pause(BaseItem.ITEM_S, 0.5f, p)
              .tail(1f).configure(t, TweenEquation.QUAD_OUT);

        pause(BaseItem.ITEM_A, 0, p)
              .tail(1f).configure(t * 0.8f, null);
    }

    public void hide(float pause, float duration)
    {
        final float p = pause + RandomService.get(0f, 0.2f);
        final float t = duration + RandomService.get(0f, 0.1f);
        final float c = Math.max(p, get(BaseItem.ITEM_A));

        pause(BaseItem.ITEM_A, c, p)
              .tail(0).configure(t, null);
    }

    private static final float fix(float x)
    {
        if( x < 0) return 0;
        if( x > 1) return 1;
        return x;
    }

    public void draw(SpriteBatch sb)
    {
        final float hp = UI.halfpixel;
        final float s = getScale();
//        final float s2 = s * 1.33f;
        final float s2 = get(ITEM_S2) * s * 1.33f;
        final float x = getX() + hp;
        final float y = getY() + hp;
        final float r = getRotation();
        final float w2 = w / 2;
        final float h2 = h / 2;

        final float a = fix( getAlpha() );
        final float sel0 = fix(tweener.get(0));
        final float sel1 = fix(tweener.get(1));



        // shadow animation
        sb.setColor( 1, 1, 1, a);
        sb.draw(shadows[(sel0 < 0.5f)  ? shadow1 : shadow2],
                x, y, w2, h2, w, h, s2, s2, r);

        // icon animation
        if(sel1 < 1 && index1 != -1) {
            sb.setColor( cr, cg, cb, a * (1 - sel1));
            sb.draw(icons[index1],
                    x, y, w2, h2, w, h, s, s, r);
        }

        if(sel1 > 0 && index2 != -1) {
            sb.setColor( cr, cg, cb, a * sel1);
            sb.draw(icons[index2],
                    x, y, w2, h2, w, h, s, s, r);
        }


    }

}
