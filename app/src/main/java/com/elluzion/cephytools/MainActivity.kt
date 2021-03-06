package com.elluzion.cephytools

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.elluzion.cephytools.actions.selector.ActionSelectorSheet
import com.elluzion.cephytools.etc.Constants
import com.elluzion.cephytools.etc.Utils.checkRootAccess
import com.elluzion.cephytools.etc.Utils.getCurrentAction
import com.elluzion.cephytools.etc.Utils.getHumanizedActionString
import com.elluzion.cephytools.etc.Utils.writeToFile

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.prefcard_current_action.*
import kotlinx.android.synthetic.main.prefcard_feedback.*
import kotlinx.android.synthetic.main.prefcard_status.*

class MainActivity : AppCompatActivity() {

    val acs = ActionSelectorSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateViews()
        if (!checkRootAccess())
            rootAccessError()
    }

    fun updateViews() {
        updateStatusInfoCard()
        updateActionTypeCard()
        updateSelectorPrefCard()
        updateButtonDisableCard()
        updateFeedbackCard()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        updateActionTypeCard()
    }

    private fun updateStatusInfoCard() {
        summary_app_version.text = BuildConfig.VERSION_NAME
        summary_android_version.text = Build.VERSION.RELEASE
    }

    private fun updateActionTypeCard() {
        current_action_label.text = getHumanizedActionString(getCurrentAction(), applicationContext)
        if (getCurrentAction() != acs.NO_FUNCTION_ITEM && getHumanizedActionString(getCurrentAction(), applicationContext) != getString(R.string.unsupported_action))
            current_action_label.setTextColor(resources.getColor(R.color.green_enabled))
        else
            current_action_label.setTextColor(resources.getColor(R.color.red_disabled))
    }

    private fun updateButtonDisableCard() {
        prefcard_disable_layout.setOnClickListener {
            if (getCurrentAction() != "NOF") {
                writeToFile("NOF")
                updateActionTypeCard()
                Toast.makeText(this, getString(R.string.disable_toast), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateFeedbackCard() {
        feedback_telegram_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.URL_TELEGRAM)
            startActivity(intent)
        }
        feedback_bmac_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.URL_BMAC)
            startActivity(intent)
        }
        feedback_source_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.URL_SOURCE)
            startActivity(intent)
        }
    }

    private fun updateSelectorPrefCard() {
        prefcard_selector_layout.setOnClickListener {
            acs.show(supportFragmentManager, ActionSelectorSheet.TAG)
        }
    }

    private fun rootAccessError() {
        main_activity_content.removeAllViews()
        layoutInflater.inflate(R.layout.root_missing, main_activity_content)
    }
}