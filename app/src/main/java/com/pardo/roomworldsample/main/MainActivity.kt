package com.pardo.roomworldsample.main

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.AsyncLayoutInflater
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.pardo.roomwithaword.WordViewModel
import com.pardo.roomwithaword.entities.Word
import com.pardo.roomworldsample.R
import com.pardo.roomworldsample.crud.NewWordActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
    }

    lateinit var layout : FrameLayout
    lateinit var fab : FloatingActionButton
    lateinit var search : Button
    lateinit var delete : Button
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
                search = findViewById(R.id.btb_search)
                search.setOnClickListener{
                    getThis<MainActivity>().onSearchRequested()
                }
                delete = findViewById(R.id.btb_delete)
                delete.setOnClickListener {
                    wordViewModel?.deleteAll()?.observe(getThis<MainActivity>(), Observer<Boolean> { result ->
                        if(result!!) myUI?.clearData()
                    })
                }
            }
        })

        setSupportActionBar(toolbar)

        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            wordViewModel?.find(query)?.observe(this, Observer<Word?> {
                t ->
                var snackbar = Snackbar.make(findViewById(android.R.id.content), "the Word is "+t?.getWord(), Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(getThis<MainActivity>().resources.getColor(android.R.color.holo_red_dark))
                snackbar.show()
            })
        }
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

fun <T: Activity> Activity.getThis() : T{
    return this as T
}
