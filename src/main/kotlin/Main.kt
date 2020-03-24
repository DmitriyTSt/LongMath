import java.math.BigInteger

// variant 1

fun main() {
    // для проверки результатов раскомментируйте consoleRun()
    /**
     *
     * Внимание при проверке степени! Readme: Конец функции consoleRun
     *
     */
    consoleRun()
    // для проверки времени выполнения раскомментируйте timeTest()
//    timeTest()
}

fun consoleRun() {
    println("Введите чисто a")

    var str = readLine() ?: ""
    while (!str.contains(Regex("^[-]?[0-9]+$"))) {
        println("Incorrect big number")
        str = readLine() ?: ""
    }
    val a = CustomBigInt(str)
    val aBuildIn = BigInteger(str)
    println("Введите чисто b")
    str = readLine() ?: ""
    while (!str.contains(Regex("^[-]?[0-9]+$"))) {
        println("Incorrect big number")
        str = readLine() ?: ""
    }
    val b = CustomBigInt(str)
    val bBuildIn = BigInteger(str)

    /**
     * Далее идут блоки для проверки действий, если какое-то не нужно - закомментируйте
     */

    // сложение
    val sumTrue = (a + b).toString() == (aBuildIn + bBuildIn).toString()
    println("a + b = ${a + b} check with buildIn: $sumTrue")
    if (!sumTrue) {
        println("${a + b} vs ${aBuildIn + bBuildIn}")
    }
    // вычитание
    val minusTrue = (a - b).toString() == (aBuildIn - bBuildIn).toString()
    println("a - b = ${a - b} check with buildIn: $minusTrue")
    if (!minusTrue) {
        println("${a - b} vs ${aBuildIn - bBuildIn}")
    }
    // умножение
    val mulTrue = (a * b).toString() == (aBuildIn * bBuildIn).toString()
    println("a * b = ${a * b} check with buildIn: $mulTrue")
    if (!mulTrue) {
        println("${a * b} vs ${aBuildIn * bBuildIn}")
    }
    // деление
    val divTrue = (a / b).toString() == (aBuildIn / bBuildIn).toString()
    println("a / b = ${a / b} check with buildIn: $divTrue")
    if (!divTrue) {
        println("${a / b} vs ${aBuildIn / bBuildIn}")
    }
    // остаток
    val modTrue = (a % b).toString() == (aBuildIn % bBuildIn).toString()
    println("a % b = ${a % b} check with buildIn: $modTrue")
    if (!modTrue) {
        println("${a % b} vs ${aBuildIn % bBuildIn}")
    }
    // степень с проверкой со встроенными большими числами
    /**
     * Стоит отметить что встроенная длинная арифметика проверяется только тогда, когда второе число короткое
     * Для запуска подсчета возведения в длинную степень, используйте закомментированный код ниже,
     * а этот наоборот закомментируйте
     */
    val powerTrue = (a.pow(b)).toString() == (aBuildIn.pow(bBuildIn.toInt())).toString()
    println("a ^ b = ${a.pow(b)} check with buildIn: $powerTrue")
    if (!powerTrue) {
        println("${a.pow(b)} vs ${aBuildIn.pow(bBuildIn.toInt())}")
    }
    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * Степень без проверки
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    println("a ^ b = ${a.pow(b)}")
}

fun timeTest() {
    testBuildIn()
    testCustom()
}

fun testBuildIn() {
    val startTime1 = System.currentTimeMillis()
    val a1 = BigInteger("1234567890")
    val b1 = BigInteger("123")
    val c1 = a1.pow(b1.toInt())
    println(c1)
    println("BuildIn ${System.currentTimeMillis() - startTime1} ms")
}

fun testCustom() {
    val startTime = System.currentTimeMillis()
    val a = CustomBigInt("1234567890")
    val b = CustomBigInt("123")
    val c = a.pow(b)
    println(c)
    println("Custom ${System.currentTimeMillis() - startTime} ms")
}