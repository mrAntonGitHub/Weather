package com.i.o.mob.dev.weather.di.modules

import com.i.o.mob.dev.weather.repository.Repository
import com.i.o.mob.dev.weather.repository.RepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideRepository(repositoryImpl: RepositoryImpl): Repository {
        return repositoryImpl
    }

}