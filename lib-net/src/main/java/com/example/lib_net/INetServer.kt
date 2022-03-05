package com.example.lib_net

import com.example.lib_pojo.respondBean.MerchantInfoBean
import io.reactivex.Observable
import retrofit2.http.*

interface INetServer {

    @GET("/repos/{owner}/{repo}/contributors")
    fun getMerchantInfo(@Path("owner") owner: String, @Path("repo") repo: String): Observable<List<MerchantInfoBean>>

}