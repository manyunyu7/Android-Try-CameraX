package com.feylabs.learn_camera_x

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.feylabs.learn_camera_x.RazConst.REQUIRED_PERMISSIONS
import com.feylabs.learn_camera_x.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (allPermissionGranted()) {
            Toast.makeText(this, "We Have Permission", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS,
                RazConst.REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RazConst.REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {

            } else {
                SnackBarHelper.showSnakbarTypeOne(binding.root,"Permission Not Granted")
            }
        }
    }

    private fun allPermissionGranted(): Boolean =
        RazConst.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

}