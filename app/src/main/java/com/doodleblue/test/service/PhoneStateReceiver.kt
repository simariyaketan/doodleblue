package com.doodleblue.test.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.core.util.Preconditions
import androidx.databinding.DataBindingUtil
import com.doodleblue.test.R
import com.doodleblue.test.databinding.ReceiverPopupBinding
import com.doodleblue.test.model.ContactTableModel
import com.doodleblue.test.repository.FavoriteContact

public class PhoneStateReceiver : BroadcastReceiver() {

    lateinit var receiverPopup: ReceiverPopupBinding
    lateinit var wm: WindowManager

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            println("Receiver start")
            val state = intent!!.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Toast.makeText(
                    context,
                    "Ringing State Number is -$incomingNumber = ",
                    Toast.LENGTH_SHORT
                ).show()

                var contactTableModel: ContactTableModel? = FavoriteContact().getAllUsers(
                    context!!,
                    incomingNumber!!
                )

                if(contactTableModel != null) {
                    println("Receiver start " + contactTableModel!!.name)
                    OpenCallerDetail(context, contactTableModel)
                }

            }
            if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                Toast.makeText(context, "Call Received State", Toast.LENGTH_SHORT).show()
                if (wm != null)
                    wm.removeView(receiverPopup.root)
            }
            if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                Toast.makeText(context, "Call Idle State", Toast.LENGTH_SHORT).show()
                if (wm != null)
                    wm.removeView(receiverPopup.root)
            }
        } catch (e: Exception) {
            println(e.message)
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi")
    fun OpenCallerDetail(context: Context?, contactTableModel: ContactTableModel) {
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.TYPE_TOAST
                    or WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
            PixelFormat.TRANSLUCENT
        )

        wm = context?.getSystemService(WINDOW_SERVICE) as WindowManager
        receiverPopup = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.receiver_popup,
            null, true
        )

        receiverPopup.contactTableModel = contactTableModel
        Preconditions.checkNotNull(wm).addView(receiverPopup.root, params)
    }
}