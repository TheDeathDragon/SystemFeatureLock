package la.shiro.systemfeaturelock

import android.app.Application
import android.content.Context

class SystemFeatureApplication : Application() {

    companion object {
        private lateinit var instance: SystemFeatureApplication
        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}