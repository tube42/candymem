package se.tube42.kidsmem.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.control.*;

// TileSprite is a single candy that is either hidden or shown or animatng
// between the two
public class TileSprite
extends SpriteItem
implements TweenListener
{
    public static final int
          STATE_HIDDEN = 1,
          STATE_MATCHED = 2,
          STATE_SHOWN = 3,
          STATE_ANIM_SHOW = 4
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

    public TweenNode animShow(boolean visible)
    {
		final float pause = 0.1f;
		final float duration = 0.5f;

        final TweenEquation eq1 = visible ? TweenEquation.BACK_OUT : null;
        final float p = pause + RandomService.get(0, 0.1f);
        final float d = duration + RandomService.get(0, 0.1f);

        pause(ITEM_A, visible ? 0 : 1, p)
              .tail(visible ? 1 : 0).configure(d, null);
        return pause(ITEM_S, visible ? 0 : 1, p)
              .tail(visible ? 1 : 0).configure(d * 1.2f, eq1);
    }

	public TweenNode animFlip(boolean show)
	{
		pause(BaseItem.ITEM_A, 1f, 0.3f)
			.tail(0.4f).configure(0.06f, null)
			.pause(0.2f)
			.tail(1.0f).configure(0.1f, null)
			;

		return set(BaseItem.ITEM_S, 1.0f, 1.1f).configure(0.3f, null)
			.tail(0.2f).configure(0.1f, null)
			.finish(this, show ? TileSprite.STATE_SHOWN : TileSprite.STATE_HIDDEN)
			.tail(1.2f).configure(0.20f, null)
			.tail(1.0f).configure(0.10f, null)
			;
	}

	public TweenNode match()
	{
		final float r = RandomService.get(0.01f, 0.25f);
		pause(BaseItem.ITEM_S, 1f, r)
			.tail(1.1f).configure(0.14f, null)
			.tail(0.3f).configure(0.3f, null)
			.finish(this, TileSprite.STATE_MATCHED)
			.tail(1.2f).configure(0.2f, null)
			.tail(1f).configure(0.1f, null);

		return pause(BaseItem.ITEM_A, 1f, r + 0.4f)
			.tail(0.5f).configure(0.04f, null)
			.tail(1.0f).configure(0.05f, null)
			;
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

        sb.draw(tr,
                x + addx * w * 1 + 0.5f,
                y + addy * h * 1 + 0.5f,
                w2, h2, w, h, s, s, r);
    }

    protected void draw_texture(SpriteBatch sb, TextureRegion texture)
    {
        final float s = getScale();
        final float x = getX();
        final float y = getY();
        final float r = getRotation();
        final float w2 = w / 2;
        final float h2 = h / 2;

        sb.draw(texture,
                x + 0.5f, y + 0.5f,
                w2, h2, w, h, s, s, r);
    }

    // ---------------------------------------------------
    // TweenListener
    public void onFinish(Item item, int index, int msg)
    {
        state = msg;
    }
}
