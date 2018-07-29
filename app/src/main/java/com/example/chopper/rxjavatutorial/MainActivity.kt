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

    val compositeDisposable = CompositeDisposable()
    lateinit var animalsObservable: Observable<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animalsObservable = Observable.fromArray(
                "Ant", "Ape",
                "Bat", "Bee", "Bear", "Butterfly",
                "Cat", "Crab", "Cod",
                "Dog", "Dove",
                "Fox", "Frog"
        )

        compositeDisposable.add(
                observeAnimals()
                        .filter {
                            it.startsWith("b")
                        }
                        .subscribeWith(object : DisposableObserver<String>() {
                            override fun onComplete() {
                                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_SHORT).show()
                            }

                            override fun onNext(t: String) {
                                Toast.makeText(this@MainActivity, "onNext " + t, Toast.LENGTH_SHORT).show()
                            }

                            override fun onError(e: Throwable) {
                                Toast.makeText(this@MainActivity, "Error " + e.message, Toast.LENGTH_SHORT).show()
                            }
                        })
        )

        compositeDisposable.add(
                observeAnimals()
                        .filter {
                            it.startsWith("c")
                        }
                        .map {
                            it.toUpperCase()
                        }
                        .subscribeWith(object : DisposableObserver<String>() {
                            override fun onComplete() {
                                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_SHORT).show()
                            }

                            override fun onNext(t: String) {
                                Toast.makeText(this@MainActivity, "onNext " + t, Toast.LENGTH_SHORT).show()
                            }

                            override fun onError(e: Throwable) {
                                Toast.makeText(this@MainActivity, "Error " + e.message, Toast.LENGTH_SHORT).show()
                            }
                        })
        )
    }

    private fun observeAnimals() : Observable<String> {
        return animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}
