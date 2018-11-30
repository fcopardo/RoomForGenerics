package com.pardo.roomworldsample.main

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import com.pardo.roomwithaword.WordViewModel

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.content.Intent
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.AsyncLayoutInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.pardo.roomwithaword.WordViewModel2
import com.pardo.roomwithaword.entities.Word
import com.pardo.roomworldsample.R
import com.pardo.roomworldsample.crud.NewWordActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
    }

    lateinit var layout : FrameLayout
    lateinit var fab : FloatingActionButton
    var myUI : MainUI? = null
    var wordViewModel : WordViewModel? = null

    override fun onResume() {
        super.onResume()
        wordViewModel?.getAllData()?.observe(this, Observer<MutableList<Word>> { t ->
            if (t != null) {
                myUI?.setData(t)
            }
            Log.e("RoomDB", "viewmodel Observer called")
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        myUI = MainUI(this)
        val inflater = AsyncLayoutInflater(this)
        inflater.inflate(R.layout.activity_main, myUI, object : AsyncLayoutInflater.OnInflateFinishedListener{
            override fun onInflateFinished(p0: View, p1: Int, p2: ViewGroup?) {
                setContentView(p0)
                layout = findViewById(R.id.main_content)
                fab = this@MainActivity.findViewById(R.id.fab)
                val params = CoordinatorLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                myUI?.layoutParams = params
                layout.addView(myUI)
                fab.setOnClickListener { view ->
                    val intent = Intent(this@MainActivity, NewWordActivity::class.java)
                    startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE)
                }
            }
        })

        setSupportActionBar(toolbar)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val word = Word(data!!.getStringExtra(NewWordActivity.EXTRA_REPLY))
            wordViewModel?.persist(word)
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }
}
