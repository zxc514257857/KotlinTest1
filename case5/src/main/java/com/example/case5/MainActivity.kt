package com.example.case5

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ServiceUtils
import kotlinx.coroutines.*
import java.lang.ref.SoftReference

/**
 * 1、“”?.let{true} ?:fale  表示不为null返回true，为null返回false
 * 2、协程的使用
 * 3、字节和字符的关系
 * 4、通过SoftReference进行 Context上下文的传递或获取
 * 5、自定义Exception
 * 6、Git图谱学习
 * 7、在服务中开启广播（广播没有像Activity和Service 有Utils类）
 * 8、通过Activity常量和Service常量跳转Activity和Service  以及服务和广播的关闭
 * 9、
 * 
 */
class MainActivity : AppCompatActivity() {

    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        // ?. 和 ?: 以及return的连用
        Log.e("TAG", "return : ${test1()}")  // return : true

        // 协程使用  https://www.jianshu.com/p/b0b9e04d8793
        // 协程的两个分发器  mainDispatcher和 ioDispatcher
        val mainDispatcher = Dispatchers.Main
        val ioDispatcher = Dispatchers.IO
        // 协程的两个作用域  CoroutineScope 和GlobalScope
        CoroutineScope(Dispatchers.Main).launch { }
//        loadData()
        GlobalScope.launch(Dispatchers.Main) { }
//        loadData1()
        // CoroutineScope.launch + async 执行任务 （主线程中加入子线程）
//        loadData2()
        // CoroutineScope.launch + withContext 执行任务 （主线程中加入子线程）
        // 上一个例子中的方法，我们可以正常的运行。但我们浪费了启用第二个后台任务协程的资源。如果我们只启用一个协程，可以使用withContext来优化我们的代码
//        loadData3()
        // GlobalScope.launch + withContext 执行任务 （主线程中加入子线程）
//        loadData4()
        // CoroutineScope.launch + withContext （串行执行多个任务）
//        loadData5()
        // GlobalScope.launch + async  （并行执行多个任务）
//        loadData6()
        // withTimeoutOrNull()  （设置并判断协程超时情况）
//        loadData7()
        // job.cancel()  （取消协程）
        val loadData8 = loadData8()
        Log.e("TAG", "loadData8: 2")
        loadData8.cancel()
        Log.e("TAG", "loadData8: 3")
        // 处理协程中的异常  使用try...catch方式处理
        loadData9()


        // 全量更新和增量更新的区别。全量更新是全部更新，增量更新是增加的部分更新

        // 字符和字节的关系：
        // 一般来说，一个英文字符占一个字节；一个中文字符占两个字节；目前使用的一般就是十进制和十六进制的转换
        // 5A(0x) -> 90    59(0x) -> 89
        // 02x 表示 以16进制输出，不足两位前面补0，大于等于两位原样输出
        Log.e("TAG", "onCreate1: " + String.format("%02x", 6))  // 06
        Log.e("TAG", "onCreate2: " + String.format("%02x", 12))  // 0c
        Log.e("TAG", "onCreate3: " + String.format("%02x", 12311111))  // bbda47
        // 2x或者x或者3x  都表示 原样输出
        Log.e("TAG", "onCreate4: " + String.format("%x", 6))  // 6
        Log.e("TAG", "onCreate5: " + String.format("%x", 12))  // c
        Log.e("TAG", "onCreate6: " + String.format("%x", 12311111))  // bbda47
        // 04x 表示 以16进制输出，不足四位前面补0，大于等于四位原样输出
        Log.e("TAG", "onCreate7: " + String.format("%04x", 6))  // 0006
        Log.e("TAG", "onCreate8: " + String.format("%04x", 12))  // 000c
        Log.e("TAG", "onCreate9: " + String.format("%04x", 12311111))  // bbda47
        // %d 表示 以10进制输出，不足四位前面补0，大于等于四位原样输出
        Log.e("TAG", "onCreate10: " + String.format("%04d", 12))  // 0012
        // %c 表示 以字符方式输出
        Log.e("TAG", "onCreate11: " + String.format("%c", 12))  // 
        // %s 表示 以字符串方式输出
        Log.e("TAG", "onCreate12: " + String.format("%s", 12))  // 12
        // 如果格式化字符串，前面不能加0，加了就会报错
//        Log.e("TAG", "onCreate13: " + String.format("%04s", 12))
//        Log.e("TAG", "onCreate14: " + String.format("%04s", "12"))

