package se.tube42.kidsmem.control;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.view.*;

/*
 * game logic.
 *
 * since animation code in GameScene was getting too
 * complicated we also moved most of that to this class
 */

public class GameHelper
{
    // temp buffers for reset
    private static int [] tmp1 = null;
    private static int [] tmp2 = null;
    private static float [] tmp3 = new float[3];

    // ---------------------------------------------------------------
    // board logic
    public static void reset()
    {
        final TileSprite [] tiles = World.tiles;
        final TileSprite [] board = World.board;

        // 1. decide which ones to include
        final int cnt1 = Assets.reg_candy1.length;
        if(tmp1 == null) tmp1 = new int[cnt1];
        for(int i = 0; i < cnt1; i++) tmp1[i] = i;
        for(int i = 0; i < cnt1; i++) {
            int j = RandomService.getInt(cnt1);
            int tmp = tmp1[j];
            tmp1[j] = tmp1[i];
            tmp1[i] = tmp;
        }

        // 2. shuffle their order
        final int cnt2 = board.length;
        if(tmp2 == null) tmp2 = new int[cnt1];  // note: dont use cnt2 here!
        for(int i = 0; i < cnt2; i++) tmp2[i] = i / 2;
        for(int i = 0; i < cnt2; i++) {
            int j = RandomService.getInt(cnt2);
            int tmp = tmp2[j];
            tmp2[j] = tmp2[i];
            tmp2[i] = tmp;
        }

        // 3. reset the board
        for(int i = 0; i < board.length; i++) {
            board[i] = tiles[i];

            board[i].id = tmp1[ tmp2[i]];
            board[i].setState(TileSprite.STATE_HIDDEN);
        }

        World.state = World.STATE_SEL1;
        World.cnt_total = 0;
        World.cnt_match = 0;

        // 4. set board position
        positionBoard();
    }

    public static void positionBoard()
    {
        if(World.board == null) return;

        final int bw = World.board_w;
        final int bh = World.board_h;
        final int n = World.board.length;
        final int size = UI.size;
        final int gap = UI.gap;

        for(int i = 0; i < n; i++) {
            final TileSprite si = World.board[i];
            final float y = UI.board_y0 + (i / bw) * gap;
            final float x = UI.board_x0 + (i % bw) * gap;
            si.setPosition(x, y);
            si.setSize(size, size);
        }
    }

    public static TileSprite getTileAt(float x, float y)
    {
        for(TileSprite it : World.board)
            if(it.hit(x, y))
                return it;
        return null;
    }


    public static boolean gameIsFinished()
    {
        for(int i = 0; i < World.board.length; i++)
            if(World.board[i].getState() != TileSprite.STATE_MATCHED)
                return false;

        return true;
    }

    public static void select(TileSprite sel, TweenListener tl, int msg1, int msg2)
    {
        if(sel == null ||
           (World.state != World.STATE_SEL1 &&
            World.state != World.STATE_SEL2))
            return;

        if(sel.getState() != TileSprite.STATE_HIDDEN) {
            GameHelper.animAlreadyUsed(sel);
            return;
        }

        ServiceProvider.playOne(Assets.sound_hit);
        final TweenNode tn = GameHelper.animShowCandy(sel);

        if(World.state == World.STATE_SEL1) {
            World.sel1 = sel;
            World.sel2 = null;
            World.state = World.STATE_SEL2;
            tn.finish(tl, msg1);
        } else {
            World.sel2 = sel;
            World.state = World.STATE_TEMP;
            GameHelper.animDimRemaining(true);
            tn.pause(0.6f).finish(tl, msg2);
        }
    }

    public static boolean checkSelection(TweenListener tl, int msg_match, int msg_not_match)
    {
        final boolean match = World.sel1.id == World.sel2.id;
        final TweenNode tn = GameHelper.animHideCandy(World.sel2, match);
        GameHelper.animHideCandy(World.sel1, match);

        GameHelper.animDimRemaining(false);
        World.state = World.STATE_SEL1;

        if(match) {
            animWhenCorrect(World.sel1, World.sel2);
            ServiceProvider.playOne(Assets.sound_yes);
            tn.pause(0.8f).finish(tl, msg_match);
            return true;
        } else {
            animWhenIncorrect(World.sel1, World.sel2);
            ServiceProvider.playOne(Assets.sound_no);
            tn.finish(tl, msg_not_match);
            return false;
        }
    }

    public static void showEnd(TweenListener tl, int msg_end,
              MessageListener ml, int msg_firework)
    {
        ServiceProvider.play(Assets.sound_end);

        GameHelper.animBoardOut()
              .pause(3).finish(tl, msg_end);


        final int repeats = (int)Math.max(1, Math.min(4,
                  (5 * World.cnt_match) / (1 + World.cnt_total)
                  ));

        for(int i = 0; i < World.fireworks.length; i++) {
            JobService.add( ml, 20 + i * 300,
                      msg_firework, repeats, null, null);
        }

    }

    // ---------------------------------------------------------------
    // Animation code

