package com.pengxh.easywallpaper.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/15 16:08
 */
class FileUtil {
    companion object {
        private const val Tag = "FileUtil"

        /**
         * 保存壁纸
         *
         * Android 10 创建文件夹需要声明 android:requestLegacyExternalStorage="true"
         * */
        fun saveWallpaper(context: Context, drawable: BitmapDrawable): Boolean {
            val bitmap = drawable.bitmap
            //创建保存壁纸的文件夹
            val wallpaperDir = File(Environment.getExternalStorageDirectory(), "EasyWallpaper")
            if (!wallpaperDir.exists()) {
                wallpaperDir.mkdir()
            }
            val fileName = System.currentTimeMillis().toString() + ".jpg"
            val file = File(wallpaperDir, fileName)
            try {
                //保存图片
                val fos = FileOutputStream(file)
                val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()

                //保存图片后发送广播通知更新数据库
                try {
                    MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        file.absolutePath,
                        fileName,
                        null
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                // 通知图库更新
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + "/sdcard/namecard/")
                    )
                )
                return isSuccess
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}