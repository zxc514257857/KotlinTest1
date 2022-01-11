package com.example.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.app.enumtest.CityEnum
import com.example.app.enumtest.ErrorCodeEnum
import com.example.app.itemview.SignView
import com.example.app.observertest.*
import com.example.app.singtentest.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * 1、单例写法
 * 2、Observable使用 发送通知
 * 3、Handler 通知、延时定时任务、内存泄漏
 * 4、反射写法
 * 5、addView写法
 * 6、kotlin扩展函数使用
 * 7、在Test类中运行Java代码
 */
class MainActivity : AppCompatActivity() {

    val liveDataModel by lazy {
        // ViewModelProvider已经被弃用的方法
//        ViewModelProviders.of(this).get(LiveDataModel::class.java)
        // 目前使用的方法
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(LiveDataModel::class.java)
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("TAG", "subStringAndDefault1: ${"123456".subStringAndDefault("883399", 1, 5)}")
        // 如果想修改默认值的话，需要设置对应的参数名
        Log.e("TAG", "subStringAndDefault2: ${"123456".subStringAndDefault("883399", end = 10)}")

        // 再写一遍单例的写法 SingletonTest
        TestJava1.getInstance().print()
        TestJava2.getInstance().print()
        TestJava3.getInstance().print()
        TestJava4.getInstance().print()
        TestJava5.getInstance().print()
        TestKotlin1.getInstance().print()
        TestKotlin2.getInstance().print()
        TestKotlin3.getInstance().print()
        TestKotlin4.getInstance().print()
        TestKotlin5.getInstance().print()
        TestKotlin6.getInstance().print()
        // 通过单例模式传参，不要在静态里面进行传参  通过非静态方法将参数引入，然后在类中的其他地方调用
        TestKotlin7.getInstance().print(this)

        TestJava1.getInstance().print()
        TestJava2.getInstance().print()
        TestJava3.getInstance().print()
        TestJava4.getInstance().print()
        TestJava5.getInstance().print()
        TestKotlin1.getInstance().print()
        TestKotlin2.getInstance().print()
        TestKotlin3.getInstance().print()
        TestKotlin4.getInstance().print()
        TestKotlin5.getInstance().print()
        TestKotlin6.getInstance().print()

        // 再写一遍观察者的写法 ObserverTest
        // 一般都是先注册，再发送(顺序反过来的话，是接收不到数据的)
//        MyObservable.getInstance().addObserver(this)  // 在本类中使用，本类实现了Observer接口
        // Observer是一个接口，哪里实现 哪里就可以接收到数据  也可以在Activity中实现这个接口，那就在activity中接收
//        MyObservable.getInstance().addObserver(this)
//        MyObservable.getInstance().addObserver(MyObserver())
        // 一般来说，发是一个地方，收是一个地方，一般就是在注册的地方接收，注册就是为了接收数据（发送和接收如果在同一个地方就直接变量传参了呀，还会用观察者传参？）
        MyObservable.getInstance().addObserver { o: Observable?, arg: Any? ->
            Log.e("TAG", "update1: $arg")
        }
        MyObservable.getInstance().post("observer test")

        // 这个执行的速度不是很快，是取的最后一次变动的数据
        // 先注册或者后注册都会收到数据
        // setValue设置数据和postValue设置数据的区别：setValue传数据收到的和它发的是同一个线程 postValue传数据收到一定是在主线程
        // 也就是说setValue接收可以控制线程 postValue接收一定是在主线程
        liveDataModel.observeData.postValue("livedata observer test")
//        SystemClock.sleep(5000)
        // 可以在其他类中实现这个接口，也可以在本类或者本方法中实现这个接口
//        liveDataModel.observeData.observe(this, LiveDataObserver())
//        liveDataModel.observeData.observe(this, this)
        // 小括号创建对象引用，大括号自己创建引用
        // viewModel只能在activity和fragment中调用 viewModel可以看作是Presenter，activity中不想放的东西都可以放进去
        liveDataModel.observeData.observe(this, { t: String ->
            Log.e("TAG", "onChanged2: $t")
        })

        // 这里泛型的用途是限制传入数据的数据类型
//        EventBusUtils.post(EventBean(AppConstant.EventCode.TEST_POST, "event observer test"))
        // 黏性事件的意思就是说 先发送后注册，也有一个集合去存取注册之前发送的时候，在注册之后将数据进行发送
        EventBusUtils.postSticky(
            EventBean(
                AppConstant.EventCode.MAIN_POST,
                "eventbus observer test1"
            )
        )
        EventBusUtils.postSticky(
            EventBean(
                AppConstant.EventCode.ASYNC_POST,
                "eventbus observer test2"
            )
        )
        // 接收一定要在注册的Subscriber中，这里的Subscriber不一定是在Activity，在所有的类中都可以
//        EventBusUtils.register(this)
        EventBusUtils.register(MySubscriber())

        // Handler 数据的发送和接收  , Handler 防止内存泄漏的使用
        // Handler在哪个线程创建 handlerMessage方法就在哪个线程执行
        // 如果子线程创建handler 就要使用Looper.prepare() 和 Looper.loop()
//        thread {
//        Looper.prepare()
        val weakHandler = WeakHandler(this) { message ->
            when (message?.what) {
                // 可以在这里执行更新UI操作
                AppConstant.EventCode.HANDLER_POST1 -> {
                    Log.e(
                        "TAG",
                        "weakHandler1: ${message.obj as String} ::: ${Thread.currentThread()}"
                    )
                }
                AppConstant.EventCode.HANDLER_POST2 -> {
                    Log.e(
                        "TAG",
                        "weakHandler2: ${message.obj ?: "error"} ::: ${Thread.currentThread()}"
                    )
                }
            }
        }
//         Looper.loop()
//      }
        val message1 = weakHandler.obtainMessage()
        message1.what = AppConstant.EventCode.HANDLER_POST1
        message1.obj = "handler_post1"
        // 发送一般是在子线程发送  handler的创建一般是在主线程创建的
        weakHandler.handleMessage(message1)
        weakHandler.sendEmptyMessage(AppConstant.EventCode.HANDLER_POST2)

        // 裸声明Handler的构造方法已经被废弃了，要在构造里面传入 Looper和回调
        val testHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                if (msg.what == AppConstant.EventCode.TEST1) {
                    Log.e("TAG", "handleMessage: ")
                    // 可以在这里执行更新UI操作
                    // 可以写一个UIHandler，专门更新UI操作
                }
                // handleMessage这里的返回值的意思是：是同步处理消息还是异步处理消息（true是异步，false是同步，默认是异步处理）
                return false
            }
        })
        testHandler.sendEmptyMessage(AppConstant.EventCode.TEST1)

        // UIHandler使用
        UIHandler.post {
            Log.e("TAG", "UIHandler.post 更新UI")
        }
        UIHandler.postDelayed({
            Log.e("TAG", "UIHandler.postDelayed 更新UI")
        }, 3000L)


        // 延迟执行的任务推荐Handler
        val delayHandler = Handler(Looper.getMainLooper())
        // runnable不使用的时候记得remove掉 handler.remove
        val delayRunnable = Runnable {
            Log.e("TAG", "delay do sth")
        }
        delayHandler.postDelayed(delayRunnable, 3000L)

        // 定时执行的任务推荐Handler和RxJava
        val timerHandler = Handler(Looper.getMainLooper())
        val TIMER_CONSTANT = 3000L
        // runnable不使用的时候记得remove掉 handler.remove
        // 这里想不出办法解决，只能使用成员变量的方式解决
