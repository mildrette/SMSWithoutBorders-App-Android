package com.example.sw0b_001.Models.Platforms;

import android.content.Context;
import android.content.Intent;

import androidx.room.Room;

import com.example.sw0b_001.Database.Datastore;
import com.example.sw0b_001.EmailComposeActivity;
import com.example.sw0b_001.TextComposeActivity;

import java.util.ArrayList;
import java.util.List;

public class PlatformsHandler {
    static public Intent getIntent(Context context, String platform_name, String type) {
        Intent intent = null;
        switch(type) {
            case "email": {
                intent = new Intent(context, EmailComposeActivity.class);
                break;
            }

            case "text": {
                intent = new Intent(context, TextComposeActivity.class);
                break;
            }
            // TODO: put a default here
        }
        if(intent != null ) {
            intent.putExtra("platform_name", platform_name);
        }
        return intent;
    }

    private static Platform fetchPlatform(Context context, long platformID) throws Throwable {
        final Platform[] platforms = new Platform[1];
        Thread fetchPlatformThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore databaseConnection = Room.databaseBuilder(context,
                        Datastore.class, Datastore.DatabaseName)
                        .fallbackToDestructiveMigration()
                        .build();

                PlatformDao platformDao = databaseConnection.platformDao();
                platforms[0] = platformDao.get(platformID);
            }
        });

        try {
            fetchPlatformThread.start();
            fetchPlatformThread.join();
        } catch (InterruptedException e) {
            throw e.fillInStackTrace();
        }

        return platforms[0];
    }

    public static Platform getPlatform(Context context, long platformId) {
        Platform platform = new Platform();
        try {
            platform = fetchPlatform(context, platformId);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return platform;
    }

    public static List<Platform> getAllPlatforms(Context context) {
        final List<Platform>[] platforms = new List[]{new ArrayList<>()};

        Thread fetchPlatformsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore platformDb = Room.databaseBuilder(context,
                        Datastore.class, Datastore.DatabaseName)
                        .fallbackToDestructiveMigration()
                        .build();
                PlatformDao platformsDao = platformDb.platformDao();
                platforms[0] = platformsDao.getAll();
            }
        });
        fetchPlatformsThread.start();
        try {
            fetchPlatformsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return platforms[0];
    }
}
