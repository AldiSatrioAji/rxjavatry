package com.example.chopper.rxjavatutorial

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    var disposable: Disposable? = null
    lateinit var animalsObservable: Observable<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Observable.range(1, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    it % 2 == 0
                }
                .map {
                    it.toString() + " is a number"
                }
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        Toast.makeText(this@MainActivity, "data completed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                        Toast.makeText(this@MainActivity, "subscribing...", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNext(t: String) {
                        Toast.makeText(this@MainActivity, "data " + t, Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(this@MainActivity, "data error", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
