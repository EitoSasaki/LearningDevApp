package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class SetTimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_time)

        //ViewオブジェクトのIDを取得
        val setCountTimeMinute = findViewById<Spinner>(R.id.setCountTimeMinute)
        val setCountTimeSecond = findViewById<Spinner>(R.id.setCountTimeSecond)
        val setIntervalMinute = findViewById<Spinner>(R.id.setIntervalMinute)
        val setIntervalSecond = findViewById<Spinner>(R.id.setIntervalSecond)
        val setCount = findViewById<Spinner>(R.id.setCount)
        val setButton = findViewById<Button>(R.id.setButton)

        //配列の要素をSpinnerのドロップダウンに入れる
        val timeAdapter = ArrayAdapter.createFromResource(this, R.array.listTime, android.R.layout.simple_spinner_item)
        val setCountAdapter = ArrayAdapter.createFromResource(this, R.array.setCount, android.R.layout.simple_spinner_item)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        setCountTimeMinute.setAdapter(timeAdapter)
        setCountTimeSecond.setAdapter(timeAdapter)
        setIntervalMinute.setAdapter(timeAdapter)
        setIntervalSecond.setAdapter(timeAdapter)
        setCount.setAdapter(setCountAdapter)

        //MainActivityに入力値を渡す
        setButton.setOnClickListener {
            //spinnerの値を取得
            val countTimeMinute = setCountTimeMinute.getSelectedItem() as String
            val countTimeSecond = setCountTimeSecond.getSelectedItem() as String
            val intervalMinute = setIntervalMinute.getSelectedItem() as String
            val intervalSecond = setIntervalSecond.getSelectedItem() as String
            val set = setCount.getSelectedItem() as String

            //入力値を整理するためのインスタンスを生成
            val putTime = putTime(countTimeMinute, countTimeSecond, intervalMinute, intervalSecond, set)

            //セットする時間などの情報をMainActivityに渡す
            val intent: Intent = Intent(this, MainActivity::class.java)
            intent.putExtra("countTime", putTime.countTime)
            intent.putExtra("interval", putTime.interval)
            intent.putExtra("set", putTime.set)
            startActivity(intent)
        }

    }

    internal inner class putTime(cntTimeMinute: String, cntTimeSecond: String, intervalMinute: String, intervalSecond: String, setCount: String) {

        //MainActivityに渡すメンバを設定
        var countTime: Long = 0
        var interval: Long = 0
        val set: Int = Integer.parseInt(setCount)

        //インスタンスが生成されたときに実行
        init{
            getTimerValue(cntTimeMinute, cntTimeSecond, intervalMinute, intervalSecond)
        }

        //文字列の時間を数値にキャストし、ミリ秒に変換した状態でメンバ変数に格納する
        private fun getTimerValue(cntTimeMinute: String, cntTimeSecond: String, intervalMinute: String, intervalSecond: String) {
            countTime = toMilliSecond(Integer.parseInt(cntTimeMinute), Integer.parseInt(cntTimeSecond))
            interval = toMilliSecond(Integer.parseInt(intervalMinute), Integer.parseInt(intervalSecond))
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
