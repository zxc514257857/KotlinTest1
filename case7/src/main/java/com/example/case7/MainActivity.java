package com.example.case7;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apt_annotation.Print;
import com.example.apt_annotation.TestAnno;
import com.example.lib_net.NetServer;

import io.reactivex.schedulers.Schedulers;

/**
 * 1.java注解 https://juejin.cn/post/6982471491568812040
 * java中的注解，3.0以后偶不需要添加什么依赖和插件了，否则会报错
 * 注解的初步认识，见zhr.class
 * 2.APT  编译时注解。在编译器通过注解生成.class文件。能够方便的生成重复性代码
 * 如ButterKnife、Dagger、EventBus。编译时注解不是很常用
 * 我们经常使用到的注解是运行时注解：比如retrofit中的注解和Gson以及fastjson注解
 * https://blog.csdn.net/lvkaixuan/article/details/119784291
 * 方法：
 * -创建两个javalib(注意选的是java or kotlin lib而不是android library)，一个用来定义注解(apt-annotation)，一个用来扫描注解(apt-processor)
 * -获取到添加注解的成员变量名
 * -动态生成类和方法，用IO生成文件
 * 3.推送的通知和透传
 * 4.git merge 合并分支
 * 5.kotlin基础知识归纳
 * 6.gradle依赖配置的关键字
 * 依赖项有三种：module依赖、lib依赖和 远程依赖
 * implementation project(path: ':log')
 * implementation files('libs/alipay.aar')  // 添加jar包或aar依赖时使用
 * implementation fileTree(dir: 'libs', include: ['*.jar']) // 添加jar包或aar依赖时使用
 * implementation 'com.google.code.gson:gson:2.8.5'
 * https://blog.csdn.net/smallbabylong/article/details/106219930
 * ext{} 是指额外属性 extra的缩写    rootProject.ext.xxx
 * 依赖配置有：implementation、api、compileOnly、runtimeOnly，差异在构建内容以及参与构建的时机
 * compile 被implementation和api取代
 * provided 被compileOnly取代
 * apk 被runtimeOnly取代
 * <p>
 * api的依赖是可以传递的， moduleA api依赖 moduleB ， moduleB api依赖 moduleC，则A可以调用C中的方法
 * 如果moduleB不让A调用C中的方法，则需要将moduleB implementation依赖 moduleC。使用implementation实现了依赖隔离
 * api依赖 编译的速度比implementation 依赖的速度要慢一点。因为api依赖动了一个节点，整个链都需要重新编译。implemetation依赖只会影响到它可以调用的节点
 * api依赖表示module需要对外的，implementation表示module不需要对外，只需要对内的
 * <p>
 * compileOnly 表示只在编译时生效，不会参与打包。只是为了解决编译时找不到类的问题，运行时不予处理
 * runtimeOnly 表示只在运行时生效，不会参与编译。很少使用
 * testImplementation 在单元测试的编译以及打test包时有效
 * debugImplementation 在debug模式的编译以及打debug包时有效
 * releaseImplementation 在releae模式的编译以及打release包时有效
 * 如果想在debug模式下使用，在release模式下不使用，则 debugImplementation 'com.github.DingProg.NetworkCaptureSelf:library:v1.0.1'
 * <p>
 * 7.gradle查看并输出依赖树  查看依赖树 gradlew app:dependencies
 * 输出依赖树： gradlew -q :app:dependencies > log.txt
 * 8.anr系统有自己保存的日志：/data/anr/traces.txt
 * 采报错的日志用的是tombstone。墓碑日志
 * log日志上传用的是自己的日志上传框架
 * 9.adb monkey的使用
 */
@TestAnno
public class MainActivity extends AppCompatActivity {

    @Print
    private String name;
    @Print
    private int age;

    @zhr // 这里定义的注解，没有任何意义，就只是一个标签而已
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Print和zhr都没有正式用起来

        // TestAnno注解用起来了，在注解使用的activity中 Toast内容
        InjectActivity.inject(this);

        // 目前的推送有两种方式：一种是通知，一种是透传
        // 通知就是在通知栏显示的UI，只能在通知栏显示，并对通知栏进行少量的自定义
        // 然后就是借助推送实现的透传消息，就是我们可以收到我们想要格式的json字符串信息，进行透传消息的接收

