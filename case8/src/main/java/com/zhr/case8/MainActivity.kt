package com.zhr.case8

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import com.google.gson.Gson
import com.zhr.case8.databinding.ActivityMainBinding
import java.lang.ref.SoftReference
import kotlin.properties.Delegates

/**
 * 1.按钮selector效果和手动写延时效果的区别
 * selector有按下和抬起效果，然后再跳转
 * 手动延时效果，看不见效果就直接跳转了，在跳转后才出现效果
 * 2.让View去承担起更多的能力的思路去想
 * 3.设置自定义的LayoutParams
 * 4.测试日志发送log框架(本地log日志采集以及一键采集日志发送日志到对方邮箱中)   https://www.jianshu.com/p/a32607921c02
 * 5.爱奇艺 xCrash 使用
 * 6.lateinit 和 Delegates(代理、委托类)
 * 7.kotlin中list集合api使用
 * 8.Sonar 代码质量管理工具 http://www.sonar.org.cn/
 * https://blog.csdn.net/cc_want/article/details/84225318
 * 9.kotlin 知识梳理：https://www.jianshu.com/p/f9e78d6c54bd
 * 10.GreenDao 数据库升级的几种写法
 * https://blog.csdn.net/jifenglie/article/details/103892986       https://blog.csdn.net/qq_29924041/article/details/86584702
 * 11.判断某个类是否被初始化   lateinit 变量是否被初始化，可以通过 isInitialized 进行判断
 */
class MainActivity : AppCompatActivity() {

    private val baseViewModel: BaseViewModel<Any> by viewModels {
        SavedStateViewModelFactory(this.application, this)
    }

