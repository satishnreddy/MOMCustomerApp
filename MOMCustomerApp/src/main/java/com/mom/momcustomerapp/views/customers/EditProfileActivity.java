package com.mom.momcustomerapp.views.customers;

import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.location.Address;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.customers.api.EmployeeClient;
import com.mom.momcustomerapp.controllers.customers.models.EmployeeModel;
import com.mom.momcustomerapp.controllers.stores.model.ProfileModel;
import com.mom.momcustomerapp.customviews.CameraButton;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.views.shared.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity implements CameraButton.CameraButtonResultListener {


    @BindView(R.id.activity_edit_profile_et_name)
    EditText mEtName;
    @BindView(R.id.activity_edit_profile_et_mobile)
    EditText mEtMobile;
    @BindView(R.id.activity_edit_profile_et_email)
    EditText mEtEmail;
    @BindView(R.id.activity_edit_profile_et_address)
    EditText mEtAddress;
    @BindView(R.id.activity_edit_profile_et_city)
    EditText mEtCity;
    @BindView(R.id.activity_edit_profile_et_pincode)
    EditText mEtPincode;
    @BindView(R.id.activity_edit_profile_et_state)
    EditText mEtState;
    @BindView(R.id.activity_edit_profile_et_country)
    EditText mEtCountry;

    private ProfileModel mProfileModel;
    private ProfileModel.UserProfileModel mUserProfileModel;
    private EmployeeModel mEmployeeModel;
    private CameraButton mCamerabutton;
    private boolean isImageChanged = false;
    private Uri mImageUri;
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_edit_profile);
        ButterKnife.bind(this);

        mActivity = this;
        setupToolBar();
        loadProfileData();
        updateUI();
    }

    /**
     * Set up the {@link Toolbar}.
     */
    private void setupToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadProfileData() {
        String profile = MOMApplication.getSharedPref().getProfile();
        mProfileModel = new Gson().fromJson(profile, ProfileModel.class);

        mCamerabutton = new CameraButton(this);
        mCamerabutton.setCameraButtonResultListener(this);
        //mCamerabutton.setImageName(mProfileModel.getUserProfile().getName().replaceAll(" ", "_") + ".jpeg");
    }

    private void updateUI() {

        mEtCity.setEnabled(false);
        mEtState.setEnabled(false);
        mEtCity.setAlpha(0.38f);mEtState.setAlpha(0.38f);mEtCountry.setAlpha(0.38f);
        mEtCountry.setEnabled(false);
        if (mProfileModel != null) {
            mUserProfileModel = mProfileModel.getUserProfile();

            mEtName.setText(mUserProfileModel.getName());
            mEtMobile.setText(mUserProfileModel.getPhoneNumber());
            mEtEmail.setText(mUserProfileModel.getEmail());

            if (!TextUtils.isEmpty(mUserProfileModel.getAddress())) {
                mEtAddress.setText(mUserProfileModel.getAddress());
            }

            if (!TextUtils.isEmpty(mUserProfileModel.getCity())) {
                mEtCity.setText(mUserProfileModel.getCity());
            }

            if (!TextUtils.isEmpty(mUserProfileModel.getZip())) {
                mEtPincode.setText(mUserProfileModel.getZip());
            }

            if (!TextUtils.isEmpty(mUserProfileModel.getState())) {
                mEtState.setText(mUserProfileModel.getState());
            }

            if (!TextUtils.isEmpty(mUserProfileModel.getCountry())) {
                mEtCountry.setText(mUserProfileModel.getCountry());
            }

            mEtMobile.setEnabled(false);
            mEtName.setEnabled(false);

            /*if (mUserProfileModel.getImage() != null && mUserProfileModel.getImage().size() > 0) {
                ProfileModel.ImageModel imageModel = mUserProfileModel.getImage().get(0);
                if (imageModel != null) {
                    if (!TextUtils.isEmpty(imageModel.getImagePath())) {
                        GlideLoader.url(mActivity).load(imageModel.getImagePath()).into(mIvImage);
                        mBlurView.setOverlayColor(Color.parseColor("#CCffffff"));
                    }
                }
            }*/
            String currentStoreId = MOMApplication.getInstance().getStoreId();
            List<ProfileModel.StoreProfileModel> stores = mProfileModel.getStores();
            if (stores != null){
                for (int i = 0; i < stores.size(); i++) {
                    ProfileModel.StoreProfileModel storeProfileModel = stores.get(i);
                    if (currentStoreId.equals(storeProfileModel.getStoreId())){
                        String storeImgUrl = storeProfileModel.getStore_image();
                        if (!TextUtils.isEmpty(storeImgUrl)){
                            //GlideLoader.urlNoCache(mActivity).load(storeImgUrl).into(mIvImage);
                            /*mBlurView.setOverlayColor(Color.parseColor("#CCffffff"));*/
                        }
                        break;
                    }
                }
            }

            mEtPincode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String pincodeInStr = s.toString().trim();
                    if (pincodeInStr.length() == 6){
                        updateCityStateCountry(pincodeInStr);
                    }
                }
            });
        }
    }

    private void updateCityStateCountry(String pincodeInStr) {
        showLoadingDialog();

        EmployeeClient employeeClient = ServiceGenerator.createService(EmployeeClient.class, MOMApplication.getInstance().getToken());
        Call<String> call = employeeClient.getLocationDetails(pincodeInStr);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoadingDialog();
                if (response.isSuccessful()){
                    String body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        JSONArray responseMessage = jsonObject.getJSONArray("pincodeResponseDetails");
                        if (responseMessage.length() > 0){

                            JSONObject firstResult = responseMessage.getJSONObject(0);
                            String cityName = firstResult.getString("cityName");
                            String stateName = firstResult.getString("stateName");
                            String countryName = firstResult.getString("countryName");
                            mEtCity.setText(cityName);
                            mEtState.setText(stateName);
                            mEtCountry.setText(countryName);
                        }
                        else {
                            showErrorDialog(getString(R.string.city_state_not_found_from_pincode));
                            mEtCity.setText("");
                            mEtState.setText("");
                            mEtCountry.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog(getString(R.string.invalid_server_response));
                    }
                }
                else {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });

    }

    private void validateData() {

        String name = "", phone = "", email = "", address = "", pincode = "", city = "", state = "",country="";
        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(mEtName.getText())) {
            name = mEtName.getText().toString().trim();

        } else {
            mEtName.setError(getString(R.string.error_field_required));
            focusView = mEtName;
            cancel = true;
        }
        pincode = mEtPincode.getText().toString().trim();
        if (pincode.length() != 6){
            mEtPincode.setError(getString(R.string.error_field_required_pincode));
            focusView = mEtPincode;
            cancel = true;
        }

        if (!cancel) {
            if (!TextUtils.isEmpty(mEtEmail.getText())) {
                email = mEtEmail.getText().toString().trim();

//                if (!isEmailValid(email)) {
//                    mEtEmail.setError(getString(R.string.error_invalid_email));
//                    focusView = mEtEmail;
//                    cancel = true;
//                }
            }

            if (!TextUtils.isEmpty(mEtAddress.getText())) {
                address = mEtAddress.getText().toString().trim();
            }

            if (!TextUtils.isEmpty(mEtPincode.getText())) {
                pincode = mEtPincode.getText().toString().trim();
            }

            if (!TextUtils.isEmpty(mEtCity.getText())) {
                city = mEtCity.getText().toString().trim();
            }

            if (!TextUtils.isEmpty(mEtState.getText())) {
                state = mEtState.getText().toString();
            }

            if (!TextUtils.isEmpty(mEtCountry.getText())) {
                country = mEtCountry.getText().toString();
            }


        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (mEmployeeModel == null) {
                mEmployeeModel = new EmployeeModel();
            }

            mEmployeeModel.setName(name);
            mEmployeeModel.setEmail(email);

            if (!TextUtils.isEmpty(address)) {
                mEmployeeModel.setAddress(address);
            }
            if (!TextUtils.isEmpty(pincode)) {
                mEmployeeModel.setZip(pincode);
            }
            if (!TextUtils.isEmpty(city)) {
                mEmployeeModel.setCity(city);
            }
            if (!TextUtils.isEmpty(state)) {
                mEmployeeModel.setState(state);
            }
            if (!TextUtils.isEmpty(country)){
                mEmployeeModel.setCountry(country);
            }

            mEmployeeModel.setPerson_id(mUserProfileModel.getPersonId());
            mEmployeeModel.setPhone_number(mUserProfileModel.getPhoneNumber());

            switch (MOMApplication.getSharedPref().getUserType()) {
                case "Expert":
                    mEmployeeModel.setUserType("1");
                    break;

                case "Advanced":
                    mEmployeeModel.setUserType("2");
                    break;

                case "Basic":
                    mEmployeeModel.setUserType("3");
                    break;

                default:
                    mEmployeeModel.setUserType("3");
                    break;
            }
            updateProfile();
        }
    }

    private boolean isEmailValid(String emailAddress) {
        if (emailAddress.contains("@")) {
            String emailLocalPart = emailAddress.substring(emailAddress.indexOf("@"));
            if (emailLocalPart.length() > 64) {
                return false;
            }
        }

        if (emailAddress.length() > 254) {
            return false;
        }

        String expression = "[A-Za-z0-9][\\.\\w]*+@([\\w]+\\.)+[A-Za-z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }

    @OnClick({R.id.activity_edit_profile_btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_edit_profile_btn_submit:
                View focusView = getCurrentFocus();
                if (focusView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                validateData();
                break;
//            case R.id.activity_edit_profile_fab_photo:
//                mCamerabutton.performClick();
//                break;
        }
    }

    private void updateForm(Address address) {

        StringBuilder sb = new StringBuilder();

        String feature = address.getFeatureName();
        String thoroughfare = address.getThoroughfare();
        String subLocality = address.getSubLocality();
        String locality = address.getLocality();
        String adminArea = address.getAdminArea();
        String postalCode = address.getPostalCode();

        if (!TextUtils.isEmpty(feature)) {
            sb.append(feature).append(", ");
        }
        if (!TextUtils.isEmpty(thoroughfare) && !feature.equalsIgnoreCase(thoroughfare)) {
            sb.append(address.getThoroughfare()).append(", ");
        }
        if (!TextUtils.isEmpty(subLocality)) {
            sb.append(subLocality).append(", ");
        }

        String firstAddress = sb.toString().trim();
        if (!TextUtils.isEmpty(firstAddress)) {
            firstAddress = firstAddress.substring(0, firstAddress.lastIndexOf(", "));
        }

        if (!TextUtils.isEmpty(firstAddress)) {
            mEtAddress.setText(firstAddress);
        } else {
            mEtAddress.setText("");
        }

        if (!TextUtils.isEmpty(locality)) {
            mEtCity.setText(locality);
        } else {
            mEtCity.setText("");
        }

        if (!TextUtils.isEmpty(postalCode)) {
            mEtPincode.setText(postalCode);
        } else {
            mEtPincode.setText("");
        }

        if (!TextUtils.isEmpty(adminArea)) {
            mEtState.setText(adminArea);
        } else {
            mEtState.setText("");
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CameraButton.TAKE_PICTURE_REQUEST_CODE || requestCode == CameraButton.SELECT_PICTURE_REQUEST_CODE || requestCode == CameraButton.AFTER_IMAGE_CROPPING) {
            mCamerabutton.handleActivityResults(requestCode, resultCode, data);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CameraButton.REQUEST_WRITE_EXTERNAL_STORAGE || requestCode == CameraButton.REQUEST_CAMERA) {
            mCamerabutton.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    public void imageSaved(Uri uri) {

        Toast.makeText(this, "Photo Selected : " + uri.getPath(), Toast.LENGTH_SHORT).show();
        mImageUri = uri;
        isImageChanged = true;
//          mIvImage.setImageDrawable(null);
        Bitmap selectedImage = loadFromUri(uri);
        // mIvImage.setImageBitmap(selectedImage);

//        mIvImage.setImageURI(uri);
        /*mBlurView.setOverlayColor(Color.parseColor("#CCffffff"));*/
    }

    private void uploadImage() {
        if (mImageUri != null) {
            String stringUri = mImageUri.getPath();
            if (stringUri.contains("root/")) {
                stringUri = stringUri.replace("root/", "");
                stringUri = Environment.getExternalStorageDirectory().getAbsolutePath() + stringUri;
            }
            String storeId = MOMApplication.getInstance().getStoreId();
            uploadProfileImage(stringUri, storeId);
        }
    }
    public void updateProfile() {
        showLoadingDialog();
        EmployeeClient employeeClient = ServiceGenerator.createService(EmployeeClient.class, MOMApplication.getInstance().getToken());
        Call<EmployeeModel> call = employeeClient.updateEmployee(mEmployeeModel.getPerson_id(), mEmployeeModel);
        call.enqueue(new Callback<EmployeeModel>() {
            @Override
            public void onResponse(Call<EmployeeModel> call, Response<EmployeeModel> response) {
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    mEmployeeModel = response.body();
                    if (mEmployeeModel == null) {
                        showErrorDialog(getString(R.string.sww));
                        return;
                    }
                    if (!TextUtils.isEmpty(mEmployeeModel.getPerson_id())) {
                        if (isImageChanged) {
                            uploadImage();
                        } else {
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        showErrorDialog("Error", "Something went wrong. Response is empty.");
                    }
                } else {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<EmployeeModel> call, Throwable t) {
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }

        });
    }

    private void uploadProfileImage(String imagePath, String itemId) {
        showLoadingDialog();
        EmployeeClient employeeClient = ServiceGenerator.createService(EmployeeClient.class, MOMApplication.getInstance().getToken());
        MultipartBody.Part body;

        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        body = MultipartBody.Part.createFormData("profile_img", file.getName(), requestFile);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), itemId);

        Call<String> call = employeeClient.uploadProfileImage(name, body);
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    responseString = responseString.replaceAll("\\n", "");
                    try {
                        if (!TextUtils.isEmpty(responseString)) {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            if (jsonResponse.get("status").toString().equalsIgnoreCase("success")) {
                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                            else
                                showErrorDialog(getString(R.string.sww));
                        } else {
                            showErrorDialog("Something went wrong. Response is empty.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog(getString(R.string.invalid_server_response));
                    }
                } else {
                    if (response.code() == 413)
                    {
                        //showErrorDialog(ErrorUtils.getErrorString(response));

                        showErrorDialog(getString(R.string.store_pic_image_size_to_large));
                    }
                    else {
                        showErrorDialog(ErrorUtils.getErrorString(response));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });
    }

}
