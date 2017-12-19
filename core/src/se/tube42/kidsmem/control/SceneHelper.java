package se.tube42.kidsmem.control;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;


import se.tube42.lib.scene.*;
import se.tube42.lib.tweeny.TweenEquation;
import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.view.*;


public class SceneHelper
{
	public static void reset()
	{
		// this is to avoid issues when android  re-uses static stuff
		World.scene_game = null;
		World.scene_menu = null;
	}

	public static void change(Scene s, boolean animate)
	{
		if(animate) {
			World.mgr.setScene(s, 400);
			World.zoom.set(0, 0.33f).configure(0.4f, TweenEquation.LINEAR)
				.tail(1).configure(0.6f, TweenEquation.LINEAR);
		} else {
			World.mgr.setScene(s);
		}
	}

	public static void showMenu()
	{
		if(World.scene_menu == null) {
			World.scene_menu = new MenuScene();
			change(World.scene_menu, false);
			return;
		}
		change(World.scene_menu, true);
	}

	public static void showGame()
	{
		if(World.scene_game == null)
			World.scene_game = new GameScene();
		change(World.scene_game, true);

	}
}
