package pl.pjatk.kn_miniprojekt5.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
    }

    @Override
    public void onDisabled(Context context)
    {
    }

    @Override
    public void onReceive(Context context, Intent i)
    {
        super.onReceive(context, i);

        if (i.getAction().equals("pl.pjatk.kn_miniprojekt5.widget.action.visitWWW"))
        {
            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pja.edu.pl"));
            browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browse);
        }

        if (i.getAction().equals("pl.pjatk.kn_miniprojekt5.widget.action.setBackground"))
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            int id = preferences.getInt("bg", R.drawable.pic2);
            int nextId = 0;
            if(id == R.drawable.pic1)
            {
                nextId = R.drawable.pic2;
            }
            else
            {
                nextId = R.drawable.pic1;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), nextId);
            remoteViews.setImageViewBitmap(R.id.imageViewBitmap, bitmap);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids)
            {
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
            }

            SharedPreferences.Editor sharedPreferencesEditor = preferences.edit();
            sharedPreferencesEditor.putInt("bg", nextId);
            sharedPreferencesEditor.apply();
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        int bitmapId = 0;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        Intent goToUrl = new Intent("pl.pjatk.kn_miniprojekt5.widget.action.visitWWW");
        Intent setBackground = new Intent("pl.pjatk.kn_miniprojekt5.widget.action.setBackground");
        Intent start = new Intent(context, MediaService.class);
        Intent pause = new Intent(context, MediaService.class);
        Intent stop = new Intent(context, MediaService.class);
        Intent skip = new Intent(context, MediaService.class);

        start.setAction("pl.pjatk.kn_miniprojekt5.widget.action.start");
        pause.setAction("pl.pjatk.kn_miniprojekt5.widget.action.pause");
        stop.setAction("pl.pjatk.kn_miniprojekt5.widget.action.stop");
        skip.setAction("pl.pjatk.kn_miniprojekt5.widget.action.skip");

        PendingIntent pendingGoToUrlIntent = PendingIntent.getBroadcast(context, 0, goToUrl, 0);
        PendingIntent pendingSetBackgroundIntent = PendingIntent.getBroadcast(context, 0, setBackground, 0);
        PendingIntent pendingStartIntent = PendingIntent.getService(context, 0, start, 0);
        PendingIntent pendingPauseIntent = PendingIntent.getService(context, 0, pause, 0);
        PendingIntent pendingStopIntent = PendingIntent.getService(context, 0, stop, 0);
        PendingIntent pendingSkipIntent = PendingIntent.getService(context, 0, skip, 0);

        remoteViews.setOnClickPendingIntent(R.id.visitWWWButton, pendingGoToUrlIntent);
        remoteViews.setOnClickPendingIntent(R.id.setBackgroundButton, pendingSetBackgroundIntent);
        remoteViews.setOnClickPendingIntent(R.id.startButton, pendingStartIntent);
        remoteViews.setOnClickPendingIntent(R.id.pauseButton, pendingPauseIntent);
        remoteViews.setOnClickPendingIntent(R.id.stopButton, pendingStopIntent);
        remoteViews.setOnClickPendingIntent(R.id.skipButton, pendingSkipIntent);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        bitmapId = sharedPreferences.getInt("bg", R.drawable.pic1);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        remoteViews.setImageViewBitmap(R.id.imageViewBitmap, bitmap);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}

