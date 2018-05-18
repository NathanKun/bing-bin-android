package io.bingbin.bingbinandroid.views.webActivity

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.webkit.*
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import io.bingbin.bingbinandroid.BingBinApp
import io.bingbin.bingbinandroid.R
import io.bingbin.bingbinandroid.utils.BingBinHttp
import io.bingbin.bingbinandroid.views.BottomNavigationViewEx
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider
import javax.inject.Inject


class WebActivity : AppCompatActivity() {

    private val _event = "event"
    private val _forum = "forum"
    private val _fileRequestCode = 1
    private val _filePermissionRequestCode = 2
    private val _uploadFileType = "image/*"
    private val _baseUrl = "https://forum.bingbin.io"

    private var cameraMessage: String? = null
    private var filePath: ValueCallback<Array<Uri>>? = null

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var currentPage: String
    private lateinit var token: String
    private lateinit var navigation: BottomNavigationViewEx
    private var smartLocationLocation: SmartLocation.LocationControl? = null
    private var smartLocationGeoCoding: SmartLocation.GeocodingControl? = null
    private var locationProvider: LocationGooglePlayServicesWithFallbackProvider? = null

    private var currentCity: String = ""
    private var lastLocation: Location? = null

    @Inject
    lateinit var bbh: BingBinHttp

    // bottom navigation listener
    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {

        return@OnNavigationItemSelectedListener when (it.itemId) {
            R.id.navigation_web_event -> run {
                loadPage(_event)
                return@run true
            }
            R.id.navigation_web_recognition -> run {
                backToMainActivity()
                return@run true
            }
            R.id.navigation_web_forum -> run {
                loadPage(_forum)
                return@run true
            }
            else -> run {
                return@run false
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        (application as BingBinApp).netComponent.inject(this)

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
        navigation.enableAnimation(false)
        navigation.selectedItemId = if (toPage == _event) R.id.navigation_web_event else R.id.navigation_web_forum
        navigation.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener

        // init web view
        val webViewLayout: LinearLayout = findViewById(R.id.web_webview_layout)
        mAgentWeb = AgentWeb.with(this@WebActivity)
                .setAgentWebParent(webViewLayout, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient((object : WebChromeClient() {
                    //Handling input[type="file"] requests for android API 21+
                    override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
                        checkFilePermissionGranted()
                        if (filePath != null) {
                            filePath!!.onReceiveValue(null)
                        }
                        filePath = filePathCallback
                        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                        contentSelectionIntent.type = _uploadFileType
                        val intentArray: Array<Intent?> = arrayOfNulls(0)
                        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                        chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser")
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                        startActivityForResult(chooserIntent, _fileRequestCode)

                        return true
                    }

                    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                        android.util.Log.d("WebViewConsole", consoleMessage.message())
                        return true
                    }
                }))
                .createAgentWeb()
                .ready()
                .go("$_baseUrl?bbt=$token&toPage=$toPage")

        mAgentWeb.jsInterfaceHolder.addJavaObject("android", AndroidInterface(this))

        // smart location
        locationProvider = LocationGooglePlayServicesWithFallbackProvider(this)
        smartLocationLocation = SmartLocation.with(this)
                .location(locationProvider)
                .oneFix()
        smartLocationGeoCoding = SmartLocation.with(this).geocoding()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent == null) return

        token = intent.getStringExtra("token")
        loadPage(intent.getStringExtra("toPage"))
    }

    private fun getLocation() {
        lastLocation = smartLocationLocation!!.lastLocation

        smartLocationLocation!!.start({
            smartLocationGeoCoding!!
                    .reverse(it, { _: Location, results: MutableList<Address> ->
                        if (results.size > 0) {
                            for (adr in results) {
                                if (adr.locality != null && adr.locality.isNotEmpty()) {
                                    currentCity = adr.locality
                                }
                            }
                        } else {
                            currentCity = ""
                        }

                        Log.d("geocoding", "currentCity=$currentCity")
                        mAgentWeb.jsAccessEntrace.callJs("window['common-provider'].zone.run(() => {window['common-provider'].component.outsideSetLocation('$currentCity')})")
                    }
                    )
        })
    }

