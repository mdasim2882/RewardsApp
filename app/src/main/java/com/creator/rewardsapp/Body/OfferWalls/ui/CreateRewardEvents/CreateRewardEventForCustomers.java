package com.creator.rewardsapp.Body.OfferWalls.ui.CreateRewardEvents;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.creator.rewardsapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.creator.rewardsapp.Body.Authentication.SignUpActivity.MANDATORY;


public class CreateRewardEventForCustomers extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private EditText cStartDate, cEndDate;
    private EditText cContactno, cShopName;
    private EditText cOfferAmount, cProduct, cParticipants;
    private TextInputLayout cLLStartDate, cLLEndDate;
    private TextInputLayout cLLContactno, cLLShopName;
    private TextInputLayout cLLOfferAmount, cLLProduct, cLLParticipants;
    private MaterialButton createOfferbtn, addMoreOfferButton;
    private LinearLayout formSecondOffer;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    TextView t1, t2, t3, t4, t5;
    TextView s1, s2, s3, s4, s5;
    private boolean isAdded;
    private EditText moneySecondOffer, participantSecondOffer, productSecondOffer;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.create_reward_events, container, false);
        initializeViews(root);
        findCalendarAndsetText();
        return root;
    }

    private void initializeViews(View root) {

        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null)
            floatingActionButton.hide();

        createOfferbtn = root.findViewById(R.id.createOfferBtn);
        addMoreOfferButton = root.findViewById(R.id.addMoreofferbtn);
        cStartDate = root.findViewById(R.id.cStartDate);
        cEndDate = root.findViewById(R.id.cEndDate);
        cContactno = root.findViewById(R.id.create_offer_edt_txt_contact_no);
        cShopName = root.findViewById(R.id.create_offer_edt_txt_shop_name);

        cOfferAmount = root.findViewById(R.id.create_offer_amount);
        cParticipants = root.findViewById(R.id.create_offer_peoples_limit);
        cProduct = root.findViewById(R.id.create_offer_product);

        //Layouts
        cLLStartDate = root.findViewById(R.id.ll_start_date);
        cLLEndDate = root.findViewById(R.id.ll_end_date);
        cLLContactno = root.findViewById(R.id.ll_shopContactno);
        cLLShopName = root.findViewById(R.id.ll_shopname);
        formSecondOffer = root.findViewById(R.id.formSecondOffer);

        t1 = root.findViewById(R.id.t1);
        t2 = root.findViewById(R.id.t2);
        t3 = root.findViewById(R.id.t3);
        t4 = root.findViewById(R.id.t4);
        t5 = root.findViewById(R.id.t5);

        s1 = root.findViewById(R.id.s1);
        s2 = root.findViewById(R.id.s2);
        s3 = root.findViewById(R.id.s3);
        s4 = root.findViewById(R.id.s4);
        s5 = root.findViewById(R.id.s5);

        // Creating Offer
        createOfferbtn.setOnClickListener(v -> {
//            showToaster("Creating Offer...");
            if (isStartDateValid() && isEndDateValid() && isShopNameValid() && isContactnoValid()) {
                extractInputs();
            }
        });
        moneySecondOffer = (EditText) root.findViewById(R.id.screate_offer_amount);
        participantSecondOffer = (EditText) root.findViewById(R.id.screate_offer_peoples_limit);
        productSecondOffer = (EditText) root.findViewById(R.id.screate_offer_product);
        // Adding more offer iff user wants
        isAdded = false;
        addMoreOfferButton.setOnClickListener(v -> {
            if (!isAdded) {
                formSecondOffer.setVisibility(View.VISIBLE);
                productSecondOffer.requestFocus();
//                InputMethodManager systemService = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                systemService.showSoftInput(productSecondOffer, InputMethodManager.SHOW_IMPLICIT);
                isAdded = true;
                addMoreOfferButton.setIconResource(R.drawable.ic_round_remove_circle_24);
                addMoreOfferButton.setText("Remove offer");
            } else {
                formSecondOffer.setVisibility(View.GONE);
                addMoreOfferButton.setIconResource(R.drawable.ic_round_add_circle_24);
                addMoreOfferButton.setText("Add more offer");
                isAdded = false;
            }


        });


    }


    private void extractInputs() {
        String getStartDate = cStartDate.getText().toString();
        String getEndDate = cEndDate.getText().toString();
        String getShopName = cShopName.getText().toString();
        String getContactno = cLLContactno.getPrefixText().toString() + " " + cContactno.getText().toString();


        String getOfferAmount = cOfferAmount.getText().toString();
        String getParticipantsCount = cParticipants.getText().toString();
        String getOfferProduct = cProduct.getText().toString();
        String offer = t1.getText().toString() + " " + getOfferAmount + " " + t2.getText().toString() +
                " " + getParticipantsCount + " " + t3.getText().toString() +
                " " + t4.getText().toString() + " " + getOfferProduct + " " + t5.getText().toString();

        if (!isValidOfferDetails(getOfferAmount) || !isValidOfferDetails(getParticipantsCount) || !isValidOfferDetails(getOfferProduct)) {
            FixedVariable.showToaster(getActivity(), "Please, fill offer details to continue");
            return;
        }


        CreateOfferObject createOffer = new CreateOfferObject();
        createOffer.setStartDate(getStartDate);
        createOffer.setEndDate(getEndDate);
        createOffer.setShopname(getShopName);
        createOffer.setContactno(getContactno);

        createOffer.setOfferPrice(getOfferAmount);
        createOffer.setMaxParticipants(Long.parseLong("0"));
        createOffer.setOfferProduct(getOfferProduct);
        createOffer.setFirstOffer(offer);

        if (isAdded) {
            String getSecondOfferAmount = moneySecondOffer.getText().toString();
            String getSecondParticipantsCount = participantSecondOffer.getText().toString();
            String getSecondOfferProduct = productSecondOffer.getText().toString();
            String secondOffer = t1.getText().toString() + " " + getSecondOfferAmount + " " + t2.getText().toString() +
                    " " + getSecondParticipantsCount + " " + t3.getText().toString() +
                    " " + t4.getText().toString() + " " + getSecondOfferProduct + " " + t5.getText().toString();

            if (!isValidOfferDetails(getSecondOfferAmount) || !isValidOfferDetails(getSecondParticipantsCount) || !isValidOfferDetails(getSecondOfferProduct)) {
                FixedVariable.showToaster(getActivity(), "Please, fill offer details to continue");
                return;
            }
            createOffer.setSecondOffer(secondOffer);

        }


        String shopId = db.collection("Offers").document().getId().trim();
        createOffer.setOfferId(shopId);
        createOffer.setCreatorId(mAuth.getCurrentUser().getUid());

        Log.d(TAG, "extractInputs: isAdded: " + isAdded + " \nCreateOffersObject=>\n" + createOffer.toString());
        db.collection("Offers")
                .document(shopId)
                .set(createOffer)
                .addOnCompleteListener(task -> Log.d(TAG, "Success"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));


        Map<String, Object> userShops = new HashMap<>();
        userShops.put("creatorId", mAuth.getCurrentUser().getUid());
        userShops.put("shopId", FieldValue.arrayUnion(shopId));
        db.collection("Shops")
                .document(mAuth.getCurrentUser().getUid())
                .set(userShops, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    FixedVariable.showToaster(getActivity(), "Offer created successfully");
                    // Add NavController here
                    FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
                    boolean isShown = floatingActionButton.isShown();
                    if (!isShown)
                        floatingActionButton.show();
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_home);
                    navController.popBackStack();
                });

    }

    private boolean isValidOfferDetails(String string) {
        if (string == null || string.isEmpty() || string.trim().isEmpty())
            return false;

        return true;
    }


    private boolean isStartDateValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(cStartDate.getText().toString())) {

            cLLStartDate.setErrorEnabled(true);
            cLLStartDate.setError(MANDATORY);
            contactStatus = false;
        } else {
            cLLStartDate.setErrorEnabled(false);
            cLLStartDate.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isEndDateValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(cEndDate.getText().toString())) {

            cLLEndDate.setErrorEnabled(true);
            cLLEndDate.setError(MANDATORY);
            contactStatus = false;
        } else {
            cLLEndDate.setErrorEnabled(false);
            cLLEndDate.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isShopNameValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(cShopName.getText().toString())) {

            cLLShopName.setErrorEnabled(true);
            cLLShopName.setError(MANDATORY);
            contactStatus = false;
        } else {
            cLLShopName.setErrorEnabled(false);
            cLLShopName.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isContactnoValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(cContactno.getText().toString())) {

            cLLContactno.setErrorEnabled(true);
            cLLContactno.setError(MANDATORY);
            contactStatus = false;
        } else {
            cLLContactno.setErrorEnabled(false);
            cLLContactno.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }


    //******************DATE********************//
    private void findCalendarAndsetText() {
        Calendar startDateCalendar, endDateCalendar;
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // ODO Auto-generated method stub
            startDateCalendar.set(Calendar.YEAR, year);
            startDateCalendar.set(Calendar.MONTH, monthOfYear);
            startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(startDateCalendar, true, false);
        };

        cStartDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date, startDateCalendar
                    .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                    startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        DatePickerDialog.OnDateSetListener endDate = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            endDateCalendar.set(Calendar.YEAR, year);
            endDateCalendar.set(Calendar.MONTH, monthOfYear);
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(endDateCalendar, false, true);
        };

        cEndDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), endDate, endDateCalendar
                    .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                    endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }


    private void updateLabel(Calendar myCalendar, boolean start, boolean end) {
        String myFormat = "dd MMMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (start)
            cStartDate.setText(sdf.format(myCalendar.getTime()));
        if (end)
            cEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void showToaster(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        boolean isShown = floatingActionButton.isShown();
        if (isShown)
            floatingActionButton.hide();
    }
}