package com.fitmanager.app.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.widget.ImageView
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.erikagtierrez.multiple_media_picker.Gallery
import com.fitmanager.app.R
import com.fitmanager.app.model.MealVO
import com.fitmanager.app.model.ResultMessageVO
import com.fitmanager.app.network.MemberRestService
import com.fitmanager.app.util.Constant
import com.fitmanager.app.util.RetroUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * 코치 식단등록 페이지
 */

class InsertMealActivity : AppCompatActivity() {

    lateinit internal var mContext: Context
    private var mCoachId: Int = 0
    private val mMealImageList = ArrayList<MealVO>()
    private var mType: String? = null

    @BindView(R.id.img_camera)
    lateinit var imgCamera: ImageView

    @BindView(R.id.tv_gallery_count)
    internal var tvGalleryCount: AppCompatTextView? = null                                                               // 갤러리 선택된 이미지 갯수

    @BindView(R.id.togg_btn_breakfast)
    internal var toggBtnBreakfast: ToggleButton? = null

    @BindView(R.id.togg_btn_lunch)
    internal var toggBtnLunch: ToggleButton? = null

    @BindView(R.id.togg_btn_dinner)
    internal var toggBtnDinner: ToggleButton? = null

    @BindView(R.id.togg_btn_dessert)
    internal var toggBtnDessert: ToggleButton? = null

    @BindView(R.id.edit_title)
    internal var editTitle: AppCompatEditText? = null

    @BindView(R.id.btn_insert)
    internal var btnInsert: AppCompatButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_meal_insert)
        ButterKnife.bind(this)
        getIntentInit()
        /* 식단올릴 사진 갤러리 열기  */
        imgCamera!!.setOnClickListener {
            checkPermissionREAD_EXTERNAL_STORAGE(mContext)

            val intent: Intent
            intent = Intent(mContext, Gallery::class.java)
            // Set the title
            intent.putExtra("title", "Select media")
            // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
            intent.putExtra("mode", 1)
            intent.putExtra("maxSelection", 3) // Optional
            startActivityForResult(intent, OPEN_MEDIA_PICKER)
        }

        /* 아침 클릭 */
        toggBtnBreakfast!!.setOnClickListener {
            mType = toggBtnBreakfast!!.tag.toString()
            setButtonState(toggBtnBreakfast, true)
            setButtonState(toggBtnLunch, false)
            setButtonState(toggBtnDinner, false)
            setButtonState(toggBtnDessert, false)

            if (toggBtnBreakfast!!.isChecked) {
                toggBtnBreakfast!!.setBackgroundResource(R.color.maincolor)
            } else {
                toggBtnBreakfast!!.setBackgroundResource(R.color.gray)
            }
        }

        /* 점심 클릭 */
        toggBtnLunch!!.setOnClickListener {
            mType = toggBtnLunch!!.tag.toString()
            setButtonState(toggBtnBreakfast, false)
            setButtonState(toggBtnLunch, true)
            setButtonState(toggBtnDinner, false)
            setButtonState(toggBtnDessert, false)
            if (toggBtnLunch!!.isChecked) {
                toggBtnLunch!!.setBackgroundResource(R.color.maincolor)
            } else {
                toggBtnLunch!!.setBackgroundResource(R.color.gray)
            }
        }


        /* 저녁 클릭 */
        toggBtnDinner!!.setOnClickListener {
            mType = toggBtnDinner!!.tag.toString()
            setButtonState(toggBtnBreakfast, false)
            setButtonState(toggBtnLunch, false)
            setButtonState(toggBtnDinner, true)
            setButtonState(toggBtnDessert, false)

            if (toggBtnDinner!!.isChecked) {
                toggBtnDinner!!.setBackgroundResource(R.color.maincolor)
            } else {
                toggBtnDinner!!.setBackgroundResource(R.color.gray)
            }
        }

        /* 간식 클릭 */
        toggBtnDessert!!.setOnClickListener {
            mType = toggBtnDessert!!.tag.toString()
            setButtonState(toggBtnBreakfast, false)
            setButtonState(toggBtnLunch, false)
            setButtonState(toggBtnDinner, false)
            setButtonState(toggBtnDessert, true)

            if (toggBtnDessert!!.isChecked) {
                toggBtnDessert!!.setBackgroundResource(R.color.maincolor)
            } else {
                toggBtnDessert!!.setBackgroundResource(R.color.gray)
            }
        }

        btnInsert!!.setOnClickListener {
            val params = HashMap<String, Any>()
            params.put("title", editTitle!!.toString())
            params.put("coachId", mCoachId)
            params.put("mealImages", mMealImageList)
            params.put("type", mType!!.toString())
            params.put("content", editTitle!!.toString())
            requestCoachMealInsert(params)
        }

    }


    private fun getIntentInit() {
        val intent = intent
        val bundle = intent.extras
        mCoachId = bundle!!.getInt("coachId")
        Log.d(TAG, "targetId : " + mCoachId)
    }

    private fun setButtonState(btn: ToggleButton?, isChecked: Boolean) {
        btn!!.isChecked = isChecked
        if (isChecked) {
            btn.setBackgroundResource(R.color.maincolor)
        } else {
            btn.setBackgroundResource(R.color.gray)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == OPEN_MEDIA_PICKER) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK && data != null) {
                val selectionResult = data.getStringArrayListExtra("result")
            }
        }
    }


    fun checkPermissionREAD_EXTERNAL_STORAGE(
            context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE)

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    context,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                return false
            } else {
                return true
            }

        } else {
            return true
        }
    }


    fun showDialog(msg: String, context: Context,
                   permission: String) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage(msg + " permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes
        ) { dialog, which ->
            ActivityCompat.requestPermissions(context as Activity,
                    arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    /**
     * 식단 등록 요청
     * **********************************************************************************************************************************************
     *
     * @param param : email,accessToken
     */
    private fun requestCoachMealInsert(param: Map<String, Any>) {


        val requestLoginCall = RetroUtil.createService(Constant.SERVER_ADDR, MemberRestService::class.java).requestCoachMealInsert(param)

        requestLoginCall.enqueue(object : Callback<ResultMessageVO> {
            override fun onResponse(call: Call<ResultMessageVO>, response: Response<ResultMessageVO>) {

            }

            override fun onFailure(call: Call<ResultMessageVO>, t: Throwable) {

            }
        })

    }

    companion object {

        val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
        internal val OPEN_MEDIA_PICKER = 1

        private val TAG = "InsertMealActivity"
    }


}
