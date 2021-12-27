package com.example.case3.builder

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case3
 * @Author: zhr
 * @Date: 2021/12/16 16:14
 * @Version: V1.0
 */
/**
 * Builder 建造者模式
 */
class Person {

    /**
     * 静态内部类 Builder类  包含所有Person属性。
     */
    class Builder {

        private var name: String? = null
        private var sex: String? = null
        private var age = 30
        private var phone = "18888888888"

        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        fun sex(sex: String?): Builder {
            this.sex = sex
            return this
        }

        fun build(): Person {
            return Person()
        }

        override fun toString(): String {
            return "Builder(name=$name, sex=$sex, age=$age, phone='$phone')"
        }
    }
}