class Day6 extends DayCommon {
    def readFish() {
        long[] fish = new long[9]

        this.readInput().split(",").collect { it.toInteger() }.each {
            fish[it]++
        }

        return fish
    }

    def evaluateDays(def numDays) {
        def fish = this.readFish()

        for (def days in 0..<numDays) {
            def newFish = fish[0]
            fish[0] = 0

            for (i in 1..<fish.length) {
                fish[i - 1] = fish[i]
            }

            fish[6] += newFish
            fish[8] = newFish
        }

        return fish.sum()
    }

    @Override
    def doPart1() {
        def fish = this.evaluateDays(80)
        println "How many lanternfish would there be after 80 days? $fish"
    }

    @Override
    def doPart2() {
        def fish = this.evaluateDays(256)
        println "How many lanternfish would there be after 256 days? $fish"
    }
}
