package se.tube42.kidsmem.data;

import static se.tube42.kidsmem.data.Constants.*;

// Board contains game board state
public class Board
{
    // game state
    public static final int
		STATE_NONE = 0,
		STATE_SEL1 = 2,
		STATE_SEL2 = 3,
		STATE_WIN = 5
		;

	public int state;
	public int count, w, h;
	public int []board;
    public int cnt_total, cnt_match;
	public int sel1, sel2;

	public Board()
	{
		board = new int[COUNT_W * COUNT_H];
	}
}
