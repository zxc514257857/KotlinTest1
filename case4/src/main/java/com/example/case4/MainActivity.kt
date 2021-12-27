package com.example.case4

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.LongAdder
import java.util.function.IntBinaryOperator
import java.util.function.IntUnaryOperator
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    // lateinit 后面跟的是引用数据类型，跟基本数据类型不能用lateinit
//    lateinit var a: Int
//    lateinit var b: TextView
//    lateinit var c: String

    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)

        // 测试return 作用的区域
        // retrun 作用在单个的方法中 而不是作用在整个应用程序中
        testReturn()

        // gradle插件依赖使用阿里云远程依赖替换国外服务器依赖；gradle wrapper使用本地gradle替换，可以增加编译速度

        // as的搜索，注意左侧的下拉箭头；以及搜索时的 A W R字母筛选 ； A表示 大小写匹配  W表示 全字符匹配
        // R表示 正则匹配；前中后位置，大小写字母不一致，字母缺失都可以匹配到（应该是默认的匹配方式）

        // git的 rollback 回滚以及 Stash和UnStash 暂存功能的使用
        // git的新创建分支是在目前现有的分支的基础上创建起来的

        // Android中创建Lib module和application module的区别
        // lib module是需要依赖主module而存在的  application module是每个可以单独存在的
        // application module左下角是一个点  lib module左下角是一本书

        // 测试list的removeFirst 和 removeLast
        val lists = LinkedList<String>()
        lists.add("111")
        lists.add("222")
        lists.add("333")
        lists.add("444")
        val changeEnum = ChangeEnum.DEFAULT
        when (changeEnum) {
            ChangeEnum.REMOVE_LAST -> {
                lists.removeLast()
                // onCreate: 删除最后,,,lists: [111, 222, 333]
                Log.e("TAG", "onCreate: ${changeEnum.value},,,lists: $lists")
            }
            ChangeEnum.REMOVE_FIRST -> {
                lists.removeFirst()
                // onCreate: 删除第一,,,lists: [222, 333, 444]
                Log.e("TAG", "onCreate: ${changeEnum.value},,,lists: $lists")
            }
            else -> {
                // onCreate: 不变,,,lists: [111, 222, 333, 444]
                Log.e("TAG", "onCreate: ${changeEnum.value},,,lists: $lists")
            }
        }

        // android 中的Pair使用
        // Pair 和triple的区别，Map保存的是多个key value，Pair保存的是一个Key value
        val pair = Pair("aaa", "bbb")
        Log.e("TAG", "pair: ${pair.first},,,${pair.second}")  // pair: aaa,,,bbb
        val triple = Triple("111", "222", "333")
        Log.e(
            "TAG",
            "triple: ${triple.first},,,${triple.second},,,${triple.third}"
        )  // triple: 111,,,222,,,333

        // atomic的使用（原子的） 可以解决多线程并发问题
        val atomicBoolean = AtomicBoolean(true)
        // 获取当前的值
        val booleanGet1 = atomicBoolean.get()
        Log.e("TAG", "booleanGet1: $booleanGet1")  // booleanGet1: true
        // set 多线程共享变量的修改对其他线程立即可见
        atomicBoolean.set(false)
        val booleanGet2 = atomicBoolean.get()
        Log.e("TAG", "booleanGet2: $booleanGet2")  // booleanGet2: false
        // lazySet 多线程共享变量的修改对其他线程不会立即可见
        atomicBoolean.lazySet(true)
        // 获取的旧值（返回的旧值），设置的新值
        val booleanPrevGet = atomicBoolean.getAndSet(false)
        Log.e("TAG", "booleanPrevGet: $booleanPrevGet")   // booleanPrevGet: true（旧值为true，新值为false）
        // 原值和现值进行比较，如果比较一致设置新值，不一致则不设置新值
        val booleanCompareSet = atomicBoolean.compareAndSet(true, true)
        // booleanCompareSet: false（旧值是false，期望是true，期望相同返回true，期望不同返回false，目前是不同 返回false）
        // 返回的值是 旧值和期望值是否相同，，，相同返回true，不同返回false
        Log.e("TAG", "booleanCompareSet: $booleanCompareSet")
        // atomicInteger 设置默认值
        val atomicInteger = AtomicInteger(0)
        val integerGet1 = atomicInteger.get()
        Log.e("TAG", "integerGet1: $integerGet1")  // integerGet1: 0
        // 返回旧值（0），新增新值（2）
        val integerGet2 = atomicInteger.getAndAdd(2)
        Log.e("TAG", "integerGet2: $integerGet2")  // integerGet2: 0
        // 验证之前的getAndAdd 指令
        val integerGet3 = atomicInteger.get()
        Log.e("TAG", "integerGet3: $integerGet3")  // integerGet3: 2
        // 先加再返回新值（2+2）
        val integerGet4 = atomicInteger.addAndGet(2)
        Log.e("TAG", "integerGet4: $integerGet4")  // integerGet4: 4
        atomicInteger.set(5)
        val integerGet5 = atomicInteger.get()
        Log.e("TAG", "integerGet5: $integerGet5")  // integerGet5: 5
        // 原值（5）和比较值（5）进行比较，如果相同，则设置期望值（6）并返回 ；；返回值的意思是比较值是否相同，相同返回true，不同返回false
        val compareAndSet = atomicInteger.compareAndSet(5, 6)
        Log.e("TAG", "compareAndSet---: $compareAndSet")  // compareAndSet---: true ;;;更新的新值为6
        // 先获取再加一(返回的是旧值)
        val integerGet6 = atomicInteger.getAndIncrement()
        Log.e("TAG", "integerGet6: $integerGet6")  // integerGet6: 6
        // 检测新值是否加上 -- 加上了1
        val integerGet7 = atomicInteger.get()
        Log.e("TAG", "integerGet7: $integerGet7")  // integerGet7: 7
        // 先加一再获取值
        val integerGet8 = atomicInteger.incrementAndGet()
        Log.e("TAG", "integerGet8: $integerGet8")  // integerGet8: 8
