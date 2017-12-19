package se.tube42.kidsmem.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.control.*;

// GameStat is a text used for game statistics that animates when changed
public class GameStat extends BaseText
implements TweenListener
{
	private String next_text;

    public GameStat()
    {
        super(Assets.font1);
		setColor(Constants.COLOR_FG1);
        clear();
    }

    public void clear()
    {
        setText("");
        setAlpha(0);
    }

    public void setAnimatedText(String txt)
    {
		if(txt.equals(getText()) && next_text == null)
			return;

        next_text = txt;
        set(ITEM_A, 0).configure(0.5f, null).finish(this, 0)
			  .tail(1).configure(0.5f, null);

    }


    public void onFinish(Item item, int index, int data)
    {
		setText(next_text);
		next_text = null;
    }

}
