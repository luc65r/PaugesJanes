package fr.paugesjanes

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse

inline fun htmx(buildHtmxResponse: HtmxResponse.Builder.() -> Unit): HtmxResponse {
    val builder = HtmxResponse.Builder()
    builder.buildHtmxResponse()
    return builder.build()
}