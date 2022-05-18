package com.example.sw0b_001.Models;

import android.content.Context;
import android.util.Base64;

import androidx.room.Room;

import com.example.sw0b_001.Database.Datastore;
import com.example.sw0b_001.Models.GatewayServers.GatewayServer;
import com.example.sw0b_001.Models.GatewayServers.GatewayServersDAO;
import com.example.sw0b_001.Models.GatewayServers.GatewayServersHandler;
import com.example.sw0b_001.Security.SecurityHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PublisherHandler {

    private static List<GatewayServer> getGatewayServers(Context context) throws Throwable {
        Datastore databaseConnection = Room.databaseBuilder(context,
                Datastore.class, Datastore.DatabaseName).build();
        final List<GatewayServer>[] gatewayServers = new List[]{new ArrayList<>()};
        Thread fetchGatewayClientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GatewayServersDAO gatewayServerDao = databaseConnection.gatewayServersDAO();
                gatewayServers[0] = gatewayServerDao.getAll();
            }
        });

        try {
            fetchGatewayClientThread.start();
            fetchGatewayClientThread.join();
        } catch (InterruptedException e) {
            throw e.fillInStackTrace();
        }

        return gatewayServers[0];
    }


    public static String[] getEncryptEmailContent(Context context, String emailContent) throws Throwable {
        SecurityHandler securityHandler = new SecurityHandler(context);
        String randomStringForIv = securityHandler.generateRandom(16);

        GatewayServer gatewayServer = getGatewayServers(context).get(0);
        String keystoreAlias = GatewayServersHandler.buildKeyStoreAlias(gatewayServer.getUrl() );

        try {
            byte[] encryptedEmailContent = securityHandler.encryptWithSharedKeyAES(randomStringForIv.getBytes(), emailContent.getBytes(StandardCharsets.UTF_8), keystoreAlias);

            return new String[]{randomStringForIv, Base64.encodeToString(encryptedEmailContent, Base64.NO_WRAP)};
        }
        catch(Exception e ) {
            throw new Throwable(e);
        }
    }

    public static String formatForPublishing(Context context, String formattedContent) throws Throwable {
        try {
            String[] encryptedIVEmailContent = getEncryptEmailContent(context, formattedContent);

            String IV = encryptedIVEmailContent[0];
            String encryptedEmailContent = encryptedIVEmailContent[1];

            final String encryptedContent = IV + encryptedEmailContent;

            return encryptedContent;
        }
        catch(Exception e ) {
            throw new Throwable(e);
        }
    }
}
