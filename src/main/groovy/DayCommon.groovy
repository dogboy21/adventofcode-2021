abstract class DayCommon implements Runnable {

    def readInput(boolean test = false) {
        def fileName = "/" + this.getClass().getSimpleName().toLowerCase()
        if (test) fileName += "_test"
        fileName += ".txt"

        return this.getClass().getResource(fileName)
    }

    def readInputLines(boolean test = false) {
        return this.readInput(test).readLines()
    }

    abstract def doPart1()

    abstract def doPart2()

    @Override
    void run() {
        println "Executing part 1:"
        this.doPart1()
        println ""

        println "Executing part 2:"
        this.doPart2()
    }
}
