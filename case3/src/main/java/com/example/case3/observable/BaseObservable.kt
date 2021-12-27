package com.example.case3.observable

import com.example.case3.loger.LoggerUtils
import java.util.*
import kotlin.reflect.full.allSuperclasses

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case3
 * @Author: zhr
 * @Date: 2021/12/16 18:05
 * @Version: V1.0
 */
class BaseObservable<T>: Observable() {

    private fun doNotify(t: T){
        setChanged()
        notifyObservers(t)
    }

    fun updateData(t: T){
        try {
            doNotify(t)
        } catch (e: Exception) {
            e.printStackTrace()
            LoggerUtils.getInstance().error(this.javaClass.name, "notify failed error = ${e.message}")
        }
    }

    override fun notifyObservers(arg: Any?) {
        var arrLocal: Array<Any>
        synchronized(this) {
            if (!hasChanged()) return
            arrLocal = getObservers().toTypedArray()
            clearChanged()
        }
        for (i in arrLocal.indices.reversed()){
            try {
                (arrLocal[i] as Observer).update(this, arg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getObservers() : ArrayList<Observer>{
        var observers = ArrayList<Observer>()
        try {
            val superclass = this::class.allSuperclasses

            if(superclass.isNotEmpty()){
                superclass.forEach { c ->
                    val name = c.qualifiedName ?: ""
                    if(name.contains("java.util.Observable")){
                        /** 此处特别注意 本地java源码中的是Vector<Observer> obs, 设备中的 是ArrayList<Observer> observers*/
                        val superFiled = c.java.getDeclaredField("observers")//.getDeclaredField("obs")
                        superFiled.isAccessible = true
                        observers = superFiled.get(this) as ArrayList<Observer>
                    }else{
//                        LogUtils.debug("observer filed size = ${c.simpleName}")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return observers
    }

    fun getObserverSize() = getObservers().size

    fun containObserver(observer: Observer) : Boolean{
        val obsList = getObservers()
        return obsList.contains(observer)
    }
}