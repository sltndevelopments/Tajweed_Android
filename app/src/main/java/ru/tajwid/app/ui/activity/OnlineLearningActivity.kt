package ru.tajwid.app.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activitiy_learning.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Group
import ru.tajwid.app.content.data.OnlineLearning
import ru.tajwid.app.ui.adapter.TimeZoneAdapter
import kotlin.properties.Delegates

class OnlineLearningActivity : AppCompatActivity() {
    private val timeZoneAdapter: TimeZoneAdapter by lazy {
        TimeZoneAdapter()
    }
    private val list: MutableList<Group> = mutableListOf()

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
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
            if (selectedGroup == null) {
                showMessage("Пожалуйста, подождите...")
                return@setOnClickListener
            }

            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(selectedGroup?.link)
                startActivity(i)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }

        // навигация назад  в меню
        val toolbar = findViewById<Toolbar>(R.id.learning_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        swipe_refresh.setOnRefreshListener {
            database.get().addOnCompleteListener {
                it.result.getValue(OnlineLearning::class.java)?.let {
                    fillData(it)
                }
            }
        }

        radio_group_1.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radiobutton1 -> selectedGroup = list.getOrNull(0)
                R.id.radiobutton2 -> selectedGroup = list.getOrNull(1)
                R.id.radiobutton3 -> selectedGroup = list.getOrNull(2)
            }
        }
        swipe_refresh.isRefreshing = true
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(OnlineLearning::class.java)?.let {
                    fillData(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fillData(data: OnlineLearning) {
        list.clear()
        list.addAll(data.groups)

        if (list.isEmpty()) return

        selectedGroup = list[0]
        radio_group_1.check(R.id.radiobutton1)
        swipe_refresh.isRefreshing = false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

fun Activity.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showMessage(@StringRes messageRes: Int) {
    Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
}