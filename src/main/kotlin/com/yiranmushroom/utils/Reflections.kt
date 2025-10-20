package com.yiranmushroom.utils

import java.lang.reflect.Field

inline fun <reified C, reified T> GetAllStaticMembersFromClassDerivedFrom(): List<T> {
    val targetClass = C::class.java

    val staticFields = C::class.java.declaredFields.filter {
        java.lang.reflect.Modifier.isStatic(it.modifiers) && T::class.java.isAssignableFrom(it.type)
    }.mapNotNull {
        it.isAccessible = true
        try {
            it.get(null) as T
        } catch (e: Exception) {
            null
        }
    }

    var companionInstance: Field? = null

    try {
        companionInstance = targetClass.getDeclaredField("Companion")
    } catch (e: NoSuchFieldException) {

    }

    if (companionInstance == null) {
        return staticFields
    }

    val singletonFields = C::class.java.declaredFields.filter {
        T::class.java.isAssignableFrom(it.type)
    }.mapNotNull {
        try {
            it.get(companionInstance) as T
        } catch (e: Exception) {
            null
        }
    }

    // remove duplicates
    val uniqueSet = HashSet<Any>()
    (staticFields + singletonFields).forEach {
        uniqueSet += it as Any
    }
    return uniqueSet.map { it as T }

}
