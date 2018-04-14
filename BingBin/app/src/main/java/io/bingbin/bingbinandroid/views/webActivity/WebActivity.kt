package io.bingbin.bingbinandroid.views.webActivity

import android.os.Bundle
import android.os.Process
import android.util.Log
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import io.bingbin.bingbinandroid.R
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import io.bingbin.bingbinandroid.views.BottomNavigationViewEx
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.content.Context


class WebActivity : AppCompatActivity() {
    private val _event = "event"
    private val _forum = "forum"

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var currentPage: String
    private lateinit var token: String
    private lateinit var navigation: BottomNavigationViewEx

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
        Log.d("webactivity", "on create")

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

        val webviewLayout: LinearLayout = findViewById(R.id.web_webview_layout)
        navigation = findViewById(R.id.web_navigation)

        val intent = intent
        token = intent.getStringExtra("token")
        val toPage = intent.getStringExtra("toPage")
        currentPage = toPage

        // init bottom navigation
        navigation.enableShiftingMode(false)
        navigation.enableItemShiftingMode(false)
        navigation.setTextVisibility(false)
        navigation.selectedItemId = if(toPage == _event)  R.id.navigation_web_event else R.id.navigation_web_forum
        navigation.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(webviewLayout, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("https://forum.bingbin.io?bbt=$token&toPage=$toPage")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("webactivity", "on new intent")
        setIntent(intent)//must store the new intent unless getIntent() will return the old one

        token = intent.getStringExtra("token")
        val toPage = intent.getStringExtra("toPage")
        loadPage(toPage)
    }

    private fun loadPage(page: String) {
        if(currentPage != page) {
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

}
