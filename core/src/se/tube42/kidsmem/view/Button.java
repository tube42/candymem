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

public class Button extends SpriteItem
{
    private int []indices;
	private int current;

    public Button(TextureRegion []icons, int ...indices)
    {
		super(icons);
		this.indices = indices;
		this.current = 0;
		setColor(COLOR_FG1);
		update();
	}

	public void setCurrent(int i)
	{
		if(i >= 0 && i < indices.length)
			current = i;
		update();
	}
	public int getCurrent()
	{
		return current;
	}

	public void next()
	{
		setCurrent( (current + 1) % indices.length );
	}

	private void update()
	{
		setIndex( indices[current]);
	}
}
