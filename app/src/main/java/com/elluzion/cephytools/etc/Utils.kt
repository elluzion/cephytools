package com.elluzion.cephytools.etc

import android.content.Context
import android.widget.Toast
import com.elluzion.cephytools.R
import java.io.*

import com.elluzion.cephytools.etc.Constants.KEYLAYOUT_FILE_PATH

object Utils {
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

    fun getHumanizedActionString(actionName: String, context: Context): String? {
        fun findIndex(arr: Array<String>, item: String): Int {
            return arr.indexOf(item)
        }

        val arr = ActionArrays.nativeFunctionArray
        return try {
            ActionArrays.nativeFunctionArrayHumanized[findIndex(arr, actionName)]
        } catch (ex: ArrayIndexOutOfBoundsException) {
            context.getString(R.string.unsupported_action)
        }
    }

    fun getCurrentAction(): String {
         return getTrimmedStringFromFile(KEYLAYOUT_FILE_PATH, 1).replace("\\s".toRegex(), "")
    }

    fun checkRootAccess(context: Context) {
        val p: Process
        try {
            p = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(p.outputStream)
            os.writeBytes("exit\n")
            os.flush()
        } catch (e: IOException) {
            Toast.makeText(context, "Error: $e", Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    fun writeToFile(newAction: String) {
        val contentWeKeep = getTrimmedStringFromFile(KEYLAYOUT_FILE_PATH, 0)
        val p = Runtime.getRuntime().exec("su")
        val os = DataOutputStream(p.outputStream)
        // Remount / as read-write
        os.writeBytes("mount -o remount,rw /\n")
        // Change the action
        os.writeBytes("cp -n $KEYLAYOUT_FILE_PATH $KEYLAYOUT_FILE_PATH.bak\n")
        os.writeBytes("echo '$contentWeKeep\nkey 689   $newAction' > $KEYLAYOUT_FILE_PATH\n")
        os.writeBytes("sed -i '/^\$/d' $KEYLAYOUT_FILE_PATH\n")
        // Remount / as read-only
        os.writeBytes("mount -o remount,ro /\n")
        os.writeBytes("exit\n")
        os.close()
        p.waitFor()
    }
}