@file: Suppress(Warnings.NOTHING_TO_INLINE)

package ru.spbstu.wheels

import kotlinx.warnings.Warnings

/*
* Essentially, your good ol' array as you know it, but T is not reified.
* Is needed to avoid dealing with Array<Any?> when implementing array-based data structures.
* */
@Suppress(Warnings.UNCHECKED_CAST, Warnings.NOTHING_TO_INLINE)
inline class TArray<T>
@Deprecated(message = "Do not use")
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
@PublishedApi
internal constructor(@PublishedApi internal val inner: Array<Any?>) {

    @Suppress("DEPRECATION")
    constructor(size: Int) : this(arrayOfNulls(size))

//    @Suppress("DEPRECATION")
//    constructor(size: Int, init: (Int) -> T) : this(Array<Any?>(size, init))

    inline operator fun get(index: Int): T? = inner[index] as T?
    inline operator fun set(index: Int, value: T?) {
        inner[index] = value
    }

    inline val size: Int get() = inner.size

    inline operator fun iterator(): Iterator<T?> =
            iterator {
                for (i in 0 until size) yield(get(i))
            }

    override fun toString(): String {
        if(size == 0) return "[]"
        val sb = StringBuilder("[")
        sb.append(get(0))
        for(i in 1 until size) sb.append(", ").append(get(i))
        sb.append("]")
        return "$sb"
    }
}

inline fun <T> TArray(size: Int, init: (Int) -> T): TArray<T> {
    val res = TArray<T>(size)
    for(i in 0 until size) res[i] = init(i)
    return res
}

@Suppress(Warnings.DEPRECATION, Warnings.UNCHECKED_CAST)
fun <T> tarrayOf(vararg values: T): TArray<T> = TArray(values.size) { values[it] }

@Suppress(Warnings.UNCHECKED_CAST, Warnings.NOTHING_TO_INLINE)
inline fun <T> TArray<out T>.asList(): List<T?> = inner.asList() as List<T?>

inline fun <T> TArray<out T>.copyInto(destination: TArray<T>,
                                      destinationOffset: Int = 0,
                                      startIndex: Int = 0,
                                      endIndex: Int = size) {
    inner.copyInto(destination.inner, destinationOffset, startIndex, endIndex)
}

@Suppress(Warnings.DEPRECATION)
inline fun <T> TArray<out T>.copyOf(): TArray<T> = TArray(inner.copyOf())

@Suppress(Warnings.DEPRECATION)
inline fun <T> TArray<out T>.copyOf(newSize: Int): TArray<T> = TArray(inner.copyOf(newSize))

@Suppress(Warnings.DEPRECATION)
inline fun <T> TArray<out T>.copyOfRange(fromIndex: Int, toIndex: Int): TArray<T> =
        TArray(inner.copyOfRange(fromIndex, toIndex))

inline fun <T> TArray<T>.fill(element: T, fromIndex: Int = 0, toIndex: Int = size) {
    inner.fill(element, fromIndex, toIndex)
}

inline fun <T : Comparable<T>> TArray<T>.sort() {
    inner.sort()
}

inline fun <T> TArray<T>.sortWith(cmp: Comparator<T?>) {
    @Suppress(Warnings.UNCHECKED_CAST)
    inner.sortWith(Comparator { a, b -> cmp.compare(a as T?, b as T?) })
}

fun <T> Collection<T>.toTArray(): TArray<T> {
    val res = TArray<T>(size)
    for ((i, e) in this.withIndex()) res[i] = e
    return res
}

inline fun <reified T> TArray<out T>.toTypedArray(): Array<T?> = Array(size) { get(it) }

fun <T> TArray<out T>.withIndex(): Iterable<IndexedValue<T?>> = Iterable {
    iterator {
        for (i in 0 until size) yield(IndexedValue(i, get(i)))
    }
}

inline fun <A, B> TArray<out A>.map(body: (A?) -> B?): TArray<B> {
    val res = TArray<B>(size)
    for (i in 0 until size) res[i] = body(this[i])
    return res
}

fun <T> TArray<out T>.asIterable(): Iterable<T?> = Iterable { iterator() }
fun <T> TArray<out T>.asSequence(): Sequence<T?> = Sequence { iterator() }