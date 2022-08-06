package com.mom.momcustomerapp.data.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.utils.AESEncryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/*
 * Created by nishant on 01/03/17.
 */

public class MOMSharedPreferences
{

	/* singleton reference to access the share persistence data  */
	private static MOMSharedPreferences mMOMSharedPreferences = null;
	private static SharedPreferences mSharedPreferences = null;

	private MOMSharedPreferences()
	{
	}

	public static MOMSharedPreferences getInstance(Context context)
	{
		if (mMOMSharedPreferences == null)
		{
			mMOMSharedPreferences = new MOMSharedPreferences();
			mSharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF, Context.MODE_PRIVATE);
		}
		return mMOMSharedPreferences;
	}

	private SharedPreferences getSharedPreferences()
	{
		if (mSharedPreferences == null)
		{
			//mSharedPreferences = context.getApplicationContext().getSharedPreferences(Consts.SHARED_PREF, Context.MODE_PRIVATE);
		}
		return mSharedPreferences;
	}

	public void setLoggedOut()
	{
		setToken("");
		setVender("");
		setStoreId("");
		setPersonIdInSync("");
		setMswipeUsername("");

		boolean isPermissionTaken = isPermissionTaken();
		mSharedPreferences.edit().clear().commit();
		setPermissionTaken(isPermissionTaken);




		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_TOKEN, "");
		editor.putString(Consts.PREF_VENDER, "");
		editor.putString(Consts.PREF_PROFILE, "");
		editor.putString(Consts.PREF_PROFILE_IMAGE, "");
		editor.putString(Consts.PREF_USER_TYPE, "");
		editor.putBoolean(Consts.PREF_PROFILE_SETUP, false);
		editor.putBoolean(Consts.PREF_PROFILE_SETUP_NOTIFIED_OWNER, false);
		editor.putString(Consts.PREF_PRINTER_LABEL, "");
		editor.putString(Consts.PREF_PRINTER_RECEIPT, "");
		editor.apply();
	}

	public String getToken() {
		String token = getSharedPreferences().getString(Consts.PREF_TOKEN, "");
		if (TextUtils.isEmpty(token)) {
			token = getSharedPreferences().getString(Consts.PREF_TOKEN, "");
		}
        try {
            String encodedToken = AESEncryption.decrypt(token);
            return encodedToken;
		}
		catch (IllegalBlockSizeException e){
			e.printStackTrace();
		}
		catch (BadPaddingException e){
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	public void setToken(String token)
	{
		String tokenToSave = "";
		try {
			tokenToSave = AESEncryption.encrypt(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_TOKEN, tokenToSave);
		editor.apply();
	}

	public String getVender() {
		String vender = getSharedPreferences().getString(Consts.PREF_VENDER, "");
		if (TextUtils.isEmpty(vender)) {
			vender = getSharedPreferences().getString(Consts.PREF_VENDER, "");
		}
		return vender;
	}

	public void setVender(String vender) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_VENDER, vender);
		editor.apply();
	}

	public String getProfile() {
		String profile = getSharedPreferences().getString(Consts.PREF_PROFILE, "");
		if (TextUtils.isEmpty(profile)) {
			profile = getSharedPreferences().getString(Consts.PREF_PROFILE, "");
		}
		return profile;
	}

	public void setProfile(String profile) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_PROFILE, profile);
		editor.apply();
	}


	public boolean getProfileSetup() {
		boolean profilesetup = getSharedPreferences().getBoolean(Consts.PREF_PROFILE_SETUP, false);
		if (!profilesetup) {
			profilesetup = getSharedPreferences().getBoolean(Consts.PREF_PROFILE_SETUP, false);
		}
		return profilesetup;
	}

	public void setProfileSetup(boolean profileSetup) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_PROFILE_SETUP, profileSetup);
		editor.apply();
	}

	public String getProfileImage() {
		String profileImg = getSharedPreferences().getString(Consts.PREF_PROFILE_IMAGE, "");
		if (TextUtils.isEmpty(profileImg)) {
			profileImg = getSharedPreferences().getString(Consts.PREF_PROFILE_IMAGE, "");
		}
		return profileImg;
	}

	public void setProfileImage(String profileImage) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_PROFILE_IMAGE, profileImage);
		editor.apply();
	}

	public String getUserType() {
		String userType = getSharedPreferences().getString(Consts.PREF_USER_TYPE, "Basic");
		if (userType.equalsIgnoreCase("Basic")) {
			userType = getSharedPreferences().getString(Consts.PREF_USER_TYPE, "Basic");
		}
		return userType;
	}

	public void setUserType(String userType) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_USER_TYPE, userType);
		editor.apply();
	}

	public boolean isOwnerUpdated() {
		boolean isOwnerUpdated = getSharedPreferences().getBoolean(Consts.PREF_PROFILE_SETUP_NOTIFIED_OWNER, false);
		if (!isOwnerUpdated) {
			isOwnerUpdated = getSharedPreferences().getBoolean(Consts.PREF_PROFILE_SETUP_NOTIFIED_OWNER, false);
		}
		return isOwnerUpdated;
	}

	public void setOwnerUpdated(boolean isNotified) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_PROFILE_SETUP_NOTIFIED_OWNER, isNotified);
		editor.apply();
	}


	public boolean isDataCleared() {
		return getSharedPreferences().getBoolean(Consts.PREF_IS_DATA_CLEARED, false);
	}

	public void setDataCleared(boolean isDataCleared) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_IS_DATA_CLEARED, isDataCleared);
		editor.apply();
	}

	public boolean isPermissionTaken() {
		return getSharedPreferences().getBoolean(Consts.PREF_IS_PERMISSION_TAKEN, false);
	}

	public void setPermissionTaken(boolean isPermissionTaken) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_IS_PERMISSION_TAKEN, isPermissionTaken);
		editor.apply();
	}

	public String getReceiptPrinter() {
		return getSharedPreferences().getString(Consts.PREF_PRINTER_RECEIPT, Consts.PRINTER_TYPE_XPRINTER);
	}

	public void setReceiptPrinter(String printer) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_PRINTER_RECEIPT, printer);
		editor.apply();
	}

	public String getGlobalDiscountTotal() {
		return getSharedPreferences().getString(Consts.PREF_GLOBAL_DISCOUNT_TOTAL, "0");
	}

	public void setGlobalDiscountTotal(String total) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_GLOBAL_DISCOUNT_TOTAL, total);
		editor.apply();
	}

	public String getLabelPrinter() {
		return getSharedPreferences().getString(Consts.PREF_PRINTER_LABEL, Consts.PRINTER_TYPE_XPRINTER);
	}

	public void setLabelPrinter(String printer) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_PRINTER_LABEL, printer);
		editor.apply();
	}

	public String getReceiptPrinterMac() {
		return getSharedPreferences().getString(Consts.PREF_PRINTER_RECEIPT_MAC, "");
	}

	public void setReceiptPrinterMac(String macAddress) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_PRINTER_RECEIPT_MAC, macAddress);
		editor.apply();
	}

	public String getLabelPrinterMac() {
		return getSharedPreferences().getString(Consts.PREF_PRINTER_LABEL_MAC, "");
	}

	public void setLabelPrinterMac(String macAddress) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_PRINTER_LABEL_MAC, macAddress);
		editor.apply();
	}

	public String getXprinterPrinterConnection() {
		return getSharedPreferences().getString(Consts.PREF_XPRINTER_CONN, Consts.PRINTER_TYPE_XPRINTER_BLUETOOTH);
	}

	public void setXprinterPrinterConnection(String printerConn) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_XPRINTER_CONN, printerConn);
		editor.apply();
	}

	public String getUsbPrinterName() {
		return getSharedPreferences().getString(Consts.PREF_USB_PRINTER_NAME, "");
	}

	public void setUsbPrinterName(String name) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_USB_PRINTER_NAME, name);
		editor.apply();
	}



	public boolean isStockTakingEnd() {
		boolean isStockTakingEnd = getSharedPreferences().getBoolean(Consts.PREF_STOCK_TAKING_END, false);
		return isStockTakingEnd;
	}

	public void setStockTakingEnd(boolean stockTakingEnd) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_STOCK_TAKING_END, stockTakingEnd);
		editor.apply();
	}

	public boolean isMerchantAuthenticated() {
		return !TextUtils.isEmpty(getMswipeReferenceId()) && !TextUtils.isEmpty(getMswipeSessionToken());
	}

	public String getMswipeUsername() {
		String userNameToReturn = getSharedPreferences().getString(Consts.MSWIPE_USERNAME, "");
		try {
            return AESEncryption.decrypt(userNameToReturn);
		}
		catch (IllegalBlockSizeException e){
			e.printStackTrace();
		}
		catch (BadPaddingException e){
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return userNameToReturn;
	}

	public void setMswipeUsername(String username) {
		String userNameToSet = "";
		try {
			userNameToSet = AESEncryption.encrypt(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_USERNAME, userNameToSet);
		editor.apply();
	}

	public String getMswipePassword() {
		return getSharedPreferences().getString(Consts.MSWIPE_PASSWORD, "");
	}

	public void setMswipePassword(String password) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_PASSWORD, password);
		editor.apply();
	}


	public String getMswipeSessionToken() {
		return getSharedPreferences().getString(Consts.MSWIPE_SESSION_TOKEN, "");
	}

	public void setMswipeSessionToken(String sessionToken) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_SESSION_TOKEN, sessionToken);
		editor.apply();
	}

	public String getMswipeReferenceId() {
		return getSharedPreferences().getString(Consts.MSWIPE_REFERENCE_ID, "");
	}

	public void setMswipeReferenceId(String referenceId) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_REFERENCE_ID, referenceId);
		editor.apply();
	}

	public boolean isMswipeTipEnabled() {
		return getSharedPreferences().getBoolean(Consts.MSWIPE_TIP_ENABLED, false);
	}

	public void setMswipeTipEnabled(boolean isTipEnabled) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.MSWIPE_TIP_ENABLED, isTipEnabled);
		editor.apply();
	}

	public String getMswipeCurrenyCode() {
		return getSharedPreferences().getString(Consts.MSWIPE_CURRENY_CODE, "");
	}

	public void setMswipeCurrenyCode(String currenyCode) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_CURRENY_CODE, currenyCode);
		editor.apply();
	}

	public float getMswipeConveniencePercentage() {
		return getSharedPreferences().getFloat(Consts.MSWIPE_CONVENIENCE_PERCENTAGE, 0.0f);
	}

	public void setMswipeConveniencePercentage(float conveniencePercentage) {
		Editor editor = getSharedPreferences().edit();
		editor.putFloat(Consts.MSWIPE_CONVENIENCE_PERCENTAGE, conveniencePercentage);
		editor.apply();
	}

	public float getMswipeServicePercentageOnConvenience() {
		return getSharedPreferences().getFloat(Consts.MSWIPE_SERVICE_PERCENTAGE_ON_CONVENIENCE, 0.0f);
	}

	public void setMswipeServicePercentageOnConvenience(float servicePercentageOnConvenience) {
		Editor editor = getSharedPreferences().edit();
		editor.putFloat(Consts.MSWIPE_SERVICE_PERCENTAGE_ON_CONVENIENCE, servicePercentageOnConvenience);
		editor.apply();
	}

	public String getMswipeLastTrxAmount() {
		return getSharedPreferences().getString(Consts.MSWIPE_LAST_TRX_AMOUNT, "");
	}

	public void setMswipeLastTrxAmount(String lastTrxAmount) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_LAST_TRX_AMOUNT, lastTrxAmount);
		editor.apply();
	}

	public String getMswipeLastTrxLastFourDigits() {
		return getSharedPreferences().getString(Consts.MSWIPE_LAST_TRX_LAST_FOUR_DIGITS, "");
	}

	public void setMswipeLastTrxLastFourDigits(String lastTrxLastFourDigits) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_LAST_TRX_LAST_FOUR_DIGITS, lastTrxLastFourDigits);
		editor.apply();
	}

	public String getMswipeLastTrxKsn() {
		return getSharedPreferences().getString(Consts.MSWIPE_LAST_TRX_KSN, "");
	}

	public void setMswipeLastTrxKsn(String lastTrxKsn) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_LAST_TRX_KSN, lastTrxKsn);
		editor.apply();
	}

	public boolean isMswipePinBypass() {
		return getSharedPreferences().getBoolean(Consts.MSWIPE_PIN_BYPASS, false);
	}

	public void setMswipePinBypass(boolean pinBypass) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.MSWIPE_PIN_BYPASS, pinBypass);
		editor.apply();
	}

	public boolean isMswipeReceiptEnabled() {
		return getSharedPreferences().getBoolean(Consts.MSWIPE_RECEIPT_ENABLED, false);
	}

	public void setMswipeReceiptEnabled(boolean receiptEnabled) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.MSWIPE_RECEIPT_ENABLED, receiptEnabled);
		editor.apply();
	}

	public boolean isMswipePrinterSupport() {
		return getSharedPreferences().getBoolean(Consts.MSWIPE_PRINTER_SUPPORT, false);
	}

	public void setMswipePrinterSupport(boolean printerSupport) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.MSWIPE_PRINTER_SUPPORT, printerSupport);
		editor.apply();
	}

	public boolean isMswipePrintSignature() {
		return getSharedPreferences().getBoolean(Consts.MSWIPE_PRINT_SIGNATURE, false);
	}

	public void setMswipePrintSignature(boolean isMswipePrintSignature) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.MSWIPE_PRINT_SIGNATURE, isMswipePrintSignature);
		editor.apply();
	}

	public String getMswipePrinterAddress() {
		return getSharedPreferences().getString(Consts.MSWIPE_PRINTER_ADDRESS, "");
	}

	public void setMswipePrinterAddress(String printerAddress) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.MSWIPE_PRINTER_ADDRESS, printerAddress);
		editor.apply();
	}


	public int getDefaultLabelTemplate() {
		return getSharedPreferences().getInt(Consts.PREF_DEF_LABEL_TEMPLATE, 1);
	}

	public void setDefaultLabelTemplate(int template_id) {
		Editor editor = getSharedPreferences().edit();
		editor.putInt(Consts.PREF_DEF_LABEL_TEMPLATE, template_id);
		editor.apply();
	}

	public void setStoreId(String vender) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_STORE_ID, vender);
		editor.apply();
	}

	public String getStoreId(){
		String storeId = getSharedPreferences().getString(Consts.PREF_STORE_ID, "");
		return storeId;
	}

	public void setStoreName(String vender) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_STORE_NAME, vender);
		editor.apply();
	}

	public String getStoreName(){
		String storeId = getSharedPreferences().getString(Consts.PREF_STORE_NAME, "");
		return storeId;
	}

	public void setPersonIdInSync(String vender) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_PERSON_ID, vender);
		editor.commit();
	}


	public String getPersonId(){
		String personId = getSharedPreferences().getString(Consts.PREF_PERSON_ID, "");
		return personId;
	}

	public void setCustAddress(String vender)
	{
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_CUST_ADDRESS, vender);
		editor.commit();
	}
	public String getCustAddress()
	{
		String personId = getSharedPreferences().getString(Consts.PREF_CUST_ADDRESS, "");
		return personId;
	}

	public void setDeliveryType(int deliveryType) {
		Editor editor = getSharedPreferences().edit();
		editor.putInt(Consts.PREF_DELIVERY_TYPE, deliveryType);
		editor.apply();
	}

	public int getDeliveryType() {
		//0 is for home delivery 1 for pickup
		return   getSharedPreferences().getInt(Consts.PREF_DELIVERY_TYPE, 0);
	}

	public void setShouldLogFirstProduct(boolean shouldLog) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_SHOULD_LOG_FIRST_PRODUCT, shouldLog);
		editor.apply();
	}

	public boolean getShouldLogFirstProduct(){
		boolean shouldLog = getSharedPreferences().getBoolean(Consts.PREF_SHOULD_LOG_FIRST_PRODUCT, false);
		return shouldLog;
	}

	public void setShowAppRateTime(long time) {
		Editor editor = getSharedPreferences().edit();
		editor.putLong(Consts.PREF_SHOW_RATE_APP_TIME, time);
		editor.apply();
	}

	public long getShowAppRateTime(){
		long time = getSharedPreferences().getLong(Consts.PREF_SHOW_RATE_APP_TIME, -1);
		return time;
	}

	public void setSecondOrderCompleted() {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_SECOND_ORDER_RECEIVED, true);
		editor.apply();
	}

	public boolean isSecondOrderCompleted(){
		boolean shouldLog = getSharedPreferences().getBoolean(Consts.PREF_SECOND_ORDER_RECEIVED, false);
		return shouldLog;
	}

	public void setStoreImageSettings(String value) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_STORE_IMAGE_SETTING, value);
		editor.commit();
	}


	public String getStoreImageSettings(){
		String personId = getSharedPreferences().getString(Consts.PREF_STORE_IMAGE_SETTING, "0");
		return personId;
	}

	public void setCatImageSettings(String value) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_CATEGORY_IMAGE_SETTING, value);
		editor.commit();
	}


	public String getCatImageSettings(){
		String personId = getSharedPreferences().getString(Consts.PREF_CATEGORY_IMAGE_SETTING, "0");
		return personId;
	}

	public void setItemImageSettings(String value) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_ITEM_IMAGE_SETTING, value);
		editor.commit();
	}

	public String getItemImageSettings(){
		String personId = getSharedPreferences().getString(Consts.PREF_ITEM_IMAGE_SETTING, "0");
		return personId;
	}

	public void setName(String value) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_NAME, value);
		editor.commit();
	}

	public String getName(){
		String personId = getSharedPreferences().getString(Consts.PREF_NAME, " ");
		return personId;
	}

	public void setReferralID(String value) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_REFERRAL_ID, value);
		editor.commit();
	}

	public String getReferralID(){
		String personId = getSharedPreferences().getString(Consts.PREF_REFERRAL_ID, "");
		return personId;
	}

	public void setReferralSent(boolean value) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(Consts.PREF_REFERRAL_SENT, value);
		editor.commit();
	}

	public boolean isReferralSent(){
		boolean personId = getSharedPreferences().getBoolean(Consts.PREF_REFERRAL_SENT, false);
		return personId;
	}
	public void setSignupNo(String value) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_SIGNUPP_NUM, value);
		editor.commit();
	}

	public String getSignupNo(){
		String personId = getSharedPreferences().getString(Consts.PREF_SIGNUPP_NUM, "");
		return personId;
	}

	public void setLanguage(String value) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Consts.PREF_LANGUAGE, value);
		editor.commit();
	}

	public String getLanguage(){
		String personId = getSharedPreferences().getString(Consts.PREF_LANGUAGE, "en");
		return personId;
	}

}