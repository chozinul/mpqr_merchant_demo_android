package com.mastercard.labs.mpqrmerchant.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.event.TransactionsUpdateEvent;
import com.mastercard.labs.mpqrmerchant.main.MainActivity;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.transaction.detail.TransactionDetailActivity;
import com.mastercard.labs.mpqrmerchant.transaction.list.TransactionListActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/14/17
 */
public class FirebaseListener extends FirebaseMessagingService {
    private static final String TAG = "FirebaseListener";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault());

    public FirebaseListener() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String message = data.get("data");
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message: " + message);

        Transaction transaction = getTransaction(remoteMessage.getData());
        if (transaction == null) {
            Log.e(TAG, "Transaction message received but could not be parsed properly");
        } else {
            EventBus.getDefault().post(new TransactionsUpdateEvent());
            showTransactionNotification(transaction);
        }
    }

    private Transaction getTransaction(Map<String, String> data) {
        try {
            Transaction transaction = new Transaction();
            transaction.setTransactionAmount(Double.parseDouble(data.get("transactionAmount")));
            transaction.setTipAmount(Double.parseDouble(data.get("tipAmount")));
            transaction.setCurrencyNumericCode(data.get("currencyNumericCode"));
            transaction.setTransactionDate(dateFormat.parse(data.get("transactionDate")));
            transaction.setReferenceId(data.get("referenceId"));
            transaction.setTerminalNumber(data.get("terminalNumber"));
            transaction.setInvoiceNumber(data.get("invoiceNumber"));

            transaction = RealmDataSource.getInstance().saveTransaction(LoginManager.getInstance().getLoggedInUserId(), transaction);

            return transaction;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void showTransactionNotification(Transaction transaction) {
        Intent intent = TransactionDetailActivity.newIntent(this, transaction.getReferenceId());
        intent.setData(Uri.parse("mpqr://merchant/" + transaction.getReferenceId()));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        stackBuilder.addNextIntent(TransactionListActivity.newIntent(this, LoginManager.getInstance().getLoggedInUserId()));
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Transaction completed")
                .setContentText(String.format(Locale.getDefault(), "Transaction completed of amount: %s %,.2f", transaction.getCurrencyCode(), transaction.getTotal()))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = notificationBuilder.build();

        int uniqueId = 0;
        if (transaction.getReferenceId() != null) {
            uniqueId = transaction.getReferenceId().hashCode();
        }
        notificationManager.notify(uniqueId, notification);
    }
}
