package com.example.myfirstapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var timerText: TextView? = null
    private val dataFormat = SimpleDateFormat("mm:ss:SSS", Locale.US)
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //時間の設定
        var countTime: Long = 5000
        var interval: Long = 1000
        var setCount: Long = 0

        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        timerText = findViewById<TextView>(R.id.displayTime)

        timerText!!.setText(dataFormat.format(countTime))

        //インスタンスを生成
        var r = CountDown(countTime, interval, setCount)

        startButton.setOnClickListener {
            if(r.timer <= 0){
                //カウントが0になったらインスタンスを再生成
                r = CountDown(countTime, interval, setCount)
            }
            //開始
            handler.post(r)
        }

        stopButton.setOnClickListener {
            // 中止
            handler.removeCallbacks(r)
        }
    }

    internal inner class CountDown(countTime: Long, interval: Long, setCount: Long) : Runnable {
        private var mode: Int = 1  //-1のときはインターバル
        var timer: Long = countTime
        var interval: Long = interval
        val set: Long = setCount
        var setCount: Long = 0
        override fun run() {
            //タイマーのカウントが0以下になったら処理を終了
            if(timer > 0) {
                timer = timer - 39
                timerText!!.setText(dataFormat.format(timer))
                //表示時間がマイナスにならないように調整
                if(timer - 39 <= 0){
                    timerText!!.setText(dataFormat.format(0))
                }
                //handlerが重複して処理を呼ばないようにキャンセル
                handler.removeCallbacks(this)
                handler.postDelayed(this, 39)
            }
            else {
                timerText!!.setText(dataFormat.format(0))
                handler.removeCallbacks(this)
            }
        }

        //モードで
        private fun changeMode(mode: Int){

        }

    }

}