//        val timerRunnable = Runnable {
//            Log.e("TAG", "timerRunnable")
//            timerHandler.postDelayed(this, TIMER_CONSTANT)
//        }
        // 迫不得已的解决方法
        var timerRunnable: Runnable? = null
        timerRunnable = Runnable {
            Log.e("TAG", "timerRunnable")
            timerHandler.postDelayed(timerRunnable!!, TIMER_CONSTANT)
        }
        timerHandler.postDelayed(timerRunnable, TIMER_CONSTANT)

        // RxJava 实现定时任务(这里的Observable是Rxjava的Observable)
        io.reactivex.Observable.interval(TIMER_CONSTANT, TIMER_CONSTANT, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            // rxandroid要导包
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.e("TAG", "Observable.interval")
            }

        // 反射的写法 获取私有无参和有参方法、获取私有属性（公用的方法和属性也是可以获取到）
        // 私有无参方法
        val clz = Class.forName("com.example.app.ReflectTest")
        val obj = clz.newInstance()
        val method = clz.getDeclaredMethod("getName")
        method.isAccessible = true
        val data = method.invoke(obj)
        Log.e("TAG", "data:$data")
        // 私有有参方法
        val clz1 = Class.forName("com.example.app.ReflectTest")
        val obj1 = clz1.newInstance()
        val method1 = clz1.getDeclaredMethod("setName", String::class.java)
        method1.isAccessible = true
        val data1 = method1.invoke(obj1, "my name")
        Log.e("TAG", "data1:$data1")
        // 私有属性
        val clz2 = Class.forName("com.example.app.ReflectTest")
        val obj2 = clz2.newInstance()
        val method2 = clz2.getDeclaredField("name")
        method2.isAccessible = true
        val data2 = method2.get(obj2)
        Log.e("TAG", "data2:$data2")
        method2.set(obj2, "修改后名字")
        val data3 = method2.get(obj2)
        Log.e("TAG", "data3:$data3")

        // 测试泛型类的使用
        val container = Container<String>()
        container.setValue("123")
        Log.e("TAG", "onCreate1: ${container.getValue()}")
        // 测试泛型接口的使用
        Log.e("TAG", "onCreate2: ${IntGenerator().generate(66)}")
        Log.e("TAG", "onCreate3: ${StringGenerator().generate(20)}")

        // 测试枚举使用
        // 首先最基础的就是 当常量进行使用  打印这个对象，相当于是字面常量
        Log.e("TAG", "ErrorCodeEnum1: ${ErrorCodeEnum.DEVICE_NOT_EXIST}")
        // 然后就是根据 根据键去获取对象   打印这个对象，相当于是字面常量
        Log.e("TAG", "ErrorCodeEnum2: ${ErrorCodeEnum.getEnum(1000)}")
        // 根据这个对象去获取键和获取值
        Log.e("TAG", "ErrorCodeEnum3: ${ErrorCodeEnum.DEVICE_NOT_EXIST.code}")
        Log.e("TAG", "ErrorCodeEnum4: ${ErrorCodeEnum.DEVICE_NOT_EXIST.msg}")
        // 可以根据对象去获取键 或者获取值
        Log.e("TAG", "ErrorCodeEnum5: ${ErrorCodeEnum.getEnum(1000).code}")
        Log.e("TAG", "ErrorCodeEnum6: ${ErrorCodeEnum.getEnum(1000).msg}")
        // 相当是获取这个类的所有对象
        ErrorCodeEnum.values().forEach {
            Log.e("TAG", "ErrorCodeEnum -> ${it.msg}")
        }

        // 枚举作用，一种是自己手动修改匹配某种规则  一种是根据情况判断，符合某种情况，执行某个条件
        Log.e("TAG", "CityEnum1: ${CityEnum.getEnum("wuhan")}")
        Log.e("TAG", "CityEnum2: ${CityEnum.getEnum("wuhan").cityName}")
        Log.e("TAG", "CityEnum3: ${CityEnum.WUHAN}")
        Log.e("TAG", "CityEnum4: ${CityEnum.WUHAN.cityName}")
