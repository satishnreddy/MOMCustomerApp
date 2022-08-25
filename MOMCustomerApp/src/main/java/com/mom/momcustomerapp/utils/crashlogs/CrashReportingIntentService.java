/*
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the license at LICENSE.txt
 * or http://www.opensource.org/licenses/cddl1.php.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 * 
 * 
 * Copyright 2010 Tom Quist 
 * All rights reserved Use is subject to license terms.
 */
package com.mom.momcustomerapp.utils.crashlogs;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.application.app;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.utils.crashlogs.data.SherlockDatabaseHelper;
import com.mswipetech.sdk.network.util.Logs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrashReportingIntentService extends Service
{
	boolean submitcalled =false;

	@Override
	// execution of service will start
	// on calling this method
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(app.is_DEBUGGING_ON)
			Logs.adb("Crash log service started activity save crash logs");
		if(submitcalled)
			submitCustomerLogs();
		submitcalled = true;
		// returns the status
		// of the program
		return START_STICKY;
	}

	@Override
	// execution of the service will
	// stop on calling this method
	public void onDestroy() {
		submitcalled = false;
		super.onDestroy();


	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void submitCustomerLogs()
	{
		SherlockDatabaseHelper database = new SherlockDatabaseHelper(this);
		database.flushCrash();


		if(MOMApplication.getSharedPref().getIsCrashed())
		{
			String placeOfCrash = MOMApplication.getSharedPref().getplaceOfCrash();
			String reasonOfCrash = MOMApplication.getSharedPref().getreasonOfCrash();
			String stackTrace = MOMApplication.getSharedPref().getstackTrace();
			String deviceInfo = MOMApplication.getSharedPref().getdeviceInfo();
			String personid = MOMApplication.getInstance().getPersonId();
			String username = MOMApplication.getInstance().getMswipeUsername();

			if(app.is_DEBUGGING_ON)
				Logs.adb("Splashscreen activity save crash logs");

			CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class);
			Call<String> call = customerClient.saveCrashLogs(placeOfCrash, reasonOfCrash, stackTrace,
					deviceInfo, personid, username);

			call.enqueue(new Callback<String>() {
				@Override
				public void onResponse(Call<String> call, Response<String> response)
				{
					if(response.isSuccessful()) {
						if (app.is_DEBUGGING_ON)
							Logs.adb("Splashscreen activity save crash logs response " + response.body());
						resetCrashLog();

					}else {
						if (app.is_DEBUGGING_ON)
							Logs.adb("Splashscreen activity save crash logs error response " + response.body());
						if (app.is_DEBUGGING_ON)
							Logs.adb("Splashscreen activity save crash logs error response " + response.errorBody());

						resetCrashLog();

					}
				}

				@Override
				public void onFailure(Call<String> call, Throwable t)
				{

					resetCrashLog();
				}
			});
		}
	}

	public void resetCrashLog()
	{
		MOMApplication.getSharedPref().setplaceOfCrash("");
		MOMApplication.getSharedPref().setreasonOfCrash("");
		MOMApplication.getSharedPref().setstackTrace("");
		MOMApplication.getSharedPref().setdeviceInfo("");
		MOMApplication.getSharedPref().setIsCrashed(false);
	}
}
