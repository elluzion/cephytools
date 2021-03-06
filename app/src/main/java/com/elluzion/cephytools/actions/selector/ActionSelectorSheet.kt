package com.elluzion.cephytools.actions.selector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import com.elluzion.cephytools.R
import com.elluzion.cephytools.etc.ActionArrays
import com.elluzion.cephytools.etc.Utils
import com.elluzion.cephytools.etc.Utils.getHumanizedActionString

import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ActionSelectorSheet : BottomSheetDialogFragment() {

    public var NO_FUNCTION_ITEM = "NOF"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.action_selector_sheet, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation

        inflateButtons()
    }

    @SuppressLint("InflateParams")
    private fun inflateButtons() {
        val views = ArrayList<Any>()
        val parentLayout: LinearLayout = (view?.parent as View).findViewById(R.id.selector_sheet_buttons_parent_layout)
        val inflater = LayoutInflater.from(context)

        for (item in ActionArrays.nativeFunctionArray) {
            if (item != NO_FUNCTION_ITEM) {
                val view: View = inflater.inflate(
                    R.layout.action_selector_sheet_button_singleton,
                    null)

                val label = view.findViewById<View>(R.id.singleton_label) as TextView
                val layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT)

                label.text = getHumanizedActionString(item, context as Context)
                layoutParams.bottomMargin = 16

                view.layoutParams = layoutParams
                view.setOnClickListener {
                    Utils.writeToFile(item)
                    view.setBackgroundResource(R.drawable.outlined_button_background)
                    Toast.makeText(
                        activity, String.format(
                            getString(R.string.selector_toast),
                            getHumanizedActionString(item, context as Context)),
                        Toast.LENGTH_LONG)
                        .show()
                    this.dismiss()
                }
                views.add(view)
            }
        }
        for (i in 0 until views.size)
            parentLayout.addView(views[i] as View)
    }

    companion object {
        const val TAG = "Sheet"
    }
}