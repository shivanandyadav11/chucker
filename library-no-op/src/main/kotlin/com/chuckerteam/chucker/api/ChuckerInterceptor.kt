package com.chuckerteam.chucker.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * No-op implementation.
 */
@Suppress("UnusedPrivateMember", "UNUSED_PARAMETER")
public class ChuckerInterceptor private constructor(
    builder: Builder,
) : Interceptor {
    /**
     * No-op implementation.
     */
    public constructor(context: Context) : this(Builder(context))

    public fun redactHeaders(vararg names: String): ChuckerInterceptor = this

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }

    /**
     * No-op implementation.
     */
    public class Builder(
        private val context: Context,
    ) {
        public fun collector(collector: ChuckerCollector): Builder = this

        public fun maxContentLength(length: Long): Builder = this

        public fun redactHeaders(headerNames: Iterable<String>): Builder = this

        public fun redactHeaders(vararg headerNames: String): Builder = this

        public fun alwaysReadResponseBody(enable: Boolean): Builder = this

        public fun addBodyDecoder(decoder: Any): Builder = this

        public fun createShortcut(enable: Boolean): Builder = this

        public fun skipPaths(vararg paths: String): Builder = this

        public fun skipPaths(paths: Regex): Builder = this

        public fun skipDomains(vararg domains: String): Builder = this

        public fun skipDomains(domains: Regex): Builder = this

        public fun build(): ChuckerInterceptor = ChuckerInterceptor(this)
    }
}
