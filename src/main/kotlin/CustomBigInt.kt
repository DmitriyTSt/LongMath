class CustomBigInt(str: String = "") {
    companion object {
        private const val BASE = 10
        private const val DEFAULT_BASE = 10
        private const val BASE_KOEF = 1
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
        val ans = CustomBigInt()
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
        a.normalize()
        b.normalize()
        ans.normalize()
        return ans
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
        val ans = CustomBigInt()
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
        a.normalize()
        b.normalize()
        if (k == -1) {
            val c = minusPositive(b, a)
            c.negative = true
            return c
        } else {
            ans.normalize()
            return ans
        }
    }

    operator fun times(number: CustomBigInt): CustomBigInt {
        return mulPositive(this, number, this.negative != number.negative)
    }

    private fun mulPositive(a: CustomBigInt, b: CustomBigInt, negative: Boolean = false): CustomBigInt {
        val ans = CustomBigInt()
        repeat(a.value.size + b.value.size + 1) {
            ans.value.add(0)
        }
        repeat(a.value.size) {
            var i = 0
            var k = 0
            do {
                val t = 1L *
                        b.value[i] *
                        a.value[it] +
                        ans.value[i + it] +
                        k
                ans.value[i + it] = (t % BASE.toLong()).toInt()
                k = (t / BASE.toLong()).toInt()
                i++
            } while (i < b.value.size)
            ans.value[it + b.value.size] = k
        }
        ans.negative = negative
        ans.normalize()
        return ans
    }

    operator fun div(number: CustomBigInt): CustomBigInt {
        return if (number.value.size == 1) {
            divModShort(number.value.first(), this.negative != number.negative)
        } else {
            divModPositive(this, number, this.negative != number.negative)
        }
    }

    operator fun rem(number: CustomBigInt): CustomBigInt {
        return if (number.value.size == 1) {
            divModShort(number.value.first(), this.negative != number.negative, false)
        } else {
            divModPositive(this, number, this.negative != number.negative, false)
        }
    }

    private fun divModPositive(a: CustomBigInt, b: CustomBigInt, negative: Boolean = false, isDiv: Boolean = true): CustomBigInt {
        if (b.value.size == 1) {
            throw Exception("not implemented div by n == 1")
        }
        val d = BASE / (b.value.last() + 1)
        var u = a
        var v = b
        val n = v.value.size
        val m = u.value.size - v.value.size
        u *= CustomBigInt(d.toString())
        v *= CustomBigInt(d.toString())
        if (u.value.size == a.value.size) {
            u.value.add(0)
        }

        val ans = CustomBigInt()
        repeat(m + 1) {
            ans.value.add(0)
        }
        var j = m
        do {
            var q = (u.value[j + n] * BASE + u.value[j + n - 1]) / v.value.last()
            var r = (u.value[j + n] * BASE + u.value[j + n - 1]) % v.value.last()

            while ((q == BASE || q * v.value[n - 2] > BASE * r + u.value[j + n - 2]) && r < BASE) {
                q--
                r += v.value[n - 1]
            }
            val temp = CustomBigInt(q.toString()) * v
            while (temp.value.size < n + 1) {
                temp.value.add(0)
            }
            var flagNegative = false
            var tempBig = CustomBigInt()
            tempBig.value.clear()
            repeat(n + 1) {
                tempBig.value.add(u.value[it + j])
            }
            tempBig -= temp
            flagNegative = tempBig.negative == true
            if (flagNegative) {
                // u[j+n..j] += b^(n-1)
                // возвращаем обратно
                repeat(n + 1) {
                    u.value[it + j] +=
                            temp.value[it]
                }
                val tempBig = CustomBigInt()
                tempBig.value.clear()
                repeat(n + 1) {
                    tempBig.value.add(u.value[it + j])
                }
                tempBig.value.add(1)
                // tempBig is part u + b^(n+1)
                val res = tempBig - temp
                repeat(n + 1) {
                    u.value[it + j] = res.value[it]
                }
            } else {
                while (tempBig.value.size < n + 1) {
                    tempBig.value.add(0)
                }
                repeat(n + 1) {
                    u.value[it + j] = tempBig.value[it]
                }
            }
            ans.value[j] = q
            if (flagNegative) {
                // if flag negative D6
                ans.value[j]--
                var tempBig = CustomBigInt()
                tempBig.value.clear()
                repeat(n + 1) {
                    tempBig.value.add(u.value[j + it])
                }
                tempBig += v
//                tempBig.value.removeAt(tempBig.value.lastIndex)
                repeat(n + 1) {
                    u.value[j + it] = tempBig.value[it]
                }
            }
            j--
        } while (j >= 0)
        ans.negative = negative
        ans.normalize()
        return if (isDiv) {
            ans
        } else {
            val tempBig = CustomBigInt()
            tempBig.value.clear()
            repeat(n) {
                tempBig.value.add(u.value[it])
            }
            tempBig / CustomBigInt(d.toString())
        }
    }

    private fun divModShort(number: Int, negative: Boolean, isDiv: Boolean = true): CustomBigInt {
        val ans = CustomBigInt()
        repeat(this.value.size) {
            ans.value.add(0)
        }
        var k = 0
        repeat(this.value.size) {
            val i = this.value.size - 1 - it
            val cur = 1L * this.value[i] + k * BASE
            ans.value[i] = (cur / number).toInt()
            k = (cur % number).toInt()
        }
        ans.normalize()
        ans.negative = negative
        return if (isDiv) {
            ans
        } else {
            CustomBigInt(k.toString())
        }
    }

    private fun normalize() {
        while (value.size > 1 && value[value.lastIndex] == 0) {
            value.removeAt(value.lastIndex)
        }
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