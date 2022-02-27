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
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

/**
 * 1、kotlin中 内联函数的使用（let、also、with、run、apply等函数），常用let、run、apply、also
 * https://blog.csdn.net/u013064109/article/details/78786646
 * Kotlin的高阶函数：就是以另外一个函数作为参数或者返回值的函数
 * https://www.jianshu.com/p/4da8968ec305
 * 2、fastjson和gson都用一下：序列化和反序列化；自定义工具类来进行反序列化
 * Gson反序列化泛型丢失问题
 * 3、expose和 transient 限定数据Bean的序列化和反序列化
 * 4、StrictMode 性能调优利器   https://blog.csdn.net/weixin_40763897/article/details/89018306
 *   https://blog.csdn.net/mynameishuangshuai/article/details/51742375
 * 5、View.inflate（Inflater.inflate）的学习使用
 * 6、Kotlin构造方法传参的几种写法
 * 7、git合并本地多次提交记录
 * 8、修改AndroidStudio内存大小
 * 9、Git 分支
 * 10、CountDownLatch的使用
 * 11、打印错误栈，找bug
 * 12、Log的日志格式及Log日志的过滤不显示
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
        // T.let方法的参数是函数类型，  T.let(block:(T)->R):R   let 返回的R,表示返回函数体中最后一行的值
        // let的作用：做非空判断以及通过it进行调用
        val result = lists?.let {
            it.add("冰敦敦")
            it.add("雪融融")
            true
        }
        Log.e(TAG, "result: $result")  // result: true
        Log.e(TAG, "lists: $lists")  // lists: [冰敦敦, 雪融融]
        // 学习一下方法中以方法作为参数的写法  -- 高阶函数
        test { i, j ->
            i + j
        }  // test: 5
        test { i, j ->
            i * j
        }  // test: 6
        test(test1())  // test: 5
        test(test2())  // test: 6
//        test(test3()) // Required:  (Int, Int) → Int   Found:  (Int, Int) → Boolean

        // with函数：  with(receiver:T,block:T.()->R):R
        // with函数的作用，可以省去类名多次调用时的繁琐情况，直接使用 this.xxx或者xxx即可
        // with 返回的R,表示返回函数体中最后一行的值
        val result1 = with(lists) {
            add("冰敦敦")
            add("雪融融")
        } // with的这个写法是 with(lists,{add("冰敦敦")  add("雪融融")}) 的一种缩写
        Log.e(TAG, "result1: $result1") // result1: true
        Log.e(TAG, "lists1: $lists")  // lists1: [冰敦敦, 雪融融, 冰敦敦, 雪融融]

        // run函数：   T.run(block:T.()->R):R   // 这里的T.() 就代表的是this
        // run = let + with
        // let 在函数体中必须使用it进行指代；with可以用this进行指代，同时解决了参数的不能判空问题
        // run 同时可以使用this，也可以进行判空     run返回的R,表示返回函数体中最后一行的值
        val result2 = lists?.run {
            this.add("冰敦敦")
            this.add("雪融融")
        }
        Log.e(TAG, "result2: $result2")  // result2: true
        Log.e(TAG, "lists2: $lists")  // lists2: [冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融]

        // apply函数：  T.apply(block:T.()->Unit):T   // 这里的T.() 就代表的是this，这里的返回值是T，表示是它本身
        // apply 约等于 run
        // apply和run不同的地方 run函数没有返回值，apply函数的返回值是其本身
        // 如果需要返回值链式调用的话，就使用apply，否则使用run
        val result3 = lists?.apply {
            this.add("冰敦敦")
            this.add("雪融融")
        }
        Log.e(TAG, "result3: $result3")  // result3: [冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融]
        Log.e(TAG, "lists3: $lists")  // lists3: [冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融]

        // also函数：  T.also(block:(T)->Unit):T   // 里面用it，外面用this，这里的返回值是T，表示是它本身
        // also 可以替代let。。。run可以替代let和with。。。apply约等于run
        val result4 = lists?.also {
            it.add("冰敦敦")
            it.add("雪融融")
        }
        Log.e(
            TAG,
            "result4: $result4"
        )  // result4: [冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融]
        Log.e(TAG, "lists4: $lists")  // lists4: [冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融, 冰敦敦, 雪融融]

        // kotlin高阶函数
        // 表示这个参数为函数类型，传入两个值，返回的是布尔类型
        val sum = { x: Int, y: Int ->
            x > y
        }
        // 表示这个函数没有传参，返回值是unit类型
        val action = { println("action 也是函数类型") }
        // 表示这个函数有传参Int类型，返回值是Unit类型
        val action1 = { a: Int -> println("action 也是函数类型$a") }
        // 函数返回值是可空类型
        val action2: (Int, Int) -> Int? = { a, b ->
            null
        }
        // 函数本身为可空类型
        val action3: ((Int, Int) -> Boolean)? = null
        // 函数类型的参数调用
        performRequest("https://www.baidu.com") { code, content ->
            Log.e(TAG, "code: $code, content: $content")
        } // code: 200, content: success
        // 实现filter函数过滤    // 函数里面是需满足的过滤条件
        val filte1 = "zxc123".filter {
//            it in 'a'..'z'     // filte1: zxc
//            it in '1'..'3'        // filte1: 123
            it != '1' && it != '2'    // filte1: zxc3
        }
        Log.e(TAG, "filte1: $filte1")

        // 参数是函数类型
        val filte2 = lists.filter {
            it == "冰敦敦"
        }
        Log.e(TAG, "filte2: $filte2")  // filte2: [雪融融, 雪融融, 雪融融, 雪融融, 雪融融]

        // 参数是函数类型
        val filte3 = "zxc123".filterIndex {
            it >= 3
        }
        Log.e(TAG, "filte3: $filte3")  // filte3: zxc

        // 参数是函数类型
        val toString1 = lists.toString1()
        Log.e(TAG, "toString1: $toString1")  // toString1: 冰敦敦,雪融融,冰敦敦,雪融融,冰敦敦,雪融融,冰敦敦,雪融融,冰敦敦,雪融融

        // 返回值是函数类型
        val shippingCalculator = getShippingCalculator(Delivery.STANDARD)
        val order = Order().run {
            this.itemCount = 10
            val invoke = shippingCalculator.invoke(this)
            invoke
        }
        Log.e(TAG, "order: $order")

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
        val animal = Animal("xiaobai", "cn")
        animal.getData()
        // Kotlin构造方法传参 -- 方法二：通过 constructor关键字写构造
        val plant = Plant("juhua", "cn")
        plant.getData()

        // git合并本地多次提交记录：(远端的多次提交记录不好删除，所以谨慎提交远端内容，本地编辑好再提交远端)
        // 1选择你要回滚的代码版本记录
        // 2右键选择 Reset Current Branch to Here
        // 3Git Reset 选择Soft
        // 4重新编辑提交内容提交本地即可

        // 修改AndroidStudio内存大小
        // E:\Develop\AndroidStudio\4.2.1\bin\studio64.exe.vmoptions
        // -Xms 是JVM启动的起始堆内存，堆内存是分配给对象的内存
        // -Xmx 是 Java 虚拟机启动时的参数，用于限制最大堆内存
        // -Xms256m    全部乘以2 为 -Xms512m
        // -Xms1280m   全部乘以2 为 -Xms2560m

        // Git 分支
        // fix分支  feature分支(开发分支)  基线分支(打包分支 release分支)
        // 从基线分支拉一个fix(xxx)分支，修改完成立马合到基线分支中
        // 从基线分支拉一个feature(xxx)分支，开发完成，测试完成，会打一个git Tag。标识 Release版本的版本号
        // https://www.bilibili.com/video/BV1Lb4y1J7eF?p=16
        // git的分支都是可以删掉的，不管是不是自己创建的分支
        // git fetch 是指当别人上传分支到远端，但你在本地没有显示的时候可以使用fetch
        // git pull 和 update project的功能是一样的： pull = fetch + merge

        // CountDownLatch 类的使用
        countDownTest()

        // 打印错误栈，找bug
        printStack()

        // Log的日志格式及Log日志的过滤不显示
        // 2022-01-27 13:15:12.985 8258-8258/com.whpe.pos.hubei.wuhan E/LoggerUtils:  xxx
        // 日期        时间         PID-TID   包名               log优先级  logtag       logmsg
        // Log日志的过滤不显示     ^(?!.*(AAA|BBB).*$
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

    private fun initStrickMode() {
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
    private fun inflateDemo() {
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
    private fun test(testFun: (Int, Int) -> Int) {
        val data = testFun.invoke(2, 3)
        Log.e(TAG, "test: $data")
    }

    private fun test1(): (Int, Int) -> Int {
        return { i, j ->
            i + j
        }
    }

    private fun test2(): (Int, Int) -> Int {
        return { i, j ->
            i * j
        }
    }

    private fun test3(): (Int, Int) -> Boolean {
        return { i, j ->
            if (i > j) true else false
        }
    }

    private fun countDownTest() {
        // 计数器 每countDown一次就减一，减到0才执行await()及后面的方法
        val countDownLatch = CountDownLatch(2)
        Log.e(TAG, "全班同学开始考试，一共两个考生")
        thread {
            Log.e(TAG, "第一个同学交卷")
            countDownLatch.countDown()
        }
        thread {
            Log.e(TAG, "第二个同学交卷")
            countDownLatch.countDown()
        }
        try {
            countDownLatch.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e(TAG, "老师清点试卷")
    }

    // 函数类型的参数调用
    private fun performRequest(url: String, callback: (code: Int, content: String) -> Unit) {
        callback.invoke(200, "success")
//        callback(200, "success")
    }

    // 这里隐含了String的变量，用this. 进行调用
    private fun String.filter(predicate: (Char) -> Boolean): String {
        val sb = StringBuilder()
        for (index in this.indices) {
            val element = this[index]
            // 这里写的是判断的条件 ，如果满足判断条件 则添加数据并进行返回
            if (predicate(element)) sb.append(element)
        }
        return sb.toString()
    }

    private fun <T> List<T>.filter(predicate: (T) -> Boolean): List<T> {
        val list = mutableListOf<T>()
        for (index in indices) {
            // 判断条件不满足，则添加
            if (!predicate(this[index])) {
                list.add(this[index])
            }
        }
        return list
    }

    private fun String.filterIndex(predicate: (Int) -> Boolean): String {
        if (predicate(this.length)) {
            return this.substring(0, 3)
        }
        return this
    }

    private fun <T> Collection<T>.toString1(
        prefix: String = "",
        postfix: String = "",
        separator: String = ",",
        transform: (T) -> String = { it.toString() }
    ): String {
        val result = java.lang.StringBuilder()
        result.append(prefix)
        for ((index, element) in this.withIndex()) {
            if (index > 0) {
                result.append(separator)
            }
            result.append(transform.invoke(element))
        }
        result.append(postfix)
        return result.toString()
    }

    // 函数类型的参数为可空类型
    private fun foo(callback: (() -> Unit)?) {
//        callback?.invoke()
        if (null != callback) callback.invoke()
    }

    // 返回值是一个函数类型的函数
    private fun getShippingCalculator(delivery: Delivery): (Order) -> Int {
        if (delivery == Delivery.EXPIRED) {
            return { aa -> 3 * aa.itemCount }
        }
        return { aa -> 2 * aa.itemCount }
    }

    enum class Delivery { STANDARD, EXPIRED }
    class Order {
        var itemCount = 0
    }

    private fun printStack() {
        try {
            1 / 0
        } catch (e: Exception) {
            val stringBuilder = StringBuilder()
            e.stackTrace.forEach {
                stringBuilder.append(it.toString())
            }
            Log.e(TAG, "printStackTrace: $stringBuilder")

            e.printStackTrace()
            Log.e(TAG, "printStackTrace: ${e.message}")
        }
    }
}