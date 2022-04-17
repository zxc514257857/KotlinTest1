package com.zhr.case9

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9
 * @Author: zhr
 * @Date: 2022/4/8 17:52
 * @Version: V1.0
 */
object ServiceGenerator {

    private val mGsonWithFilter: Gson = GsonBuilder()
        // 自定义类型适配器
        .registerTypeAdapter(JsonObject::class.java, JsonOrderTypeAdapter())
        // 关闭特殊字符转义
        .disableHtmlEscaping()
        .create()

    // retrofit builder
    private var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(AppConstant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(mGsonWithFilter))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

    // retrofit object
    private var retrofit: Retrofit = builder.build()

    // log interceptor 打印网络请求日志
    private val loggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(AppConstant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(AppConstant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(AppConstant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)

    // service generator 供外部调用
    fun <S> createService(serviceClass: Class<S>?): S {
        //未添加拦截器 且 处于开发模式
        if (!httpClientBuilder.interceptors().contains(loggingInterceptor) && BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(loggingInterceptor)
            builder = builder.client(httpClientBuilder.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }
}