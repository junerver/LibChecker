package com.absinthe.libchecker.view.statistics

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absinthe.libchecker.base.BaseActivity
import com.absinthe.libchecker.recyclerview.BottomSheetRecyclerView
import com.absinthe.libchecker.recyclerview.adapter.AppAdapter
import com.absinthe.libchecker.utils.LCAppUtils
import com.absinthe.libchecker.utils.extensions.addPaddingTop
import com.absinthe.libchecker.utils.extensions.dp
import com.absinthe.libchecker.view.app.IHeaderView
import com.absinthe.libchecker.view.detail.AndroidVersionLabelView
import com.absinthe.libraries.utils.view.BottomSheetHeaderView
import me.zhanghai.android.fastscroll.FastScrollerBuilder

@SuppressLint("ViewConstructor")
class ClassifyDialogView(context: Context, val lifecycleScope: LifecycleCoroutineScope) :
  LinearLayout(context), IHeaderView {

  val adapter = AppAdapter()

  private val header = BottomSheetHeaderView(context).apply {
    layoutParams =
      LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }

  private val list = BottomSheetRecyclerView(context).apply {
    layoutParams = LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT,
      1.0f
    ).also {
      it.topMargin = 4.dp
    }
    layoutManager = LinearLayoutManager(context)
    adapter = this@ClassifyDialogView.adapter
    overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    isVerticalScrollBarEnabled = false
    clipToPadding = false
    clipChildren = false
    isNestedScrollingEnabled = false
    setHasFixedSize(true)
    FastScrollerBuilder(this).useMd2Style().build()
  }

  init {
    orientation = VERTICAL
    addPaddingTop(16.dp)
    adapter.apply {
      setOnItemClickListener { _, _, position ->
        LCAppUtils.launchDetailPage(context as BaseActivity<*>, adapter.getItem(position))
      }
      setEmptyView(
        LibReferenceLoadingView(context).apply {
          layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500.dp)
        }
      )
    }
    addView(header)
    addView(list)
  }

  private val androidVersionView = AndroidVersionLabelView(context).apply {
    layoutParams = LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
    setPadding(0, 4.dp, 0, 4.dp)
  }

  fun addAndroidVersionView(triple: Triple<Int, String, Int?>?) {
    if (androidVersionView.parent == null && triple != null) {
      androidVersionView.setIcon(triple.third)
      androidVersionView.text.text = triple.second
      addView(androidVersionView, 1)
    }
  }

  override fun getHeaderView(): BottomSheetHeaderView {
    return header
  }
}
