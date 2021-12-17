class Day17 extends DayCommon {
    def getTrajectoryPoint(def initialVector, def t) {
        initialVector = initialVector.collect { it }

        def x = 0
        def y = 0

        for (i in 0..<t) {
            x += initialVector[0]
            y += initialVector[1]

            initialVector[0] = initialVector[0] > 0 ? initialVector[0] - 1 : initialVector[0] < 0 ? initialVector[0] + 1 : 0
            initialVector[1] = initialVector[1] - 1
        }

        return [x, y]
    }

    def getTrajectory(def initialVector, def targetArea) {
        def points = []
        def t = 1

        while (true) {
            def point = this.getTrajectoryPoint(initialVector, t)

            if (point[0] > targetArea[0][1] || point[1] < targetArea[1][0]) {
                return points
            }

            points.add(point)

            t += 1
        }
    }

    def isInTargetArea(def point, def targetArea) {
        return point[0] >= targetArea[0][0] && point[0] <= targetArea[0][1] && point[1] >= targetArea[1][0] && point[1] <= targetArea[1][1]
    }

    @Override
    def doPart1() {
        def targetArea = this.readInput().substring(13).split(", ").collect {
            it.substring(2).split("\\.\\.").collect { it.toInteger() }
        }

        def highestY = Integer.MIN_VALUE
        for (def x in (0..200)) {
            for (def y in (-200..200)) {
                def initialVector = [x, y]
                def trajectory = this.getTrajectory(initialVector, targetArea)
                if (!trajectory.isEmpty() && this.isInTargetArea(trajectory[trajectory.size() - 1], targetArea)) {
                    if (trajectory.max { it[1] }[1] > highestY) {
                        highestY = trajectory.max { it[1] }[1]
                    }
                }
            }
        }

        println "What is the highest y position it reaches on this trajectory? $highestY"
    }

    @Override
    def doPart2() {
        def targetArea = this.readInput().substring(13).split(", ").collect {
            it.substring(2).split("\\.\\.").collect { it.toInteger() }
        }

        def vectors = 0
        for (def x in 0..200) {
            for (def y in -200..200) {
                def initialVector = [x, y]
                def trajectory = this.getTrajectory(initialVector, targetArea)
                if (!trajectory.isEmpty() && this.isInTargetArea(trajectory[trajectory.size() - 1], targetArea)) {
                    vectors++
                }
            }
        }

        println "How many distinct initial velocity values cause the probe to be within the target area after any step? $vectors"
    }
}
