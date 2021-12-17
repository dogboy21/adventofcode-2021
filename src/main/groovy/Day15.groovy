class Day15 extends DayCommon {
    def readGrid() {
        def inputLines = this.readInputLines()
        def height = inputLines.size()
        def grid = inputLines.collect {
            it.split("").collect { it.toInteger() }
        }.flatten()
        int width = grid.size() / height
        return [grid, width]
    }

    def getNeighbors(def current, def gridWidth) {
        int x = current % gridWidth
        int y = current / gridWidth

        return [
                [x - 1, y],
                [x + 1, y],
                [x, y - 1],
                [x, y + 1]
        ].findAll { it[0] >= 0 && it[1] >= 0 && it[0] < gridWidth && it[1] < gridWidth }
                .collect { it[1] * gridWidth + it[0] }
    }

    def manhattanDistanceHeuristic(def width, def goal, def current) {
        def x = current % width
        def y = current / width
        def gx = goal % width
        def gy = goal / width
        def dx = Math.abs(x - gx)
        def dy = Math.abs(y - gy)
        return (int) (dx + dy)
    }

    def aStar(def start, def goal, def grid, def gridWidth, def hFunc) {
        hFunc = hFunc.curry(goal)

        def gScore = new HashMap<Integer, Integer>()
        def fScore = new HashMap<Integer, Integer>()

        def openSet = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            int compare(Integer a, Integer b) {
                return fScore.getOrDefault(a, Integer.MAX_VALUE) - fScore.getOrDefault(b, Integer.MAX_VALUE)
            }
        })

        openSet.add(start)
        gScore.put(start, 0)
        fScore.put(start, hFunc(start))

        while (!openSet.isEmpty()) {
            def current = openSet.poll()
            if (current == goal) {
                return gScore.get(current)
            }

            openSet.remove(current)

            for (def neighbor in this.getNeighbors(current, gridWidth)) {
                if (neighbor < 0 || neighbor >= grid.size()) {
                    continue
                }

                def tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + grid[neighbor]
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    gScore.put(neighbor, tentativeGScore)
                    fScore.put(neighbor, tentativeGScore + hFunc(neighbor))
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor)
                    }
                }
            }
        }
    }

    def growInputGrid(def grid, def gridWidth, def factor) {
        def newGrid = new int[grid.size() * factor * factor]
        def newGridWidth = gridWidth * factor

        for (i in 0..<newGrid.size()) {
            int x = i % newGridWidth
            int y = i / newGridWidth

            int subGridX = x / gridWidth
            int subGridY = y / gridWidth

            int innerSubGridX = x % gridWidth
            int innerSubGridY = y % gridWidth

            def dist = subGridX + subGridY
            def mirrorValue = grid[innerSubGridX + innerSubGridY * gridWidth]
            newGrid[i] = (mirrorValue + dist) <= 9 ? mirrorValue + dist : mirrorValue + dist - 9
        }

        return newGrid
    }

    @Override
    def doPart1() {
        def input = this.readGrid()
        def grid = input[0]
        def width = input[1]

        def totalRisk = this.aStar(0, grid.size() - 1, grid, width, this.&manhattanDistanceHeuristic.curry(width))
        println "What is the lowest total risk of any path from the top left to the bottom right? $totalRisk"
    }

    @Override
    def doPart2() {
        def input = this.readGrid()
        def grid = input[0]
        def width = input[1]

        grid = this.growInputGrid(grid, width, 5) as List
        width *= 5

        def totalRisk = this.aStar(0, grid.size() - 1, grid, width, this.&manhattanDistanceHeuristic.curry(width))
        println "What is the lowest total risk of any path from the top left to the bottom right? $totalRisk"
    }
}
