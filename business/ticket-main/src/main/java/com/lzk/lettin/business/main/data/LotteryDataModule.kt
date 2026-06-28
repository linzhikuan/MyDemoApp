package com.lzk.lettin.business.main.data

import android.content.Context
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
 *
 * 当前接入：huiniao.top（完全免费、无需注册）
 *   - baseUrl：【LOTTERY_BASE_URL】
 *   - 历史接口：/interface/home/lotteryHistory?type=ssq|dlt&limit=30
 *
 * 若以后需要切换到其他服务商（如聚合数据、极速数据、istero 等），
 * 只需修改 LOTTERY_BASE_URL + LotteryApiModels 的 @SerializedName 字段即可，
 * 业务层 / ViewModel / UI 都不需要改动。
 */
@Module
@InstallIn(SingletonComponent::class)
object LotteryDataModule {
    // 填真实彩票开奖 API 的 baseUrl。
    // 当前使用 huiniao.top 的免费 HTTPS 线路。
    private const val LOTTERY_BASE_URL = "https://api.huiniao.top/"

    // 是否启用真实 API。baseUrl 非空即启用。
    private fun shouldUseRealApi(): Boolean = LOTTERY_BASE_URL.isNotEmpty()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        return OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideLotteryApi(client: OkHttpClient): LotteryApi =
        Retrofit
            .Builder()
            .baseUrl(LOTTERY_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LotteryApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): LotteryDatabase = LotteryDatabase.get(context)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        @ApplicationContext context: Context,
        api: LotteryApi,
    ): LotteryRemoteDataSource =
        if (shouldUseRealApi()) {
            RealLotteryRemoteDataSource(api = api, ioDispatcher = Dispatchers.IO)
        } else {
            // 兼容 fallback：如果 baseUrl 留空，走本地 assets mock 数据
            MockLotteryRemoteDataSource(context, com.google.gson.Gson())
        }

    @Provides
    @Singleton
    fun provideRepository(
        db: LotteryDatabase,
        remote: LotteryRemoteDataSource,
    ): LotteryRepository = LotteryRepository(db.lotteryDrawDao(), remote, db.savedTicketDao())
}
