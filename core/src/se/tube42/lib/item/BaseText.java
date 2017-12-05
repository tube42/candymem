package se.tube42.lib.item;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;

public class BaseText extends BaseItem
{

    private String text;
    private BitmapFont font;
    private float alignv, alignh;
    private int mw;
    private GlyphLayout gl;

    public BaseText(BitmapFont font)
    {
        this.font = font;
        this.gl = new GlyphLayout();
        setText("");
        setAlignment(0, 0);
        setMaxWidth(0);
    }

    public void setMaxWidth(int mw)
    {
        this.mw = mw;
        calcBounds();
    }

    public void setText(String text)
    {
        this.text = text;
        calcBounds();
    }

    public String getText()
    {
        return text;
    }

    public BitmapFont getFont()
    {
        return font;
    }

    public void setAlignment(float ah, float av)
    {
        alignh = ah;
        alignv = av;
        calcBounds();
    }

    public void calcBounds()
    {
        if(text != null) {
            if(mw > 0) {
                gl.setText(font, text, 0, text.length(), font.getColor(), mw, Align.left, false, null);
            } else {
                  gl.setText(font, text);
            }
            w = (int)(0.5f + gl.width);
            h = (int)(0.5f + gl.height);
        } else {
            w = h = 1;
        }
    }

    // --------------------------------------------

    public void draw(SpriteBatch sb)
    {
        if(this.text != null) {
            draw_text(sb,
                      getX() + alignh * w,
                      getY() + alignv * h
                      );
        }
    }

    protected void draw_text(SpriteBatch sb, float x, float y)
    {
        font.setColor( cr, cg, cb, getAlpha());
        if(mw > 0) {
            font.draw(sb, text, x, y, mw, Align.left, true);            
        } else {
            font.draw(sb, text, x, y);
        }
    }

    public boolean hit(float x, float y)
    {
        // special hit function for text that uses alignments
        final float x0 = getX() + alignh * w;
        final float y0 = getY() + alignh * h;
        final float x1 = x0 + w;
        final float y1 = y0 + h;
        return x >= x0 && x <= x1 && y >= y0 && y <= y1;
    }

}
