package com.chuckerteam.chucker.internal.support

import android.content.Context
import com.chuckerteam.chucker.internal.data.entity.HttpHeader
import com.chuckerteam.chucker.internal.data.entity.HttpTransaction
import okio.Buffer
import okio.Source

internal class TransactionCurlCommandSharable(
    private val transaction: HttpTransaction,
) : Sharable {
    override fun toSharableContent(context: Context): Source =
        Buffer().apply {
            var compressed = false
            writeUtf8("curl -X ${transaction.method}")
            val headers = transaction.getParsedRequestHeaders()

            headers?.forEach { header ->
                if (isCompressed(header)) {
                    compressed = true
                }
                val headerValue = escapeHeaderValue(header.value)
                writeUtf8(" -H \"${header.name}: ${headerValue}\"")
            }

            val requestBody = transaction.requestBody
            if (!requestBody.isNullOrEmpty()) {
                // try to keep to a single line and use a subshell to preserve any line breaks
                writeUtf8(" --data $'${requestBody.replace("\n", "\\n")}'")
            }
            writeUtf8((if (compressed) " --compressed " else " ") + transaction.getFormattedUrl(encode = false))
        }

    private fun isCompressed(header: HttpHeader): Boolean =
        (
            "Accept-Encoding".equals(header.name, ignoreCase = true) &&
                "gzip".contains(header.value, ignoreCase = true) ||
                "br".contains(header.value, ignoreCase = true)
        )

    private fun escapeHeaderValue(value: String): String {
        // escape double quotes from header value to prevent getting an invalid curl
        return value.replace("\"", "\\\"")
    }
}