//        atomicInteger.getAndDecrement()   // 先获取再减一
//        atomicInteger.decrementAndGet()   // 先减一再获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // updata 表示的是修改
//            atomicInteger.getAndUpdate(IntUnaryOperator {
//                it / 2
//            })
            // 在传参的时候，省略了 (IntUnaryOperator   ) 就变成了这个样子
            val andUpdate = atomicInteger.getAndUpdate {
                // 返回了原值，并更新了现值为原值除以2
                it / 2
            }
            Log.e("TAG", "andUpdate: $andUpdate")  // andUpdate: 8
            val get = atomicInteger.get()
            Log.e("TAG", "get: $get")  // get: 4
            // 表示的是更新并且获取
//            atomicInteger.updateAndGet()

            // Accumulate 表示的是增加
            val accumulateAndGet =
                // x表示现值 ， pre表示前值 ， now表示现值
                atomicInteger.accumulateAndGet(28, IntBinaryOperator { pre, now ->
                    Log.e("TAG", "pre: $pre,,,now:$now")  // pre: 4,,,now: 28
                })
            // 21   为什么算出来的这个值，目前搞不太懂
            Log.e("TAG", "accumulateAndGet: $accumulateAndGet")
        }

        // 模拟一个累加操作
        val atomicInteger1 = AtomicInteger(1)
        var incrementAndGet = 1
        for(a in 1 until 5){
            Log.e("TAG", "aaaa: $a")
            incrementAndGet = atomicInteger1.incrementAndGet()
        }
        Log.e("TAG", "incrementAndGet: $incrementAndGet")


        //https://zhuanlan.zhihu.com/p/138819184
        // volatile 不稳定的  ，它保护的是变量的安全，不能保护线程的安全
        // 它保证变量对所有线程可见。即其他线程修改了这个值，它保证新值对其他所有线程是可见的 ； volatile能够禁止指令重排
