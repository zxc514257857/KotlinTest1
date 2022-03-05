package com.example.lib_net

import android.util.Log
import com.example.lib_pojo.respondBean.MerchantInfoBean
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case7.net
 * @Author: zhr
 * @Date: 2022/2/28 10:12
 * @Version: V1.0
 */
class NetServer private constructor(baseUrl: String) {

    private val mGson = Gson()
    private var requestService: INetServer

    companion object {
        private var mInstance: NetServer? = null
        private val TIMEOUT = 10L

        @Synchronized
        fun getInstance(baseUrl: String): NetServer {
            if (mInstance == null) {
                mInstance = NetServer(baseUrl)
            }
            return mInstance!!
        }
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            // 应该是用的是addNetworkInterceptor而不是addInterceptor
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        requestService = retrofit.create(INetServer::class.java)

        RxJavaPlugins.setErrorHandler {
            Log.e("TAG", "RxJavaPlugins 避免取消订阅后，异常无法捕获")
        }
    }

    fun getMerchantInfo(user: String , repo: String): Observable<List<MerchantInfoBean>> {
        return Observable.create { emitter ->
            requestService.getMerchantInfo(user, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    Log.e("TAG", "getMerchantInfo: success,,,$it")
                    emitter.onNext(it)
                }, {
                    Log.e("TAG", "getMerchantInfo: error")
                    emitter.onError(Exception("getMerchantInfo: error"))
                }, {
                    Log.e("TAG", "getMerchantInfo: complete")
                    emitter.onComplete()
                })
        }
    }
}