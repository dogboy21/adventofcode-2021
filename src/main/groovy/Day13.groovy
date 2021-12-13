class Day13 extends DayCommon {
    def readPointsAndFolds(def points, def folds) {
        this.readInputLines().each {
            if (it.startsWith("fold along ")) {
                folds.add(it.substring(11))
            } else if (it.contains(",")) {
                points.add(it.split(",").collect { it.toInteger() }.toArray())
            }
        }
    }

    def printPoints(def points) {
        def maxX = points.max { it[0] }[0]
        def maxY = points.max { it[1] }[1]

        for (def y in 0..maxY) {
            for (def x in 0..maxX) {
                if (points.any { it[0] == x && it[1] == y }) {
                    print "#"
                } else {
                    print "."
                }
            }
            println ""
        }
    }

    def executeFolds(def points, def folds, def breakAfterFirst = false) {
        for (def fold in folds) {
            def data = fold.split("=")
            if (data[0] == "y") {
                this.foldHorizontally(points, data[1].toInteger())
            } else if (data[0] == "x") {
                this.foldVertically(points, data[1].toInteger())
            }

            if (breakAfterFirst) break
        }
    }

    def foldHorizontally(def points, def pos) {
        points.each {
            if (it[1] > pos) {
                it[1] = pos - (it[1] - pos)
            }
        }

        points.unique()
    }

    def foldVertically(def points, def pos) {
        points.each {
            if (it[0] > pos) {
                it[0] = pos - (it[0] - pos)
            }
        }

        points.unique()
    }

    @Override
    def doPart1() {
        def points = []
        def folds = []
        this.readPointsAndFolds(points, folds)
        this.executeFolds(points, folds, true)

        println "How many dots are visible after completing just the first fold instruction on your transparent paper? ${points.size()}"
    }

    @Override
    def doPart2() {
        def points = []
        def folds = []
        this.readPointsAndFolds(points, folds)
        this.executeFolds(points, folds)

        this.printPoints(points)
    }
}
