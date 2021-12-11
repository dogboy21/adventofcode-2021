class Day10 extends DayCommon {
    def bracketPairs = [
            "(": ")",
            "[": "]",
            "{": "}",
            "<": ">",
    ]

    def bracketErrorPoints = [
            ")": 3,
            "]": 57,
            "}": 1197,
            ">": 25137,
    ]

    def bracketCompletionPoints = [
            ")": 1,
            "]": 2,
            "}": 3,
            ">": 4,
    ]

    def verifyLine(def line) {
        def stack = new Stack<String>()
        for (def c in line) {
            if (this.bracketPairs.containsKey(c)) {
                stack.push(c)
            } else if (this.bracketPairs.containsValue(c)) {
                if (stack.isEmpty()) {
                    throw new RuntimeException("Unmatched closing bracket: " + c)
                }
                def open = this.bracketPairs.get(stack.pop())
                if (open != c) return c
            }
        }

        return null
    }

    def repairLine(def line) {
        def stack = new Stack<String>()
        for (def c in line) {
            if (this.bracketPairs.containsKey(c)) {
                stack.push(c)
            } else if (this.bracketPairs.containsValue(c)) {
                if (stack.isEmpty()) {
                    throw new RuntimeException("Unmatched closing bracket: " + c)
                }
                def open = this.bracketPairs.get(stack.pop())
                if (open != c) throw new RuntimeException("Mismatched brackets: " + open + " " + c)
            }
        }

        return stack.reverse(false).collect { this.bracketPairs.get(it) }.join("")
    }

    @Override
    def doPart1() {
        def input = this.readInputLines()
        def errorScore = input.collect {
            def wrongChar = this.verifyLine(it)
            if (!wrongChar) return 0
            return this.bracketErrorPoints.get(wrongChar)
        }.sum()

        println "What is the total syntax error score for those errors? $errorScore"
    }

    @Override
    def doPart2() {
        def input = this.readInputLines().findAll { this.verifyLine(it) == null }
        def repairScores = input.collect {
            def repaired = this.repairLine(it)
            long score = 0
            for (def c in repaired) {
                score *= 5
                score += this.bracketCompletionPoints.get(c)
            }

            return score
        }.sort()

        println repairScores
        def middleScore = repairScores.get((repairScores.size() / 2) as int)
        println "What is the middle score? $middleScore"
    }
}
