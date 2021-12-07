class Day7 extends DayCommon {
    @Override
    def doPart1() {
        def input = this.readInput().split(",").collect { it.toInteger() }
        def alignmentPositions = (input.min()..input.max()).collectEntries { alignPos ->
            [alignPos, input.collect { Math.abs(it - alignPos) }.sum()]
        }

        def result = alignmentPositions.min { it.value }.value
        println "How much fuel must they spend to align to that position? $result"
    }

    @Override
    def doPart2() {
        def input = this.readInput().split(",").collect { it.toInteger() }
        def alignmentPositions = (input.min()..input.max()).collectEntries { alignPos ->
            [alignPos, input.collect {
                def n = Math.abs(it - alignPos)
                return n * (n + 1) / 2
            }.sum()]
        }

        def result = alignmentPositions.min { it.value }.value
        println "How much fuel must they spend to align to that position? $result"
    }
}
