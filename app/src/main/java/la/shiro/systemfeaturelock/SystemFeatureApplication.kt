package la.shiro.systemfeaturelock

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.TetheringManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager

class SystemFeatureApplication : Application() {

    companion object {
        private lateinit var instance: SystemFeatureApplication
        private lateinit var tetheringManager: TetheringManager
        private lateinit var wifiManager: WifiManager
        private lateinit var bluetoothAdapter: BluetoothAdapter
        private lateinit var telephonyManager: TelephonyManager
        fun getAppContext(): Context = instance.applicationContext
        fun getTetheringManager(): TetheringManager = tetheringManager
        fun getWifiManager(): WifiManager = wifiManager
        fun getBluetoothAdapter(): BluetoothAdapter = bluetoothAdapter
        fun getTelephonyManager(): TelephonyManager = telephonyManager

    }

    override fun onCreate() {
        instance = this
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        tetheringManager = getSystemService(TetheringManager::class.java) as TetheringManager
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        telephonyManager = getSystemService(TelephonyManager::class.java) as TelephonyManager
        super.onCreate()
    }
}