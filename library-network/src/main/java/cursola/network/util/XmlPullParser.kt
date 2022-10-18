package cursola.network.util

import org.xmlpull.v1.XmlPullParser

internal inline fun XmlPullParser.tag(
    name: String,
    namespace: String? = null,
    body: XmlPullParser.() -> Unit
) {
    require(XmlPullParser.START_TAG, namespace, name)
    while (next() != XmlPullParser.END_TAG) {
        if (eventType != XmlPullParser.START_TAG) {
            continue
        }
        body()
    }
}

internal fun XmlPullParser.skipEntry() {
    if (eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when (next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth++
        }
    }
}

internal fun XmlPullParser.skipUntilTag(
    name: String,
    namespace: String? = null,
    body: XmlPullParser.() -> Unit
) {
    if (this.name == name) tag(name, namespace, body)
    else skipEntry()
}

internal fun XmlPullParser.optionalTag(
    name: String,
    namespace: String? = null,
    body: XmlPullParser.() -> Unit
) {
    if (this.name == name) tag(name, namespace, body)
}