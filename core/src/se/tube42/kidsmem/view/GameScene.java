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
import static se.tube42.kidsmem.data.Assets.*;

public class GameScene extends Scene
implements MessageListener, TweenListener
{
	public static final int
		MSG_BOARD_CHECK = 0,
		MSG_FIREWORK = 1,
		MSG_END = 2
		;

	public static final int
		  COUNT_FIREWORKS = 12,
		  COUNT_PARTICLES = 12
          ;

	private int cntf; // firework turn variable

	private Board board;
    private SpriteItem top;
	private Layer tile_layer;
	private ParticleLayer particle_layer;
    private GameStat text0, text1;

	private TileSprite []tiles;
	private FireworkItem []fireworks;

    public GameScene()
    {
    	super("game");

		this.board = new Board();
		this.cntf = 0;

		tiles = new TileSprite[COUNT_W * COUNT_H];
        for(int i = 0; i < tiles.length; i++)
			tiles[i] = new TileSprite(Assets.reg_tiles, Assets.reg_candy1);

        fireworks = new FireworkItem[COUNT_FIREWORKS];
        for(int i = 0; i < fireworks.length; i++)
            fireworks[i] = new FireworkItem();
		getLayer(0).add(fireworks);

		// bar and text stuff
		top = new SpriteItem(reg_rect);
		top.setColor(COLOR_BG2);

		text0 = new GameStat();
		text1 = new GameStat();
		text0.setAlignment(0, +0.5f);
		text1.setAlignment(-1, +0.5f);

		getLayer(1).add(top);
        getLayer(1).add(text0);
		getLayer(1).add(text1);

		particle_layer = new ParticleLayer();
		addLayer(particle_layer);

		tile_layer = getLayer(3);// tiles will go here
    }

    public void onShow()
    {
		GameHelper.reset(board, tiles, Settings.size);
		positionBoard(UI.sw,UI.sh);

		tile_layer.clear();
		for(int i = 0; i < board.count; i++) {
			tile_layer.add(tiles[i]);
			tiles[i].setAlpha(0);
			tiles[i].animShow(true);
		}
		top.set(BaseItem.ITEM_A, 0,1).configure(0.75f, TweenEquation.LINEAR);

		updateText();
	}

	public void onHide()
	{
		top.set(BaseItem.ITEM_A, 1, 0).configure(0.25f, TweenEquation.LINEAR);
	}

    public void resize(int sw, int sh)
    {
		super.resize(sw, sh);
		positionBoard(sw, sh);
	}
	private void positionBoard(int sw, int sh)
	{
		final int h0 = Math.max(16, sh / 12);
        top.setPosition(0, 0, sh - h0);
        top.setSize(sw, h0);

		text0.setPosition(h0 / 2, sh - h0 / 2);
		text1.setPosition(sw - h0 / 2, sh - h0 / 2);

		// position tiles
		if(board.w == 0 || board.h == 0)
			return;

		sh -= h0;
		final int size = (~3) & (int)Math.min( 0.9f * sw / board.w, 0.9f * sh / board.h);
		final int gapx = sw / board.w - size;
		final int gapy = sh / board.h - size;

		for(int y = 0; y < board.h; y++) {
			for(int x = 0; x < board.w; x++) {
				final int n = x + y * board.w;
				tiles[ n].setSize(size,size);
				tiles[ n].setPosition(
					0.5f * gapx + x * (size + gapx),
					1.0f * gapy + y * (size + gapy));
			}
		}
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
		case MSG_BOARD_CHECK:
			if(GameHelper.check(board)) {
				if(board.state == Board.STATE_WIN)
					gameWon();
				else
					tileWon();
			} else {
				tileLost();
			}
			updateText();
			break;

		case MSG_FIREWORK:
			fireworks(data0);
			if(data0 <= 0)
				JobService.add( this, 2500 , MSG_END);
			break;

		case MSG_END:
			for(int i = 0; i < fireworks.length; i++)
				fireworks[i].stop();
			SceneHelper.showMenu();
			break;
        }
	}

	private void fireworks(int level)
	{
		if(level < 0)
			 return;

		final int cnt = RandomService.getInt(2) + 2;
		for(int i = 0; i < cnt; i++)  {
			cntf = (cntf + 1) % fireworks.length;
				fireworks[cntf].emit();
		}

		JobService.add( this, 500, MSG_FIREWORK, level - 1, null, null);
	}

	private void updateText()
	{
		if(board.cnt_total == 0) {
			text0.clear();
			text1.clear();
		} else {
			final int best = Statistics.best[Settings.size];
			if(best > 0)
				text0.setAnimatedText("" + board.cnt_total + " (" + best + ")");
			else
				text0.setAnimatedText("" + board.cnt_total);
			text1.setAnimatedText("" + (board.cnt_match * 100 / board.cnt_total) + "%");
		}
	}

	private void dropParticles(TileSprite t)
	{
		for(int i = 0; i < COUNT_PARTICLES; i++) {
			Particle p = particle_layer.create(0.3f + i * 0.05f, 0.7f + i * 0.2f);
			// p.attach(t);
			p.configure(reg_candy1, t.id, 0x20FFFFFF);

			p.setSize((int)(t.getW() / 2), (int)(t.getH() / 2));
			p.setPosition(
				t.getX() + t.getW() / 3,
				t.getY() + t.getH() / 3);

			p.setVelocity(
				RandomService.get(-200, +200),
				RandomService.get(-200, +400),
				RandomService.get(-720,+720));
			p.setAcceleration(
				RandomService.get(-50,+50),
				RandomService.get(-500, 0),
				RandomService.get(-90,+90));
		}
	}

	private void tileWon()
	{
		dropParticles(tiles[board.sel1]);
		dropParticles(tiles[board.sel2]);
		tiles[board.sel1].match();
		tiles[board.sel2].match();
		ServiceProvider.playOne(Assets.sound_yes);
	}

	private void tileLost()
	{
		ServiceProvider.playOne(Assets.sound_no);
		tiles[board.sel1].animFlip(false);
		tiles[board.sel2].animFlip(false);
	}
	private void gameWon()
	{
		// udate stats
		Statistics.count[Settings.size]++;
		if(Statistics.best[Settings.size] == -1 || Statistics.best[Settings.size] > board.cnt_total) {
			Statistics.best[Settings.size] = board.cnt_total;
		}
		IOHelper.saveStatistics();

		// end sound and animation and firewors
		ServiceProvider.play(Assets.sound_end);

		for(int i = 0; i < board.count; i++)
			tiles[i].animShow(false);

		final int repeats = (int)Math.max(1, Math.min(4, (5 * board.cnt_match)
			/ (1 + board.cnt_total)));

		fireworks(repeats); // MSG_END will be posted at end if thos
	}

	private void goBack()
	{
        SceneHelper.showMenu();
	}

	private int getTileAt(float x, float y)
    {
		for(int i = 0; i < board.count; i++)
            if(tiles[i].hit(x, y))
                return i;
        return -1;
    }

	public boolean type(int key, boolean down)
    {
        if(down) {
            if (key == Keys.BACK || key == Keys.ESCAPE) {
				goBack();
			}
		}
		return true;
	}

    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {
        if(down && !drag) {
			final int sel = getTileAt(x, y);
			if(sel == -1 || tiles[sel].getState() != TileSprite.STATE_HIDDEN)
				return false;

			if(!GameHelper.select(board, sel))
				return false;

			TweenNode tn = tiles[sel].animFlip(true);

			if(board.state == Board.STATE_SEL2)
				tn.pause(0.6f).finish(this, MSG_BOARD_CHECK);
			ServiceProvider.playOne(Assets.sound_hit);
			return true;
		}
        return false;
	}
}

