class Day1 extends DayCommon {

    @Override
    def doPart1() {
        def numbers = readInput().collect { it.toInteger() }
        def increments = numbers.collate(2, 1, false).findAll { it.get(1) > it.get(0) }.size()
        println "How many measurements are larger than the previous measurement? $increments"
    }

    @Override
    def doPart2() {
        def numbers = readInput().collect { it.toInteger() }
        def slidingWindow = numbers.collate(3, 1, false)
        def windowSums = slidingWindow.collect { it.sum() }
        def increments = windowSums.collate(2, 1, false).findAll { it.get(1) > it.get(0) }.size()
        println "How many sums are larger than the previous sum? $increments"
    }

}
