package com.elluzion.cephytools

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.ai_remap_activity.*
import java.io.*


class AIButtonRemapperActivity : AppCompatActivity() {
    private val KEYLAYOUT_FILE_PATH = "/system/usr/keylayout/gpio-keys.kl";
    private val CACHED_KEYLAYOUT_PATH = "/data/data/com.elluzion.cephytools/cache/gpio-keys.kl"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ai_remap_activity)
        checkRootAccess()
        setUpButtons()
        updateCurrentActionLabel(false)
    }

    private fun setUpButtons() {
        buttonBack.setOnClickListener { this.finish() }
        buttonVOICEASSIST.setOnClickListener { writeToFile("VOICE_ASSIST") }
        buttonCAMERA.setOnClickListener { writeToFile("CAMERA") }
        buttonSYSRQ.setOnClickListener { writeToFile("SYSRQ") }
        buttonCALL.setOnClickListener { writeToFile("CALL") }
        buttonCONTACTS.setOnClickListener { writeToFile("CONTACTS") }
        buttonMUSIC.setOnClickListener { writeToFile("MUSIC") }
        buttonVOLUME_MUTE.setOnClickListener { writeToFile("VOLUME_MUTE") }
        buttonMEDIA_PLAY_PAUSE.setOnClickListener { writeToFile("MEDIA_PLAY_PAUSE") }
        buttonMEDIA_NEXT.setOnClickListener { writeToFile("MEDIA_NEXT") }
        buttonAPP_SWITCH.setOnClickListener { writeToFile("APP_SWITCH") }
//        buttonQPANEL_ON_OFF.setOnClickListener { writeToFile("QPANEL_ON_OFF") }
        buttonEXPLORER.setOnClickListener { writeToFile("EXPLORER") }
        buttonCALENDAR.setOnClickListener { writeToFile("CALENDAR") }
        buttonCALCULATOR.setOnClickListener { writeToFile("CALCULATOR") }
        buttonNOF.setOnClickListener { writeToFile("NOF") }
    }

    @Throws(IOException::class)
    fun writeToFile(newAction: String) {
        val currentAction = getTrimmedStringFromFile(KEYLAYOUT_FILE_PATH, 1)
        val same = getTrimmedStringFromFile(KEYLAYOUT_FILE_PATH, 0)
        val p = Runtime.getRuntime().exec("su")
        val os = DataOutputStream(p.outputStream)
        // Remount / as read-write
        os.writeBytes("mount -o remount,rw /\n")
        // Do the stuff
        os.writeBytes("cp -n $KEYLAYOUT_FILE_PATH $KEYLAYOUT_FILE_PATH.bak\n")
        os.writeBytes("cp -r $KEYLAYOUT_FILE_PATH $CACHED_KEYLAYOUT_PATH\n")
        os.writeBytes("echo '$same\nkey 689   $newAction' > $CACHED_KEYLAYOUT_PATH\n")
        os.writeBytes("sed -i '/^\$/d' $CACHED_KEYLAYOUT_PATH\n")
        os.writeBytes("cp -r $CACHED_KEYLAYOUT_PATH $KEYLAYOUT_FILE_PATH\n")
        os.writeBytes("rm -rf $CACHED_KEYLAYOUT_PATH\n")
        // Remount / as read-only
        os.writeBytes("mount -o remount,ro /\n");
        os.writeBytes("exit\n");
        os.close();
        p.waitFor()
        updateCurrentActionLabel(true)
    }

    // http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
    @Throws(java.lang.Exception::class)
    fun getTrimmedStringFromFile(filePath: String, splitPart: Int): String {
        val fl = File(filePath)
        val fin = FileInputStream(fl)
        val ret = convertStreamToString(fin)
        fin.close()

        if (ret.contains("key 689   ")) {
            val trimmed = ret.split("key 689   ")
            return trimmed[splitPart]
        }
        return ""
    }

    @Throws(java.lang.Exception::class)
    fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }


    private fun updateCurrentActionLabel(newSet: Boolean) {
        if (getTrimmedStringFromFile(KEYLAYOUT_FILE_PATH, 1) != "") {
            titleAction.text = getTrimmedStringFromFile(KEYLAYOUT_FILE_PATH, 1)
        } else {
            Toast.makeText(this, "Error: Malformed file $KEYLAYOUT_FILE_PATH", Toast.LENGTH_SHORT).show()
            return
        }
        if (newSet) {
            labelCurrentAction.text = getString(R.string.applied_after_reboot)
        }
    }

    private fun checkRootAccess() {
        val p: Process
        try {
            p = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(p.outputStream)
            os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n")

            os.writeBytes("exit\n")
            os.flush()
            try {
                p.waitFor()
                if (p.exitValue() != 255) {
                    // Success
                } else {
                    Toast.makeText(this, "Error: This app needs root privileges to run!", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
            } catch (e: InterruptedException) {
                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                this.finish()
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }
}