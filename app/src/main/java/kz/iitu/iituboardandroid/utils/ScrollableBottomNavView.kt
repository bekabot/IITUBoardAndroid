package kz.iitu.iituboardandroid.utils

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

//this type of annotation is not working in kotlin... damn... T_T
//@CoordinatorLayout.DefaultBehavior(ScrollableBottomNavView.Behavior::class)
class ScrollableBottomNavView(context: Context,
                              attributeSet: AttributeSet) : BottomNavigationView(context, attributeSet) {

    class Behavior(context: Context,
                   attributeSet: AttributeSet) : CoordinatorLayout.Behavior<ScrollableBottomNavView>(context, attributeSet) {

        override fun layoutDependsOn(parent: CoordinatorLayout,
                                     child: ScrollableBottomNavView, dependency: View): Boolean {

            return dependency is AppBarLayout //|| dependency is Snackbar.SnackbarLayout
        }

        override fun onDependentViewChanged(parent: CoordinatorLayout, child: ScrollableBottomNavView, dependency: View): Boolean {

            if (dependency is AppBarLayout) {
                val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
                val bottomMargin = layoutParams.bottomMargin
                val distanceToScroll = child.height + bottomMargin

                var actionBarHeight = 0
                val tv = TypedValue()
                if (parent.context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, parent.context.resources.displayMetrics)
                }

                val ratio = dependency.y / actionBarHeight
                child.translationY = -distanceToScroll * ratio
            }
            return true
        }
    }
}