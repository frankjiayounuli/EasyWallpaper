package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aihook.alertview.library.AlertView
import com.aihook.alertview.library.OnItemClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pengxh.easywallpaper.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup


/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/15 10:06
 */
class BigPictureAdapter(ctx: Context, list: ArrayList<String>) :
    RecyclerView.Adapter<BigPictureAdapter.ItemViewHolder>() {

    private var context: Context = ctx
    private var beanList: ArrayList<String> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_bigpicture, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val childDocument = withContext(Dispatchers.IO) {
                Jsoup.connect(beanList[position]).timeout(10 * 1000).get()
            }
            val bigImageUrl = childDocument.select("img[class]").first().attr("src")
            Glide.with(context).load(bigImageUrl).into(holder.photoView)

            holder.photoView.setOnLongClickListener {
                AlertView(
                    "提示",
                    "是否保存此张壁纸",
                    "取消",
                    arrayOf("确定"),
                    null,
                    context,
                    AlertView.Style.Alert,
                    OnItemClickListener { o, position ->
                        if (position == 0) {
                            GlobalScope.launch(Dispatchers.Main) {
                                val drawable = withContext(Dispatchers.IO) {
                                    Glide.with(context).load(bigImageUrl)
                                        .apply(RequestOptions().centerCrop())
                                        .into(
                                            Target.SIZE_ORIGINAL,
                                            Target.SIZE_ORIGINAL
                                        ).get()
                                }
                                saveWallpaper(drawable)
                            }
                        }
                    }).setCancelable(false).show()
                false
            }
        }
    }

    override fun getItemCount(): Int = beanList.size

    inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var photoView: ImageView = itemView.findViewById(R.id.photoView)
    }

    private fun saveWallpaper(drawable: Drawable) {
        // 首先保存图片
//        val appDir =
//            File(Environment.getExternalStorageDirectory(), "EasyWallpaper")
//        if (!appDir.exists()) {
//            appDir.mkdir()
//        }
//        val fileName = System.currentTimeMillis().toString() + ".png"
//        val file = File(appDir, fileName)
//        try {
//            val fos = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//            fos.flush()
//            fos.close()
//            EasyToast.showToast("保存成功", EasyToast.SUCCESS)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            EasyToast.showToast("保存失败", EasyToast.ERROR)
//        }
//        // 把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(
//                context.contentResolver,
//                file.absolutePath,
//                fileName,
//                null
//            )
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        // 通知图库更新
//        context.sendBroadcast(
//            Intent(
//                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                Uri.parse("file://" + "/sdcard/namecard/")
//            )
//        )
    }
}