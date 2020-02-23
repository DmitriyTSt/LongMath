class CustomBigInt(val str: String) {
    companion object {
        private const val BASE = 10
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
        unsignedStr.reversed().forEach {
            try {
                value.add(it.toString().toInt())
            } catch (e: NumberFormatException) {
                java.lang.NumberFormatException("Skip error char '$it'").printStackTrace()
            }
        }
    }

    operator fun plus(number: CustomBigInt): CustomBigInt {
        if (!this.negative && !number.negative) {
            return sumPositive(this, number)
        } else if (this.negative && number.negative) {
            return sumPositive(this, number, true)
        } else {
            return if (this.negative) {
                number - this
            } else {
                this - number
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
        return minusPositive(this, number)
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
            val c = b - this
            c.negative = true
            return c
        } else {
            return ans.normalized()
        }
    }

    private fun normalized(): CustomBigInt {
        while (value.size > 1 && value[value.lastIndex] == 0) {
            value.removeAt(value.lastIndex)
        }
        return this
    }

    override fun toString(): String {
        return (if (negative) "-" else "") + value.joinToString("").reversed()
    }

}