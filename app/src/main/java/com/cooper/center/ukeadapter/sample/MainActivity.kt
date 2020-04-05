package com.cooper.center.ukeadapter.sample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooper.center.ukeadapter.UkeAdapter
import com.cooper.center.ukeadapter.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = DataBindingUtil.setContentView(this, R.layout.activity_main)

        layout()
    }

    private fun layout() {
        val adapter = UkeAdapter.Builder()
            .binding(Card1::class.java, R.layout.view_card_1)
            .viewModel(Card2::class.java, R.layout.view_card_2, Card2ViewModel::class.java)
            .build()

        adapter.add(Card1("Card Type 1 Red", Color.RED))
        adapter.add(Card2("Card Type 2 Red, Green", "What are you talking about?", Color.RED, Color.GREEN))
        adapter.add(Card1("Card Type 1 Black", Color.BLACK))
        adapter.add(Card1("Card Type 1 Green", Color.GREEN))
        adapter.add(Card2("Card Type 2 Blue, Magenta", "Nice talk to you!", Color.BLUE, Color.MAGENTA))
        adapter.add(Card1("Card Type 1 Blue", Color.BLUE))
        adapter.add(Card2("Card Type 2 Red, Cyan", "Please wait for me.", Color.RED, Color.CYAN))

        binder.recyclerView.layoutManager = LinearLayoutManager(this)
        binder.recyclerView.adapter = adapter
    }

}
