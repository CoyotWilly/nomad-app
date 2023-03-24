package com.coyotwilly.nomad

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import androidx.core.content.ContextCompat

class ThemeWatcher(view: View) {
    private var bgColor: Int = R.color.white
    init {

        when(view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> bgColor = R.color.shape_bg_dark
            Configuration.UI_MODE_NIGHT_NO -> bgColor = R.color.shape_bg
            Configuration.UI_MODE_NIGHT_UNDEFINED -> println("NIGHT undefined, JavaScript Dev Keeeee")
        }
        when(view.background){
            is ShapeDrawable -> (view.background as ShapeDrawable).paint.color = ContextCompat.getColor(view.context, bgColor)
            is GradientDrawable -> (view.background as GradientDrawable).setColor(
                ContextCompat.getColor(view.context,bgColor))
            is ColorDrawable -> (view.background as ColorDrawable).color = ContextCompat.getColor(view.context, bgColor)
        }
    }
    fun imgBackground(view: View) {
        bgColor = R.color.login_ic_bg
        when(view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> bgColor = R.color.black
            Configuration.UI_MODE_NIGHT_NO -> bgColor = R.color.login_ic_bg
            Configuration.UI_MODE_NIGHT_UNDEFINED -> println("NIGHT undefined, JavaScript Dev Keeee")
        }
        when(view.background){
            is ShapeDrawable -> (view.background as ShapeDrawable).paint.color = ContextCompat.getColor(view.context, bgColor)
            is GradientDrawable -> (view.background as GradientDrawable).setColor(
                ContextCompat.getColor(view.context,bgColor))
            is ColorDrawable -> (view.background as ColorDrawable).color = ContextCompat.getColor(view.context, bgColor)
        }
    }
}