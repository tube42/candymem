package se.tube42.lib.item;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;

public class PatchItem extends BaseItem
{
    protected NinePatch np;

    public PatchItem(NinePatch np)
    {
        this.np = np;
    }

    public void draw(SpriteBatch sb)
    {
        final float a = getAlpha();
        final float s = getScale();
        final float x0 = getX() + w * (1 - s) / 2;
        final float y0 = getY() + h * (1 - s) / 2;

        sb.setColor( cr, cg, cb, a);
        np.draw((Batch)sb, x0, y0, w * s, h * s);
    }
}
