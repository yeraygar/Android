package garcia.yeray.ucollect

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class TransformadorVista : ViewPager2.PageTransformer {
    private val minScale : Float = 0.70f
    private val minAlpha = 0.5f
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        if(position < -1) {
            page.alpha = 0f
        }else if(position <= 1) {
            val scaleFactor = minScale.coerceAtLeast(1 - abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if(position < 0) {
                page.translationX = horzMargin - vertMargin / 2
            }else{
                page.translationX = -horzMargin + vertMargin /2
            }

            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            page.alpha = minAlpha + (scaleFactor - minScale) / (1 - minScale) * (1 - minAlpha)
        }
    }

}