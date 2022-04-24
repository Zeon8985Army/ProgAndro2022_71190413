package id.ac.ukdw.pertemuan8_71190413

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.pertemuan8_71190413.FirstFragment
import com.example.pertemuan8_71190413.SecondFragment
import com.example.pertemuan8_71190413.ThirdFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // View Pager
        val viewPager = findViewById<ViewPager2>(R.id.pagerArea)
        val listFragment: ArrayList<Fragment> = arrayListOf(FirstFragment(), SecondFragment(), ThirdFragment())
        val pagerAdapter = PagerAdapter(this, listFragment)
        viewPager.adapter = pagerAdapter

    }

    class PagerAdapter(val activity: AppCompatActivity, val listFragment: ArrayList<Fragment>): FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return listFragment.size
        }
        override fun createFragment(position: Int): Fragment {
            return listFragment.get(position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)= when(item.itemId) {
        R.id.itm_go1 -> {
            val viewPager = findViewById<ViewPager2>(R.id.pagerArea)
            viewPager.setCurrentItem(0)
            true
        }
        R.id.itm_go2 -> {
            val viewPager = findViewById<ViewPager2>(R.id.pagerArea)
            viewPager.setCurrentItem(1)
            true
        }
        R.id.itm_go3 -> {
            val viewPager = findViewById<ViewPager2>(R.id.pagerArea)
            viewPager.setCurrentItem(2)
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }
    }
}