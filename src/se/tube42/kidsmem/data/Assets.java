
package se.tube42.kidsmem.data;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;


public final class Assets
{


    // -------------------------------------------------
    // ninepatch
    public static NinePatch np_shadow;
    public static NinePatch np_botshadow;


    // -------------------------------------------------
    // fonts
    public static BitmapFont font1;

    // -------------------------------------------------
    // particles
    public static ParticleEffectPool pep_explode;
    public static ParticleEffectPool pep_firework, pep_trail;

    // -------------------------------------------------
    // texture
    public static Texture tex_candy1, tex_candy2, tex_tiles;
    public static Texture tex_menu, tex_menu_shadow;

    // -------------------------------------------------
    // regions
    public static TextureRegion [] reg_candy1, reg_candy2, reg_tiles;
    public static TextureRegion [] reg_rect;
    public static TextureRegion [] reg_menu, reg_menu_shadow;


    // -------------------------------------------------
    // Sounds
    public static Sound [] sound_yes, sound_no, sound_hit;
    public static Sound sound_start, sound_end;

    public static Sound talk_on, talk_off, talk_play;
    public static Sound [] talk_hardness;
}
