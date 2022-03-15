package artem.surodeev.pine.Main

import android.util.Log
import artem.surodeev.pine.Image
import artem.surodeev.pine.SearchImage
import artem.surodeev.pine.UnsplashConnectionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainModel {
    private const val clientId = "eF_bLVFDCslThuj0adnqM7p2ClhsowFB4KrVxFSO4oA"

    private val retrofit = Retrofit.Builder().baseUrl("https://api.unsplash.com").addConverterFactory(
        GsonConverterFactory.create()).build()
    private val unsplashConnectionService = retrofit.create(UnsplashConnectionService::class.java)

    private var pageNumber = 1
    private var controller: MainController? = null

    fun setController(controller: MainController) {
        MainModel.controller = controller
    }

    fun getImages(isFirstPage: Boolean) {
        pageNumber = if (isFirstPage) 1 else ++pageNumber
        unsplashConnectionService.getImages(clientId, pageNumber).enqueue(object : Callback<ArrayList<Image>> {
            override fun onResponse(call: Call<ArrayList<Image>>, response: Response<ArrayList<Image>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        controller?.updateImagesList(body)
                    } else {
                        controller?.showError()
                    }
                } else {
                    controller?.showError()
                }
            }

            override fun onFailure(call: Call<ArrayList<Image>>, t: Throwable) {
                controller?.showError()
            }
        })
    }

    fun getImagesByKeyword(keyword: String, isFirstPage: Boolean) {
        pageNumber = if (isFirstPage) 1 else ++pageNumber
        if (keyword.isEmpty()) {
            getImages(isFirstPage)
        } else {
            unsplashConnectionService.getImagesByKeyword(keyword, clientId, pageNumber).enqueue(object : Callback<SearchImage> {
                override fun onResponse(call: Call<SearchImage>, response: Response<SearchImage>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && response.isSuccessful) {
                            controller?.updateImagesList(body.results)
                        } else {
                            controller?.showError()
                        }
                    } else {
                        controller?.showError()
                    }
                }

                override fun onFailure(call: Call<SearchImage>, t: Throwable) {
                    controller?.showError()
                }
            })
        }
    }

    fun openFullscreenImage(image: Image) {
        controller?.openFullScreenImage(image)
    }

    fun error() {
        controller?.showError()
    }
}