    private val stateViewModel: StateViewModel<Any> by viewModels {
        SavedStateViewModelFactory(this.application, this)
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    // lateinit 只能修饰引用数据类型
    private lateinit var a: String

    // 基本数据类型要通过 Delegates类使用
    private var num: Int by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        binding.tv.setOnClickListener {
//            val list = mutableListOf<String>()
//            list.add("1")
//            list.add("2")
//            viewModel.baseLiveData.postValue(BaseEvent(AppConstant.TEST_CLICK, list))

            val baseLiveData = baseViewModel.baseLiveData
        }


        // 测试用的，领悟思想 （总体思路，丰富View的内容，模块化构思，让View具备更多的能力）
//        StateView<Boolean>(this, this).getLayoutRes()

        // 测试使用layoutparams  LayoutParams设置布局，子控件告诉父控件，自己要如何布局
        findViewById<LinearLayout>(R.id.ll).visibility = View.GONE
        // fragment通过name进行布局的引用
        // 如果不设置layoutparams，则取用的是默认的layoutparams

        // 爱奇艺xcrash日志
        // android ABI（二进制接口交互规则） Application Binary Interface
        xCrashUtil.init(SoftReference<Context>(this), "sdcard/Download/whpe/crash")
//        Log.e("TAG", "onCreate: " + 1 / 0)

        // Builder建造者模式的原理：每设置一个属性都会返回此Builder对象本身，可以方便通过链式进行调用

        // 自己封装的不为null的函数
        ifNotNull("18888888888", "123456") { username, password ->
            login(username, password)
        }
        // listOf()和mutableListOf() 一个是固定集合 一个是可变集合
        // https://blog.csdn.net/u013064109/article/details/78786646 这篇文章值得仔细看

        // data类 = 数据Bean类
        // object类 = 饿汉式单例类  object关键字（对象声明(静态类) object class，伴生对象(静态方法) companion object
        // ，对象表达式(改变写法的匿名内部类以及将匿名对象存储到变量中) object::）
        // Java中使用Object饿汉式单例类： DataManager.INSTANCE.getDataBaseName()
        ObjectTest1.a()  // test a:
        ObjectTest1.b()  // test b:
        ObjectTest2.a()  // test a:
        // 使用伴生对象实现工厂方法的例子
        val facebookUser = User.newFacebookUser(0)
        val subscribingUser = User.newSubscribingUser("aaa")
        Log.e(
            "TAG",
            "facebookUser: $facebookUser,subscribingUser: $subscribingUser"
        )  // facebookUser: com.zhr.case8.User@6729f6a,subscribingUser: com.zhr.case8.User@15c5c5b
        Log.e(
            "TAG",
            "facebookUser.nickname: ${facebookUser.nickname},subscribingUser.nickname: ${subscribingUser.nickname}"
        )  // facebookUser.nickname: tom,subscribingUser.nickname: jeck

        // 在伴生对象中实现接口
        testLog(Person)  // System.out: I am Person

        // 伴生对象的扩展方法
        val people = People("tom")
        Log.e("TAG", "people: ${People.Companion.toJson(people)}")  // people: people:tom

        // Lambda表达式，本质是 可以传递给函数的一小段代码。可以轻松的把通用的代码结构抽取成库函数
        // 可以把lambda表达式存储在一个变量中，把变量当作普通函数对待
        val sum = { x: Int, y: Int -> x + y }
        Log.e("TAG", "sum: ${sum(3, 5)}")  // sum: 8

        // run方法，可以当作代码块使用
        run {
            Log.e("TAG", "I am run")  // I am run
        }

        val getAge = { p: Person -> p.name }
        Log.e(
            "TAG",
            "getAge: $getAge"
        )  // getAge: Function1<com.zhr.case8.Person, java.lang.String>

        // kotlin list集合中api使用
        val animals = listOf<Animal>(Animal("cat", 3), Animal("dog", 2), Animal("duck", 1))
        // sublist 切割集合，包左不包右
        Log.e(
            "TAG",
            "animals1: ${Gson().toJson(animals.subList(0, 1))}"
        )  // animals1: [{"age":3,"name":"cat"}]
        // asReversed 集合中倒序排列
        Log.e(
            "TAG",
            "animals2: ${Gson().toJson(animals.asReversed())}"
        )  // animals2: [{"age":1,"name":"duck"},{"age":2,"name":"dog"},{"age":3,"name":"cat"}]
        // component1表示取集合中的第一个值
        Log.e(
            "TAG",
            "animals3: ${Gson().toJson(animals.component1())}"
        )  // animals3: {"age":3,"name":"cat"}
        // take 取集合中的前两个值
        Log.e(
            "TAG",
            "animals4: ${Gson().toJson(animals.take(2))}"
        )  // animals4: [{"age":3,"name":"cat"},{"age":2,"name":"dog"}]
        // takeLast 取集合中的后两个值
        Log.e(
            "TAG",
            "animals5: ${Gson().toJson(animals.takeLast(2))}"
        )  // animals5: [{"age":2,"name":"dog"},{"age":1,"name":"duck"}]
        Log.e(
            "TAG", "animals6: ${
                // takeWhile 从头开始匹配符合条件的值，匹配不到则停止
                // takeLastWhile 从尾部开始匹配符合条件的值，匹配不到则停止
                Gson().toJson(animals.takeWhile {
                    it.age == 3
                })  // animals6: [{"age":3,"name":"cat"}]
            }"
        )
        Log.e(
            "TAG", "animals7: ${
                // takeif 如果符合条件则返回集合本身，不符合则返回null
                Gson().toJson(animals.takeIf {
                    it.size == 3
                })  // animals7: [{"age":3,"name":"cat"},{"age":2,"name":"dog"},{"age":1,"name":"duck"}]
            }"
        )
        Log.e(
            "TAG", "animals8: ${
                // takeunless 如果不符合条件则返回集合本身，符合则返回null
                Gson().toJson(animals.takeUnless {
                    it.size == 3
                })  // animals8: null
            }"
        )
        Log.e(
            "TAG", "animals9: ${
                // drop 丢掉前n个并返回，n是从1开始
                Gson().toJson(animals.drop(2))
            }"  // animals9: [{"age":1,"name":"duck"}]
        )
        Log.e(
            "TAG", "animals10: ${
                // dropLast 丢掉后n个并返回，n是从最后1个开始
                Gson().toJson(animals.dropLast(2))
            }"  // animals10: [{"age":3,"name":"cat"}]
        )
        Log.e(
            "TAG", "animals11: ${
                // dropWhile 从头开始找，丢掉第一个满足条件的并返回，后面的不会处理
                Gson().toJson(animals.dropWhile {
                    it.name == "dog"  // animals11: [{"age":3,"name":"cat"},{"age":2,"name":"dog"},{"age":1,"name":"duck"}]
                    // it.name = "cat"  // animals11: [{"age":2,"name":"dog"},{"age":1,"name":"duck"}]
                })
            }"
        )
        Log.e(
            "TAG", "animals12: ${
                // dropLastWhile 从后面开始找，丢掉最后一个满足条件的并返回，前面的不会处理
                Gson().toJson(animals.dropLastWhile {
                    it.name == "dog"  // animals12: [{"age":3,"name":"cat"},{"age":2,"name":"dog"},{"age":1,"name":"duck"}]
                    // it.name == "duck"  // animals12: [{"age":3,"name":"cat"},{"age":2,"name":"dog"}]
                })
            }"
        )
        Log.e(
            "TAG", "animals13: ${
                // map会根据条件 重新创建一个新的集合
                // foreach 只是对集合进行遍历
                Gson().toJson(animals.map {
                    it.age // it.age + 1
                })
            }"  // animals13: [4,3,2]
        )
        val animals15 = listOf(1..3, 6..9)
        Log.e(
            "TAG",
            "animals15: ${Gson().toJson(animals15)}"
        )  // animals15: [{"first":1,"last":3,"step":1},{"first":6,"last":9,"step":1}]
        // flatmap 把同类型的数据压平
        Log.e(
            "TAG",
            "animals15: ${Gson().toJson(animals15.flatMap { it.asIterable() })}"
        )  // animals15: [1,2,3,6,7,8,9]
        val animals16 = listOf(1, 2, 5, 6)
        // fold 3+1+2+5+6 = 17  集合和提供的值相操作
        Log.e(
            "TAG",
            "animal16: ${Gson().toJson(animals16.fold(3, { a, b -> a + b }))}"
        )  // animal16: 17
        // reduce 1+2+5+6 = 14    集合之间的值互相操作
        Log.e(
            "TAG",
            "animal17: ${Gson().toJson(animals16.reduce { a, b -> a + b })}"
        )  // animal17: 14
        // joinToString 是把集合内容通过自己自定义的方式，转化为String字符串
        Log.e(
            "TAG", "animals18: ${
                animals16.joinToString { it ->
                    it.toString()
                }
            }"
        )  // animals18: 1, 2, 5, 6
        Log.e("TAG", "animals19: ${
            animals.joinToString {
                it.name.plus("(${it.age})")
            }
        }") // animals19: cat(3), dog(2), duck(1)
        Log.e(
            "TAG", "animals20: ${
                // 分隔符， 开始符号， 结束符号
                animals.joinToString(separator = "***", prefix = "<", postfix = ">") {
                    it.age.toString()
                }
            }"
        )  // animals20: <3***2***1>

        // filter 根据指定的规则进行过滤，返回新过滤的集合
        val newList = animals16.filter { it ->
            it % 2 == 0
        }
        Log.e("TAG", "newList: ${Gson().toJson(newList)}")  // newList: [2,6]
        // ?. 可空调用符(如果不为null则执行)    ?: 如果不为空使用前面，为空使用后面
        // !! 非空断言(把任何值转换为非空类型 如果为空会抛出异常)
        // 消除非空调用 -> lateinit 延迟初始化  // lateinit的缺点是，在使用之前一定要初始化，不初始化会报错
        // 所以lateinit使用一定是要确定一定会初始化才会调用的
        if ("".isNullOrEmpty()) {
        }
        if ("".isNullOrBlank()) {
        }
        // isNullOrEmpty 和 isNullOrBlank的区别： 都可以判断空和null，isNullOrBlank判断得更仔细一点
        var data: String? = null
        Log.e("TAG", "onCreate1: ${data.isNullOrBlank()?.toString()}") // true
        Log.e("TAG", "onCreate1: ${data.isNullOrEmpty()?.toString()}") // true
        data = ""
        Log.e("TAG", "onCreate2: ${data.isNullOrBlank()?.toString()}") // true
        Log.e("TAG", "onCreate2: ${data.isNullOrEmpty()?.toString()}") // true
        data = "hello"
        Log.e("TAG", "onCreate3: ${data.isNullOrBlank()?.toString()}") // false
        Log.e("TAG", "onCreate3: ${data.isNullOrEmpty()?.toString()}") // false
        data = " " // 空格  isNullOrBlank可以判断   isNullOrEmpty不能判断
        Log.e("TAG", "onCreate4: ${data.isNullOrBlank()?.toString()}") // true
        Log.e("TAG", "onCreate4: ${data.isNullOrEmpty()?.toString()}") // false
        data = "\u001E" // 不可见字符  isNullOrBlank可以判断   isNullOrEmpty不能判断
        Log.e("TAG", "onCreate5: ${data.isNullOrBlank()?.toString()}") // true
        Log.e("TAG", "onCreate5: ${data.isNullOrBlank()?.toString()}") // false

        var count = 0
        var i = 0
        while (i <= 127) {
            System.out.printf("%3c", i)  // 打印每个ascii字符内容
            count++
            if (count % 10 == 0) //这里改为10就是每行打印10个
                println("\n")
            i++
        }
        println("\n")

        // 泛型类型的可空性  泛型如果指定了非空上界，就不需要进行非空判断了
        printHashCode2("a")
        printHashCode1("a")

        // 测试kotlin和Java null类型的互调
        val string1: String? = null
        NullTest.nullable(string1)  // java中的 @Nullable注解，就是可空类型  String?
        val string2 = ""
        NullTest.notNull(string2)  // java中的 @NotNull注解，就是非空类型  String  如果可空需要非空断言
        NullTest.normal(string2)    // java中 不加注解的类型，就是平台类型  String！  可传空，也可传非空
        // java中不加注解的类型 默认就是平台类型，kotlin重写的时候，可以定义为可空类型，也可以定义为非空类型
        // kotlin中不存在基本数据类型和引用数据类型，只存在可空类型和非空类型
