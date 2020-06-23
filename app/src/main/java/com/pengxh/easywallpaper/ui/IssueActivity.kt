package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.FileUtil
import com.pengxh.easywallpaper.utils.NewGlideEngine
import com.pengxh.easywallpaper.utils.SendMailUtil
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_issue.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/22 17:47
 */
class IssueActivity : BaseNormalActivity() {

    companion object {
        private const val Tag: String = "IssueActivity"
        private const val REQUEST_CODE_SELECT = 100
    }

    private var imageURI: Uri? = null

    override fun initLayoutView(): Int = R.layout.activity_issue

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleView.text = "问题反馈"
        mTitleRightView.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun initEvent() {
        inputIssueView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
                if (s != null) {
                    inputStringLength.text = "${s.length}/150"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
            }
        })
        selectPicture.setOnClickListener {
            Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true) //是否显示数字
                .showSingleMediaType(true)
                //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                //参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .captureStrategy(CaptureStrategy(true, "com.pengxh.easywallpaper.fileprovider"))
                .countable(true)
                //最大选择数量为9
                .maxSelectable(1)
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //界面中缩略图的质量
                .thumbnailScale(0.85f)
                //蓝色主题
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(NewGlideEngine())
                .forResult(REQUEST_CODE_SELECT)
        }
        submitIssue.setOnClickListener {
            //发送邮件
            val emailText = inputIssueView.text.toString().trim()
            if (emailText == "") {
                EasyToast.showToast("请描述您遇到的问题", EasyToast.ERROR)
                return@setOnClickListener
            }
            if (imageURI == null) {
                SendMailUtil.sendTextEmail(emailText)
            } else {
                SendMailUtil.sendAttachFileEmail(
                    emailText,
                    FileUtil.getRealFilePath(this, imageURI)!!
                )
            }
        }
        mTitleLeftView.setOnClickListener {
            this.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT && resultCode == AppCompatActivity.RESULT_OK) {
            //图片路径
            imageURI = Matisse.obtainResult(data)[0]
            selectPicture.visibility = View.GONE
            imageLayout.visibility = View.VISIBLE
            uploadImage.setImageURI(imageURI)
            deleteImage.setOnClickListener {
                uploadImage.setImageDrawable(null)
                imageLayout.visibility = View.GONE
                selectPicture.visibility = View.VISIBLE
            }
            uploadImage.setOnClickListener {
                val intent = Intent(this, BigPictureActivity::class.java)
                intent.putExtra("imageURI", imageURI.toString())
                startActivity(intent)
            }
        }
    }
}