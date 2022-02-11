package com.example.case6

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken

/**
 * 1、kotlin中 内联函数的使用（let、also、with、run、apply等函数）
 * kotlin高阶函数 ： Standard.kt
 *
 * 2、fastjson和gson都用一下：序列化和反序列化；自定义工具类来进行反序列化
 * Gson反序列化泛型丢失问题
 * 3、expose和 transient 限定数据Bean的序列化和反序列化
 * 4、StrictMode 性能调优利器   https://blog.csdn.net/weixin_40763897/article/details/89018306
 *   https://blog.csdn.net/mynameishuangshuai/article/details/51742375
 * 5、View.inflate（Inflater.inflate）的学习使用
 * 6、Kotlin构造方法传参的几种写法
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lists = mutableListOf<String>()
        // 这个demo也在test目录下写着，可以看一下
        // [100, 哈哈, 32.05]
        Log.e(
            TAG, "onCreate: ${
                mutableListOf<Any>().apply {
                    add(100)
                    add("哈哈")
                    add(32.05)
                }
            }"
        )

        // 点进去查看let函数
        // 表明 let是一个作用域函数，需要通过对象去调用    T.let()
        // T.let方法的参数是函数类型，let参数的返回值类型和函数的返回值类型一致   T.let(block:(T)->R): R
        // let的作用：做非空判断以及通过it进行调用
        lists?.let {
            it.add("冰敦敦")
            it.add("雪融融")
            it.add("雪融融")
        }
        // 学习一下方法中以方法作为参数的写法  -- 高阶函数
        test { i, j ->
            i + j
        }
        test { i, j ->
            i * j
        }


        // fastjson和gson的区别
        val person = Person()
        person.name = "zhr"
        person.age = 18
        Log.e(TAG, "gson1: ${Gson().toJson(person)}")  // gson1: {"age":18,"name":"zhr"}
        Log.e(
            TAG,
            "fastJson1: ${JSON.toJSON(person)}"
        )  // fastJson1: {"age":18,"country":"CN","name":"zhr"}
        // 区别一：Gson 不理getMethod方法中定义的值  Fastjson会把getMethod中返回的值也使用起来

        val map = mutableMapOf<String, Any>()
        map["name"] = "zhr"
        map["age"] = 18
        map["country"] = "CN"
        Log.e(TAG, "gson2: ${Gson().toJson(map)}")  // gson2: {"name":"zhr","age":18,"country":"CN"}
        Log.e(
            TAG,
            "fastJson2: ${JSON.toJSON(map)}"
        )  // fastJson2: {"name":"zhr","age":18,"country":"CN"}

        val linkedHashMap = LinkedHashMap<String, Any>()
        linkedHashMap["name"] = "zhr"
        linkedHashMap["age"] = 18
        linkedHashMap["country"] = "CN"
        Log.e(
            TAG,
            "gson3: ${Gson().toJson(linkedHashMap)}"
        )  // gson3: {"name":"zhr","age":18,"country":"CN"}
        Log.e(
            TAG,
            "fastJson3: ${JSON.toJSON(linkedHashMap)}"
        )  // fastJson3: {"name":"zhr","age":18,"country":"CN"}

        // toJson 是指序列化（数据Bean转字符串）  fromJson 是指反序列化（字符串转数据Bean）
        val json1 = "{\"name\":\"zhr\",\"age\":18,\"country\":\"CN\"}"
        val json2 =
            "[{\"age\":18,\"country\":\"CN\",\"name\":\"zhr\"},{\"age\":18,\"country\":\"CN\",\"name\":\"zhr\"}]"

        // 序列化为Bean对象
        val p1 = Gson().fromJson(json1, Person::class.java)   // Bean对象，这样反序列化
        Log.e(TAG, "person1: $p1")    // person1: Person(name=zhr, age=18, country=CN)
        val p2 = JSON.parseObject(json1, Person::class.java)
        Log.e(TAG, "person2: $p2")    // person2: Person(name=zhr, age=18, country=CN)
        val p3 = Gson().fromJson<Person>(
            json1,
            object : TypeToken<Person>() {}.type
        )  // 说明：Bean对象也可以用TypeToken传参的方式
        Log.e(TAG, "person3: $p3")    // person3: Person(name=zhr, age=18, country=CN)

        // 序列化为集合
        val persons1: List<Person> =
            Gson().fromJson(json2, object : TypeToken<List<Person>>() {}.type)
        Log.e(
            TAG,
            "persons1: $persons1"
        )  // persons1: [Person(name=zhr, age=18, country=CN), Person(name=zhr, age=18, country=CN)]
        val persons2: List<Person> = JSON.parseArray(json2, Person::class.java)
        Log.e(
            TAG,
            "persons2: $persons2"
        )  // persons2: [Person(name=zhr, age=18, country=CN), Person(name=zhr, age=18, country=CN)]

        // 序列化为Map
        val map1 = Gson().fromJson(json1, HashMap::class.java)
        val map2 = JSON.parseObject(json1, HashMap::class.java)
        Log.e(TAG, "map1: $map1")   // map1: {name=zhr, age=18.0, country=CN}
        Log.e(TAG, "map2: $map2")   // map2: {name=zhr, age=18, country=CN}
        // 区别二：通过反序列化将json字符串转化为Map，int数据类型的数据会自动转为double类型
        val map3 = getGson()?.fromJson(json1, HashMap::class.java)
        Log.e(TAG, "map3: $map3")   // map3: {name="zhr", age=18, country="CN"}
        map3?.forEach {
            // map4: name,,,"zhr"
            // map4: age,,,18
            // map4: country,,,"CN"
            Log.e(TAG, "map4: ${it.key},,,${it.value}")
        }

        // 通过自定义工具类来进行序列化和反序列化
        // java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to com.example.case6.Person
//        val parse1 = JsonUtils.parseObject1<Person>(json1)
//        Log.e(TAG, "parse1: $parse1")   // 这里因为泛型丢失，转不了，会报错
        val parse2 = JsonUtils.parseObject2<Person>(json1)
        Log.e(TAG, "parse2: $parse2")  // parse2: Person(name=zhr, age=18, country=CN)
        val parse3 = JsonUtils.parseObject1<List<Person>>(json2)
        Log.e(
            TAG,
            "parse3: $parse3"
        )  // parse3: [{age=18.0, country=CN, name=zhr}, {age=18.0, country=CN, name=zhr}]
        val parse4 = JsonUtils.parseObject2<List<Person>>(json2)
        Log.e(
            TAG,
            "parse4: $parse4"
        )  // parse4: [Person(name=zhr, age=18, country=CN), Person(name=zhr, age=18, country=CN)]
        val parse5 = JsonUtils.parseObject<Person>(json1, Person::class.java)
        Log.e(TAG, "parse5: $parse5")  // parse5: Person(name=zhr, age=18, country=CN)
        val parse6: List<Person>? =
            JsonUtils.parseObject(json2, object : TypeToken<List<Person>>() {}.type)
        Log.e(
            TAG,
            "parse6: $parse6"
        )   // parse6: [Person(name=zhr, age=18, country=CN), Person(name=zhr, age=18, country=CN)]
        val parse7 = JsonUtils.parseObject1<Map<String, Any>>(json1)
        Log.e(TAG, "parse7: $parse7")   // parse7: {name=zhr, age=18.0, country=CN}
        val parse8 = JsonUtils.parseObject2<Map<String, Any>>(json1)
        Log.e(TAG, "parse8: $parse8")
        // 测试使用Expose字段来序列化和反序列化(之前默认的是所有的字段都需要序列化和反序列化的)
        // expose表示默认使用序列化和反序列化（默认为true）；expose(ser=true,deser=false)表示使用序列化，不使用反序列化
        // 不加expose 但调用了excludeFieldsWithoutExposeAnnotation，则表示不使用序列化和反序列化
        // Transient 表示不进行序列化 等同于 expose(ser=false,deser=false)
        val people = People()
        people.name = "xxx"
        people.age = 18
        people.sex = "男"
        people.country = "CN"
        val people1 = JsonUtils.toJson1(people)
        Log.e(TAG, "people1: $people1")
        val json3 = "{\"name\":\"xxx\",\"age\":18,\"sex\":\"男\",\"country\":\"CN\"}"
        val people2 = JsonUtils.parseObject3<People>(json3, People::class.java)
        Log.e(
            TAG,
            "people2: ${people2?.name},,,${people2?.age},,,${people2?.sex},,,${people2?.country}"
        )

        // StrickMode 性能调优利器
        initStrickMode()

        // Inflate demo
        inflateDemo()

        // 自定义View的Context不需要使用软引用，因为View的生命周期依赖于Activity等父容器，其生命周期短于父容器的生命周期
        // Kotlin构造方法传参 -- 方法一:直接在头上写构造
        val animal = Animal("xiaobai","cn")
        animal.getData()
        // Kotlin构造方法传参 -- 方法二：通过 constructor关键字写构造
        val plant = Plant("juhua","cn")
        plant.getData()
    }

    private fun getGson(): Gson? {
        return GsonBuilder().registerTypeAdapter(HashMap::class.java,
            JsonDeserializer<Any?> { json, typeOfT, context ->
                val resultMap: HashMap<String, Any> = HashMap()
                val jsonObject = json.asJsonObject
                val entrySet = jsonObject.entrySet()
                for ((key, value) in entrySet) {
                    resultMap[key] = value
                }
                resultMap
            }).create()
    }

    fun initStrickMode() {
        // 一般放在Application的 onCreate() 中
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "initStrickMode: ")
            // 严苛模式 分为线程策略和虚拟机策略
            StrictMode.ThreadPolicy.Builder().let {
                // 检测所有     it.permitAll() 允许所有
                it.detectAll()  // detectCustomSlowCalls 检测自定义耗时调用  detectResourceMismatches 检测资源不匹配  detectUnbufferedIo 检测无缓存IO
                // 惩罚形式
                it.penaltyDialog() // dialog 以弹框提示  log 以打印提示   death 以闪退提示   dropBox 记录违规信息到/data/system/dropbox目录下  FlashScreen 以屏幕闪烁提示（弹出屏幕外圈红色线框，有的设备不支持）
                it.penaltyLog()
                it.build()
            }
            StrictMode.VmPolicy.Builder().let {
                // 检测所有
                it.detectAll()  // detectLeakedSqlLiteObjects 检测泄漏的sqlite对象  detectActivityLeaks 检测activity内存泄漏
                // detectLeakedClosableObjects 检测泄漏的未关闭对象  detectLeakedRegistrationObjects 检测泄漏的广播对象
                // detectFileUriExposure 检测文件Uri暴露  detectCleartextNetwork 检测明文网络
                // detectContentUriWithoutPermission()  检测您的应用在其外部启动 Activity ,意外忘记向其他应用授予权限
                // detectUntaggedSockets()  检测您的应用使用网络流量，没有使用 setThreadStatsTag(int) 将流量标记用于调试目的
                // detectCredentialProtectedWhileLocked()  检测锁定时受保护的凭据
                // detectIncorrectContextUse()  检测不正确的上下文使用
                // it.setClassInstanceLimit(this::class.java, 2)  设置某个类处于内存中的实例上限，可以协助检查内存泄露
                // 与上面的内容一致
                it.penaltyLog()
                it.build()
            }
            // 打印耗时调用操作
            StrictMode.noteSlowCall("打印耗时调用操作！")
        }
    }

    @SuppressLint("SetTextI18n")
    fun inflateDemo() {
//        val container = findViewById<ConstraintLayout>(R.id.cl)
//        View.inflate()  // 其实就是 LayoutInflater，把这个搞懂就行了
        // LayoutInflater.inflate(子布局，父布局，xxx(是否将子布局添加到父布局上))  父布局不为null，xxx为true；为null，xxx为false；（一定不为null，因为要指定父布局；为null不显示子布局）
        // 不为null，也可以手动设置为false （设置了父布局，但是没有添加到父布局上，还是不显示内容）
        // 最理想的状态就是，设置子布局和父布局，attachToRoot用默认值true，父布局设置具体宽高，子布局充满屏幕
//        val rootView = LayoutInflater.from(this).inflate(R.layout.test_view, container)
        // View.inflate() 其实底层也是调用了Inflater.inflate()方法，父布局为null，attach默认为false，父布局不为null，attach默认为true
//        val rootView = View.inflate(this, R.layout.result_view, container)
//        rootView.findViewById<TextView>(R.id.tv_result_status).text = "刷脸成功"
//        rootView.findViewById<TextView>(R.id.tv_customer_info).text = "131****0562  **海"
        findViewById<ConstraintLayout>(R.id.cl).addView(
            ErrorResultView(this),
            // 然后指定一下addView的 填充方式为 铺满的填充方式
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    // 表示这里需要传的是函数类型(方法名自定义为testFun)，方法的返回参数为Int类型，返回值为Int类型
    // 声明一个函数类型的参数（Boolean）-> Int ,表明此函数类型接收一个Boolean类型的参数，返回的是一个Int类型
    // (Boolean,Int) -> Int，表明此函数类型接收一个Boolean及Int类型的参数，返回的是Int类型
    // () -> Int，表示函数类型不接收参数，返回的值是Int类型
    // () -> Unit，表示函数类型不接收参数，且没有返回值
    fun test(testFun: (Int, Int) -> Int) {
        val data = testFun.invoke(2, 3)
        Log.e(TAG, "test: $data")
    }

    fun test1(): (Int, Int) -> Int {
        return { i, j ->
            i + j
        }
    }
}