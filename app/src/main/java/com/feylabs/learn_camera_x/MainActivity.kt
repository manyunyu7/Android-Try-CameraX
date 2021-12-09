package com.feylabs.learn_camera_x

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.feylabs.learn_camera_x.RazConst.REQUIRED_PERMISSIONS
import com.feylabs.learn_camera_x.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.LifecycleOwner


class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        outputDirectory = getOutputDirectory()

        if (allPermissionGranted()) {
            startCamera()
            Toast.makeText(this, "We Have Permission", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS,
                RazConst.REQUEST_CODE_PERMISSION
            )
        }

        binding.btnTakePhoto.setOnClickListener {
            SnackBarHelper.showSnakbarTypeTwo(
                binding.root,
                "Take Photo"
            )
            takePhoto()
        }

    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                RazConst.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(p0: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo Saved"
                    SnackBarHelper.showSnakbarTypeTwo(binding.root, "$msg $savedUri")
                }

                override fun onError(p0: ImageCaptureException) {
                    SnackBarHelper.showSnakbarTypeTwo(binding.root, p0.message)
                }

            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RazConst.REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                SnackBarHelper.showSnakbarTypeOne(binding.root, "Permission Not Granted")
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val previewView = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }

            imageCapture = ImageCapture.Builder()
                .build()

            binding.btnFlashlight.setOnClickListener {
                Toast.makeText(this, "Fl", Toast.LENGTH_SHORT).show()
                if (imageCapture?.flashMode == ImageCapture.FLASH_MODE_OFF ||
                    imageCapture?.flashMode == ImageCapture.FLASH_MODE_AUTO
                ) {
                    imageCapture?.flashMode = ImageCapture.FLASH_MODE_ON
                }

                if (imageCapture?.flashMode == ImageCapture.FLASH_MODE_ON) {
                    imageCapture?.flashMode = ImageCapture.FLASH_MODE_OFF
                }

            }


            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                val cam: Camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, previewView, imageCapture
                )

                binding.btnFlashlight.setOnClickListener {
                    if (cam.getCameraInfo().hasFlashUnit()) {

                        when(cam.cameraInfo.torchState.value){
                            TorchState.ON->{
                                cam.getCameraControl().enableTorch(false) // or false
                            }
                            TorchState.OFF->{
                                cam.getCameraControl().enableTorch(true) // or false
                            }
                        }

                    }
                }

            } catch (e: Exception) {
                SnackBarHelper.showSnakbarTypeTwo(binding.root, "Camera Tidak Tersedia")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionGranted(): Boolean =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

}