        // (0x04|23 << 3) (02x) = 4 or (23 * 2 ^ 3) = 188(02x) = bc
        // shl(lsh) 有符号左移  shr(rsh) 有符号右移  ushr 无符号右移    inv 按位取反
        // and 位与运算  or 位或运算  xor 按位异或
        // 0&0=0  0&1=0  1&0=0  1&1=1 与
        // 0|0=0  0|1=1  1|0=1  1|1=1 或
        Log.e("TAG", "onCreate13: " + String.format("%02x", 0x04 or (23 shl 3)))  // bc
        // (0x04&24 >> 2) (02x) = 4 and (24 / 2 / 2) = 4(02x) = 04
        Log.e("TAG", "onCreate14: " + String.format("%02x", 0x04 and (24 shr 2)))  // 04

//        正常
//        指令, 接收022E0022113000002E00005A00005A00005A00005A00005A00005A00005A00005A00005A0000520000000000000094379000
//        指令, 发送80dc02b830022E0022113000002d00005A00005A00005A00005A00005A00005A00005A00005A00005A000052000000000000009437
//                80dc02dc
//
//        异常
//        指令, 接收022E0022123100005A00005A00005A00005A00005A00005A00005A00005A00005A00005A00005A00005A00000000241F9000
//        指令, 发送80dc02b830022E0022123100005900005A00005A00005A00005A00005A00005A00005A00005A00005A00005A00005A00000000241F

        // 为什么传入的Context，全部都用SoftReference进行包裹
//        val softReference = SoftReference<Context>(this.applicationContext)
        val softReference = SoftReference<Context>(this)
        // 通过softReference.get() 获取context上下文   解决因为内存不足方便Context类进行回收，防止内存泄漏问题
        val context = softReference.get()

        // 自定义Exception的写法
        // 如果exception被catch住 通过system.err会精准过滤出来，也可以通过Worn Tag过滤；如果抛出Exception，可以通过Exception进行过滤
        testException()

        // Git图谱学习 - git graph图
        // origin/xxx 分支 就是指远程分支
        // 每一个点* 代表一次提交记录   最左边的那条线代表主流分支   / 表示有新分支 \表示分支合入  | 表示分支前进
        // 每次提交记录信息包括：提交信息内容、时间、用户名、哈希值（8位）、本次提交代码提交到的分支名以及创建的TAG（中间的小木板）
        // Merge branch 'hotfix' of http:xxx into hotfix  将远程分支合并到本地分支并提交
        // 每个分支的颜色不会固定的代表某个分支
        // 创建分支时，如果从分支图上不好区分，最好在提交的时候，将项目名写清楚，可以有效地区分分支。比如：feature(xxx项目)/fix(xxx项目)
        // 创建分支名时，最好能够体现，是从哪个分支上切出来的分支。即如果原分支是feature_idface分支，新建的可以为feature_idface_qinglong分支
        // HEAD就是一个指针 指向当前所在分支的本地分支
        // 分为master分支、develop分支，在dev分支上开发，定期将稳定的版本同步到master分支上即可。所有的开发人员基于dev分支再建分支进行开发，
        // 调试bug等，改完之后，再合并到dev分支上去，这样就会保证一直维护的是一套代码，而不是多个分支的代码

        // git中 cherry-pick 功能使用。在本分支上选择其他分支修改的节点内容，右键选择Cherry-Pick，即可把修改的内容合并到本分支上
        // git中 Merge Changes 功能使用。在本分支上选择哪个分支合并到本分支上

        // 在服务中开启广播
//        startService(Intent(this, MonitorService::class.java))

        // 通过包名和常量跳转activity和广播
        // 通过包名和类名 字符串进行跳转
        ServiceUtils.startService(ServiceConstant.MONITOR_SERVICE)
        tv.setOnClickListener {
            ActivityUtils.startActivity(this.packageName, ActivityConstant.TEST_HOME)
            ServiceUtils.stopService(ServiceConstant.MONITOR_SERVICE)
            finish()
        }


