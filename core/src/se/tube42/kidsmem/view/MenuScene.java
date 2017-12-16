package se.tube42.kidsmem.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Setters;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Input.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.control.*;

import static se.tube42.kidsmem.data.Assets.*;
import static se.tube42.kidsmem.data.UI.*;
import static se.tube42.kidsmem.data.Constants.*;

public class MenuScene extends Scene
{
    public static final int
          MSG_SHOW_GAME = 0
          ;

    private BaseText msg;
	private Button button_sound, button_full, button_mode, button_play;
	private Button []buttons;
	private SpriteItem bar;

	private SceneManager mgr;
	private int y0, y1, y2, x0, x1, x2;
	private float timeplay;
	private volatile float timeback;


    private final static int
		ID_SOUND = 0,
		ID_FULL = 1,
		ID_MODE = 2,
		ID_PLAY = 3
		;
    // -----------------------------------------

    public MenuScene(SceneManager mgr)
    {
    	super("menu");
        this.mgr = mgr;

		final Layer l0 = getLayer(0);
		l0.add( bar = new SpriteItem(reg_rect));
		bar.setColor(COLOR_BG2);

		final Layer l1 = getLayer(1);
		button_play = new Button(reg_icons, ICON_PLAY);
		button_sound = new Button(reg_icons, ICON_SOUND_OFF, ICON_SOUND_ON);
		button_full = new Button(reg_icons, ICON_FULL_OFF, ICON_FULL_ON);
		button_mode = new Button(reg_icons,
			ICON_MODE_EASY, ICON_MODE_NORMAL, ICON_MODE_HARD);

		buttons = new Button[]{button_sound, button_full, button_mode, button_play};
		for(Button b : buttons) {
			l1.add(b);
			b.setColor( COLOR_FG1);
		}

        l1.add( msg = new BaseText(font1) );
        msg.setColor(COLOR_FG1);
        msg.setText("");
        msg.setAlignment(-0.5f, + 0.5f);

		getSettings();
	}

	public void onShow()
	{
		for(int i = 0; i < 3; i++) {
			final int c = 0xFF & (COLOR_BG1 >> (i * 8));
			World.bgc.set(i, c / 256f);
		}

		final BaseItem []all = {bar, button_sound, button_full, button_mode, button_play};
		TweenHelper.animate(buttons, BaseItem.ITEM_A, 0, 1, 1.0f, 1.5f,
			0.3f, 0.8f, TweenEquation.CUBE_IN);
		bar.set(BaseItem.ITEM_A, 0,1).configure(2f, TweenEquation.LINEAR);
		setMessage(null);
	}

	public void onHide()
	{
		IOHelper.saveSettings();

		TweenHelper.animate(buttons, BaseItem.ITEM_A, 1, 0, 0.2f, 0.3f, TweenEquation.CUBE_IN);
		bar.set(BaseItem.ITEM_A, 1,0).configure(0.25f, TweenEquation.LINEAR);

	}

	private void setMessage(String str)
	{
		if(str == null) {
			msg.setText("");
			return;
		}

		msg.setText(str);

		// fly in animation
		final int xc = UI.sw / 2;
		msg.set(BaseItem.ITEM_X, 0, xc)
			.configure(0.25f, TweenEquation.CUBE_OUT)
			.pause(1.25f)
			.tail(2 * xc).configure(0.25f,TweenEquation.CUBE_OUT);

		msg.set(BaseItem.ITEM_A, 0, 1)
			.configure(0.5f, TweenEquation.CUBE_OUT)
			.pause(1.0f).tail(0).configure(0.5f,TweenEquation.CUBE_OUT);
	}

	private void getSettings()
	{
		button_sound.setCurrent( Settings.sound ? 1 : 0);
		button_full.setCurrent(Settings.fullscreen ? 1 : 0);
		button_mode.setCurrent(Settings.size);
	}

	private void setSettings()
	{
		Settings.sound = button_sound.getCurrent() == 1;
		Settings.fullscreen = button_full.getCurrent() == 1;
		Settings.size = button_mode.getCurrent();
	}


	public void resize(int sw, int sh)
    {
    	super.resize(sw, sh);


		final int button_big = Math.min( sw / 4, sh / 6) & ~15;
		final int button_small = button_big / 2;

		y0 = sh / 2;
		y2 = sh / 4 + button_small;
		y1 = sh / 4 - button_small;

		x1 = sw / 2;
		x0 = sw / 4;
		x2 = sw - x0;

		bar.setSize(sw, y1 - y2);
		bar.setPosition(0, y2);

        button_play.setSize(button_big, button_big);
		button_sound.setSize(button_small, button_small);
        button_mode.setSize(button_small, button_small);
        button_full.setSize(button_small, button_small);

		final int yc = ( y1 + y2 - button_small) / 2;
		button_play.setPosition(x1 - button_big / 2, y0);
        button_sound.setPosition(x0 - button_small / 2, yc);
        button_mode.setPosition(x1 - button_small / 2, yc);
        button_full.setPosition(x2 - button_small / 2, yc);

		msg.setPosition(sw / 2, button_mode.getY() - button_small);
	}

	public void onUpdate(float dt)
    {
		super.onUpdate(dt);

		timeback += dt;

        timeplay += dt;
        if(timeplay > 0) {
			timeplay = -RandomService.get(3, 8);
            button_play.set(Button.ITEM_S, 1, 1.1f).configure(0.2f, null)
                  .tail(1.0f).configure(0.2f, null)
                  .pause(0.4f)
                  .tail(1.1f).configure(0.2f, null)
                  .tail(1.0f).configure(0.2f, null)
                  ;
        }
    }

	public boolean type(int key, boolean down)
    {
        if(down) {
            if (key == Keys.BACK || key == Keys.ESCAPE) {
				if(timeback < 3) {
					IOHelper.saveSettings(); // save settings
                    Gdx.app.exit();
                } else {
					timeback = 0;
                    if(World.sys != null)
                        World.sys.showMessage("Press again to exit");
                }
            }
        }
        return true;
    }

    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {
		if(!down || drag) return true;

		Button b = buttonAt(x, y);
		if(b == null)
			return false;

		b.next();
		setSettings();

		if(b == button_sound) {
			ServiceProvider.play(Settings.sound ? talk_on : talk_off);
			setMessage(Settings.sound ? "sound on" : "sound off");
		} else if(b == button_mode) {
			final int best = Statistics.best[Settings.size];
			if(best > 0)
				setMessage(MODE_NAMES[Settings.size] + " (" + best + ")");
			else
				setMessage(MODE_NAMES[Settings.size]);
			ServiceProvider.play(talk_hardness[Settings.size]);
		} else if(b == button_full) {
			if(World.sys != null)
				World.sys.setFullscreen(Settings.fullscreen);
			setMessage(Settings.fullscreen ? "full screen" : "");
		} else if(b == button_play) {
			if(World.scene_game == null)
				World.scene_game = new GameScene(mgr);
			mgr.setScene(World.scene_game, 750);
			/*
			// write configuration to World
			// World.sound_enabled is already set
			World.board_w = 2 + Settings.size;
			World.board_h = Settings.size == 0 ? 3 : 2 + Settings.size * 2;
			SizeHelper.resizeBoard(UI.sw, UI.sh); // must resize since board_w/h may have changed

			// anim buttons out (and move to game scene)
			anim_menus_out();
			*/
		}

		return true;
	}

	private Button buttonAt(float x, float y)
    {
		for(Button b : buttons)
			if(b.hit(x, y))
			return b;
		return null;
	}
}
