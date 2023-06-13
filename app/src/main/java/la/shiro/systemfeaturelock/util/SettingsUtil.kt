package la.shiro.systemfeaturelock.util

import android.provider.Settings
import android.util.Log
import la.shiro.systemfeaturelock.SystemFeatureApplication

const val USER_HANDLE = -2
const val TAG = "Rin"

class SettingsUtil {
    companion object {
        fun setSystemFeatureEnabled(key: String, isEnabled: Boolean) {
            Log.d(TAG, "setSystemFeatureEnabled: $key, $isEnabled")
            Settings.System.putIntForUser(
                SystemFeatureApplication.getAppContext().contentResolver, key, 1, USER_HANDLE
            )
        }

        fun getSystemFeatureEnabled(key: String): Boolean {
            val result = Settings.System.getIntForUser(
                SystemFeatureApplication.getAppContext().contentResolver, key, 0, USER_HANDLE
            ) == 1
            Log.d(TAG, "getSystemFeatureEnabled: $key, $result")
            return result
        }
    }
}