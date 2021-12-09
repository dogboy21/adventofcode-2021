class Day9 extends DayCommon {
    def readInputArray() {
        def input = this.readInputLines(false).collect {
            it.chars.collect { it.toString().toInteger() }
        }

        return input
    }

    def getNeighbors(def input, def x, def y) {
        def neighborIndices = [
                [x - 1, y], [x + 1, y],
                [x, y - 1], [x, y + 1]
        ].findAll { it[0] >= 0 && it[1] >= 0 && it[0] < input[0].size() && it[1] < input.size() }

        return neighborIndices.collect() { [it[0], it[1], input[it[1]][it[0]]] }
    }

    def findLowSpots(def input) {
        def lowSpots = []

        (0..<input[0].size()).each { x ->
            (0..<input.size()).each { y ->
                def neighbors = this.getNeighbors(input, x, y).collect { it[2] }
                if (input[y][x] < neighbors.min()) lowSpots.add([x, y])
            }
        }

        return lowSpots
    }

    def growBasin(def basin, def input, def x, def y) {
        basin.add([x, y])
        def neighbors = this.getNeighbors(input, x, y).findAll { it[2] < 9 && it[2] > input[y][x] }
        neighbors.each {
            basin.add([it[0], it[1]])
            this.growBasin(basin, input, it[0], it[1])
        }
    }

    def getBasins(def input) {
        def lowSpots = this.findLowSpots(input)
        def basins = []

        lowSpots.each { lowSpot ->
            def basin = []
            this.growBasin(basin, input, lowSpot[0], lowSpot[1])
            basins.add(basin.unique())
        }

        return basins
    }

    @Override
    def doPart1() {
        def input = this.readInputArray()
        def lowSpots = this.findLowSpots(input)
        def riskLevel = lowSpots.collect { input[it[1]][it[0]] + 1 }.sum()

        println "What is the sum of the risk levels of all low points on your heightmap? $riskLevel"
    }

    @Override
    def doPart2() {
        def input = this.readInputArray()
        def basins = this.getBasins(input).sort { -(it.size()) }
        def sizes = basins.subList(0, 3).collect { it.size() }

        def result = 1
        sizes.each { result *= it }

        println "What do you get if you multiply together the sizes of the three largest basins? $result"
    }
}
