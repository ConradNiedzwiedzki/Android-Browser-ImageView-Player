package pl.pjatk.kn_miniprojekt5.widget;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class MediaService extends Service
{
    MediaPlayer mediaPlayer = null;

    @Override
    public void onCreate()
    {
    }

    @Override
    public int onStartCommand(Intent i, int flags, int id)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean flag;
        int next;
        String action = i.getAction();
        int sId = sharedPreferences.getInt("sound", R.raw.sound1);

        if(sId == R.raw.sound1)
        {
            next = R.raw.sound2;
        }
        else
        {
            next = R.raw.sound1;
        }

        if (mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer.create(this, sId);
        }

        if (action == "pl.pjatk.kn_miniprojekt5.widget.action.start" && !mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }

        if (action == "pl.pjatk.kn_miniprojekt5.widget.action.pause" && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }

        if (action == "pl.pjatk.kn_miniprojekt5.widget.action.stop")
        {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
            mediaPlayer = MediaPlayer.create(this, sId);
        }

        if (action == "pl.pjatk.kn_miniprojekt5.widget.action.skip")
        {
            flag = false;
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                flag = true;
            }

            mediaPlayer = MediaPlayer.create(this, next);
            if (flag)
            {
                mediaPlayer.start();
            }

            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putInt("sound", next);
            sharedPreferencesEditor.apply();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
    }
}
