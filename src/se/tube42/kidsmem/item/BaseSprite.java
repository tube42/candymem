
package se.tube42.kidsmem.item;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.item.*;

public class BaseSprite extends SpriteItem
{
    public static final int
          ITEM_S2 = 5
          ;

    public BaseSprite(TextureRegion [] textures, int index)
    {
        super(textures, index);
        setScale2(1);
    }

    // --------------------------------------------

  	public float getScale2()
    {
        return get(ITEM_S2);
    }

    public void setScale2(float s)
    {
        setImmediate(ITEM_S2, s);
    }

    public void setScale2(float dur, float s)
    {
        set(ITEM_S2, s).configure(dur, null);
    }


    public float getScale()
    {
        return super.getScale() * getScale2();
    }

}
