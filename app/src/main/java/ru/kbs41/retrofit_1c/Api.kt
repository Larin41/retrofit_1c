package ru.kbs41.retrofit_1c

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import ru.kbs41.retrofit_1c.datamodels.NewProduct
import ru.kbs41.retrofit_1c.datamodels.ProductResponseModel
import ru.kbs41.retrofit_1c.datamodels.ResponseModel

interface Api {

    @GET("hs/v1/test/connection")
    fun testConnection(
        @Header("Authorization") credential: String
    ): Call<Void>

    @GET("hs/v1/product/name/{name}")
    fun getProductBuName(
        @Header("Authorization") credential: String,
        @Path(value = "name", encoded = false) name: String
    ): Call<ProductResponseModel>

    @POST("hs/v1/product/add")
    fun addNewProduct(
        @Header("Authorization") credential: String,
        @Body data: NewProduct
    ): Call<ResponseModel>

}