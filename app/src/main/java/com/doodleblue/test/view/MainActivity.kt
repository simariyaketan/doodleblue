package com.doodleblue.test.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.doodleblue.test.R
import com.doodleblue.test.databinding.ActivityMainBinding
import com.doodleblue.test.model.ContactDetail
import com.doodleblue.test.model.ContactTableModel
import com.doodleblue.test.room.ContactDatabase
import com.doodleblue.test.view.adapter.ContactDetailAdapter
import com.doodleblue.test.viewmodel.ContactViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), ContactDetailAdapter.ContactAdapterClickListner {

    private var contactViewModel: ContactViewModel? = null
    val REQUEST_CODE = 1
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var contactDetailAdapter: ContactDetailAdapter
    var contactsArrayList: ArrayList<ContactDetail> = ArrayList()
    var favoriteContactArray: ArrayList<ContactTableModel> = ArrayList()

    var PERMISSIONS = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.SYSTEM_ALERT_WINDOW,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        contactViewModel = ContactViewModel(applicationContext)
        activityMainBinding.contactViewModel = contactViewModel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, *PERMISSIONS))
                requestPermissions(
                    PERMISSIONS, REQUEST_CODE
                )
            else {
                initView()
            }
        } else {
            initView()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (REQUEST_CODE == requestCode) {
            initView()
        }
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission: Int =
                ContextCompat.checkSelfPermission(applicationContext, it)
            hasPermission == PackageManager.PERMISSION_GRANTED
        } else true

    }

    fun initView() {

        contactViewModel!!.getFavoriteContact(this)!!
            .observe(this, Observer<List<ContactTableModel>> {
                favoriteContactArray.clear()
                favoriteContactArray.addAll(it)
                Log.d("size", "size " + favoriteContactArray.size)
                for (cal in contactsArrayList) {
                    for (fca in favoriteContactArray) {
                        if (cal.name == fca.name && cal.phoneNumber == fca.number) {
                            cal.isSelected = true
                            break
                        }
                    }
                }
                contactDetailAdapter.notifyDataSetChanged()
            })

        contactsArrayList.addAll(contactViewModel?.getContacts()!!)
        contactDetailAdapter = ContactDetailAdapter()
        contactDetailAdapter.setContacts(contactsArrayList)
        contactDetailAdapter.setContactAdapterClickListner(this)
        activityMainBinding.recycleContactList.adapter = contactDetailAdapter
    }

    override fun AddContactToFavorite(position: Int) {
        var contactDetail: ContactDetail = contactsArrayList.get(position)

        if (contactDetail.isSelected) {
            contactDetail.isSelected = false
            contactViewModel!!.removeFavoriteContact(
                this,
                contactDetail.name,
                contactDetail.phoneNumber
            )
        } else {
            contactDetail.isSelected = true
            contactViewModel!!.insertData(this, contactDetail.name, contactDetail.phoneNumber)
        }
        contactDetailAdapter.notifyItemChanged(position)

    }
}