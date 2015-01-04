
package se.tube42.kidsmem.data;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;

import se.tube42.kidsmem.*;
import se.tube42.kidsmem.item.*;


public class World
implements StateHolder
{
    // game state
    public static final int
          STATE_NONE = 0,
          STATE_TEMP = 1,
          STATE_SEL1 = 2,
          STATE_SEL2 = 3,
          STATE_CHECK = 4,
          STATE_END = 5
          ;
    public static int state = STATE_SEL1;

    // sprites etc
    public static TileSprite [] tiles;

    // screen and board
    public static SceneManager mgr;
    public static Item color_bg;

    public static int board_w, board_h;
    public static TileSprite sel1, sel2;
    public static TileSprite [] board;
    public static ParticleItem [] particles;
    public static FireworkItem [] fireworks;

    // misc:
    public static SystemHandler sys = null;


    // counters
    public static int cnt_total, cnt_match;

    // ----------------------------
    // StateHolder
    public void setState(int state) { this.state = state; }
    public int getState() { return state; }
}