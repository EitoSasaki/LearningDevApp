package com.example.myfirstapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class SetTimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_time)

        val setCountTimeMinute = findViewById<Spinner>(R.id.setCountTimeMinute)
        val setCountTimeSecond = findViewById<Spinner>(R.id.setCountTimeSecond)
        val setIntervalMinute = findViewById<Spinner>(R.id.setIntervalMinute)
        val setIntervalSecond = findViewById<Spinner>(R.id.setIntervalSecond)
        val setCount = findViewById<Spinner>(R.id.setCount)

        val timeAdapter = ArrayAdapter.createFromResource(this, R.array.listTime, android.R.layout.simple_spinner_item)
        val setCountAdapter = ArrayAdapter.createFromResource(this, R.array.setCount, android.R.layout.simple_spinner_item)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        setCountTimeMinute.setAdapter(timeAdapter)
        setCountTimeSecond.setAdapter(timeAdapter)
        setIntervalMinute.setAdapter(timeAdapter)
        setIntervalSecond.setAdapter(timeAdapter)
        setCount.setAdapter(setCountAdapter)

        val setButton = findViewById<Button>(R.id.setButton)

        setButton.setOnClickListener {
            val countTimeMinute = setCountTimeMinute.getSelectedItem() as String
            val countTimeSecond = setCountTimeSecond.getSelectedItem() as String
            val intervalMinute = setIntervalMinute.getSelectedItem() as String
            val intervalSecond = setIntervalSecond.getSelectedItem() as String
            val set = setCount.getSelectedItem() as String

            val putTime = putTime(countTimeMinute, countTimeSecond, intervalMinute, intervalSecond, set)

        }

    }

    internal inner class putTime(cntTimeMinute: String, cntTimeSecond: String, intervalMinute: String, intervalSecond: String, setCount: String) {

        //MainActivityに渡すメンバを設定
        var countTime: Long = 0
        var interval: Long = 0
        val set: Int = Integer.parseInt(setCount)

        //インスタンスを生成されたときに実行
        init{
            getTimerValue(cntTimeMinute, cntTimeSecond, intervalMinute, intervalSecond)
        }

        //文字列の時間やセット数を数値にキャストし配列でデータを返す
        private fun getTimerValue(cntTimeMinute: String, cntTimeSecond: String, intervalMinute: String, intervalSecond: String) {



        }

        //ミリ秒に変換
        private fun toMilliSecond(minute: Int, second: Int): Long{
            var millis: Long
            var minuteToMillis: Long = minute.toLong()
            var secondToMillis: Long = second.toLong()

            minuteToMillis = minuteToMillis * 60 * 1000
            secondToMillis = secondToMillis * 1000

            millis = minuteToMillis + secondToMillis
            return millis
        }
    }
}
