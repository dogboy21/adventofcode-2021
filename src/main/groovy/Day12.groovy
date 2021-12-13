class Day12 extends DayCommon {
    def readNeighborMap() {
        def neighbors = new HashMap()
        this.readInputLines()
                .collect { it.split("-") }
                .each {
                    neighbors.put(it[0], neighbors.getOrDefault(it[0], []) + it[1])
                    neighbors.put(it[1], neighbors.getOrDefault(it[1], []) + it[0])
                }

        return neighbors
    }

    def depthFirstSearch(def path, def goal, def neighborMap, def paths, def neighborFilter) {
        if (path.last() == goal) {
            paths.add(path)
            return
        }

        for (def neighbor in neighborMap[path.last()]) {
            if (neighborFilter.call(neighbor, path)) continue

            this.depthFirstSearch(path + neighbor, goal, neighborMap, paths, neighborFilter)
        }
    }

    def isStrLowercase(def str) {
        return str.chars.every { Character.isLowerCase(it) }
    }

    @Override
    def doPart1() {
        def neighbors = this.readNeighborMap()

        def paths = []
        this.depthFirstSearch(["start"], "end", neighbors, paths, { neighbor, path ->
            return this.isStrLowercase(neighbor) && path.contains(neighbor)
        })

        println "How many paths through this cave system are there that visit small caves at most once? ${paths.size()}"
    }

    @Override
    def doPart2() {
        def neighbors = this.readNeighborMap()

        def paths = []
        this.depthFirstSearch(["start"], "end", neighbors, paths, { neighbor, path ->
            if (!this.isStrLowercase(neighbor)) return false
            if (neighbor == "end") return false
            if (neighbor == "start" && path.contains(neighbor)) return true

            def lowercaseNodes = path.findAll { it.chars.every { Character.isLowerCase(it) } }.groupBy { it }

            return lowercaseNodes.any { it.value.size() >= 2 } && lowercaseNodes.containsKey(neighbor)
        })

        println "Given these new rules, how many paths through this cave system are there? ${paths.size()}"
    }
}