package com.i.o.mob.dev.weather.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class LinearLayoutManagerWrapper(context: Context, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

}