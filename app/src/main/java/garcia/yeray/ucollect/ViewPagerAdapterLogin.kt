package garcia.yeray.ucollect

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapterLogin(fragment: MainActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return Login()
            1 -> return Signup()
            else -> return Login()
        }
    }
}