package io.bingbin.bingbinandroid.views.webActivity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import io.bingbin.bingbinandroid.R
import io.bingbin.bingbinandroid.views.BottomNavigationViewEx
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider


class WebActivity : AppCompatActivity() {
    private val _event = "event"
    private val _forum = "forum"

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var currentPage: String
    private lateinit var token: String
    private lateinit var navigation: BottomNavigationViewEx

    private var currentCity: String = ""
    private var lastLocation: Location? = null

    // bottom navigation listener
    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_web_event -> run {
                navigation.menu.getItem(0).isChecked = true
                navigation.menu.getItem(1).isChecked = false
                navigation.menu.getItem(2).isChecked = false
                loadPage(_event)
                return@run true
            }
            R.id.navigation_web_recognition -> run {
                navigation.menu.getItem(0).isChecked = false
                navigation.menu.getItem(1).isChecked = true
                navigation.menu.getItem(2).isChecked = false
                onBackPressed()
                return@run true
            }
            R.id.navigation_web_forum -> run {
                navigation.menu.getItem(0).isChecked = false
                navigation.menu.getItem(1).isChecked = false
                navigation.menu.getItem(2).isChecked = true
                loadPage(_forum)
                return@run true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // broadcast receiver for ending this activity when MainActivity end
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, intent: Intent) {
                val action = intent.action
                if (action == "finish_web_activity") {
                    unregisterReceiver(this)
                    mAgentWeb.destroy()
                    finish()
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("finish_web_activity"))

        // receive intent
        val intent = intent
        token = intent.getStringExtra("token")
        val toPage = intent.getStringExtra("toPage")
        currentPage = toPage

        // init bottom navigation
        navigation = findViewById(R.id.web_navigation)

        // init bottom navigation
        navigation.enableShiftingMode(false)
        navigation.enableItemShiftingMode(false)
        navigation.setTextVisibility(false)
        navigation.selectedItemId = if (toPage == _event) R.id.navigation_web_event else R.id.navigation_web_forum
        navigation.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener

        // init web view
        val webViewLayout: LinearLayout = findViewById(R.id.web_webview_layout)
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(webViewLayout, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("https://forum.bingbin.io?bbt=$token&toPage=$toPage")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent == null) return

        token = intent.getStringExtra("token")
        loadPage(intent.getStringExtra("toPage"))
    }


    private fun getLocation() {
        lastLocation = SmartLocation.with(this).location().lastLocation
        Log.d("geocoding", "last location $lastLocation")

        SmartLocation.with(this).location(LocationGooglePlayServicesWithFallbackProvider(this))
                .oneFix()
                .start({
                    Log.d("geocoding", it.toString())
                    SmartLocation.with(this).geocoding()
                            .reverse(it, { _: Location, results: MutableList<Address> ->
                                Log.d("geocoding", results.toString())
                                Log.d("geocoding", "size=${results.size}")


                                if (results.size > 0) {
                                    for (adr in results) {
                                        Log.d("geocoding", "adr=$adr")
                                        if (adr.locality != null && adr.locality.isNotEmpty()) {
                                            currentCity = adr.locality
                                        }
                                    }
                                } else {
                                    currentCity = ""
                                }

                                Log.d("geocoding", "currentCity=$currentCity")
                                mAgentWeb.jsAccessEntrace.callJs("console.log('call from android')")
                                mAgentWeb.jsAccessEntrace.callJs("window['outsideSetLocation']($currentCity)")
                            }
                            )
                })
    }

    private fun loadPage(page: String) {
        if (currentPage != page) {
            currentPage = page
            val url = "https://forum.bingbin.io?bbt=$token&toPage=$page"
            mAgentWeb.urlLoader.loadUrl(url)
        }
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()

        // get current location
        if (isGPSPermissionGranted()) {
            getLocation()
        }

        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    private fun isGPSPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "ACCESS_FINE_LOCATION Permission is granted")
                true
            } else {
                Log.d("Permission", "ACCESS_FINE_LOCATION Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission", "ACCESS_FINE_LOCATION Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.v("Permission", "Permission: " + permissions[0] + " was " + grantResults[0])
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
            getLocation()
        }
    }
}
