package se.tube42.kidsmem.control;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.view.*;

import static se.tube42.kidsmem.data.Assets.*;
import static se.tube42.kidsmem.data.UI.*;
import static se.tube42.kidsmem.data.Constants.*;

public class SizeHelper
{
    public static void resize(int sw, int sh)
    {
        UI.resize(sw, sh);
		UI.scale =  (int)( 0.5f + Math.min( sw / CANVAS_W ,sh / CANVAS_H));
		if(UI.scale < 1)
			UI.scale = 1;
		else if(UI.scale > 2)
			UI.scale = 4;
    }

	/*
    private static void resize_menu(int sw, int sh)
    {
        // big menu button
        final int ps = Math.max(32, (~31) & (int)(Math.min(sw / 3, sh / 3.5f)));
		final int y2 = 2 * (sh - ps) / 3;


        // small menu button
        final int size = (~15) & Math.min(sw, sh) / 5;
        final int y0 = (int)(size * 0.7f);


        // top bar
        top_w = sw + 2;
        top_x = -1;

        top_h0 = sh - y2 - ps / 2;
        top_y0 = sh - top_h0;

        top_h1 = UI.scale * 26;
        top_y1 = sh - top_h1;

        // stats:
        stats_yc = top_y1 + top_h1 / 2;
    }

    public static void resizeBoard(int sw, int sh)
    {
        // calc board size
        final int bw = World.board_w;
        final int bh = World.board_h;

        sh -= top_h1; // remove the upper part

        size = Math.max(8,
                  ~7 & (int)(0.5f + Math.min(
                  sw / (bw + 1.2f),
                  sh / (bh + 1.2f))));

        // calc tile gap
        gap = Math.min(size , Math.min(
                  (sw - bw * size - size / 2) / (bw - 1),
                  (sh - bh * size - size / 2) / (bh - 1)
                  ));

        gap = Math.min(size , Math.min(
                  (sw - bw * size) / (bw + 1),
                  (sh - bh * size) / (bh + 1)
                  ));

        if(gap < 2) gap = 2;
        if(gap > size) gap = size;
        gap += size;

        if(World.tiles != null) {
            for(int i = 0; i < World.tiles.length; i++)
                World.tiles[i].setSize(UI.size, UI.size);
        }


        board_x0 = (int) (0.5f + sw - gap * (bw - 1) - size ) / 2;
        board_y0 = (int) (0.5f + sh - gap * (bh - 1) - size ) / 2;

        System.out.println("board origin=" + board_x0 + "," + board_y0 +
                  " size=" + size + " gap=" + gap);
	}
	*/
}
