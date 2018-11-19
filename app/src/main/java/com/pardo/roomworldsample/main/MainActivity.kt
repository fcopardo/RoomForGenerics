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
import com.pardo.roomwithaword.entities.Word
import com.pardo.roomworldsample.R
import com.pardo.roomworldsample.crud.NewWordActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
    }

    lateinit var myUI : MainUI
    lateinit var layout : ConstraintLayout
    lateinit var fab : FloatingActionButton
    lateinit var wordViewModel : WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        myUI = MainUI(this)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.main_content)
        fab = findViewById(R.id.fab)
        setSupportActionBar(toolbar)
        layout.addView(myUI)

        wordViewModel.getAllWords()?.observe(this, Observer<List<Word>> { t -> myUI.setData(t!!) })

        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val word = Word(data!!.getStringExtra(NewWordActivity.EXTRA_REPLY))
            wordViewModel.persist(word)
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }


}
