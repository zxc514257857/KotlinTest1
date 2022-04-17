package com.zhr.case9

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9
 * @Author: zhr
 * @Date: 2022/4/8 11:15
 * @Version: V1.0
 */
class BaseApiManager private constructor() {

    var retrofit: Retrofit? = null
        private set

    companion object {

        // 根据url获取ApiManager
        private var retrofitMap: MutableMap<String, BaseApiManager?> = HashMap()

        fun getInstance(
            baseUrl: String,
            converterFactory: Converter.Factory? = null,
            callAdapterFactory: CallAdapter.Factory? = null,
            requestAddHeaderInterceptor: RequestAddHeaderInterceptor? = null,
            vararg interceptors: Interceptor?
        ): BaseApiManager {
            if (retrofitMap.containsKey(baseUrl)) {
                retrofitMap[baseUrl]?.let {
                    return it
                }
            }
            val baseApiManager = BaseApiManager()
            val builder = OkHttpClient.Builder()
                .readTimeout(AppConstant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(AppConstant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(AppConstant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)

            val okHttpCliect = builder.build()
            requestAddHeaderInterceptor?.let {
                builder.addInterceptor(it)
            }
            for (interceptor in interceptors) {
                interceptor?.let {
                    builder.addInterceptor(it)
                }
            }
            val retrofitBuilder = Retrofit.Builder()
            if (converterFactory != null) {
                retrofitBuilder.addConverterFactory(converterFactory)
            } else {
                retrofitBuilder.addConverterFactory(CustomConverterFactory.create())
            }
            if(callAdapterFactory != null){
                retrofitBuilder.addCallAdapterFactory(callAdapterFactory)
            }
            baseApiManager.retrofit = retrofitBuilder.baseUrl(baseUrl).client(okHttpCliect).build()
            retrofitMap[baseUrl] = baseApiManager
            return baseApiManager
        }
    }
}

interface Callback<Response> {
    fun onFailure(call: retrofit2.Call<String>, t: Throwable?)
    fun onResponse(
        call: retrofit2.Call<String>,
        rawResponse: retrofit2.Response<String>,
        response: Response?
    )
}

abstract class RequestAddHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        addHeader(request, builder)
        return chain.proceed(builder.build())
    }

    abstract fun addHeader(
        request: Request?,
        builder: Request.Builder?
    )
}

class CustomConverterFactory : Converter.Factory {

    private val gson: Gson

    companion object {
        fun create(): CustomConverterFactory {
            return CustomConverterFactory(
                Gson()
            )
        }
    }

    private constructor(gson: Gson) : super() {
        this.gson = gson
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type === String::class.java) {
            return ScalarResponseBodyConverters.StringResponseBodyConverter.INSTANCE
        }
        if (type === Boolean::class.java || type === Boolean::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.BooleanResponseBodyConverter.INSTANCE
        }
        if (type === Byte::class.java || type === Byte::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.ByteResponseBodyConverter.INSTANCE
        }
        if (type === Char::class.java || type === Char::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.CharacterResponseBodyConverter.INSTANCE
        }
        if (type === Double::class.java || type === Double::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.DoubleResponseBodyConverter.INSTANCE
        }
        if (type === Float::class.java || type === Float::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.FloatResponseBodyConverter.INSTANCE
        }
        if (type === Int::class.java || type === Int::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.IntegerResponseBodyConverter.INSTANCE
        }
        if (type === Long::class.java || type === Long::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.LongResponseBodyConverter.INSTANCE
        }
        return if (type === Short::class.java || type === Short::class.javaPrimitiveType) {
            ScalarResponseBodyConverters.ShortResponseBodyConverter.INSTANCE
        } else null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }
}

class ScalarResponseBodyConverters private constructor() {

    internal class StringResponseBodyConverter : Converter<ResponseBody, String> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            return value.string()
        }

        companion object {
            val INSTANCE = StringResponseBodyConverter()
        }
    }

    internal class BooleanResponseBodyConverter : Converter<ResponseBody, Boolean> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Boolean {
            return java.lang.Boolean.valueOf(value.string())
        }

        companion object {
            val INSTANCE = BooleanResponseBodyConverter()
        }
    }

    internal class ByteResponseBodyConverter : Converter<ResponseBody, Byte> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Byte {
            return java.lang.Byte.valueOf(value.string())
        }

        companion object {
            val INSTANCE = ByteResponseBodyConverter()
        }
    }

    internal class CharacterResponseBodyConverter : Converter<ResponseBody, Char> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Char {
            val body = value.string()
            if (body.length != 1) {
                throw IOException("Expected body of length 1 for Character conversion but was " + body.length)
            }
            return body[0]
        }

        companion object {
            val INSTANCE = CharacterResponseBodyConverter()
        }
    }

    internal class DoubleResponseBodyConverter : Converter<ResponseBody, Double> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Double {
            return java.lang.Double.valueOf(value.string())
        }

        companion object {
            val INSTANCE = DoubleResponseBodyConverter()
        }
    }

    internal class FloatResponseBodyConverter : Converter<ResponseBody, Float> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Float {
            return java.lang.Float.valueOf(value.string())
        }

        companion object {
            val INSTANCE = FloatResponseBodyConverter()
        }
    }

    internal class IntegerResponseBodyConverter : Converter<ResponseBody, Int> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Int {
            return Integer.valueOf(value.string())
        }

        companion object {
            val INSTANCE = IntegerResponseBodyConverter()
        }
    }

    internal class LongResponseBodyConverter : Converter<ResponseBody, Long> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Long {
            return java.lang.Long.valueOf(value.string())
        }

        companion object {
            val INSTANCE = LongResponseBodyConverter()
        }
    }

    internal class ShortResponseBodyConverter : Converter<ResponseBody, Short> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Short {
            return value.string().toShort()
        }

        companion object {
            val INSTANCE = ShortResponseBodyConverter()
        }
    }
}

class GsonRequestBodyConverter<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) : Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(
            buffer.outputStream(), UTF_8
        )
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }

    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
        private val UTF_8 = Charset.forName("UTF-8")
    }
}














