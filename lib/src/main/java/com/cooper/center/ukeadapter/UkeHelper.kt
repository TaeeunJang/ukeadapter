package com.cooper.center.ukeadapter

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

object UkeHelper {

    @Throws(NotFoundMethodException::class)
    fun getBindingMethod(binderClass: Class<*>, objectClass: Class<*>): Method {
        var resultMethod: Method? = null
        for (method in binderClass.declaredMethods) {
            val parametersClasses = method.parameterTypes
            if (parametersClasses.size == 1
                && parametersClasses[0].isAssignableFrom(objectClass)) {
                resultMethod = method
                break
            }
        }
        return resultMethod ?: throw NotFoundMethodException()
    }

    @Throws(NotFoundMethodException::class, IllegalAccessException::class, InvocationTargetException::class)
    fun bind(method: Method?, binder: Any, obj: Any): Method {
        val bindingMethod = method ?: getBindingMethod(binder.javaClass, obj.javaClass)
        bindingMethod.invoke(binder, obj)
        return bindingMethod
    }

}
