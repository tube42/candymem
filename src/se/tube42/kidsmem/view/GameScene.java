package se.tube42.kidsmem.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Input.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.util.*;
import se.tube42.lib.item.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.control.*;

import static se.tube42.kidsmem.data.Constants.*;

public class GameScene extends Scene
implements MessageListener, TweenListener
{
    public static final int
          MSG_BOARD_IN = 0,
          MSG_BOARD_OUT = 1,

          MSG_SEL1_FINISH = 10,
          MSG_SEL2_FINISH = 11,
          MSG_MATCH_YES = 12,
          MSG_MATCH_NO = 13,

          MSG_ANIMATED_END = 14,
          MSG_FIREWORK = 15
          ;

    public static final int
          COUNT_FIREWORKS = 6
          ;

    // ---------------------------------------------
    private SceneManager mgr;
    private Item back_timer;
    private Layer tile_layer;
    private GameStat text0, text1;

    // end game partiles
    private int cntf;

    public GameScene(SceneManager mgr)
    {
    	super("game");

        this.mgr = mgr;
        this.back_timer = new Item(1);

        back_timer.set(0, 0);

        World.tiles = new TileSprite[COUNT_W * COUNT_H];
        World.board = new TileSprite[World.board_w * World.board_h];
        World.fireworks = new FireworkItem[COUNT_FIREWORKS];

        for(int i = 0; i < World.tiles.length; i++)
            World.tiles[i] = new TileSprite(Assets.reg_tiles, Assets.reg_candy1);

        for(int i = 0; i < World.fireworks.length; i++)
            World.fireworks[i] = new FireworkItem();

        // add some of the visible stuff to layers
        getLayer(0).add(World.fireworks);
        tile_layer = getLayer(1);

        // text stuff
        text0 = new GameStat();
        text1 = new GameStat();
        getLayer(2).add(text0);
        getLayer(2).add(text1);
    }

    public void onShow()
    {
        GameHelper.reset();
        GameHelper.animBoardHide();
        JobService.add( this, 200, MSG_BOARD_IN); // board-in

        // this will ensure that we include only the used tiles
        tile_layer.clear();
        tile_layer.add(World.board);
    }


    public void resize(int sw, int sh)
    {
    	super.resize(sw, sh);

        GameHelper.positionBoard();

        // top:
        if(World.top != null) {
            World.top.resize(false);
        }

        text0.setPosition(16, UI.stats_yc);
        text1.setPosition(sw - 16, UI.stats_yc);

        text0.setAlignment(0, +0.5f);
        text1.setAlignment(-1, +0.5f);
    }

    // --------------------------------------------------

    public boolean type(int key, boolean down)
    {
        if(down) {
            if (key == Keys.BACK || key == Keys.ESCAPE) {

                if(back_timer.get(0) > 0.1f) {
                    Gdx.app.exit();
                } else {
                    back_timer.set(0, 1f, 0f). configure(2f, null);
                    if(World.sys != null)
                        World.sys.showMessage("Press again to exit");
                }
            }
        }
        return true;
    }

    public boolean touch(int x, int y, boolean down, boolean drag)
    {
        if(World.state != World.STATE_SEL1 &&
           World.state != World.STATE_SEL2) {
            return false;
        }

        //
        if(down && !drag) {
            final TileSprite sel = GameHelper.getTileAt(x, y);

            if(sel != null) {
                GameHelper.select(sel, this, MSG_SEL1_FINISH, MSG_SEL2_FINISH );
                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------
    public void stats_update()
    {
        text0.setAnimatedText("" + World.cnt_total);
        text1.setAnimatedText("" + (World.cnt_match * 100 / World.cnt_total) + "%");
    }

    public void stats_reset()
    {
        text0.clear();
        text1.clear();
    }

    // --------------------------------------------------

    public void onFinish(Item item, int index, int data)
    {
        onMessage(data, index, item, item);
    }

    public void onMessage(int msg, int data0, Object data1, Object sender)
    {
        // System.out.println("MSG = " + msg + " data0=" + data0 + " data1=" + data1);

        switch(msg) {
        case MSG_BOARD_IN:
            GameHelper.animBoardIn();
            break;

        case MSG_BOARD_OUT:
            GameHelper.animPokeTiles();
            break;

            // -----------------------------
        case MSG_SEL1_FINISH:
            break;

        case MSG_SEL2_FINISH:
            World.cnt_total++;
            if(GameHelper.checkSelection(this, MSG_MATCH_YES, MSG_MATCH_NO))
                World.cnt_match++;

            stats_update();
            break;

        case MSG_MATCH_NO:
            break;

        case MSG_MATCH_YES:
            if(GameHelper.gameIsFinished() ) {
                World.state = World.STATE_END;
                GameHelper.showEnd(this, MSG_ANIMATED_END,
                          this, MSG_FIREWORK);
            }
            break;

        case MSG_ANIMATED_END:
            GameHelper.reset();
            GameHelper.animBoardHide();
            GameHelper.animBoardIn();
            stats_reset();
            break;

        case MSG_FIREWORK:
            if(data0 > 0) {
                cntf = (cntf + 1) % World.fireworks.length;
                World.fireworks[cntf].emit(this, MSG_FIREWORK, data0-1);
            }
            break;
        }
    }

}

