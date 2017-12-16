package se.tube42.kidsmem.control;

import se.tube42.lib.item.*;
import se.tube42.lib.tweeny.*;


public class ItemHelper
{
	// position item given alignment to its own size
	public static void position(BaseItem i, int x, int y, float xa, float ya)
	{
		i.setPosition( x + xa * i.getW(), y + ya * i.getH());
	}
}