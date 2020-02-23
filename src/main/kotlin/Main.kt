import java.math.BigInteger

fun main() {
    testCustom()
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
    val a1 = BigInteger("123456788123456788123456788")
    val b1 = BigInteger("123456789123456789123456789")
    val c1 = a1 - b1
    println(c1)
    println("BuildIn ${System.currentTimeMillis() - startTime1} ms")
}

fun testCustom() {
    val startTime = System.currentTimeMillis()
    val a = CustomBigInt("-2234789473924920479202")
    val b = CustomBigInt("-3324342578934257893442")
    val c = a - b
    println(c)
    println("Custom ${System.currentTimeMillis() - startTime} ms")
}