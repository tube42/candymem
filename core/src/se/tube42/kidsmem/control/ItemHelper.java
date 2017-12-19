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

	// position item in relation to another item
	public static void position(BaseItem me, BaseItem ref, float xa, float ya)
	{
		me.setPosition(
			ref.getX() + xa * (ref.getW() - me.getW()),
			ref.getY() + ya * (ref.getH() - me.getH()));
	}

	public static void copy(BaseItem me, BaseItem ref, boolean position,
	boolean size)
	{
		if(position)
			me.setPosition(ref.getX(),ref.getY());
		if(size)
			me.setSize(ref.getW(),ref.getH());
	}
}