package com.example.sw0b_001.ListPlatforms.Emails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sw0b_001.R;
import com.example.sw0b_001.SecurityLayer;

import java.security.KeyStore;
import java.util.ArrayList;

public class EmailSendMessageActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.sw0b_001.MESSAGE";

    String SMS_SENT = "SENT";
    String SMS_DELIVERED = "DELIVERED";

    SecurityLayer securityLayer;
    KeyStore keyStore;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);

        loadComposeInformation();
        loadPreviousMessages();

        //        Toolbar myToolbar = (Toolbar) findViewById(R.id.platform_toolbar);
//        setSupportActionBar(myToolbar);
//        // Get a support ActionBar corresponding to this toolbar
//        ActionBar ab = getSupportActionBar();
//
//        // Enable the Up button
//        ab.setDisplayHomeAsUpEnabled(true);
//
//        ab.setTitle(PLATFORM_NAME);
    }

    private void loadComposeInformation() {
        String email = getIntent().getStringExtra("text1");
        String subject = getIntent().getStringExtra("text2");
        String platform = getIntent().getStringExtra("platform_name");

        TextView tvEmail = findViewById(R.id.static_compose_to);
        TextView tvSubject = findViewById(R.id.static_compose_subject);
        TextView tvPlatform = findViewById(R.id.static_compose_platform);

        tvEmail.setText("to - " + email);
        tvSubject.setText("subject - " + subject);
        tvPlatform.setText("platform - " + platform);
    }

    private void loadPreviousMessages() {
        listView = findViewById(R.id.message_list);
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        try {
            listView.setAdapter(itemsAdapter);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickSend(View view) {
        TextView message = findViewById(R.id.layer_email_subject);
        String text = message.getText().toString();

        itemsAdapter.add(text);
        itemsAdapter.notifyDataSetChanged();

        message.setText("");
    }

    public void smsFailed() {

    }


    /*
    public void sendMessage(View view) {
        EditText eNumber = findViewById(R.id.editPhonenumber);
        EditText eText = findViewById(R.id.editMessage);

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("SENT"), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("DELIVERED"), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_DELIVERED));


        String number = eNumber.getText().toString();
        String plainText = eText.getText().toString();
        if(plainText.isEmpty()) {
            Toast.makeText(this, "Text Cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if(checkPermission(Manifest.permission.SEND_SMS)) {
                String IV = new String(securityLayer.getIV(), "UTF-8");
                String transmissionText = IV + ":" + plainText;
                byte[] encryptedText = securityLayer.encrypt(transmissionText);

                System.out.println("Transmission String: " + transmissionText);
                System.out.println("[+] Decrypted: " + new String(securityLayer.decrypt(encryptedText), "UTF-8"));


                String strEncryptedText = Base64.encodeToString(encryptedText, Base64.URL_SAFE);
                System.out.println("Transmission message: " + strEncryptedText);

                //TODO: Research what to do in case of a double sim phone
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, strEncryptedText, sentPendingIntent, deliveredPendingIntent);
                Toast.makeText(this, "Sending SMS....", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
            }
        } catch (BadPaddingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Internal Error while encrypting...", Toast.LENGTH_LONG).show();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            Toast.makeText(this, "Internal Error while encrypting...", Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);

        return (check == PackageManager.PERMISSION_GRANTED);
    }
     */

}