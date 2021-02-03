package com.elluzion.cephytools

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elluzion.cephytools.actions.selector.ActionSelectorSheet
import com.elluzion.cephytools.etc.Constants
import com.elluzion.cephytools.etc.SharedPrefController
import com.elluzion.cephytools.etc.Utils.checkRootAccess
import com.elluzion.cephytools.etc.Utils.getCurrentAction
import com.elluzion.cephytools.etc.Utils.getHumanizedActionString
import com.elluzion.cephytools.etc.Utils.writeToFile
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.prefcard_current_action.*
import kotlinx.android.synthetic.main.prefcard_feedback.*
import kotlinx.android.synthetic.main.prefcard_status.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateStatusInfoCardContents()
        updateActionTypeCard()
        updateSelectorPrefCard()
        updateButtonDisableCard()
        updateFeedbackCard()
        checkRootAccess(applicationContext)
        showRootSnackbar()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        updateActionTypeCard()
        if (hasFocus)
            Blurry.delete(window.decorView.rootView as ViewGroup)
    }

    private fun updateStatusInfoCardContents() {
        summary_app_version.text = BuildConfig.VERSION_NAME
        summary_android_version.text = Build.VERSION.RELEASE_OR_CODENAME
    }

    private fun updateActionTypeCard() {
        current_action_label.text = getHumanizedActionString(getCurrentAction())
        if (getCurrentAction() != "NOF")
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
            val prefSheet = ActionSelectorSheet()
            prefSheet.show(supportFragmentManager, ActionSelectorSheet.TAG)
            Blurry.with(applicationContext).radius(25).sampling(1).animate(100).onto(window.decorView.rootView as ViewGroup)
        }
    }

    private fun showRootSnackbar() {
        if (!SharedPrefController.getSharedPrefBool(applicationContext, "hasShowedRootSnackbar", false))
            Snackbar.make(rootLayout, R.string.root_snackbar, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getColor(R.color.colorPrimary))
                .show()
        SharedPrefController.setSharedPrefBool(applicationContext, "hasShowedRootSnackbar", true)
    }
}