package io.bingbin.bingbinandroid.views.webActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import io.bingbin.bingbinandroid.R
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import io.bingbin.bingbinandroid.views.BottomNavigationViewEx


class WebActivity : AppCompatActivity() {
    private val _event = "event"
    private val _forum = "forum"

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var currentPage: String
    private lateinit var token: String

    // bottom navigation listener
    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_web_event -> run {
                loadPage(_event)
                return@run true
            }
            R.id.navigation_web_recognition -> run {
                this.moveTaskToBack(false)
                return@run true
            }
            R.id.navigation_web_forum -> run {
                loadPage(_forum)
                return@run true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val webviewLayout: LinearLayout = findViewById(R.id.web_webview_layout)
        val navigation: BottomNavigationViewEx = findViewById(R.id.web_navigation)

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
        this.moveTaskToBack(false)
    }

}