        // git merge 合并分支
        // 分支相当于平行宇宙，平时互不打扰。当平行宇宙合并时，就需要处理一些问题了
        // 分支中的fetch、pull、push都只是在当前分支中的操作，没有将基线分支等其他分支中的东西弄过来。如果想要将基线分支内容合并过来，使用merge命令将基线分支内容合并（不使用merge不会将其他分支后面开发的内容弄过来）
        // 打包时要搞明白：1需要完善的功能点   2在哪个分支上进行打包（最好在支线分支上进行打包，打包完成，运行没问题再合并到基线分支）
        // 最好是不同的城市单独创建不同的分支，不合并到主分支上。再在不同城市上创建分支去开发不同的功能，功能线上运行稳定后将功能合并到城市分支上去
        // merge into current 选中的分支合入到当前分支    merge changes  把选择的分支合入到当前分支
        // 分支的好处：多个分支并行开发，互不耽误，提高开发效率

        // kotlin基础知识归纳
        // Kotlin中的Companion Object伴生对象就是static静态的意思，可以通过类名点进行调用
        // Companion Object伴生对象、init、构造方法三者之间的执行顺序
        // Companion Object先执行、init再执行、构造方法最后执行（无参构造先执行、有参构造后执行）

        // ctrl + H 查看类的继承关系

        // const val和val的区别
        // const必须修饰val  const val 表示public static final
        // val表示 public static final 即静态常量
        // var表示 public var 即变量
        // const val 只能在Companion Object环境下使用
        // private const val 就等同于val
        // open表示 打开，可继承。。kotlin默认是final类型的，即不可继承的

        // Java转Kotlin 即Kotlin转Java方法：
        // Java转Kotlin，右键最后一个选项即可
        // Kotlin转Java，Tools - kotlin - show kotlin bytecode

        // Object的使用场景：声明单例对象object Person{}、声明伴生对象Companion object{}、声明对象表达式 object:View.OnClickListener{}
        // lateinit 使用报错：'lateinit' modifier is not allowed on properties of primitive types
        // lateinit不能使用在八种基本数据类型上

        // object类和data类的区别：object声明类表示单例类。data声明类表示数据类  data类使用必须要用有参构造
        // 继承一个类，重写类里面的方法，如果满足一个判断，则自己实现一个判断逻辑，else就使用super.xxx父类的逻辑
        // 查看项目中的所有断点：Run - View Breakpoints...

        // Debug设置学习  Debug Type设置:Java only  通过Debug计算器进行断点调试

        // 在右侧的gradle Tasks - other 中找到assemble 的一些打渠道包的命令

        // kotlin范围限定修饰符
        // 我们在这里所说的范围是指：Project、Module、class类和子类
        // public > internal > protected > private
        // public在整个Project中可使用
        // internal在整个Module中可使用
        // protected在本类和子类（继承类）中可使用
        // private在本类中可使用

        // adb monkey的使用
        // -p 指定包名
        // --ignore-crashes 忽略crash，继续执行
        // --ignore-timeouts 忽略anr，继续执行
        // --ignore-security-exception 忽略许可错误，继续执行
        // --throttle 300  设置事件300毫秒延时
        // --monitor-native-crashes  监视并报告native层发送的崩溃代码
        // -v -v -v 提供最详细的信息
        // adb shell monkey -p com.whpe.pos.hubei.wuhan -v -v -v --ignore-crashes 10
        // -f加载monkey脚本文件进行测试（表示加载monkey script用最全的日志运行500次）
        // adb shell monkey -f sdcard/MonkeyScriptPress.txt -v -v -v 500
        // 停止monkey脚本
        // adb shell ps | findstr monkey  // 找到monkey的pid（一般pid在第一列和第二列显示）或者 adb shell ps | grep monkey
        // adb shell kill xxx(pid)   // 杀死的pid号
        // monkey script 的相关博客：https://www.cnblogs.com/bling123/p/8529521.html

        NetServer.Companion.getInstance("https://api.github.com/").getMerchantInfo("zxc514257857", "AndroidUtilCodeTest")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(merchantInfoBean -> {
                    Log.e("TAG", "getMerchantInfo: success111,,," + merchantInfoBean);
                }, throwable -> {
                    Log.e("TAG", "getMerchantInfo: error111");
                }, () -> {
                    Log.e("TAG", "getMerchantInfo: complete111");
                });
    }
}