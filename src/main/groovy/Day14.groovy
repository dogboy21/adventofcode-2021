class Day14 extends DayCommon {
    def readInputs() {
        def input = this.readInputLines()
        def polymerTemplate = input.pop()
        input.pop()
        def pairInsertionRules = input.collectEntries { it.split(" -> ") }

        return [polymerTemplate, pairInsertionRules]
    }

    def executeSteps(String input, def rules, def steps) {
        def pairs = (0..<(input.length() - 1))
                .collect { input.substring(it, it + 2) }
                .groupBy { it }
                .collectEntries { [it.key, it.value.size()]}

        def charCounts = Arrays.asList(input.chars)
                .groupBy { it.toString() }
                .collectEntries { [it.key, it.value.size()] }

        for (i in 0..<steps) {
            def pairCopy = new HashMap()
            pairCopy.putAll(pairs)

            pairCopy.each {
                def middleChar = rules.get(it.key)

                pairs.put(it.key, pairs.getOrDefault(it.key, 0) - it.value)

                def newFirstPair = it.key.substring(0, 1) + middleChar
                def newSecondPair = middleChar + it.key.substring(1)
                pairs.put(newFirstPair, (pairs.getOrDefault(newFirstPair, 0) + it.value) as long)
                pairs.put(newSecondPair, (pairs.getOrDefault(newSecondPair, 0) + it.value) as long)
                charCounts.put(middleChar, (charCounts.getOrDefault(middleChar, 0) + it.value) as long)
            }

            pairs.removeAll { it.value == 0 }
        }

        return charCounts
    }

    def printResult(def steps) {
        def inputs = this.readInputs()
        def polymerTemplate = inputs[0]
        def pairInsertionRules = inputs[1]

        def charCounts = this.executeSteps(polymerTemplate, pairInsertionRules, steps)
        def result = charCounts.max { it.value }.value - charCounts.min { it.value }.value

        println "What do you get if you take the quantity of the most common element and subtract the quantity of the least common element? $result"
    }

    @Override
    def doPart1() {
        this.printResult(10)
    }

    @Override
    def doPart2() {
        this.printResult(40)
    }
}
