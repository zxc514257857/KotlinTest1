package com.example.case3.builder

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case3
 * @Author: zhr
 * @Date: 2021/12/19 21:38
 * @Version: V1.0
 */
class CompanyClient {

    class Builder {

        var companyName: String? = null
        var companyAddr: String? = null
        var companyEmployee: String? = null

        fun setCompanyName(companyName: String?): Builder {
            this.companyName = companyName
            return this
        }

        fun setCompanyAddr(companyAddr: String?): Builder {
            this.companyAddr = companyAddr
            return this
        }

        fun setCompanyEmployee(companyEmployee: String?): Builder {
            this.companyEmployee = companyEmployee
            return this
        }

        fun build(): CompanyClient {
            return CompanyClient()
        }

        override fun toString(): String {
            return "Builder(companyName=$companyName, companyAddr=$companyAddr, companyEmployee=$companyEmployee)"
        }
    }
}