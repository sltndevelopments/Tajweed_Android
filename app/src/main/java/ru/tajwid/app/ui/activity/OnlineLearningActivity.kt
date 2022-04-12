package ru.tajwid.app.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activitiy_learning.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Group
import ru.tajwid.app.content.data.OnlineLearning
import ru.tajwid.app.ui.adapter.TimeZoneAdapter
import ru.tajwid.app.ui.fragment.RegisterFragment
import kotlin.properties.Delegates

class OnlineLearningActivity : BaseActivity() {
    private val timeZoneAdapter: TimeZoneAdapter by lazy {
        TimeZoneAdapter()
    }
    private val list: MutableList<Group> = mutableListOf()

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private var selectedGroup: Group? by Delegates.observable(null) { prop, old, new ->
        if (old == new) return@observable

        timeZoneAdapter.list = new?.schedule.orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitiy_learning)
        recycler_list?.apply {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@OnlineLearningActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = timeZoneAdapter
        }

        goToClass.setOnClickListener {
            openDialog(RegisterFragment())
        }

        // навигация назад  в меню
        setupSimpleToolbar(learning_toolbar, getString(R.string.title_with_teacher))

        swipe_refresh.setOnRefreshListener {
            database.get().addOnCompleteListener {
                it.result.getValue(OnlineLearning::class.java)?.let {
                    fillData(it)
                }
            }
        }

        radio_group_1.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedGroup = (tab?.tag as? Group)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                selectedGroup = (tab?.tag as? Group)
            }

        })

        swipe_refresh.isRefreshing = true
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(OnlineLearning::class.java)?.let {
                    fillData(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showMessage(error.message)
                swipe_refresh.isRefreshing = false
            }
        })
    }

    private fun fillData(data: OnlineLearning) {
        list.clear()
        list.addAll(data.groups)

        if (list.isEmpty()) return

        radio_group_1.removeAllTabs()
        list.forEach {
            radio_group_1.addTab(radio_group_1.newTab().apply {
                text = it.name
                tag = it
            }, false)
        }

        radio_group_1.selectTab(radio_group_1.getTabAt(0))
        swipe_refresh.isRefreshing = false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}