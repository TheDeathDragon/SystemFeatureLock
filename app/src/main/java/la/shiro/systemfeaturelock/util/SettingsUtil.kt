package la.shiro.systemfeaturelock.util

import android.provider.Settings
import android.util.Log
import la.shiro.systemfeaturelock.SystemFeatureApplication
import android.os.SystemProperties

const val USER_HANDLE = -2
const val TAG = "Rin"

class SettingsUtil {
    companion object {
        fun setSystemFeatureEnabled(key: String, isEnabled: Boolean) {
            Log.d(TAG, "setSystemFeatureEnabled: $key, $isEnabled")
            SystemProperties.set("persist.sys.$key", if (isEnabled) "true" else "false")
        }

        fun getSystemFeatureEnabled(key: String): Boolean {
            val result = SystemProperties.getBoolean("persist.sys.$key", false)
            Log.d(TAG, "getSystemFeatureEnabled: $key, $result")
            return result
        }
    }
}