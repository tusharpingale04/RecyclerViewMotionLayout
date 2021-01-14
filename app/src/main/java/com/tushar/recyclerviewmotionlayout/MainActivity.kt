package com.tushar.recyclerviewmotionlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: TestAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_test)
        initRv()
    }

    private fun initRv() {
        val list = mutableListOf<String>()
        for (i in 1..20) {
            list.add("Chat $i")
        }
        myAdapter = TestAdapter(list) { onClickEvents ->
            when(onClickEvents){
                TestAdapter.OnClickEvents.MuteClick ->{
                    Toast.makeText(this, "Muted!", Toast.LENGTH_LONG).show()
                }
                TestAdapter.OnClickEvents.PinClick ->{
                    Toast.makeText(this, "Pinned!", Toast.LENGTH_LONG).show()
                }
                TestAdapter.OnClickEvents.DeleteClick ->{
                    Toast.makeText(this, "Deleted!", Toast.LENGTH_LONG).show()
                }
                is TestAdapter.OnClickEvents.OnItemClicked -> {
                    Toast.makeText(this,"Item Clicked ${onClickEvents.position + 1}",Toast.LENGTH_SHORT).show()
                }
            }
        }
        myAdapter.setHasStableIds(false)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter

    }

}