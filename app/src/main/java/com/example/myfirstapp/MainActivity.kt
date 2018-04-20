package com.example.myfirstapp

import android.media.AudioAttributes
import android.media.SoundPool
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
    private val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
    private val soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //時間の設定
        var countTime: Long = 180000
        var interval: Long = 60000
        var set: Long = 3

        //Viewの設定
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        timerText = findViewById<TextView>(R.id.displayTime)
        timerText!!.setText(dataFormat.format(countTime))

        //効果音の設定
        //var soundOne: Int
        //soundOne = soundPool.load(this, R.raw.se_maoudamashii_system49, 1) //音源：魔王魂

        //インスタンスを生成
        var r = CountDown(countTime, interval, set)

        startButton.setOnClickListener {
            if(r.timer <= 0){
                //カウントが0になったらインスタンスを再生成
                r = CountDown(countTime, interval, set)
            }
            //開始
            handler.post(r)
            //soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1F)
        }

        stopButton.setOnClickListener {
            // 中止
            handler.removeCallbacks(r)
        }
    }

    internal inner class CountDown(countTime: Long, interval: Long, set: Long) : Runnable {
        //変数の設定
        var timer: Long = 0 //カウントダウン用の時間をセット
        private val countTime: Long = countTime
        private val interval: Long = interval
        private val set: Long = set //最大セット数
        private var setCount: Long = 0 //セット数カウント用
        private var mode: Int = -1  //-1のときはインターバル

        override fun run() {
            //タイマーの更新
            if(timer <= 0) {
                //モードの変更
                mode = mode * -1
                //セットはインターバルが終了したらインクリメント
                if(mode > 0) {
                    setCount++
                }
                //セット数が超えたら終了
                if(setCount > set) {
                    handler.removeCallbacks(this)
                }
                else{
                    //タイマーをセット
                    timer = setTimer(mode)
                }
            }
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
        }

        //モードでセットする時間を切り替える
        private fun setTimer(mode: Int): Long {
            var timer: Long = 0
            if(mode > 0){
                timer = countTime
            }
            else {
                timer = interval
            }
            return timer
        }

    }

}

