def readInputNumbers() {
    def inputLines = this.getClass().getResource("/day1.txt").readLines()
    return inputLines.collect { it.toInteger() }
}

def part1() {
    def numbers = readInputNumbers()
    def increments = numbers.collate(2, 1, false).findAll { it.get(1) > it.get(0) }.size()
    println "How many measurements are larger than the previous measurement? $increments"
}

def part2() {
    def numbers = readInputNumbers()
    def slidingWindow = numbers.collate(3, 1, false)
    def windowSums = slidingWindow.collect { it.sum() }
    def increments = windowSums.collate(2, 1, false).findAll { it.get(1) > it.get(0) }.size()
    println "How many sums are larger than the previous sum? $increments"
}

part1()
part2()
