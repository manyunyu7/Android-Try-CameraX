package com.feylabs.learn_camera_x

import android.Manifest


object RazConst {
    const val TAG = "camerax"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"

    const val REQUEST_CODE_PERMISSION = 123
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

}