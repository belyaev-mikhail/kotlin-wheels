@file: Suppress(Warnings.NOTHING_TO_INLINE)
package ru.spbstu.wheels

import kotlinx.warnings.Warnings

// == values.toSet().hashCode()
fun <T> setHashCode(values: Iterable<T>): Int = values.fold(0) { acc, c -> acc + c.hashCode() }

// == values.toList().hashCode()
fun <T> orderedHashCode(values: Iterable<T>): Int = values.fold(1) { acc, c -> 31 * acc + c.hashCode() }

inline fun <A, B> hashCombine(a: A, b: B): Int {
    var result = a.hashCode()
    result = 31 * result + b.hashCode()
    return result
}
inline fun <A, B, C> hashCombine(a: A, b: B, c: C): Int {
    var result = a.hashCode()
    result = 31 * result + b.hashCode()
    result = 31 * result + c.hashCode()
    return result
}
inline fun <A, B, C, D> hashCombine(a: A, b: B, c: C, d: D): Int {
    var result = a.hashCode()
    result = 31 * result + b.hashCode()
    result = 31 * result + c.hashCode()
    result = 31 * result + d.hashCode()
    return result
}
inline fun <A, B, C, D, E> hashCombine(a: A, b: B, c: C, d: D, e: E): Int {
    var result = a.hashCode()
    result = 31 * result + b.hashCode()
    result = 31 * result + c.hashCode()
    result = 31 * result + d.hashCode()
    result = 31 * result + e.hashCode()
    return result
}
inline fun <T> hashCombine(vararg t: T): Int {
    if (t.isEmpty()) return 0
    var result = t[0].hashCode()
    for(i in 1..t.lastIndex) result = 31 * result + t[i].hashCode()
    return result
}
