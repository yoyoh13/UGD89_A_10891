package com.yohanes.ugd89_a_10891_project1

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraView(context: Context?, private var mCamera: android.hardware.Camera) : SurfaceView(context),  SurfaceHolder.Callback{

    private var currentCameraId = -1
    private  val mHolder: SurfaceHolder
    init {
        mCamera.setDisplayOrientation(90)
        mHolder = holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        }catch (e: IOException){
            Log.d("error", "camera error on SurfaceCreated" + e.message)
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i:Int, i1: Int, i2:Int) {
        if (mHolder.surface == null) return
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        }catch (e: IOException){
            Log.d("error", "camera error on SurfaceChanged" + e.message)
        }
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        mCamera.stopPreview()
        mCamera.release()
    }

    fun changeCamera(){
        if (mCamera != null) {
            mCamera.stopPreview()
        }
        mCamera.release()

        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                currentCameraId = i
        }
        //swap the id of the camera to be used
        if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
        }
        else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK
        }
        mCamera = Camera.open(currentCameraId)

        mCamera.setDisplayOrientation(90)
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: IOException) {
            Log.d("Error", "Camera error on switchCamera" + e.message)
        }
        mCamera.startPreview()
    }

}