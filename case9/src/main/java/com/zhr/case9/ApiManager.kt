package com.zhr.case9

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.reactivex.rxjava3.core.Observer
import okhttp3.Call
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package:
 * @Author: zhr
 * @Date: 2022/4/8 15:51
 * @Version: V1.0
 */
class ApiManager {

    private val baseApiManager: BaseApiManager
    private lateinit var apiService: ApiService

    private val mGsonWithFilter: Gson = GsonBuilder()
        // 自定义类型适配器
        .registerTypeAdapter(JsonObject::class.java, JsonOrderTypeAdapter())
        // 关闭特殊字符转义
        .disableHtmlEscaping()
        .create()

    private constructor() {
        baseApiManager =
            BaseApiManager.getInstance(
                AppConstant.BASE_URL,
                // 设置转换器
                GsonConverterFactory.create(mGsonWithFilter),
                // 设置适配器
                RxJava3CallAdapterFactory.create(),
                // 设置请求头插值器
                object : RequestAddHeaderInterceptor() {
                    override fun addHeader(request: Request?, builder: Request.Builder?) {
                        builder?.apply {
                            addHeader("APP-KEY", AppConstant.APP_KEY)
                            addHeader("APP-TOKEN", AppConstant.APP_TOKEN)
                        }
                    }
                },
                // 设置插值器
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
    }

    companion object {
        private val manager: ApiManager = ApiManager()
        fun get(): ApiManager {
            return manager
        }
    }

    fun login(username: String, password: String)  {

    }
}

internal class JsonOrderTypeAdapter : TypeAdapter<JsonObject>() {

    override fun write(p0: JsonWriter?, p1: JsonObject?) {
        p0?.beginObject()
        val entrys = p1?.entrySet()?.sortedBy { it.key }
        entrys?.forEach { entry ->
            p0?.name(entry.key)?.value(entry.value.toString())
        }
        p0?.endObject()
    }

    override fun read(p0: JsonReader?): JsonObject? {
        return null
    }
}