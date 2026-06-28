package com.lzk.lettin.business.main.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lzk.lettin.business.main.data.local.LotteryDatabase
import com.lzk.lettin.business.main.data.remote.LotteryRemoteDataSource
import com.lzk.lettin.business.main.data.remote.MockLotteryRemoteDataSource
import com.lzk.lettin.business.main.data.remote.RealLotteryRemoteDataSource
import com.lzk.lettin.business.main.data.remote.api.LotteryApi
import com.lzk.lettin.business.main.data.repository.LotteryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 数据源的 Hilt 绑定模块。
 * 默认使用 mock 数据以确保首次启动可运行。
 * 要切换到真实 API，请：
 *   1) 把 [USE_REAL_API] 修改为 true
 *   2) 或者修改 [baseUrl] 为实际服务域名/IP
 *   3) 也支持通过外部 BuildConfig 注入 baseUrl（推荐）
 */
@Module
@InstallIn(SingletonComponent::class)
object LotteryDataModule {

    /** true 时走真实网络 API；false 时走本地 assets mock。 */
    private const val USE_REAL_API = false

    /** 默认的 baseUrl。可按需替换。也可改为从 BuildConfig.LOTTERY_BASE_URL 注入。 */
    private const val DEFAULT_BASE_URL = "https://api.example.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().serializeNulls().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideLotteryApi(client: OkHttpClient, gson: Gson): LotteryApi {
        return Retrofit.Builder()
            .baseUrl(DEFAULT_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LotteryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LotteryDatabase =
        LotteryDatabase.get(context)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        @ApplicationContext context: Context,
        api: LotteryApi,
    ): LotteryRemoteDataSource {
        return if (USE_REAL_API) {
            RealLotteryRemoteDataSource(api = api, ioDispatcher = Dispatchers.IO)
        } else {
            MockLotteryRemoteDataSource(context, provideGson())
        }
    }

    @Provides
    @Singleton
    fun provideRepository(
        db: LotteryDatabase,
        remote: LotteryRemoteDataSource,
    ): LotteryRepository =
        LotteryRepository(db.lotteryDrawDao(), remote, db.savedTicketDao())
}