//        Log.e("TAG", "CityEnum5: ${CityEnum.valueOf("wuhan")}")
//        Log.e("TAG", "CityEnum6: ${CityEnum.WUHAN.ordinal}")
//        Log.e("TAG", "CityEnum7: ${CityEnum.WUHAN.name}")


        val arrayListOf = arrayListOf<ViewGroup>()
        val rootView = findViewById<LinearLayout>(R.id.root)
        val ll = LinearLayout(this)
        val signView1 = SignView(this)
        val signView2 = SignView(this)
        val signView3 = SignView(this)
        val signView4 = SignView(this)

//        signView1.setBackgroundColor(Color.WHITE)

        ll.addView(signView1)
        ll.addView(signView2)
        ll.addView(signView3)
        ll.addView(signView4)

        arrayListOf.add(signView1)
        arrayListOf.add(signView2)
        arrayListOf.add(signView3)
        arrayListOf.add(signView4)

        rootView.orientation = LinearLayout.HORIZONTAL
        rootView.gravity = Gravity.CENTER
        // 根布局的父布局是FrameLayout  现在根布局换成 ConstraintLayout
        rootView.setBackgroundColor(Color.BLACK)

        ll.orientation = LinearLayout.HORIZONTAL
        ll.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        ll.gravity = Gravity.CENTER
        ll.setBackgroundColor(Color.RED)

        arrayListOf.forEach {
            it.layoutParams =
                LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f)
            it.setBackgroundColor(Color.BLUE)
        }

        rootView.addView(ll)
    }
}

// kotlin中扩展函数使用
// 给原有函数再加一些方法，就可以使用
fun String.subStringAndDefault(default: String, start: Int = 0, end: Int = this.length): String {
    return try {
        this.substring(start, end)
    } catch (e: Exception) {
        default
    }
}

// 在androidstudio中运行java代码，可以在test类中进行
class TestJava {
    fun testPrint() {
        println("11111111111")
    }
}

// 测试私有无参方法和私有有参方法
class ReflectTest {
    private val name: String = "zhr"
    private fun getName(): String {
        return "zhr1"
    }

    private fun setName(name: String): String {
        return name
    }
}


// 泛型的使用-泛型类、泛型接口、泛型方法
// 表示它要操作的对象是未知的（比如集合的泛型，就表示要加入集合中的数据是未知的，
// 如果返回值是T就表示插入和返回的数据类型是一样的）,使用泛型主要是用来解决类型转换的问题，使用泛型用于限定传入的数据类型
class Animal<T> {}
interface IAnimal<T> {}

fun <T> initAnimal(param: T) {}

class Container<V> {

    private var value: V? = null

    fun getValue(): V? {
        return value
    }

    fun setValue(value: V?) {
        this.value = value
    }
}

// 接口功能是给实现类提供某些能力   接口是必须实现，可以调用
// 泛型主要约束传入和返回是同一种类型   里面存在的数据，传入的数据和返回的数据同一类型
interface Generator<T> {
    // 把这里的类型设置为可手动传的，然后类型不对的话再手动强转
    fun generate(obj: Any?): T
}

class IntGenerator : Generator<Int> {
    override fun generate(obj: Any?): Int {
        return Random.nextInt(20) + obj as Int
    }
}

class StringGenerator : Generator<String> {
    override fun generate(obj: Any?): String {
        return "my generate ${obj as Int + Random.nextInt(20)}"
    }
}