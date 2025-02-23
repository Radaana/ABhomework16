package ru.sigenna.bgcounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.sigenna.bgcounter.presentation.AddEntryFragment
import ru.sigenna.bgcounter.presentation.EntryListFragment
import ru.sigenna.bgcounter.presentation.SummaryFragment


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var bottomNav:BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, EntryListFragment())
                .commit()
        }

        bottomNav = findViewById(R.id.bottom_navigation)

        bottomNav?.setOnItemSelectedListener  { item ->
            val fragment: Fragment? = when(item.itemId) {
                R.id.pageAdd -> AddEntryFragment()
                R.id.pageList -> EntryListFragment()
                R.id.pageSummary -> SummaryFragment()

                else -> null
            }

            if (fragment == null) {
                return@setOnItemSelectedListener false
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()

            return@setOnItemSelectedListener true
        }
    }

    fun toList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, EntryListFragment()).commit()
        bottomNav?.selectedItemId = R.id.pageList
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNav = null
    }
}