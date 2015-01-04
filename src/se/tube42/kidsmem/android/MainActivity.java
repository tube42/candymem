package se.tube42.kidsmem.android;

import android.os.Bundle;
import android.app.Activity;
import android.net.Uri;
import android.content.Intent;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import se.tube42.kidsmem.*;
import se.tube42.kidsmem.data.*;

public class MainActivity
extends AndroidApplication
implements SystemHandler
{
    private boolean is_full = false;

    class UpdateFullscreen implements Runnable {
        @Override public void run() {
            final int flags = 0x00001000 | 0x00000004 | 0x00000002;
            try {
                int opt = getWindow().getDecorView().getSystemUiVisibility();
                int optold = opt;
                if(is_full) opt |= flags;
                else opt &= ~flags;
                getWindow().getDecorView().setSystemUiVisibility(opt);

                System.out.println("*** SystemUiVisibility " + optold + " -> " + opt); // DEBUG

            } catch(Exception exx) {
                exx.printStackTrace();
            }
        }
    }


    private Runnable update_fullscreen = new UpdateFullscreen();

    // ---------------------------------------
    // SystemHandler
    private boolean try_run(Runnable r)
    {
        try {
            runOnUiThread(r);
            return true;
        } catch(Exception exx) {
            System.out.println("[2] " + exx);
            return false;
        }
    }

    public void showMessage(final String text)
    {
        try_run(new Runnable() {
            @Override public void run(){
                try {
                    Toast t = Toast.makeText(MainActivity.this, text, 12000);
                    t.show();
                } catch(Exception exx) {
                System.out.println("ERROR: " + exx);
            }}});
    }

    public void setFullscreen(final boolean new_full)
    {
        if(new_full == is_full)
            return;

        is_full = new_full;
        try_run(update_fullscreen);
    }

    // --------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        World.sys = this;

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        // cfg.useGL20 = true;
        cfg.useWakelock = true;

        initialize(new KidsMemoryApp(), cfg);
    }
}
