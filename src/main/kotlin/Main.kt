import java.math.BigInteger

fun main() {
    consoleRun()
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
    println("a + b = ${a + b} check with buildIn: ${(a + b).toString() == (aBuildIn + bBuildIn).toString()}")
    println("a - b = ${a - b} check with buildIn: ${(a - b).toString() == (aBuildIn - bBuildIn).toString()}")
}

fun timeTest() {
    testBuildIn()
    testCustom()
    testBuildIn()
    testCustom()
    testBuildIn()
    testBuildIn()
    testBuildIn()
    testBuildIn()
    testCustom()
    testCustom()
    testCustom()
}

fun testBuildIn() {
    val startTime1 = System.currentTimeMillis()
    val a1 = BigInteger("123456788123456788123456788123456788123456788123456788123456788123456788123456788123456788123456788123456788")
    val b1 = BigInteger("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789")
    val c1 = a1 + b1
    println(c1)
    println("BuildIn ${System.currentTimeMillis() - startTime1} ms")
}

fun testCustom() {
    val startTime = System.currentTimeMillis()
    val a = CustomBigInt("123456788123456788123456788123456788123456788123456788123456788123456788123456788123456788123456788123456788")
    val b = CustomBigInt("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789")
    val c = a + b
    println(c)
    println("Custom ${System.currentTimeMillis() - startTime} ms")
}