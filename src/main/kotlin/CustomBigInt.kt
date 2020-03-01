class CustomBigInt(str: String) {
    companion object {
        private const val BASE = 1000000000
        private const val DEFAULT_BASE = 10
        private const val BASE_KOEF = 9
    }

    private val value: ArrayList<Int> = ArrayList()
    private var negative = false

    init {
        val unsignedStr = if (str.firstOrNull() == '-') {
            negative = true
            str.substring(1)
        } else {
            str
        }
        val buf = StringBuilder()
        unsignedStr.reversed().forEach {
            try {
                buf.append(it.toString())
                if (buf.length == BASE_KOEF) {
                    value.add(buf.toString().reversed().toInt())
                    buf.setLength(0)
                }
            } catch (e: NumberFormatException) {
                java.lang.NumberFormatException("Skip error char '$it'").printStackTrace()
            }
        }
        if (buf.isNotEmpty()) {
            value.add(buf.toString().reversed().toInt())
        }
    }

    operator fun plus(number: CustomBigInt): CustomBigInt {
        return if (!this.negative && !number.negative) {
            sumPositive(this, number)
        } else if (this.negative && number.negative) {
            sumPositive(this, number, true)
        } else {
            if (this.negative) {
                minusPositive(number, this)
            } else {
                minusPositive(this, number)
            }
        }
    }

    private fun sumPositive(a: CustomBigInt, b: CustomBigInt, negative: Boolean = false): CustomBigInt {
        val ans = CustomBigInt("")
        if (a.value.size < b.value.size) {
            repeat(b.value.size - a.value.size) {
                a.value.add(0)
            }
        } else {
            repeat(a.value.size - b.value.size) {
                b.value.add(0)
            }
        }
        var k = 0
        repeat(a.value.size) {
            ans.value.add((a.value[it] + b.value[it] + k) % BASE)
            k = (a.value[it] + b.value[it] + k) / BASE
        }
        ans.value.add(k)
        ans.negative = negative
        return ans.normalized()
    }

    operator fun minus(number: CustomBigInt): CustomBigInt {
        return if (!this.negative && !number.negative) {
            minusPositive(this, number)
        } else if (this.negative && number.negative) {
            minusPositive(number, this)
        } else {
            if (this.negative) {
                sumPositive(this, number, true)
            } else {
                sumPositive(this, number)
            }
        }
    }

    private fun minusPositive(a: CustomBigInt, b: CustomBigInt): CustomBigInt {
        val ans = CustomBigInt("")
        if (a.value.size < b.value.size) {
            repeat(b.value.size - a.value.size) {
                a.value.add(0)
            }
        } else {
            repeat(a.value.size - b.value.size) {
                b.value.add(0)
            }
        }
        var k = 0
        repeat(a.value.size) {
            ans.value.add(a.value[it] - b.value[it] + k)
            if (ans.value.last() < 0) {
                ans.value[ans.value.lastIndex] += BASE
                k = -1
            } else {
                k = 0
            }
        }
        if (k == -1) {
            val c = minusPositive(b, a)
            c.negative = true
            return c
        } else {
            return ans.normalized()
        }
    }

    operator fun times(number: CustomBigInt): CustomBigInt {
        return mulPositive(this, number, this.negative != number.negative)
    }

    private fun mulPositive(a: CustomBigInt, b: CustomBigInt, negative: Boolean = false): CustomBigInt {
        val ans = CustomBigInt("")
        repeat(a.value.size + b.value.size) {
            ans.value.add(0)
        }
        repeat(a.value.size) {
            var i = 0
            var k = 0
            do {
                val t = 1L * a.value[i] * b.value[it] + ans.value[i + it] + k
                ans.value[i + it] = (t % BASE.toLong()).toInt()
                k = (t / BASE.toLong()).toInt()
                i++
            } while (i < b.value.size)
            ans.value[it + b.value.size] = k
        }
        return ans.normalized()
    }

    private fun normalized(): CustomBigInt {
        while (value.size > 1 && value[value.lastIndex] == 0) {
            value.removeAt(value.lastIndex)
        }
        return this
    }

    override fun toString(): String {
        val notNormalized = (if (negative) "-" else "") +
                value.joinToString("") { "%0${BASE_KOEF}d".format(it).reversed() }.reversed()
        val normalized = StringBuilder(notNormalized)
        var i = 0
        if (normalized[i] == '-') {
            i++
        }
        while (normalized.length > 1 && normalized[i] == '0') {
            normalized.deleteCharAt(i)
        }
        return String(normalized)
    }

}