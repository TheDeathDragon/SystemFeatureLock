package la.shiro.systemfeaturelock

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import la.shiro.systemfeaturelock.ui.theme.SystemFeatureLockTheme
import la.shiro.systemfeaturelock.util.SettingsUtil


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemFeatureLockTheme {
                // A surface container using the 'background' color from the theme
                SettingsAppBar {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppBar(
    returnToMain: () -> Unit = {}
) {
    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                Text(
                    text = "系统功能设置", maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    returnToMain()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
                    )
                }
            },
        )
    }, content = { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Settings()
        }
    })
}

const val KEY_SYSTEM_FEATURE = "system_feature"
const val KEY_THIRD_PARTY_INSTALL_FEATURE = "disable_third_party_install"
const val KEY_CAMERA_FEATURE = "disable_camera"
const val KEY_FLASHLIGHT_FEATURE = "disable_flashlight"
const val KEY_BLUETOOTH_FEATURE = "disable_bluetooth"
const val KEY_MOBILE_DATA_FEATURE = "disable_mobile_data"
const val KEY_WIFI_FEATURE = "disable_wifi"
const val KEY_FACTORY_RESET_FEATURE = "disable_factory_reset"


@Composable
fun Settings() {
    val localContext = LocalContext.current
    val sharedPreferences =
        localContext.getSharedPreferences(KEY_SYSTEM_FEATURE, Context.MODE_PRIVATE)

    var thirdPartyInstallFeatureState by remember {
        mutableStateOf(sharedPreferences.getBoolean(KEY_THIRD_PARTY_INSTALL_FEATURE, false))
    }

    var cameraFeatureState by remember {
        mutableStateOf(sharedPreferences.getBoolean(KEY_CAMERA_FEATURE, false))
    }

    var flashlightFeatureState by remember {
        mutableStateOf(sharedPreferences.getBoolean(KEY_FLASHLIGHT_FEATURE, false))
    }

    var bluetoothFeatureState by remember {
        mutableStateOf(sharedPreferences.getBoolean(KEY_BLUETOOTH_FEATURE, false))
    }

    var mobileDataFeatureState by remember {
        mutableStateOf(sharedPreferences.getBoolean(KEY_MOBILE_DATA_FEATURE, false))
    }

    var wifiFeatureState by remember {
        mutableStateOf(sharedPreferences.getBoolean(KEY_WIFI_FEATURE, false))
    }

    var factoryResetFeatureState by remember {
        mutableStateOf(sharedPreferences.getBoolean(KEY_FACTORY_RESET_FEATURE, false))
    }

    var factoryResetSwitchEnabledState by remember {
        mutableStateOf(
            sharedPreferences.getBoolean(
                KEY_THIRD_PARTY_INSTALL_FEATURE, false
            ) && sharedPreferences.getBoolean(
                KEY_CAMERA_FEATURE, false
            ) && sharedPreferences.getBoolean(
                KEY_FLASHLIGHT_FEATURE, false
            ) && sharedPreferences.getBoolean(
                KEY_BLUETOOTH_FEATURE, false
            ) && sharedPreferences.getBoolean(
                KEY_MOBILE_DATA_FEATURE, false
            ) && sharedPreferences.getBoolean(
                KEY_WIFI_FEATURE, false
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        TextButton(modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(1f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                thirdPartyInstallFeatureState = !thirdPartyInstallFeatureState
                sharedPreferences.edit()
                    .putBoolean(KEY_THIRD_PARTY_INSTALL_FEATURE, thirdPartyInstallFeatureState)
                    .apply()
                if (thirdPartyInstallFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_THIRD_PARTY_INSTALL_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_THIRD_PARTY_INSTALL_FEATURE, false)
                }
                factoryResetSwitchEnabledState =
                    thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                    factoryResetFeatureState = false
                }
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "屏蔽第三方应用安装功能",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "开启将会屏蔽安装应用功能",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(modifier = Modifier,
                    checked = thirdPartyInstallFeatureState,
                    onCheckedChange = {
                        thirdPartyInstallFeatureState = it
                        sharedPreferences.edit().putBoolean(KEY_THIRD_PARTY_INSTALL_FEATURE, it)
                            .apply()
                        if (thirdPartyInstallFeatureState) {
                            SettingsUtil.setSystemFeatureEnabled(
                                KEY_THIRD_PARTY_INSTALL_FEATURE, true
                            )
                        } else {
                            SettingsUtil.setSystemFeatureEnabled(
                                KEY_THIRD_PARTY_INSTALL_FEATURE, false
                            )
                        }
                        factoryResetSwitchEnabledState =
                            thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                        if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                            SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                        } else {
                            SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                            factoryResetFeatureState = false
                        }
                    })
            }
        }
        TextButton(modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(1f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                cameraFeatureState = !cameraFeatureState
                sharedPreferences.edit().putBoolean(KEY_CAMERA_FEATURE, cameraFeatureState).apply()
                factoryResetSwitchEnabledState =
                    thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                if (cameraFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_CAMERA_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_CAMERA_FEATURE, false)
                }
                if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                    factoryResetFeatureState = false
                }
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "屏蔽摄像头功能",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "开启将会屏蔽摄像头功能",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(modifier = Modifier, checked = cameraFeatureState, onCheckedChange = {
                    cameraFeatureState = it
                    sharedPreferences.edit().putBoolean(KEY_CAMERA_FEATURE, it).apply()
                    factoryResetSwitchEnabledState =
                        thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                    if (cameraFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_CAMERA_FEATURE, true)
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_CAMERA_FEATURE, false)
                    }
                    if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                        factoryResetFeatureState = false
                    }
                })
            }
        }
        TextButton(modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(1f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                flashlightFeatureState = !flashlightFeatureState
                sharedPreferences.edit().putBoolean(KEY_FLASHLIGHT_FEATURE, flashlightFeatureState)
                    .apply()
                factoryResetSwitchEnabledState =
                    thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                if (flashlightFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FLASHLIGHT_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FLASHLIGHT_FEATURE, false)
                }
                if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                    factoryResetFeatureState = false
                }
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "屏蔽手电筒功能",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "开启将会屏蔽手电筒功能",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(modifier = Modifier, checked = flashlightFeatureState, onCheckedChange = {
                    flashlightFeatureState = it
                    sharedPreferences.edit().putBoolean(KEY_FLASHLIGHT_FEATURE, it).apply()
                    factoryResetSwitchEnabledState =
                        thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                    if (flashlightFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FLASHLIGHT_FEATURE, true)
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FLASHLIGHT_FEATURE, false)
                    }
                    if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                        factoryResetFeatureState = false
                    }
                })
            }
        }
        TextButton(modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(1f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                bluetoothFeatureState = !bluetoothFeatureState
                sharedPreferences.edit().putBoolean(KEY_BLUETOOTH_FEATURE, bluetoothFeatureState)
                    .apply()
                factoryResetSwitchEnabledState =
                    thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                if (bluetoothFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_BLUETOOTH_FEATURE, true)
                    try {
                        if (ActivityCompat.checkSelfPermission(
                                localContext,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Log.e("TAG", "Error : No Bluetooth Permission")
                        } else {
                            SystemFeatureApplication.getBluetoothAdapter().disable()
                        }
                    } catch (e: Exception) {
                        Log.e("TAG", "Error : ", e)
                    }
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_BLUETOOTH_FEATURE, false)
                }
                if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                    factoryResetFeatureState = false
                }
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "屏蔽蓝牙功能",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "开启将会屏蔽蓝牙功能",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(modifier = Modifier, checked = bluetoothFeatureState, onCheckedChange = {
                    bluetoothFeatureState = it
                    sharedPreferences.edit().putBoolean(KEY_BLUETOOTH_FEATURE, it).apply()
                    factoryResetSwitchEnabledState =
                        thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                    if (bluetoothFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_BLUETOOTH_FEATURE, true)
                        try {
                            if (ActivityCompat.checkSelfPermission(
                                    localContext,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                Log.e("TAG", "Error : No Bluetooth Permission")
                            } else {
                                SystemFeatureApplication.getBluetoothAdapter().disable()
                            }
                        } catch (e: Exception) {
                            Log.e("TAG", "Error : ", e)
                        }
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_BLUETOOTH_FEATURE, false)
                    }
                    if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                        factoryResetFeatureState = false
                    }
                })
            }
        }
        TextButton(modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(1f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                mobileDataFeatureState = !mobileDataFeatureState
                sharedPreferences.edit().putBoolean(KEY_MOBILE_DATA_FEATURE, mobileDataFeatureState)
                    .apply()
                factoryResetSwitchEnabledState =
                    thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                if (mobileDataFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_MOBILE_DATA_FEATURE, true)
                    try {
                        SystemFeatureApplication.getTelephonyManager().setDataEnabled(false)
                    } catch (e: Exception) {
                        Log.e("TAG", "Error : ", e)
                    }
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_MOBILE_DATA_FEATURE, false)
                }
                if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                    factoryResetFeatureState = false
                }
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "屏蔽数据网络功能",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "开启将会屏蔽数据网络功能",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(modifier = Modifier, checked = mobileDataFeatureState, onCheckedChange = {
                    mobileDataFeatureState = it
                    sharedPreferences.edit().putBoolean(KEY_MOBILE_DATA_FEATURE, it).apply()
                    factoryResetSwitchEnabledState =
                        thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                    if (mobileDataFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_MOBILE_DATA_FEATURE, true)
                        try {
                            SystemFeatureApplication.getTelephonyManager().setDataEnabled(false)
                        } catch (e: Exception) {
                            Log.e("TAG", "Error : ", e)
                        }
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_MOBILE_DATA_FEATURE, false)
                    }
                    if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                        factoryResetFeatureState = false
                    }
                })
            }
        }
        TextButton(modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(1f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                wifiFeatureState = !wifiFeatureState
                sharedPreferences.edit().putBoolean(KEY_WIFI_FEATURE, wifiFeatureState).apply()
                factoryResetSwitchEnabledState =
                    thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                if (wifiFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_WIFI_FEATURE, true)
                    try {
                        SystemFeatureApplication.getWifiManager().setWifiEnabled(false)
                        SystemFeatureApplication.getTetheringManager().stopTethering(0)
                    } catch (e: Exception) {
                        Log.e("TAG", "Error : ", e)
                    }
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_WIFI_FEATURE, false)
                }
                if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                    factoryResetFeatureState = false
                }
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "屏蔽WIFI功能",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "开启将会屏蔽WIFI功能",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(modifier = Modifier, checked = wifiFeatureState, onCheckedChange = {
                    wifiFeatureState = it
                    sharedPreferences.edit().putBoolean(KEY_WIFI_FEATURE, it).apply()
                    factoryResetSwitchEnabledState =
                        thirdPartyInstallFeatureState && cameraFeatureState && flashlightFeatureState && bluetoothFeatureState && mobileDataFeatureState && wifiFeatureState
                    if (wifiFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_WIFI_FEATURE, true)
                        try {
                            SystemFeatureApplication.getWifiManager().setWifiEnabled(false)
                            SystemFeatureApplication.getTetheringManager().stopTethering(0)
                        } catch (e: Exception) {
                            Log.e("TAG", "Error : ", e)
                        }
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_WIFI_FEATURE, false)
                    }
                    if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                    } else {
                        SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                        factoryResetFeatureState = false
                    }
                })
            }
        }

        TextButton(modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(1f),
            shape = RoundedCornerShape(0.dp),
            enabled = factoryResetSwitchEnabledState,
            onClick = {
                factoryResetFeatureState = !factoryResetFeatureState
                sharedPreferences.edit()
                    .putBoolean(KEY_FACTORY_RESET_FEATURE, factoryResetFeatureState).apply()
                if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                } else {
                    SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                    factoryResetFeatureState = false
                }
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "屏蔽恢复出厂设置功能",
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "当所有开关都打开时才可使用",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(modifier = Modifier,
                    checked = factoryResetFeatureState,
                    enabled = factoryResetSwitchEnabledState,
                    onCheckedChange = {
                        factoryResetFeatureState = it
                        sharedPreferences.edit().putBoolean(KEY_FACTORY_RESET_FEATURE, it).apply()
                        if (factoryResetSwitchEnabledState && factoryResetFeatureState) {
                            SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, true)
                        } else {
                            SettingsUtil.setSystemFeatureEnabled(KEY_FACTORY_RESET_FEATURE, false)
                            factoryResetFeatureState = false
                        }
                    })
            }
        }

    }

}