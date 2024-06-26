package com.example.sw0b_001.Models.Notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;

import com.example.sw0b_001.BuildConfig;
import com.example.sw0b_001.Database.Datastore;
import com.example.sw0b_001.Models.RabbitMQ;
import com.example.sw0b_001.Security.SecurityHandler;
import com.example.sw0b_001.Security.SecurityHelpers;
import com.example.sw0b_001.Security.SecurityRSA;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NotificationsHandler {

    public static final String NOTIFICATIONS_TAG = "notifications-listener-tag";
    public static final String UNIQUE_NAME = "notifications-listener-unique-name";

    public static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Notifications "
                    + " ADD COLUMN seen BOOLEAN DEFAULT 0");
        }
    };

    public static void storeNotification(Context context, long id, String message, String type) {
        Notifications notifications = new Notifications();
        notifications.id = id;
        notifications.message = message;
        notifications.type = type;
        notifications.date = new Date().getTime();

        // TODO: add to some database and get in notifications
//        Datastore databaseConnector = Room.databaseBuilder(context,
//                Datastore.class, Datastore.DatabaseName)
//                .addMigrations(MIGRATION_9_10)
//                .build();
        Datastore databaseConnector = Room.databaseBuilder(context,
                        Datastore.class, Datastore.DatabaseName)
                .build();
        NotificationsDAO notificationsDAO = databaseConnector.notificationsDAO();
        notificationsDAO.insert(notifications);
    }

    public static void beginNotificationsListeners(Context context) {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        try {
            PeriodicWorkRequest routeMessageWorkRequest = new PeriodicWorkRequest.Builder(NotificationsListener.class,
                    PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                    .setConstraints(constraints)
                    .addTag(NotificationsHandler.NOTIFICATIONS_TAG)
                    .build();


            WorkManager workManager = WorkManager.getInstance(context);
            workManager.enqueueUniquePeriodicWork(
                    NotificationsHandler.UNIQUE_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    routeMessageWorkRequest);

        }
        catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