//        Volatile和Synchronized四个不同点：
//        1 粒度不同，后者锁对象和类，前者针对变量
//        2 syn阻塞，volatile线程不阻塞
//        3 syn保证三大特性，volatile不保证原子性
//        4 syn编译器优化，volatile不优化
        // 高并发三大特性：原子性、有序性和可见性 （volatile 满足可见性和有序性，不满足原子性）

        // 无法重现volatile的作用
        // 不管在主线程还是在子线程对变量值进行修改，加不加volatile的作用都是一样的，，，这就离谱
        testVolatile()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 也是为了解决long类型的多线程同步问题，比AtomaticLong 性能更好一点 ；AtomicLong的性能差一点
            // longAdder能够实现变量的原子性；如果对象值的同步，那是否就能使用sync、以及volatile进行同步了呢？
            val longAdder = LongAdder()
            for(i in 0 until 10){
                thread {
                    longAdder.increment()
                    Log.e("TAG", "线程:${Thread.currentThread().name}, longAdder的值：${longAdder}")
                }
            }
        }

        // HashMap 和ConCurrentHashMap（线程安全的）  StringBuilder和StringBuffer（线程安全的）

        // xCrash 捕获崩溃，包括java崩溃、native崩溃和anr   爱奇艺提供的崩溃日志捕获工具
        // https://github.com/iqiyi/xCrash

        // 表示获取当前分支的 commit版本数量 可以当做 appVersionCode使用
        // def gitVersionCode() {
        //    def cmd = 'git rev-list HEAD --first-parent --count'
        //    cmd.execute().text.trim().toInteger()
        //}

        // 如果一个Bean中有共有的 内容，可以提取出来一个Bean ，其他的Bean继承这个Bean
        val goodsDataBean = GoodsDataBean()
        val mutableList = mutableListOf<String>()
        mutableList.add("111")
        goodsDataBean.code = 1
        goodsDataBean.msg = "0x01"
        goodsDataBean.data = mutableList

        // Kotlin的代码简化逻辑     https://blog.csdn.net/u013064109/article/details/78786646
//        tv.setOnClickListener(View.OnClickListener { it ->
//        })

//        tv.setOnClickListener(object: View.OnClickListener{
//            override fun onClick(v: View?) {
//            }
//        })

//        tv.setOnClickListener { it -> }

        // let  apply  also  run  with  内联扩展函数   熟悉一下 提示代码里面传入的参数

        // ?. 和 ?: 以及return的连用
        Log.e("TAG2", "return : ${test4()}")  // return : true


        "aaa".let {

        }





        // 传的Context 全部都用SoftReference进行包裹一下  SoftReference<Context>

        // 自定义Exception   STChipFailException


        // 协程使用    将执行分发到 UI线程
        GlobalScope.launch(Dispatchers.IO) {

        }
        // 将执行分发到主线程
        CoroutineScope(Dispatchers.Main).launch {

        }

        loadData()
        loadData1()

        // android 中的变体配置。。。。。。  新建一个module   现在因为变体没配好 apk打包也打不了了  todo 目前先注释掉了
        // Grow 和 sublime中的内容移到这里来
        // 手机备忘录中的内容整理 移植到这里来
        // 在服务中开启广播逻辑 熟悉一下

        // 学习Git图谱，多看git图谱，多想git图谱；；；多思考下git 图谱每个线和点的含义
    }

    @Volatile
    private var click = true
    private fun testVolatile() {
        thread {
            SystemClock.sleep(1000)
            click = false
            Log.e("TAG", "click2:")
        }
        while(click){
            Log.e("TAG", "click1:")
        }
    }

    private fun test4(): Boolean {
        var aaa: String? = ""   // aaa不为null  返回true
//        aaa = null  // aaa为null  返回false
        // 这段话的意思是，不为null 返回true 为null返回false
        val block = aaa?.let {
            true
        } ?: false
        return block
    }

    val uiScope1 = CoroutineScope(Dispatchers.Main)
    fun loadData() = uiScope1.launch {
        tv.text = "hahhahah"
        // 这两个是串行执行的
        val result1 = withContext(Dispatchers.IO) {
            delay(2000)
            // 无法执行
//            tv.text = "hahhahah1"
            Log.e("TAG", "loadData1: ${Thread.currentThread().name}")
            "result1"
        }
        val result2 = withContext(Dispatchers.IO) {
            delay(1000)
//            tv.text = "hahhahah2"
            Log.e("TAG", "loadData2: ${Thread.currentThread().name}")
            "result2"
        }
        Log.e("TAG", "result1:${result1},,,result2:${result2}")
    }

    fun loadData1() = GlobalScope.launch(Dispatchers.Main) {
        delay(2000)
//        Dispatchers.IO 会闪退    Dispatchers.Main 不会
//        tv.text = "hahhahah1"
    }

    private fun testReturn() {
        var a = 1
        test1(a)
        test2(a)
        test3(a)
    }

    private fun test1(a: Int) {
        if (a == 1) return
        // 这里return 返回了 ，不走这里
        if (a == 2) {
            Log.e("TAG", "test1: $a")
        }
    }

    private fun test2(a: Int) {
        Log.e("TAG", "test1: $a") // 1
    }

    private fun test3(a: Int) {
        Log.e("TAG", "test1: $a") // 1
    }
}