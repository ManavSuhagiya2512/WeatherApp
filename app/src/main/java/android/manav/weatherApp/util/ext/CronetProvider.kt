/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.util.ext

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.net.CronetProviderInstaller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * To guarantee that a Google Play Services Cronet implementation is available,
 * one can explicitly install the Play Services Cronet provider.
 */
suspend fun installCronetProvider(
    activity: Activity,
    activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
): Unit = withContext(Dispatchers.Default) {
    try {
        CronetProviderInstaller.installProvider(activity).await()
    } catch (e: GooglePlayServicesRepairableException) {
        showErrorDialogFragment(activity, e.connectionStatusCode, activityResultLauncher)
    } catch (e: GooglePlayServicesNotAvailableException) {
        // Use a less performant fall-back implementation of Cronet's API
    }
}

/**
 * If an update to Google Play services is required
 * or there is some other issue that can be fixed with user interaction.
 */
private suspend fun showErrorDialogFragment(
    activity: Activity,
    errorCode: Int,
    activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
) = withContext(Dispatchers.Main) {
    GoogleApiAvailability.getInstance().run {
        // Show a dialog prompting the user to install/update/enable Google Play services.
        showErrorDialogFragment(activity, errorCode, activityResultLauncher, null)
    }
}