    public static void animBoardHide()
    {
        for(TileSprite it : World.board)
            it.setAlpha(0);
    }

    public static TweenNode animBoardIn()
    {
        ServiceProvider.play(Assets.sound_start);
        positionBoard();
        return setShow(true);
    }

    public static TweenNode animBoardOut()
    {
        positionBoard();
        return setShow(false);
    }

    private static TweenNode setShow(boolean visible)
    {
        // reposition board
        final int n = World.board.length;
        final int bw = World.board_w;
        final int bh = World.board_h;

        TweenNode last = null;

        for(int y = 0; y < bh; y++)  {
            for(int x = 0; x < bw; x++)  {
                final TileSprite si = World.board[x + y * bw];
                last = si.setShow(visible,
                          0.7f + x * 0.07f + y * 0.13f,
                          0.3f);
            }
        }

        return last;
    }

    public static void animPokeTiles()
    {
        for(int i = 0; i < World.board.length; i++) {
            final TileSprite si = World.board[i];
            final float t = RandomService.get(0.2f, 0.32f);

            si.set(BaseItem.ITEM_S, 1.2f).configure(t, null)
                  .tail(0.8f).configure(t, null)
                  .tail(1.0f).configure(t, null);
        }
    }

    // ----------------------------------------------------------

    public static TweenNode animShowCandy(TileSprite item)
    {
        item.setState(TileSprite.STATE_ANIM_SHOW);

        item.pause(BaseItem.ITEM_A, 1f, 0.3f)
              .tail(0.4f).configure(0.06f, null)
              .pause(0.2f)
              .tail(1.0f).configure(0.1f, null)
              ;

        return item.set(BaseItem.ITEM_S, 1.0f, 1.1f).configure(0.3f, null)
              .tail(0.2f).configure(0.1f, null)
              .finish(item, TileSprite.STATE_SHOWN)
              .tail(1.2f).configure(0.20f, null)
              .tail(1.0f).configure(0.10f, null)
              ;
    }

    public static TweenNode animHideCandy(TileSprite item, boolean matched)
    {
        final int state = matched ? TileSprite.STATE_MATCHED
                    : TileSprite.STATE_HIDDEN;
        final float r = RandomService.get(0.0f, 0.2f);

        item.setState(TileSprite.STATE_ANIM_HIDE);

        item.pause(BaseItem.ITEM_S, 1f, r)
              .tail(1.1f).configure(0.14f, null)
              .tail(0.3f).configure(0.3f, null)
              .finish(item, state)
              .tail(1.2f).configure(0.2f, null)
              .tail(1f).configure(0.1f, null);


        return item.pause(BaseItem.ITEM_A, 1f, r + 0.4f)
              .tail(0.5f).configure(0.04f, null)
              .tail(1.0f).configure(0.05f, null)
              ;
    }


    public static void animDimRemaining(boolean dim)
    {
        for(int i = 0; i < World.board.length; i++) {
            final TileSprite ts = World.board[i];
            if(ts.getState() == TileSprite.STATE_HIDDEN) {
                final float t = RandomService.get(0.4f, 0.6f);
                final float p = (dim ? 0 : 0.5f) + RandomService.get(0.15f, 0.25f);
                final float a = ts.get(BaseItem.ITEM_A);
                ts.pause(BaseItem.ITEM_A, a, p)
                      .tail(dim ? 0.5f : 1).configure(t, null);
            }
        }
    }

    public static void animWhenCorrect(TileSprite sel1, TileSprite sel2)
    {
    }

    public static void animWhenIncorrect(TileSprite sel1, TileSprite sel2)
    {
        for(int i = 0; i < World.board.length; i++) {
            final TileSprite t = World.board[i];
            if(t.getState() == TileSprite.STATE_HIDDEN) {
                final float r = RandomService.get(0.6f, 0.8f);
                final float p = RandomService.get(0.05f, 0.12f);

                t.pause(BaseItem.ITEM_R, 0, p)
                      .tail(+25).configure(r * .2f, null)
                      .tail(-25).configure(r * .4f, null)
                      .tail(+15).configure(r * .3f, null)
                      .tail(-15).configure(r * .3f, null)
                      .tail(+ 7).configure(r * .2f, null)
                      .tail(- 7).configure(r * .2f, null)
                      .tail(0).configure(r * .1f, null)
                      ;
            }
        }
    }

    public static void animAlreadyUsed(TileSprite item)
    {
        // end old tween
        item.removeTween(BaseItem.ITEM_X, true);

        //
        final float x = item.get(BaseItem.ITEM_X);
        float s = Math.max(12, UI.size / 6);
        float t = 0.1f;


        TweenNode tmp = item.set(BaseItem.ITEM_X, x + s)
              .configure(t / 2, null);

        for(int i = 0; i < 8 && s > 2; i++) {
            tmp = tmp.tail(x - s).configure(t, null)
                  .tail(x + s).configure(t, null);
            s *= 0.75;
            t *= 0.75;
        }

        tmp.tail(x).configure(t, null);
    }

}
