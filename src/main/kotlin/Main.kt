import java.math.BigInteger

// variant 1

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
    val sumTrue = (a + b).toString() == (aBuildIn + bBuildIn).toString()
    println("a + b = ${a + b} check with buildIn: $sumTrue")
    if (!sumTrue) {
        println("${a + b} vs ${aBuildIn + bBuildIn}")
    }
    val minusTrue = (a - b).toString() == (aBuildIn - bBuildIn).toString()
    println("a - b = ${a - b} check with buildIn: $minusTrue")
    if (!minusTrue) {
        println("${a - b} vs ${aBuildIn - bBuildIn}")
    }
    // 2376493264986492376498237649362927369234 = a
    // 3465932654986923376298467239864729364792384 = b
    // 34635561617219368839219692215366437423150
    // 3463556161721936883921969002215366437423150
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