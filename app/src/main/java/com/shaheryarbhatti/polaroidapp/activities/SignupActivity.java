package com.shaheryarbhatti.polaroidapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.preferences.LocalStoragePreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.shaheryarbhatti.polaroidapp.Manifest.permission.CAMERA;
import static com.shaheryarbhatti.polaroidapp.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.shaheryarbhatti.polaroidapp.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//@RuntimePermissions
public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "LoginActivity";
    private AppCompatEditText nameEdt, mnameEdt,
            lnameEdt, emailEdt, passwordEdt, mnumberEdt, dobEdt, genderEdt;
    private RadioGroup genderRg;
    private ImageView takePhotoImageView;
    public SimpleDateFormat DateFormater = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private Button signupBtn;
    String[] perms = {CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    final int PERMS_REQUEST_CODE = 200;
    static final int OPEN_MEDIA_PICKER = 1;
    private LocalStoragePreferences preferences;

    public void pickDate(Context context, final EditText editText) {

        Calendar newCalender = Calendar.getInstance();
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Date curDate = newDate.getTime();
                editText.setText(DateFormater.format(curDate));

            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameEdt = findViewById(R.id.nameEdt);
        genderRg = findViewById(R.id.genderRg);
//        mnameEdt =  findViewById(R.id.mnameEdt);
//        lnameEdt =  findViewById(R.id.lnameEdt);
//        mnumberEdt =  findViewById(R.id.mnumberEdt);
        dobEdt = findViewById(R.id.dobEdt);
//        genderEdt =  findViewById(R.id.genderEdt);
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
//        takePhotoImageView =  findViewById(R.id.takePhotoImageView);
        signupBtn = findViewById(R.id.signupBtn);

        preferences = new LocalStoragePreferences(this);

        dobEdt.setOnClickListener(this);

        signupBtn.setOnClickListener(this);
       /* if (Build.VERSION.SDK_INT >= 23 && !checkPermission()) {
            Log.d(TAG, "onCreate: ask permissions");
            requestPermissions(perms, PERMS_REQUEST_CODE);
        }*/

    }

    //    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showCamera() {
        /*Intent intent = new Intent(this, Gallery.class);
        // Set the title
        intent.putExtra("title", "Select media");
        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
        intent.putExtra("mode", 1);
        intent.putExtra("maxSelection", 1); // Optional
        startActivityForResult(intent, OPEN_MEDIA_PICKER);*/
        /*ImagePicker.create(SignupActivity.this)
                .single()
                .limit(1)
                .showCamera(true)
                .start();*/
    }


    @Override
    public void onClick(View v) {
        if (v == signupBtn) {
            String pushToken = FirebaseInstanceId.getInstance().getToken();
            String gender = "female";
            if (genderRg.getCheckedRadioButtonId() == R.id.maleRadio) {
                gender = "male";
            }

            handleSignUp(nameEdt.getText().toString(), emailEdt.getText().toString(),
                    passwordEdt.getText().toString(), dobEdt.getText().toString(), gender, pushToken);
        }
        if (v == dobEdt) {
            pickDate(this, dobEdt);
        }

//            SignupActivityPermissionsDispatcher.showCameraWithPermissionCheck(SignupActivity.this);
            /*Permissions.check(this, new String[]{*//*Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, *//*Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    "Camera and storage permissions are required", new Permissions.Options()
                            .setSettingsDialogTitle("Warning!").setRationaleDialogTitle("Info"),
                    new PermissionHandler() {
                        @Override
                        public void onGranted() {


                        }
                    });*/

            /*Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            Log.d(TAG, "onPermissionsChecked: ");
                            if (report.areAllPermissionsGranted()) {
                                ImagePicker.create(SignupActivity.this)
                                        .single()
                                        .limit(1)
                                        .showCamera(true)
                                        .start();
                            }
                            for (PermissionDeniedResponse permissionDeniedResponse :
                                    report.getDeniedPermissionResponses())
                                Log.d(TAG, "onPermissionsChecked: "+ permissionDeniedResponse.getPermissionName());
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        }
                    })
                    .check();*/


    }

    private void handleSignUp(String name, String email, String password, String dob, String gender, String pushToken) {
        String signUpUrl = getResources().getString(R.string.signup_test_url);

        AndroidNetworking.post(signUpUrl)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("dob", dob)
                .addBodyParameter("gender", gender)
                .addBodyParameter("push_token", pushToken)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");
                            Log.d(TAG, "onResponse: isSuccess: " + isSuccess);
                            if (isSuccess) {
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                JSONObject userObj = dataObj.getJSONObject("user");
                                preferences.setIsLoggedIn(true);
                                preferences.setUserId(userObj.getString("id"));
                                preferences.setToken(dataObj.getString("token"));
                                preferences.setName(userObj.getString("name"));
                                preferences.setEmail(userObj.getString("email"));
                                preferences.setDOB(userObj.getString("dob"));
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });

    }

   /* private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMS_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    Log.d(TAG, "onRequestPermissionsResult: " +
                            "cameraAccepted: " + cameraAccepted +
                            "readAccepted: " + readAccepted +
                            "writeAccepted: " + writeAccepted
                    );
                    if (cameraAccepted && readAccepted && writeAccepted) {
                        Log.d(TAG, "onRequestPermissionsResult: all permssion accepted");
                    }

                }
        }*/
//        SignupActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

//    }

    /*@Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        *//*if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
//            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            Log.d(TAG, "onActivityResult: image path:" + image.getPath());
            File file = new File(image.getPath());
        }*//*
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_MEDIA_PICKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
                for (String resultStr : selectionResult) {
                    Log.d(TAG, "onActivityResult:  resultStr: " + resultStr);
                }
            }
        }
    }*/

    /*private Map<String, String> getBodyParams(){
        Map<String, String> params =new HashMap<>();
        params.put("")
        params.put("")
        params.put("")
        params.put("")
    }*/

    /*private void handleUserSignUp(){
        String signupUrl = getResources().getString(R.string.signup_test_url);

        AndroidNetworking.post(signupUrl)
                .addHeaders("Content-Type","application/json")

                .addBodyParameter("first_name", email)
                .addBodyParameter("middle_name", password)
                .addBodyParameter("last_name", email)
                .addBodyParameter("dob", password)
                .addBodyParameter("gender", password)
                .addBodyParameter("mobile_number", password)
                .addBodyParameter("username", password)
                .addBodyParameter("user_type", "user")
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("profile_pic", "url")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+ response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+ anError.getErrorBody());
                    }
                });
    }*/
}
