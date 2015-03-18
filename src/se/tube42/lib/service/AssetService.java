
package se.tube42.lib.service;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.files.*;

import java.util.*;


public class AssetService
{

    
    public static ShaderProgram loadShader(String vertexfile, String fragmentfile)
    {
        FileHandle fv = Gdx.files.internal(vertexfile);
        FileHandle ff = Gdx.files.internal(fragmentfile);
        return new ShaderProgram(fv, ff);
    }
    
    // -------------------------------------------------------------------
    // fonts
    public static BitmapFont loadFont(String name)
    {

        BitmapFont ret = new BitmapFont(
                  Gdx.files.internal(name + ".fnt"),
                  Gdx.files.internal(name + ".png"),
                  false, true);

        return ret;
    }


    public static ParticleEffectPool loadParticle(String dir, String name)
    {
    	ParticleEffect effect = new ParticleEffect();
    	effect.load(
                  Gdx.files.internal(dir + name + ".p"),
                  Gdx.files.internal(dir)
                  );

    	ParticleEffectPool pool = new ParticleEffectPool(effect, 0, 70);

    	return pool;
    }

    // -------------------------------------------------------------------

    public static NinePatch loadPatch(String filename, int l, int r, int t, int b)
    {
        final Texture te = new Texture(Gdx.files.internal(filename));
        // t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        final TextureRegion tr = new TextureRegion(te);
        return new NinePatch(tr, l, r, t, b);


    }


    // -------------------------------------------------------------------
    // texture
    public static Texture load(String filename, boolean filter)
    {
        final Texture texture = new Texture( Gdx.files.internal(filename));
        final Texture.TextureFilter tf = filter ?
              Texture.TextureFilter.Linear : Texture.TextureFilter.Nearest;
        texture.setFilter(tf, tf);
        return texture;
    }


    public static void correctRegionBorders(TextureRegion tr)
    {
        final Texture t = tr.getTexture();

        if(t.getMinFilter() != Texture.TextureFilter.Linear)
            return;

        if(t.getWidth() < 2 || t.getHeight() < 2)
            return;

        final float hpw = 0.5f / t.getWidth();
        final float hph = 0.6f / t.getHeight();

        tr.setU ( tr.getU () + hpw);
        tr.setV ( tr.getV () + hph);
        tr.setU2( tr.getU2() - hpw);
        tr.setV2( tr.getV2() - hph);

    }

    public static TextureRegion [] divide(Texture t, int w, int h)
    {
        final int tw = t.getWidth();
        final int th = t.getHeight();
        final int cw = tw / w;
        final int ch = th / h;

        if(cw < 1 || ch < 1) return null;


        final TextureRegion [] ret = new TextureRegion[cw * ch];

        for(int a = 0; a < ch; a++) {
            for(int b = 0; b < cw; b++) {
                TextureRegion t2 = new TextureRegion(t, b * w, a * h, w, h);
                ret[b + a * cw] = t2;
            }
        }

        for(int i = 0; i < ret.length; i++)
            correctRegionBorders(ret[i]);
        return ret;
    }

    public static TextureRegion [] divide(TextureRegion t,
              int w, int h, boolean correct_borders)
    {
        final int tw = t.getRegionWidth();
        final int th = t.getRegionHeight();
        final int cw = tw / w;
        final int ch = th / h;
        if(cw < 1 || ch < 1) return null;


        final TextureRegion [][] tmp = t.split(cw, ch);
        final TextureRegion [] ret = new TextureRegion[cw * ch];

        for(int a = 0; a < h; a++) {
            for(int b = 0; b < w; b++) {
                ret[b + a * cw] = tmp[a][b];
            }
        }

        if(correct_borders) {
            for(int i = 0; i < ret.length; i++)
                correctRegionBorders(ret[i]);
        }

        return ret;
    }


}
