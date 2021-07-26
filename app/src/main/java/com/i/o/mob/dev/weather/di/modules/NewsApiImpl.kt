package com.i.o.mob.dev.weather.di.modules

import com.i.o.mob.dev.weather.api.NewsApi
import com.i.o.mob.dev.weather.data.news.News
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Deferred
import javax.inject.Inject

@Module
class NewsApiImpl @Inject constructor() : NewsApi {

    @Provides
    override fun getNews(country: String): Deferred<News> {
        return NewsApi().getNews(country)
    }

}