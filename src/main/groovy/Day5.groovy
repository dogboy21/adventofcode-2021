class Day5 extends DayCommon {
    def readCoordinates() {
        return this.readInputLines().collect{ it.split(" -> ").collect { it.split(",") }.flatten().collect { it.toInteger() } }
    }

    def isHorizontalOrVertical(def coordinatePair) {
        return coordinatePair[0] == coordinatePair[2] || coordinatePair[1] == coordinatePair[3]
    }

    @Override
    def doPart1() {
        def coordinates = this.readCoordinates().findAll { this.isHorizontalOrVertical(it) }
        def points = []

        coordinates.each { coordinatePair ->
            (coordinatePair[0]..coordinatePair[2]).each { x ->
                (coordinatePair[1]..coordinatePair[3]).each { y ->
                    points.add("$x:$y")
                }
            }
        }

        def overlapping = points.groupBy { it }.findAll { it.value.size() > 1 }.size()
        println "At how many points do at least two lines overlap? $overlapping"
    }

    @Override
    def doPart2() {
        def coordinates = this.readCoordinates()
        def points = []

        coordinates.each { coordinatePair ->
            if (this.isHorizontalOrVertical(coordinatePair)) {
                (coordinatePair[0]..coordinatePair[2]).each { x ->
                    (coordinatePair[1]..coordinatePair[3]).each { y ->
                        points.add("$x:$y")
                    }
                }
            } else {
                def distance = Math.abs(coordinatePair[0] - coordinatePair[2]) as int
                def xFactor = coordinatePair[2] < coordinatePair[0] ? -1 : 1
                def yFactor = coordinatePair[3] < coordinatePair[1] ? -1 : 1

                (0..distance).each { dist ->
                    def x = coordinatePair[0] + (dist * xFactor)
                    def y = coordinatePair[1] + (dist * yFactor)
                    points.add("$x:$y")
                }
            }
        }

        def overlapping = points.groupBy { it }.findAll { it.value.size() > 1 }.size()
        println "At how many points do at least two lines overlap? $overlapping"
    }
}
