
package se.tube42.kidsmem.item;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.service.*;

public class TileSprite
extends SpriteItem
implements TweenListener
{
    public static final int
          STATE_HIDDEN = 1,
          STATE_MATCHED = 2,
          STATE_SHOWN = 3,
          STATE_ANIM_SHOW = 4,
          STATE_ANIM_HIDE = 5
          ;

    public TextureRegion []candy;
    public int id;

    public TileSprite(TextureRegion [] tiles, TextureRegion [] candy)
    {
        super(tiles, 0);
        this.w = tiles[0].getRegionWidth();
        this.h = tiles[0].getRegionHeight();
        this.candy = candy;
    }


    // ---------------------------------------------------

    public TweenNode setShow(boolean visible, float pause, float duration)
    {
        final float v0 = visible ? 0 : 1;
        final float v1 = visible ? 1 : 0;
        final TweenEquation eq1 = visible ? TweenEquation.BACK_OUT : null;

        final float p = pause + ServiceProvider.getRandom(0, 0.1f);
        final float d = duration + ServiceProvider.getRandom(0, 0.1f);


        pause(ITEM_A, v0, p)
              .tail(v1).configure(d, null);
        return pause(ITEM_S, v0, p)
              .tail(v1).configure(d * 1.2f, eq1);
    }


    // ---------------------------------------------------

    public void draw(SpriteBatch sb)
    {
        final float a = getAlpha();
        final int s = getState();

        switch(s) {
        case STATE_ANIM_SHOW:
        case STATE_HIDDEN:
            sb.setColor( cr, cg, cb, a);
            draw_texture(sb, textures[0]);
            break;

        case STATE_ANIM_HIDE:
        case STATE_SHOWN:
            sb.setColor( 1, 1, 1, a);
            draw_texture(sb, candy[id]);
            break;

        case STATE_MATCHED:
            sb.setColor( cr, cg, cb, a);
            draw_texture(sb, textures[1]);

            sb.setColor( 1, 1, 1, a);
            draw_texture(sb, candy[id], 0.4f, -0.07f, 0.35f);
            break;
        }
    }




    protected void draw_texture(SpriteBatch sb, TextureRegion tr,
              float s2, float addx, float addy)
    {
        final float s = getScale() * s2;
        final float x = getX();
        final float y = getY();
        final float r = getRotation();
        final float w2 = s * w / 2;
        final float h2 = s * h / 2;
        final float hp = UI.halfpixel;

        sb.draw(tr,
                x + addx * w * 1 + hp,
                y + addy * h * 1 + hp,
                w2, h2,
                w, h,
                s, s, r);
    }

    protected void draw_texture(SpriteBatch sb, TextureRegion texture)
    {
        final float s = getScale();
        final float x = getX();
        final float y = getY();
        final float r = getRotation();
        final float w2 = w / 2;
        final float h2 = h / 2;
        final float hp = UI.halfpixel;

        sb.draw(texture,
                x + hp, y + hp,
                w2, h2,
                w, h,
                s, s, r);
    }


    // ---------------------------------------------------
    // TweenListener
    public void onFinish(Item item, int index, int msg)
    {
        state = msg;
    }
}
