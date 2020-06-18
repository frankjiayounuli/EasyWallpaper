package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
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
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.FileUtil
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
        val bigImageLink = beanList[position]
        GlobalScope.launch(Dispatchers.Main) {
            val childDocument = withContext(Dispatchers.IO) {
                Jsoup.connect(bigImageLink).timeout(10 * 1000).get()
            }
            val bigImageUrl =
                childDocument.getElementById("pic-meinv").select("img[class]").first().attr("url")

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
                                        ).get() as BitmapDrawable
                                }
                                if (FileUtil.saveWallpaper(context, drawable)) {
                                    EasyToast.showToast("保存成功", EasyToast.SUCCESS)
                                } else {
                                    EasyToast.showToast("保存失败", EasyToast.ERROR)
                                }
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
}