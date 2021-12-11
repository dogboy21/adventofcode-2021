class Day11 extends DayCommon {
    def flashOctopus(def grid, def x, def y, def flashedOctopuses) {
        def id = "$x:$y".toString()
        if (flashedOctopuses.contains(id)) return
        flashedOctopuses.add(id)

        for (def lX in (x - 1)..(x + 1)) {
            for (def lY in (y - 1)..(y + 1)) {
                if (lX == x && lY == y) continue
                if (lX < 0 || lY < 0) continue
                if (lY >= grid.size() || lX >= grid[0].size()) continue

                grid[lY][lX]++
                if (grid[lY][lX] > 9) {
                    this.flashOctopus(grid, lX, lY, flashedOctopuses)
                }
            }
        }
    }

    def step(def grid) {
        grid.each {it.eachWithIndex{ int entry, int i -> it[i] = entry + 1 } }
        def flashedOctopuses = []

        for (y in 0..<grid.size()) {
            for (x in 0..<grid[y].size()) {
                if (grid[y][x] > 9) {
                    this.flashOctopus(grid, x, y, flashedOctopuses)
                }
            }
        }

        flashedOctopuses.each {
            def x = it.split(":")[0].toInteger()
            def y = it.split(":")[1].toInteger()
            grid[y][x] = 0
        }

        return flashedOctopuses.size()
    }

    @Override
    def doPart1() {
        def input = this.readInputLines().collect {
            it.split("").collect { it.toInteger() }
        }
        def flashes = 0

        for (i in 0..<100) {
            flashes += this.step(input)
        }

        input.each { println it.join(" ") }

        println "How many total flashes are there after 100 steps? $flashes"
    }

    @Override
    def doPart2() {
        def input = this.readInputLines().collect {
            it.split("").collect { it.toInteger() }
        }
        def numStep = 0

        while (true) {
            numStep++
            if (this.step(input) == input.size() * input[0].size()) {
                println "What is the first step during which all octopuses flash? $numStep"
                break
            }
        }

    }
}
