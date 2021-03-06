package ru.spbstu.wheels

import kotlinx.warnings.Warnings
import kotlin.js.JsName
import kotlin.jvm.JvmName

abstract class MDMap<K, V>(val inner: MutableMap<K, V> = mutableMapOf()) {
    abstract fun defaultValue(): V

    operator fun get(key: K): V = inner.getOrPut(key, this::defaultValue)
    operator fun set(key: K, value: V) = inner.set(key, value)

    fun remove(key: K) { inner.remove(key) }

    companion object {
        inline fun <K, V> withDefault(crossinline default: () -> V): MDMap<K, V> = object : MDMap<K, V>() {
            override fun defaultValue(): V = default()
        }
    }

    override fun equals(other: Any?): Boolean = other is MDMap<*, *> && inner == other.inner
    override fun hashCode(): Int = inner.hashCode()
    override fun toString(): String = inner.toString()
}

fun <K, V> MDMap<K, V>.toMap(): Map<K, V> = inner.toMap()
fun <K, V> MDMap<K, V>.asFunction(): (K) -> V = ::get

typealias MapToSet<K, VS> = MDMap<K, MutableSet<VS>>

fun <K, VS> MapToSet(): MapToSet<K, VS> = MDMap.withDefault { mutableSetOf<VS>() }

@JvmName("toMapOfSets")
fun <K, VS> MapToSet<K, VS>.toMap(): Map<K, Set<VS>> = inner.mapValues { it.value.toSet() }

typealias MapToList<K, VS> = MDMap<K, MutableList<VS>>

fun <K, VS> MapToList(): MapToList<K, VS> = MDMap.withDefault { mutableListOf<VS>() }

@JvmName("toMapOfLists")
fun <K, VS> MapToList<K, VS>.toMap(): Map<K, List<VS>> = inner.mapValues { it.value.toList() }

typealias MutableMap2D<K1, K2, VS> = MDMap<K1, MutableMap<K2, VS>>

fun <K1, K2, VS> MutableMap2D(): MutableMap2D<K1, K2, VS> = MDMap.withDefault { mutableMapOf<K2, VS>() }

@JvmName("toMapOfMaps")
fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.toMap(): Map<K1, Map<K2, VS>> = inner.toMap()

@Suppress(Warnings.NOTHING_TO_INLINE)
inline operator fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.get(key1: K1, key2: K2): VS? =
        this[key1][key2]

@Suppress(Warnings.NOTHING_TO_INLINE)
inline operator fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.set(key1: K1, key2: K2, value: VS) =
        this[key1].set(key2, value)

@Suppress(Warnings.NOTHING_TO_INLINE)
inline fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.remove(key1: K1, key2: K2) {
    val mLevel1 = this.inner[key1] ?: return
    mLevel1.remove(key2)
    if(mLevel1.isEmpty()) remove(key1)
}

@Suppress(Warnings.NOTHING_TO_INLINE)
inline fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.getOrPut(key1: K1, key2: K2, defaultValue: () -> VS) =
        this[key1].getOrPut(key2, defaultValue)

@Suppress(Warnings.NOTHING_TO_INLINE)
inline operator fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.plusAssign(triple: Triple<K1, K2, VS>) {
    this[triple.first, triple.second] = triple.third
}

@Suppress(Warnings.NOTHING_TO_INLINE)
inline operator fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.plusAssign(triples: Iterable<Triple<K1, K2, VS>>) {
    for(triple in triples) plusAssign(triple)
}

@Suppress(Warnings.NOTHING_TO_INLINE)
inline operator fun <K1, K2, VS> MutableMap2D<K1, K2, VS>.plusAssign(triples: Sequence<Triple<K1, K2, VS>>) {
    for(triple in triples) plusAssign(triple)
}

typealias MutableMap3D<K1, K2, K3, VS> = MDMap<K1, MutableMap2D<K2, K3, VS>>

fun <K1, K2, K3, VS> MutableMap3D(): MutableMap3D<K1, K2, K3, VS> = MDMap.withDefault { MutableMap2D<K2, K3, VS>() }

@JvmName("toMapOfMapsOfMaps")
fun <K1, K2, K3, VS> MutableMap3D<K1, K2, K3, VS>.toMap(): Map<K1, Map<K2, Map<K3, VS>>> =
        inner.mapValues { it.value.toMap() }

@Suppress(Warnings.NOTHING_TO_INLINE)
inline operator fun <K1, K2, K3, VS> MutableMap3D<K1, K2, K3, VS>.get(key1: K1, key2: K2, key3: K3): VS? =
        this[key1][key2][key3]

@Suppress(Warnings.NOTHING_TO_INLINE)
inline operator fun <K1, K2, K3, VS> MutableMap3D<K1, K2, K3, VS>.set(key1: K1, key2: K2, key3: K3, value: VS) =
        this[key1][key2].set(key3, value)

@Suppress(Warnings.NOTHING_TO_INLINE)
inline fun <K1, K2, K3, VS> MutableMap3D<K1, K2, K3, VS>.remove(key1: K1, key2: K2, key3: K3) {
    val mLevel1 = this.inner[key1] ?: return
    val mLevel2 = mLevel1.inner[key2] ?: return
    mLevel2.remove(key3)
    if(mLevel2.isEmpty()) mLevel1.inner.remove(key2)
    if(mLevel1.inner.isEmpty()) this.remove(key1)
}

@Suppress(Warnings.NOTHING_TO_INLINE)
inline fun <K1, K2, K3, VS> MutableMap3D<K1, K2, K3, VS>.getOrPut(key1: K1, key2: K2, key3: K3, defaultValue: () -> VS) =
        this[key1][key2].getOrPut(key3, defaultValue)
