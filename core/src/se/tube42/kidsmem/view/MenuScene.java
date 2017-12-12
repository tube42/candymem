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

import static se.tube42.kidsmem.data.Constants.*;

public class MenuScene extends Scene
implements TweenListener
{
    public static final int
          MSG_SHOW_GAME = 0
          ;

    private BaseText stats;
    private Button [] buttons;
    private SceneManager mgr;
    private boolean first, enabled;
    private float time;

    // -----------------------------------------

    public MenuScene(SceneManager mgr)
    {
    	super("menu");
        this.mgr = mgr;
        this.first = true;

        buttons = new Button[4];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(Assets.reg_menu, Assets.reg_menu_shadow, 0, 1);
            buttons[i].setPosition(i * 300, -100);
        }

        getLayer(0).add(buttons);

        // stats text
        stats = new BaseText( Assets.font1);
        stats.setColor(COLOR_STATS);
        stats.setText("");
        stats.setAlignment(-0.5f, - 2.5f);
        getLayer(1).add(stats);

        update();
    }

    public void onShow()
    {
        timer_reset();
        enabled = true;

        if(first) {
            first = false;
            anim_menus_off();
            if(World.top != null) {
                World.top.show(1.5f, 1.5f);
            }
            anim_screen_on();
        }
        anim_menus_on();
    }

    public void onHide()
    {
        IOHelper.saveSettings();
    }


    private void update()
    {
        buttons[MENU_SOUND].animateIndex(Settings.sound ? 2 : 3);
        buttons[MENU_HARDNESS].animateIndex(4 + Settings.size);
        buttons[MENU_PLAY].setIndex(7);
        buttons[MENU_FULLSCREEN].animateIndex(Settings.fullscreen ? 0 : 1);

        if(World.sys != null)
            World.sys.setFullscreen(Settings.fullscreen);

        // update score
        final int best = Statistics.best[Settings.size];
        final int count = Statistics.count[Settings.size];
        if(best > 0) {
            stats.setText("best: " + best);
        } else {
            stats.setText("");
        }
    }

    public void resize(int sw, int sh)
    {
    	super.resize(sw, sh);

        // play button:
        buttons[MENU_PLAY].setSize(UI.button_big_size, UI.button_big_size);
        buttons[MENU_PLAY].setPosition(UI.button_big_x, UI.button_big_y);

        // the small ones:
        buttons[MENU_SOUND].setSize(UI.button_small_size, UI.button_small_size);
        buttons[MENU_HARDNESS].setSize(UI.button_small_size, UI.button_small_size);
        buttons[MENU_FULLSCREEN].setSize(UI.button_small_size, UI.button_small_size);

        buttons[MENU_SOUND].setPosition( UI.button_small_x0, UI.button_small_y);
        buttons[MENU_HARDNESS].setPosition( UI.button_small_x2, UI.button_small_y);
        buttons[MENU_FULLSCREEN].setPosition( UI.button_small_x1, UI.button_small_y);


        stats.setPosition(0, sw / 2, sh);

        // top:
        if(World.top != null) {
            World.top.resize(true);
        }
    }

    // --------------------------------------------------

    private void timer_reset()
    {
        time = -10;
    }

    private void timer_update(float dt)
    {
        time += dt;
        if(time > 0) {
            timer_reset();
            buttons[MENU_PLAY].set(Button.ITEM_S2, 1, 1.1f).configure(0.2f, null)
                  .tail(1.0f).configure(0.2f, null)
                  .pause(0.4f)
                  .tail(1.1f).configure(0.2f, null)
                  .tail(1.0f).configure(0.2f, null)
                  ;
        }
    }

    public void onUpdate(float dt)
    {
        super.onUpdate(dt);
        timer_update(dt);
    }


    // --------------------------------------------------

    public boolean type(int key, boolean down)
    {
        if(down) {
            if (key == Keys.BACK || key == Keys.ESCAPE) {
                IOHelper.saveSettings(); // save settings
                Gdx.app.exit();
            }
        }
        return true;
    }

    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {
        if(!down) {
            anim_menu_release_all();
        }


        if(!enabled)
            return false;

        if(down && !drag) {
            int n = get_tile_at(x, y);
            if(n != -1) {

                timer_reset();
                buttons[n].press();

                switch(n) {
                    case MENU_SOUND: // SOUND
                    // enable sound. double ifs() to sound even when off
                    if(Settings.sound)
                        ServiceProvider.play(Assets.talk_off);

                    Settings.sound = !Settings.sound;

                    if(Settings.sound)
                        ServiceProvider.play(Assets.talk_on);
                    break;

                    case MENU_HARDNESS: // hardness
                        Settings.size = (Settings.size + 1) % 3;
                        ServiceProvider.play(Assets.talk_hardness[Settings.size]);
                    break;

                    case MENU_FULLSCREEN: // fullscreen
                    Settings.fullscreen = !Settings.fullscreen;
                    break;

                    case MENU_PLAY: // PLAY
                    enabled = false;
                    ServiceProvider.play(Assets.talk_play);

                    // write configuration to World
                    // World.sound_enabled is already set
                    World.board_w = 2 + Settings.size;
                    World.board_h = Settings.size == 0 ? 3 : 2 + Settings.size * 2;
                    SizeHelper.resizeBoard(UI.sw, UI.sh); // must resize since board_w/h may have changed

                    // anim buttons out (and move to game scene)
                    anim_menus_out();
                    break;
                }
                update();
                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------

    private void anim_screen_on()
    {
        System.out.println("screen on"); // DEBUG
        for(int i = 0; i < 3; i++)
            World.bgc.set(i, COLOR_BG[i]).configure(1f, null);
    }

    private void anim_menus_off()
    {
        System.out.println("menus off"); // DEBUG
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setScale(1f);
            buttons[i].setAlpha(0f);
        }
    }

    private void anim_menus_on()
    {
        System.out.println("menus on"); // DEBUG
        for(int i = 0; i < buttons.length; i++)
            buttons[i].show(0.3f + i * 0.15f, 0.4f);

        update(); // set correct alphas!
    }

    private void anim_menus_out()
    {
        System.out.println("menus out"); // DEBUG
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setImmediate(Button.ITEM_S2, 1f);
            buttons[i].hide(0.3f + i * 0.15f, 0.5f);
        }

        World.top.resize(false)
              .pause(1.0f).finish(this, MSG_SHOW_GAME);
    }

    private void anim_menu_release_all()
    {
        System.out.println("release all"); // DEBUG
        for(int i = 0; i < buttons.length; i++)
            buttons[i].release();
    }

    // --------------------------------------------------

    private int get_tile_at(float x, float y)
    {
        for(int i = 0; i < buttons.length; i++)
            if(buttons[i].hit(x, y))
                return i;
        return -1;
    }

    // ---------------------------------------------------
    // TweenListener
    public void onFinish(Item item, int index, int msg)
    {
        switch(msg) {
        case MSG_SHOW_GAME:
            final GameScene sg = new GameScene(mgr);
            mgr.setScene(sg);
            break;

        }
    }
}
