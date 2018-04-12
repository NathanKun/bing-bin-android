package io.bingbin.bingbinandroid.views.webActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import io.bingbin.bingbinandroid.R


class WebActivity : AppCompatActivity() {

    lateinit var preAgentWeb: AgentWeb.PreAgentWeb
    var mAgentWeb: AgentWeb? = null
    lateinit var masterLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        masterLayout = findViewById(R.id.web_masterlayout)

        preAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(masterLayout, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
    }

    fun go(toPage: String) {
        if(toPage == "event") {
            mAgentWeb = mAgentWeb.go("https://forum.bingbin.io")
        } else if(toPage == "forum") {

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
        mAgentWeb.webLifeCycle.onDestroy();
        super.onDestroy()
    }
}
