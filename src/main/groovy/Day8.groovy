class Day8 extends DayCommon {
    def segmentsPerDigit = [
            0: "abcefg",
            1: "cf",
            2: "acdeg",
            3: "acdfg",
            4: "bcdf",
            5: "abdfg",
            6: "abdefg",
            7: "acf",
            8: "abcdefg",
            9: "abcdfg",
    ]

    def sortString(def str) {
        return (str.chars as List).sort().join("")
    }

    def testSingleSegmentCombination(Map<Integer, String> candidate) {
        def charListed = candidate.collectEntries { [it.key, it.value.chars as List] }

        def topSegment = charListed.get(7) - charListed.get(1)
        def topLeftSegment = charListed.get(8) - charListed.get(3) - charListed.get(2)
        def topRightSegment = charListed.get(9) - charListed.get(5)
        def middleSegment = charListed.get(8) - charListed.get(0)
        def bottomLeftSegment = charListed.get(6) - charListed.get(5)
        def bottomRightSegment = charListed.get(3) - charListed.get(2)
        def bottomSegment = charListed.get(9) - charListed.get(4) - charListed.get(7)

        def segments = [
                topSegment,
                topLeftSegment,
                topRightSegment,
                middleSegment,
                bottomLeftSegment,
                bottomRightSegment,
                bottomSegment,
        ]

        return segments.every { it.size() == 1 } && segments.flatten().groupBy { it }.size() == 7
    }

    def decodeSegments(Map<Integer, List<String>> candidates) {
        def combinations = candidates.collect { entry ->
            entry.value.collect { [(entry.key): it] }
        }.inject { list1, list2 ->
            list1.flatten().collect { map1 ->
                list2.collect { map2 ->
                    def map = new HashMap()
                    map.putAll(map1)
                    map.putAll(map2)
                    return map
                }
            }
        }.flatten()

        def possibleCombinations = combinations.findAll { this.testSingleSegmentCombination(it) }
        if (possibleCombinations.size() > 1) {
            throw new Exception("More than 1 possible arrangement found")
        }

        return possibleCombinations.first()
    }

    def decodeEntry(def entry) {
        def signalPatterns = entry.split(" \\| ")[0].split(" ").collect { this.sortString(it) }
        def outputValues = entry.split(" \\| ")[1].split(" ").collect { this.sortString(it) }

        def digitCandidates = this.segmentsPerDigit.collectEntries { digitEntry ->
            return [digitEntry.key, signalPatterns.findAll { it.length() == digitEntry.value.length() }]
        }

        def segmentTranslation = this.decodeSegments(digitCandidates)
        return outputValues.collect { value -> segmentTranslation.find { it.value == value }.key }.join("").toInteger()
    }

    @Override
    def doPart1() {
        def input = this.readInputLines().collect {
            it.split(" \\| ")[1]
        }.collect {
            it.split(" ")
        }.flatten()


        def result = input.count {
            [1, 4, 7, 8].collect { this.segmentsPerDigit.get(it).length() }.contains(it.length())
        }

        println "In the output values, how many times do digits 1, 4, 7, or 8 appear? $result"
    }

    @Override
    def doPart2() {
        def input = this.readInputLines()
        def sum = input.collect { this.decodeEntry(it) }.sum()
        println "What do you get if you add up all of the output values? $sum"
    }
}
