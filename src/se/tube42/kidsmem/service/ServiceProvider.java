
package se.tube42.kidsmem.service;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;

import se.tube42.lib.ks.*;
import se.tube42.lib.tweeny.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;

public class ServiceProvider
{

    static {
    	StorageService.init("kidsmem-storage-1");
    }
	// ------------------------------------------------
    // StorageService

    public static void flushStorage()
    {
        StorageService.flush();
    }

    public static void save(String key, String data)
    {
        StorageService.save(key, data);
    }

    public static void saveLong(String key, long data)
    {
        StorageService.saveLong(key, data);
    }

    public static void save(String key, int data)
    {
        StorageService.save(key, data);
    }

    public static void save(String key, boolean data)
    {
        StorageService.save(key, data);
    }

    public static String load(String key, String default_)
    {
        return StorageService.load(key, default_);
    }

    public static boolean load(String key, boolean default_)
    {
        return StorageService.load(key, default_);
    }

    public static int load(String key, int default_)
    {
        return StorageService.load(key, default_);
    }

    public static long loadLong(String key, long default_)
    {
        return StorageService.loadLong(key, default_);
    }

    // ------------------------------------------------
    // IOService
    public static InputStream readFile(String name)
    {
        FileHandle fh = Gdx.files.internal(name);
        return fh == null ? null : fh.read();
    }

    // ------------------------------------------------
    // audio

    private static int play_mem = -1;

    public static Sound loadSound(String name)
    {
        String filename = "sound/" + name + ".ogg";
        return Gdx.audio.newSound(
                  Gdx.files.internal(filename));
    }

    public static Sound [] loadSoundArray(String name)
    {
        ArrayList<Sound> a = new ArrayList<Sound>();

        try {
            for(int i = 0; ; i++) {
                String filename = "sound/" + name + i + ".ogg";
                Sound s = Gdx.audio.newSound(
                          Gdx.files.internal(filename));
                a.add(s);
            }
        } catch(Exception e)  {
            // ignored
        }

        if(a.size() == 0) {
            System.err.println("nothing loaded :(");
            System.exit(20);
        }

        // this wont work...
        // return a.toArray();
        // so we do this instead...
        Sound [] ret = new Sound[a.size()];
        for(int i = 0; i < ret.length; i++)
            ret[i] = a.get(i);
        return ret;
    }


    public static void playOne(Sound [] ss)
    {
        if(ss == null)
            return;

        int n = RandomService.getInt(ss.length);

        // dont repeat the same index
        if(n == play_mem)
            n = (n + 1) % ss.length;

        play_mem = n;
        play(ss[n], 1);
    }

    public static void play(Sound s)
    {
        play(s, 0.5f);
    }
    public static void play(Sound s, float amp)
    {
        if(s == null )
            return;

        final float a = Math.max(0.9f, amp * 0.8f);
        s.stop();
        s.play(a);
    }


    // ------------------------------------------------
    // AssetService
    public static Texture loadTexture(String filename, boolean filter)
    {
        return AssetService.load(filename, filter);
    }

    public static TextureRegion [] divideTexture(Texture t, int w, int h)
    {
        return AssetService.divide(t, w, h);
    }


    public static BitmapFont loadFont(String name)
    {
        return AssetService.loadFont(name);
    }


    public static NinePatch loadPatch(String name, int l, int r, int t, int b)
    {
        return AssetService.loadPatch(name, l, r, t, b);
    }

    public static ParticleEffectPool loadParticle(String dir, String name)
    {
        return AssetService.loadParticle(dir, name);
    }

    // ------------------------------------------------
    // RandomService
    public static float getRandom()
    {
        return RandomService.get();
    }

    public static float getRandom(float min, float max)
    {
        return RandomService.get(min, max);
    }

    public static int getRandomInt(int  max)
    {
        return RandomService.getInt(max);
    }

    public static boolean flipCoin()
    {
    	return RandomService.flipCoin();
    }

    // ---------------------------------------------------
    public static Job addJob(Job job)
    {
        return JobService.add(job);
    }

    public static Job addMessage(MessageListener ml, long time, int msg)
    {
        return JobService.add(ml, time, msg);
    }

    public static Job addMessage(MessageListener ml, long time, int msg,
              int data0, Object data1)
    {
        return JobService.add(ml, time, msg, data0, data1, null);
    }

    public static void service(long dt)
    {
        JobService.service(dt);
        TweenManager.service( dt);
    }

}