    private fun clearCache() {
        runOnUiThread {
            val lastPage = currentPage
            currentPage = "blank"
            mAgentWeb.urlLoader.loadUrl("about:blank")
            val tmp: String = AgentWebConfig.getCookiesByUrl(_baseUrl) ?: ""
            mAgentWeb.clearWebCache() // this will clear cookies also
            AgentWebConfig.syncCookie(_baseUrl, tmp)
            loadPage(lastPage)
        }
    }

    private fun loadPage(page: String) {
        if (currentPage != page) {
            currentPage = page
            val url = "$_baseUrl?bbt=$token&toPage=$page"
            mAgentWeb.urlLoader.loadUrl(url)
        }
    }


    //Creating image file for upload
    /*@Throws(IOException::class)
    private fun createImage(): File {
        @SuppressLint("SimpleDateFormat")
        val fileName = SimpleDateFormat("yyyy_mm_ss").format(Date())
        val newName = "file_" + fileName + "_"
        val sdDirectory = File("${Environment.getExternalStorageDirectory()}${File.separator}BingBin")
        return File.createTempFile(newName, ".jpg", sdDirectory)
    }*/

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        // get forum version
        Thread({
            val tag = "forum_version"
            val newVersion = bbh.forumVersion
            val prefs = this.getSharedPreferences(tag, 0)
            val savedVersion = prefs!!.getString(tag, "")

            Log.d("forumVersion", "newVersion = ${newVersion ?: "null"}")
            Log.d("forumVersion", "savedVersion = $savedVersion")

            if (newVersion !== null) {
                if (newVersion != savedVersion) {
                    val editor = prefs.edit()
                    editor.putString(tag, newVersion)
                    editor.apply()
                    clearCache()
                    Log.d("forumVersion", "reloaded")
                }
            }
        }).start()

        // get current location
        if (isGPSPermissionGranted()) {
            getLocation()
        }

        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
        smartLocationLocation!!.stop()
        smartLocationGeoCoding!!.stop()
        locationProvider!!.stop()
        locationProvider = null
        smartLocationGeoCoding = null
        smartLocationLocation = null

        // LocationGooglePlayServicesProvider.context leaks
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    override fun onBackPressed() {
        //backToMainActivity()
    }

    private fun backToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> run {
                    mAgentWeb.jsAccessEntrace.callJs("window['common-provider'].zone.run(() => {window['common-provider'].component.outsideBackPress('$currentCity')})")
                    return@run true
                }
            }

        }
        return super.onKeyDown(keyCode, event)
    }

    private fun isGPSPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission(1)) {
                true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    //Checking permission for storage and camera for writing and uploading images
    private fun checkFilePermissionGranted() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

        //Checking for storage permission to write images for upload
        if (!checkPermission(2) && !checkPermission(3)) {
            ActivityCompat.requestPermissions(this@WebActivity, perms, _filePermissionRequestCode)
        }
    }

    //Checking if particular permission is given or not
    private fun checkPermission(permission: Int): Boolean {
        when (permission) {
            1 -> return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

            2 -> return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

            3 -> return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.v("Permission", "Permission: " + permissions[0] + " was " + grantResults[0])
        if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
            getLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@WebActivity, R.color.primary_color)
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == _fileRequestCode) {
                if (null == filePath) {
                    return
                }
                if (intent == null) {
                    if (cameraMessage != null) {
                        results = arrayOf(Uri.parse(cameraMessage))
                    }
                } else {
                    val dataString = intent.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
        }
        filePath!!.onReceiveValue(results)
        filePath = null
    }


    private class AndroidInterface
    (private var context: WebActivity) {

        // call in js: window.android.getLocation();
        @JavascriptInterface
        fun getLocation() {
            context.getLocation()
        }

        // call in js: window.android.clearCache();
        @JavascriptInterface
        fun clearCache() {
            context.clearCache()
        }
    }
}

