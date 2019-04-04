package com.android.camerademo

import android.graphics.ImageFormat
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

/*思路 获取camera 开启预览 预览回调 拍照
* */
class MainActivity : AppCompatActivity(), SurfaceHolder.Callback,Camera.PreviewCallback {
    override fun onPreviewFrame(data: ByteArray, camera: Camera?) {
       // Log.i("onPreviewFrame", "${String(data)}")
    }

    var camera:Camera? = null
    val logUtils = com.android.camerademo.LogUtils()

    var holder: SurfaceHolder? = null

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        camera?.stopPreview()//停止然后重新启动
        holder?.let { startPreview(camera, it) }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        releaseCamera()

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        logUtils.d("surfaceCreated")
        holder?.let { startPreview(camera, it) }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        holder = sv.holder
        holder?.addCallback(this)
    }

    /**
     * 开启相机预览
     * */
    private fun startPreview(camera: Camera?, holder: SurfaceHolder) {
        camera?.setPreviewDisplay(holder)

        camera?.setDisplayOrientation(90)
        camera?.startPreview()
        camera?.setPreviewCallback(this)

    }

    private fun getSysCamera(): Camera? {
        try {
            //打开相机，默认为后置，可以根据摄像头ID来指定打开前置还是后置
            camera = Camera.open(0)
        } catch (e: Exception) {
            camera = null
            e.printStackTrace()
            Toast.makeText(this, "获取摄像头失败", Toast.LENGTH_LONG)
        }
        return camera
    }

    fun releaseCamera() {
        camera?.stopPreview()
        camera?.setPreviewDisplay(null)
        camera?.release()
        camera = null

    }

    override fun onResume() {
        super.onResume()
        if (camera == null) {
            camera = getSysCamera()
        }
        holder?.let { startPreview(camera, it) }
    }

    override fun onPause() {
        super.onPause()
        holder?.let { startPreview(camera, it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }
}


class LogUtils {
    open fun d(s: String) {
        Log.e("craera---", "$s")
    }
}
