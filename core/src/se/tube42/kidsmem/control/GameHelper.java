package se.tube42.kidsmem.control;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.view.*;

import static se.tube42.kidsmem.data.Constants.*;


// GameHelper contains game logic moved out to simplify GameScene
public class GameHelper
{
	public static void reset(Board b, TileSprite [] tiles, int size)
	{
		int tmp;
		int []bs = b.board;

		b.w = MODE_WS[size];
		b.h = MODE_HS[size];
		b.count = b.w * b.h;
		b.cnt_match = 0;
		b.cnt_total = 0;
		b.state = Board.STATE_NONE;

		// reset, decide which ones to include:
		for(int i = 0; i < bs.length; i++ )
			b.board[i] = i / 2;
		for(int i = 0; i < bs.length; i++ ) {
			int j = 2 * RandomService.getInt(bs.length / 2);
			int k = 2 * RandomService.getInt(bs.length / 2);
			tmp = bs[j]; bs[j] = bs[k]; bs[k] = tmp;
			j++;  k++;
			tmp = bs[j]; bs[j] = bs[k]; bs[k] = tmp;
		}

		// now reandomize the board
		for(int i = 0; i < b.count; i++ ) {
			int j = RandomService.getInt(b.count);
			tmp = bs[j]; bs[j] = bs[i]; bs[i] = tmp;
		}

		// update and reset tiles
		for(int i = 0; i < b.count; i++ ) {
			tiles[i].id = bs[i];
			tiles[i].setState(TileSprite.STATE_HIDDEN);
			System.out.println(bs[i]); // DEBUG
		}
	}

	public static boolean select(Board b, int tile)
	{
		switch(b.state) {
			case Board.STATE_NONE:
				b.sel1 = tile;
				b.state = Board.STATE_SEL1;
				break;
			case Board.STATE_SEL1:
				if(b.sel1 == tile)
					return false; // already selected
				b.sel2 = tile;
				b.state = Board.STATE_SEL2;
				break;
			default:
				return false;
		}
		return true;
	}

	public static boolean check(Board b)
	{
		if(b.state == Board.STATE_SEL2) {
			b.cnt_total++;
			b.state = Board.STATE_NONE;
			if(b.board[b.sel1] == b.board[b.sel2]) {
				b.cnt_match++;
				b.state = (b.cnt_match == b.count / 2) ? Board.STATE_WIN : Board.STATE_NONE;
				return true;
			}
		}
		return false;
	}
}
