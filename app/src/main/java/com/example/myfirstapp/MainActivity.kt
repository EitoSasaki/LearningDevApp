package com.example.myfirstapp

import android.content.Intent
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
    private var setCountText: TextView? = null
    private val dateFormat = SimpleDateFormat("mm:ss:SSS", Locale.US)
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

        //タイマーの設定
        val setTimeIntent: Intent = getIntent()
        var countTime: Long = setTimeIntent.getLongExtra("countTime", 10000)
        var interval: Long = setTimeIntent.getLongExtra("interval", 5000)
        var set: Int = setTimeIntent.getIntExtra("set", 3)

        //Viewの設定
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        timerText = findViewById(R.id.displayTime)
        timerText!!.setText(dateFormat.format(countTime))
        setCountText = findViewById(R.id.countSet)
        setCountText!!.setText("SET: 1")

        //戻るボタン
        val backButton = findViewById<Button>(R.id.backSetTime)
        backButton.setOnClickListener {
            val intent = Intent(this, SetTimeActivity::class.java)
            startActivity(intent)
        }

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

    internal inner class CountDown(countTime: Long, interval: Long, set: Int) : Runnable {
        //変数の設定
        var timer: Long = 0 //カウントダウン用の時間をセット
        private val countTime: Long = countTime
        private val interval: Long = interval
        private val set: Int = set //最大セット数
        private var setCount: Int = 0 //セット数カウント用
        private var mode: Int = -1  //-1のときはインターバル

        override fun run() {
            //タイマーの更新
            if(timer <= 0) {
                updateTimer()
            }
            //タイマーのカウントが0以下になったら処理を終了
            if(timer > 0) {
                timer = timer - 31
                //表示時間がマイナスにならないように調整
                if(timer <= 0){
                    timer = 0
                }
                timerText!!.setText(dateFormat.format(timer))
                //handlerが重複して処理を呼ばないようにキャンセル
                handler.removeCallbacks(this)
                handler.postDelayed(this, 31)
            }
        }

        //タイマーの情報を更新する
        private fun updateTimer(){
            //モードの変更(intervalが０の時はmodeを１で固定)
            //初セットは必ず通るようにする
            if(interval > 0 || setCount == 0){
                mode = mode * -1
            }
            //セットはインターバルが終了したらインクリメント
            if(mode > 0) {
                setCount++
            }
            //カウントがセット数を超えたら終了(最終セットはインターバル無し)
            if(setCount > set || (setCount >= set && mode < 0)) {
                handler.removeCallbacks(this)
            }
            else{
                //セット数を表示
                setCountText!!.setText(toStringSetCount())
                //タイマーをセット
                timer = setTimer()
            }
        }

        //モードでセットする時間を切り替える
        private fun setTimer(): Long {
            var timer: Long = 0
            if(mode > 0){
                timer = countTime
            }
            else {
                timer = interval
            }
            return timer
        }

        //セット数の表示を調整する（キャスト）
        private fun toStringSetCount(): String {
            var str: String = "SET: "
            str = str + Integer.toString(setCount)
            return str
        }

    }

}

