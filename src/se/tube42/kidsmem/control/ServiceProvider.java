package se.tube42.kidsmem.control;

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

    static public void init()
    {
    	StorageService.init("kidsmem-storage-1");
    }


    public static void service(long dt)
    {
        JobService.service(dt);
        TweenManager.service( dt);
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
        if(s == null || !Settings.sound)
            return;

        final float a = Math.max(0.9f, amp * 0.8f);
        s.stop();
        s.play(a);
    }
}
