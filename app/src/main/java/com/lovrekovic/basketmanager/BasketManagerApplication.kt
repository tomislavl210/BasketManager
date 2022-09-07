package com.lovrekovic.basketmanager

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import lv.chi.photopicker.ChiliPhotoPicker
import lv.chi.photopicker.loader.ImageLoader
import org.koin.core.context.startKoin

class BasketManagerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(
                listOf(
                    repositoryModule,
                    viewModelModules
                )
            )
        }
        ChiliPhotoPicker.init(
            loader = GlideImageLoader(),
            authority = "com.lovrekovic.basketmanager.fileprovider"
        )
    }
}

internal class GlideImageLoader: ImageLoader {

    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        Glide.with(context)
            .load(uri)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(view)
    }
}