//        throw Exception("exception")    // 这样写是指一定可以抛出异常
//        @Throws(Exception::class)      // 如果有异常时，可以通过注解的方式抛出异常
//        fun aaa(){}
        // kotlin中的集合可以分为 只读集合和可变集合    Collection(只读集合)    MutableCollection(可变集合)
        val collection: Collection<String> = listOf("1", "2", "3")
        val mutableCollection: MutableCollection<String> = arrayListOf("8848")
        val copyElements = copyElements(collection, mutableCollection)
        Log.e(
            "TAG",
            "copyElements: ${Gson().toJson(copyElements)}"
        )  // copyElements::["8848","1","2","3"]

        // kotlin中的list和set、map 是只读集合，arrayList和hashSet、hashmap、mutableList 是可变集合
        // kotlin中设置的只读集合和可变集合在java中是不生效的，只针对kotlin系统生效
        // Kotlin把那些定义在Java代码中的类型看成 平台类型
        // java类型的接口(平台类型)，kotlin类型实现java类型接口    PersonParser就是kotlin类型实现java类型接口

        var array1 = arrayOf(1, 2, 3)
        array1.joinToString(separator = ",")
        Log.e("TAG", "array1: ${Gson().toJson(array1)}")  // array1: [1,2,3]
        Log.e("TAG", "array1: ${array1.joinToString(",")}") // array1: 1,2,3
        // arrayOfNulls 默认里面的值全部为null 需要显式指定元素类型
        val array2 = arrayOfNulls<Int>(3)
        array2[0] = 100
        Log.e("TAG", "array2: ${Gson().toJson(array2)}")  // array2: [100,null,null]
        Log.e("TAG", "array2: ${array2.joinToString(",")}")  // array2: 100,null,null
        val array3 = Array<String>(10) { index ->
            index.toString()
        }
        Log.e(
            "TAG",
            "array3: ${Gson().toJson(array3)}"  // array3: ["0","1","2","3","4","5","6","7","8","9"]
        )  // array3: ["0","1","2","3","4","5","6","7","8","9"]
        val array4 = IntArray(3)  // intArray 默认就是值为 0
        Log.e("TAG", "array4: ${Gson().toJson(array4)}")  // array4: [0,0,0]
        val array5 = intArrayOf(1, 2, 3)
        Log.e("TAG", "array5: ${Gson().toJson(array5)}")  // array5: [1,2,3]
        val array6 = ArrayList<Int>()
        array6 += 10
        Log.e("TAG", "array6: ${Gson().toJson(array6)}")  // array6: [10]
        array1 += 100  // += -> 朝后加约定的值
        Log.e("TAG", "array1: ${Gson().toJson(array1)}")  // array1: [1,2,3,100]

        // kotlin内联函数，当一个函数被声明为inline时，它的函数体是内联的，函数体会被直接替换到函数被调用的地方
        // 内联函数 一般和lambda表达式一起使用， 主要为了解决lambda表达式中匿名内部类导致的内存占用问题
        // 就是把匿名内部类转换为直接可以调用的类，内联函数相当于把带有inline声明的方法体代码粘到调用处了。之前是被动调用，现在inline是主动调用
        // 内联函数就是方法的拷贝
        // inline修饰符和noinline修饰符  使用内联函数的时候，需要注意函数的长度，如果长度过长则会影响性能
        inlineFun("inlineFun") {
            Log.e("TAG", "haha")
        }
        val a: () -> Unit = { Log.e("TAG", "haha") }
        inlineFun1("test", a)

        // 从lambda表达式中返回的两钟
        val persons = listOf(Person("Alice", 29), Person("Bob", 31))
        backFromFun(persons)   // 从一个封闭的函数返回  // Found!
        backFromLaber(persons)  // 使用标签返回       // Alice might be somewhere
        backFromLocal(persons)  // 默认使用局部返回    // Bob is not Alice

        // 泛型类和泛型接口  https://www.jianshu.com/p/d290f02964f5
        val holderInt = HolderInt()
        holderInt.setValue(2)
        Log.e("TAG", "holderInt: ${holderInt.getValue()}")
        val holderString = HolderString()
        holderString.setValue("buyaoshifangxian")

        // GreenDao 数据库升级的几种写法
        // greendao 数据库使用数据库拷贝升级的做法，会导致原数据被误删等不稳定等情况
        // class DatabaseUpdateOpenHelper : DaoMaster.DevOpenHelper{
        //      private val mUpdateTables = ArrayList<Class<out AbstractDao<*, *>?>?>
        //      override fun onUpgrade(){
        //          mUpdateTables.add(SiteCacheInfoDao::class.java)
        //          //  标准的GreenDao数据库升级方法，先拷贝出一个中间库，再把库中的数据恢复
        //          MigrationHelper.migrate(db, object : MigrationHelper.ReCreateAllTableListener {
        //              override fun onCreateAllTables(db: Database?, ifNotExists: Boolean) {
        //                  DaoMaster.createAllTables(db, ifNotExists)
        //              }
        //
        //              override fun onDropAllTables(db: Database?, ifExists: Boolean) {
        //                  DaoMaster.dropAllTables(db, ifExists)
        //              }
        //           }, *(mUpdateTables.toTypedArray()))
        //      }
        //
        //      或者    根据数据库新版版本号 数据库的备份和升级
        //      when(newVersion){
        //          0 -> {}
        //          1 -> {}
        //          2 -> MigrationHelper.migrate(db, SiteCacheInfoDao::class.java)
        //          else -> {}
        //      }
        //
        //      或者    通过数据库sql语句进行数据库字段的新增，不会对数据库进行拷贝和删除
        //      if (newVersion == 2) {
        //          // 在表中添加字段
        //          db?.execSQL(StringBuffer().append(
        //                  "ALTER TABLE",
        //                  " site_cache_info",
        //                  " ADD ",
        //                  "SITE_STATE",
        //                  " TEXT ",
        //                  "DEFAULT NULL"
        //              ).toString()
        //          )
        //          // 新增一个表 SiteCacheInfoDao.createTable(db, true)
        //      }
        // }



    }

    data class Person(val name: String, val age: Int)

    fun backFromFun(people: List<Person>) {
        people.forEach {
            if (it.name == "Alice") {
                println("Found!")
                return
            }
        }
        println("Alice is not found")
    }

    fun backFromLaber(people: List<Person>) {
        people.forEach label@{
            if (it.name == "Alice") return@label
        }
        println("Alice might be somewhere")
    }

    fun backFromLocal(people: List<Person>) {
        // 这种写法有点搞不懂
        people.forEach(fun(person) {
            if (person.name == "Alice") return
            println("${person.name} is not Alice")
        })
    }

    fun inlineFun(prefix: String, action: () -> Unit) {
        Log.e("TAG", "call before $prefix")
        action()
        Log.e("TAG", "call after $prefix")
    }

    inline fun inlineFun1(prefix: String, action: () -> Unit) {
        Log.e("TAG", "call before $prefix")
        action()
        Log.e("TAG", "call after $prefix")
    }

    fun <T> copyElements(
        collection: Collection<T>,
        mutableCollection: MutableCollection<T>
    ): MutableCollection<T> {
        mutableCollection.addAll(collection)
        return mutableCollection
    }

    private fun <T> printHashCode1(t: T) {
        Log.e("TAG", "printHashCode1: ${t?.hashCode()}")
    }

    private fun <T : Any> printHashCode2(t: T) {
        Log.e("TAG", "printHashCode2: ${t.hashCode()}")
    }

    private fun initViewModel() {
        baseViewModel.baseLiveData.observe(this, {
            when (it?.code) {
                AppConstant.TEST_CLICK ->
                    binding.tv.text = "".plus(it.data.toString())
                else -> {
                }
            }
        })
    }

    private fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
        if (value1 != null && value2 != null) {
            bothNotNull(value1, value2)
        }
    }

    private fun login(username: String, password: String) {}
}

class User private constructor(val nickname: String) {
    companion object {
        // 新建订阅用户
        fun newSubscribingUser(email: String) = User(getSubscribingName())

        // 新建脸书用户
        fun newFacebookUser(accountId: Int) = User(getFacebookName())
    }
}

fun getSubscribingName() = "jeck"
fun getFacebookName() = "tom"


interface LogObject {
    fun log()
}

class Person(val name: String) {
    // 在伴生对象中实现接口
    companion object : LogObject {
        override fun log() = println("I am Person")
    }
}

fun testLog(logObject: LogObject) {
    logObject.log()
}

// People().name 创建对象可以调用.name属性
class People(val name: String) {
    companion object {}
}

// 伴生对象的扩展函数
fun People.Companion.toJson(people: People): String {
    return "people:${people.name}"
}

data class Animal(var name: String, var age: Int)
