
package se.tube42.kidsmem.item;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.service.*;

import static se.tube42.kidsmem.data.Constants.*;

public class TopBar extends PatchItem
{
    public TopBar()
    {
        super(Assets.np_botshadow);

        setPosition(UI.top_x, UI.top_y0);
        setSize(UI.top_w, UI.top_h0);
    }

    public void hide()
    {
        setAlpha(0);
    }

    public void show(float pause, float duration)
    {
        pause(BaseItem.ITEM_A, 0, pause)
              .tail(1).configure(duration, null);
    }

    public TweenNode resize(boolean big)
    {
        int pos = TOP_DELTA_Y + (big ? UI.top_y0 : UI.top_y1);

        return set(ITEM_Y, pos).configure(0.45f, TweenEquation.BACK_IN);
    }


    public void draw(SpriteBatch sb)
    {
        w = UI.top_w;
        h = UI.sh - getY();
        setImmediate(ITEM_X, UI.top_x);

        super.draw(sb);
    }


}