        // 数据库的查询字段 命令学习 -- 明天试试


    }

    private fun initView() {
        tv = findViewById(R.id.tv)
    }

    private fun test1(): Boolean {
        var aaa: String? = ""   // aaa不为null  返回true
//        aaa = null  // aaa为null  返回false
        // 这段话的意思是，不为null 返回true 为null返回false
        val block = aaa?.let {
            true
        } ?: false
        return block
    }

    // 先创建一个范围，然后在范围中进行执行
    val uiScope1 = CoroutineScope(Dispatchers.Main)
    fun loadData() = uiScope1.launch {
        tv.text = "hahhahah"
        // 这两个是串行执行的
        val result1 = withContext(Dispatchers.IO) {
            // 协程中的睡眠
            delay(2000)
            // 报这样的错误：Only the original thread that created a view hierarchy can touch its views.
//            tv.text = "hahhahah1"
            Log.e("TAG", "loadData1: ${Thread.currentThread().name}")
            "result1"
        }
        val result2 = withContext(Dispatchers.IO) {
            delay(1000)
            // 报这样的错误：Only the original thread that created a view hierarchy can touch its views.
//            tv.text = "hahhahah2"
            Log.e("TAG", "loadData2: ${Thread.currentThread().name}")
            "result2"
        }
        Log.e("TAG", "result1:${result1},,,result2:${result2}")
    }

    // Dispatchers.Main 表示在主线程运行   Dispatchers.IO 表示在子线程运行
    fun loadData1() = GlobalScope.launch(Dispatchers.Main) {
        delay(2000)
//        Dispatchers.IO 会闪退    Dispatchers.Main 不会
//        tv.text = "hahhahah1"
    }

    val mainScope = CoroutineScope(Dispatchers.Main)
    fun loadData2() = mainScope.launch {
        // 主线程，更新UI
        tv.text = "hahaha"
        // 子线程，耗时性操作
        val task = async(Dispatchers.IO) {
            delay(5000)
        }
        // 父协程一直等待它的子协程完成
        task.await()
        // 主线程，更新UI
        tv.text = "11111111"
    }

    fun loadData3() = mainScope.launch {
        // 主线程，更新UI
        tv.text = "hahaha"
        // 子线程，耗时性操作
        val task = withContext(Dispatchers.IO) {
            delay(5000)
        }
        tv.text = task.toString()
    }

    fun loadData4() = GlobalScope.launch(Dispatchers.Main) {
        // 主线程，更新UI
        tv.text = "hahaha"
        // 子线程，耗时性操作
        val task = withContext(Dispatchers.IO) {
            delay(5000)
        }
        tv.text = task.toString()
    }

    fun loadData5() = mainScope.launch {
        val result3 = withContext(Dispatchers.IO) {
            delay(5000)
            Log.e("TAG", "result3:")  // 5s后执行这里 -2
        }
        val result4 = withContext(Dispatchers.IO) {
            delay(5000)
            Log.e("TAG", "result4:")  // 再5s后执行这里 -3
        }
        // 然后再执行这里 -1
        Log.e("TAG", "result3: $result3，，，result4: $result4")
    }

    fun loadData6() = GlobalScope.launch(Dispatchers.Main) {
        val task1 = async(Dispatchers.IO) {
            delay(5000)
            // 最后（4s钟之后）打印这里 -3
            Log.e("TAG", "task1:")
        }
        val task2 = async(Dispatchers.IO) {
            delay(1000)
            // 然后（1s钟之后）再打印这里 -2
            Log.e("TAG", "task2:")
        }
        // 这里先打印 -1
        Log.e("TAG", "task1: $task1，，，task2: $task2")
    }

    fun loadData7() = mainScope.launch {
        val task3 = async(Dispatchers.IO) {
            delay(5000)
            Log.e("TAG", "task3:")
        }
        // 超时2s判断方法是否执行完毕，执行未完毕返回null，执行完毕返回int值（具体值为什么，原因未知）
        val result5 = withTimeoutOrNull(2000) {
            task3.await()
        }
        Log.e("TAG", "result5: $result5")
    }

    fun loadData8() = mainScope.launch {
        withContext(Dispatchers.IO) {
            delay(5000)
        }
        Log.e("TAG", "loadData8: 1")
    }

    fun loadData9() = GlobalScope.launch(Dispatchers.Main) {
        tv.text = "hahaha"
        try {
            val task = withContext(Dispatchers.IO) {
                // 模拟加载数据
                delay(5000)
            }
            tv.text = task.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun testException() {
        try {
            // 初始化商汤芯片的代码（可能会报错的）
            testException1()
            Log.e("zhr", "testException1: ")
        } catch (e: STChipFailException) {
            e.printStackTrace()
            Log.e("zhr", "testException2: ${e.message}")
        }
    }

    @Throws(STChipFailException::class)
    fun testException1() {
        // 伪造异常  符合这种情况抛出异常
        val isInitialized = false
//        val a = 1 / 0
        if(!isInitialized){
            throw STChipFailException("加密芯片初始化失败")
        }
    }
}