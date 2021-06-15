package com.creator.rewardsapp.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadNearbyEvents;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.MyCollectionNames;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotificationJobService extends JobIntentService implements LoadNearbyEvents {
    private final String TAG = getClass().getSimpleName();
    SharedPreferences settingsShrepref;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "MyServicePref";

    public static final String KEY_USED_DATE = "UsedDate";

    //To Perform background work
    LoadNearbyEvents loadNearbyEvents;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private int totalWinners;


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationJobService.class, 123, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate -> " + TAG);
        settingsShrepref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settingsShrepref.edit();
        loadNearbyEvents = this;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");
//        String input = intent.getStringExtra("inputExtra");
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, "Execute step" + " - " + i);
            if (isStopped()) return;
            SystemClock.sleep(1000);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Log.d(TAG, "onHandleWork: DATE=> " + formatter.format(date));

        //LoadOffers and perform winnerDeclaration Task
        loadOffers();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
        Date c = Calendar.getInstance().getTime();
        String formattedDate = sdf.format(c);
        editor.putString(KEY_USED_DATE, formattedDate);
        editor.commit();

    }

    private void loadOffers() {
        /*
         * Load all the document of Offers collection one by one
         * and update the UI
         * Done
         * */
        Log.e(TAG, "loadOffers: called");
        db.collection(MyCollectionNames.OFFERS)
                .get()
                .addOnCompleteListener(task -> {
                    Log.e("checker", "onComplete: called");

                    List<CreateOfferObject> products = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                            Log.e("checker", "loadTemplates: " + bannerSnapshot.getData());
                            CreateOfferObject product = bannerSnapshot.toObject(CreateOfferObject.class);
                            products.add(product);
                        }
                        loadNearbyEvents.onNearbyLoadSuccess(products);
                    }
                })
                .addOnFailureListener(e -> loadNearbyEvents.onNearbyLoadFailed(e.getMessage()));
    }

    private void checkExpiryAndDeclareWinners(List<CreateOfferObject> templates) throws ParseException {
        List<CreateOfferObject> expired = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy");
        for (CreateOfferObject c : templates) {
            String valid_until = c.getEndDate();
            Date strDate = sdf.parse(valid_until);

            assert strDate != null;
            if (System.currentTimeMillis() > strDate.getTime()) {
                Log.d(TAG, "checkExpiryAndDeclareWinners: EXPIRED "+valid_until);
                expired.add(c);
            }
        }
        Log.d(TAG, "checkExpiryAndDeclareWinners: \n");
        List<String> expireOffers = new ArrayList<>();
        List<String> expiredShops = new ArrayList<>();


        for (CreateOfferObject x : expired) {
            Log.d(TAG, "Dates " + x.getEndDate());
            expireOffers.add(x.getOfferId());
            expiredShops.add(x.getCreatorId());
        }
        Log.d(TAG, "checkExpiryAndDeclareWinners: ExpiredOffers[]= "+expireOffers);
        Log.d(TAG, "checkExpiryAndDeclareWinners: ExpiredShops[]= "+expiredShops);
        if (expireOffers.isEmpty())
            return;
        Collections.sort(expireOffers);
        db.collection(MyCollectionNames.ALLCUSTOMERS)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments() != null) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    int pos = Collections.binarySearch(expireOffers, documentSnapshot.getId());
                    if (pos > -1) {
                        String currentOfferId = documentSnapshot.get("currentOfferId").toString();

                        db.collection(MyCollectionNames.OFFERS).document(currentOfferId)
                                .get().addOnCompleteListener(tk -> {
                            boolean isDeclared = tk.getResult().getBoolean("winnerDeclared");
                            if (!isDeclared) {
                                List<String> totalCustomers = (List<String>) documentSnapshot.get("customerId");
                                Log.d(TAG, "creator: " + currentOfferId + " => " + totalCustomers);

                                // Declare winners and update isDeclared = true in Offers Collection
                                // Get totalWinner
                                assert totalCustomers != null;
                                getTotalWinnerCount(currentOfferId, totalCustomers.size(), totalCustomers);
                            }
                        });


                    }


                }


            }
        });


    }

    private void getTotalWinnerCount(String currentOfferId, int size, List<String> totalCustomers) {
        db.collection(MyCollectionNames.OFFERS).document(currentOfferId).get()
                .addOnCompleteListener(task -> {
                    String outOfTotalField = task.getResult().getString("outOfTotal").toString();
                    Log.d(TAG, "Ratio Calculation =======> getTotalWinnerCount: outOfTotal: => " + outOfTotalField);
                    this.totalWinners = Integer.parseInt(outOfTotalField);
                    totalWinners = totalWinners == 0 ? (int) totalWinners :
                            (int) Math.round(((double) 1 / (double) totalWinners) * size);

                    Map<String, Object> winners = new HashMap<>();
                    if (totalWinners > 0) {
                        List<String> winnerList = getWinnerList(totalWinners, totalCustomers);
                        winners.put("winnerList", winnerList);
                        performThreadingonNetworkNotifications(winnerList);
                    } else
                        winners.put("winnerList", FieldValue.arrayUnion());


                    setWinnersinOffers(currentOfferId, winners);

                    if (totalCustomers != null)
                        winners.put("winnerDeclared", true);

                    //  Setting winners
                    winners.put("query", true);
                    updateWinnerStatusinOffers(currentOfferId, winners);
                });
    }
    private void performThreadingonNetworkNotifications(List<String> winnerList) {

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Log.d(TAG, "handleMessage: Executed===> " + "SUCCCESSSSS");
                } else {
                    Log.d(TAG, "handleMessage: ERRRRRRROOOOOR");
                }
            }
        };
        Thread thread = new Thread() {
            @Override
            public void run() {

                if (doSomeWork(winnerList)) {
                    //we can't update the UI from here so we'll signal our handler and it will do it for us.
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        };
        thread.start();
    }

    private boolean doSomeWork(List<String> winners) {
        return sendNotificationToEachUser(winners);
    }
    private boolean sendNotificationToEachUser(List<String> winnerList) {
        Log.d(TAG, "sendNotificationToEachUser() called with: winnerList = [" + winnerList + "]");
        PushNotificationHelper push = new PushNotificationHelper();
        for (String winner : winnerList) {

            StringBuilder sendTo=new StringBuilder();
            sendTo.append("/").append("topics").append("/").append(winner.trim());
            try {
                String res = push.sendPushNotification(sendTo.toString());
                Log.d(TAG, "sendNotificationToEachUser: status=> " + res);
            } catch (IOException e) {
                Log.d(TAG, "sendNotificationToEachUser: ERROR=> " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    private void updateWinnerStatusinOffers(String currentOfferId, Map<String, Object> winners) {
        db.collection(MyCollectionNames.OFFERS)
                .document(currentOfferId)
                .set(winners, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Winners in OfferList Set: SUCCESS ");
                }).addOnFailureListener(e -> {
            Log.d(TAG, "Winners in OfferList Set: FAILED=> " + e.getMessage());
            Log.d(TAG, "updateWinnerStatusinOffers checkExpiryAndDeclareWinners: FAILED=> "
                    + e.getMessage());
        });
    }

    private List<String> getWinnerList(int totalWinners, List<String> totalCustomers) {
        // Find n winners
        Collections.shuffle(totalCustomers);
        List<String> winList = new LinkedList<>();
        for (int i = 0; i < totalWinners; i++) {
            String x = totalCustomers.get(i);
            Log.d(TAG, "winner- " + (i + 1) + " => " + x);
            winList.add(x);
        }

        //  Setting winners
//        db.collection("AllCustomerOffers")
//                .document(creatorId)
//                .set(expiredShops);
        return winList;
    }


    private void setWinnersinOffers(String currentOfferId, Map<String, Object> winners) {
        db.collection(MyCollectionNames.ALLCUSTOMERS)
                .document(currentOfferId)
                .set(winners, SetOptions.merge());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        return super.onStopCurrentWork();
    }

    @Override
    public void onNearbyLoadSuccess(List<CreateOfferObject> templates) {
        try {
            checkExpiryAndDeclareWinners(templates);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Error in Declaring Winners: " + e.getMessage());
        }
    }

    @Override
    public void onNearbyLoadFailed(String message) {
        Log.d(TAG, "onNearbyLoadFailed: Error found: => " + message);
    }
}
