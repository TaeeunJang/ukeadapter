package com.cooper.center.ukeadapter

import java.util.HashMap

class UkePipe internal constructor() {

    private val map: HashMap<String, Any> = HashMap()
    private var connectedPipe: UkePipe? = null
    private var name: String? = null

    fun connect(pipe: UkePipe) {
        this.connectedPipe = pipe
    }

    fun set(key: String, obj: Any) {
        map[key] = obj
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, clz: Class<T>): T? {
        val obj = map[key]
        return if (obj == null || !clz.isAssignableFrom(obj.javaClass)) {
            if (connectedPipe != null)
                connectedPipe!!.get(key, clz)
            else
                null
        } else obj as T?
    }

    fun name(name: String) {
        this.name = name
    }

    fun name(): String? {
        return name
    }

    fun path(delim: String = "/"): String {
        var name = this.name
        if (name == null) {
            name = ""
        }

        return if (connectedPipe != null)
            connectedPipe!!.path(delim) + delim + name
        else
            name
    }

}
