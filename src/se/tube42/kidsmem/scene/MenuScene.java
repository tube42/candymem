package se.tube42.kidsmem.scene;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Input.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.service.*;
import se.tube42.kidsmem.logic.*;
import se.tube42.kidsmem.item.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;

import static se.tube42.kidsmem.data.Constants.*;

public class MenuScene extends Scene
implements TweenListener
{
    public static final int
          MSG_SHOW_GAME = 0
          ;

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

        update();
    }

    public void onShow()
    {
        enabled = true;
        time = -3; // force first


        if(first) {
            first = false;
            anim_menus_off();

            if(Items.top != null) {
                Items.top.show(1.5f, 1.5f);
            }

            anim_screen_on();
        }

        anim_menus_in();
    }

    public void onHide()
    {
        SettingsHelper.save(); // save settings
    }


    private void update()
    {
        buttons[0].animateIndex(Settings.sound ? 2 : 3);
        buttons[1].animateIndex(4 + Settings.size);
        buttons[2].setIndex(7);
        buttons[3].animateIndex(Settings.fullscreen ? 0 : 1);

        if(World.sys != null)
            World.sys.setFullscreen(Settings.fullscreen);
    }

    public void resize(int sw, int sh)
    {
    	super.resize(sw, sh);

        // play button:
        buttons[2].setSize(UI.button_big_size, UI.button_big_size);
        buttons[2].setPosition(UI.button_big_x, UI.button_big_y);

        // the small ones:
        buttons[0].setSize(UI.button_small_size, UI.button_small_size);
        buttons[1].setSize(UI.button_small_size, UI.button_small_size);
        buttons[3].setSize(UI.button_small_size, UI.button_small_size);

        buttons[0].setPosition( UI.button_small_x0, UI.button_small_y);
        buttons[3].setPosition( UI.button_small_x1, UI.button_small_y);
        buttons[1].setPosition( UI.button_small_x2, UI.button_small_y);

        // top:
        if(Items.top != null) {
            Items.top.resize(true);
        }
    }

    public void onUpdate(float dt)
    {
        super.onUpdate(dt);

        time += dt;
        if(time > 0) {
            time = -ServiceProvider.getRandom(5, 10);
            buttons[2].set(BaseSprite.ITEM_S2, 1, 1.1f).configure(0.2f, null)
                  .tail(1.0f).configure(0.2f, null)
                  .pause(0.4f)
                  .tail(1.1f).configure(0.2f, null)
                  .tail(1.0f).configure(0.2f, null)
                  ;
        }
    }


    // --------------------------------------------------

    public boolean type(int key, boolean down)
    {
        if(down) {
            if (key == Keys.BACK || key == Keys.ESCAPE) {
                SettingsHelper.save(); // save settings
                Gdx.app.exit();
            }
        }
        return true;
    }

    public boolean touch(int x, int y, boolean down, boolean drag)
    {

        if(!down) {
            anim_menu_release_all();
        }


        if(!enabled)
            return false;

        if(down && !drag) {

            int n = get_tile_at(x, y);
            if(n != -1) {
                anim_menu_push(n);

                switch(n) {
                    case 0: // SOUND
                    // enable sound. double ifs() to sound even when off
                    if(Settings.sound)
                        ServiceProvider.play(Assets.talk_off);

                    Settings.sound = !Settings.sound;

                    if(Settings.sound)
                        ServiceProvider.play(Assets.talk_on);
                    break;

                    case 1: // hardness
                    Settings.size = (Settings.size + 1) % 3;
                    ServiceProvider.play(Assets.talk_hardness[Settings.size]);
                    break;

                    case 3: // fullscreen
                    Settings.fullscreen = !Settings.fullscreen;
                    break;

                    case 2: // PLAY
                    enabled = false;
                    ServiceProvider.play(Assets.talk_play);

                    // write configuration to World
                    // World.sound_enabled is already set
                    World.board_w = 2 + Settings.size;
                    World.board_h = Settings.size == 0 ? 3 : 2 + Settings.size * 2;
                    SizeHelper.resizeBoard(UI.sw, UI.sh); // must resize since board_w/h may have changed

                    // anim out
                    anim_menus_out();
                }
                update();
                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------

    private void anim_screen_off()
    {
        for(int i = 0; i < 3; i++)
            World.color_bg.setImmediate(i, 0);
    }

    private void anim_screen_on()
    {
        for(int i = 0; i < 3; i++) {
            float c = ((COLOR_BG >>> ((2-i) * 8)) & 0xFF) / 255f;
            World.color_bg.set(i, 0, c).configure(1.2f, null);
        }
    }

    private void anim_menus_off()
    {
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setScale(1f);
            buttons[i].setAlpha(0f);
        }
    }

    private void anim_menus_in()
    {
        for(int i = 0; i < buttons.length; i++)
            buttons[i].show(0 + i * 0.1f, 0.4f);

        update(); // set correct alphas!
    }

    private void anim_menus_out()
    {
        buttons[2].removeTween(BaseSprite.ITEM_S2, true);

        for(int i = 0; i < buttons.length; i++)
            buttons[i].hide(0.3f + i * 0.1f, 0.7f);

        Items.top.resize(false)
              .pause(0.8f).finish(this, MSG_SHOW_GAME);
    }

    private void anim_menu_push(int index)
    {
        buttons[index].press();
    }

    private void anim_menu_release_all()
    {
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
