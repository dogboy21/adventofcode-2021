class Day3 extends DayCommon {
    def getBitsByPosition(def numbers, def position) {
        def bits = new ArrayList()

        for (def number in numbers) {
            bits.add(number.charAt(position))
        }

        return bits.groupBy { it }.collectEntries { [it.key, it.value.size()] }
    }

    @Override
    def doPart1() {
        def numbers = this.readInputLines()
        def numBits = numbers[0].length()
        def mostCommonBits = (0..<numBits).collect { this.getBitsByPosition(numbers, it).max { it.value }.key }.join("")

        def gammaRate = Integer.parseInt(mostCommonBits, 2)
        def gammaBits = Integer.toString(gammaRate, 2)

        def epsilonRate = Integer.toUnsignedLong((~gammaRate) & (1 << numBits) - 1)
        def epsilonBits = Integer.toString(epsilonRate as int, 2).padLeft(numBits, "0")

        println "Gamma Rate:   $gammaBits $gammaRate"
        println "Epsilon Rate: $epsilonBits $epsilonRate"
        println "What is the power consumption of the submarine? ${gammaRate * epsilonRate}"
    }

    @Override
    def doPart2() {
        def criterias = [
                // Oxygen Generator Rating
                'Oxygen': { bitAmounts, position, numberToFilter ->
                    def mostCommonBit = bitAmounts.get('0' as char) > bitAmounts.get('1' as char) ? '0' : '1'
                    return numberToFilter.charAt(position) == mostCommonBit
                },
                // CO2 Scrubber Rating
                'CO2': {  bitAmounts, position, numberToFilter ->
                    def leastCommonBit = bitAmounts.get('1' as char) < bitAmounts.get('0' as char) ? '1' : '0'
                    return numberToFilter.charAt(position) == leastCommonBit
                }
        ]

        def numbers = this.readInputLines(true)
        def numBits = numbers[0].length()

        def values = criterias.collectEntries {
            def numbersCopy = new ArrayList(numbers)

            for (i in 0..<numBits) {
                def bitAmounts = this.getBitsByPosition(numbersCopy, i)
                numbersCopy = numbersCopy.findAll(it.value.curry(bitAmounts, i))
                if (numbersCopy.size() == 1) {
                    return [it.key, numbersCopy[0]]
                }
            }
        }

        println values

        def lifeSupportRating = Integer.parseInt(values['Oxygen'], 2) * Integer.parseInt(values['CO2'], 2)

        println "What is the life support rating of the submarine? $lifeSupportRating"
    }
}
