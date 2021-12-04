class Day4 extends DayCommon {
    def drawMarker = -127

    def readBingoBoard(def bingoInput) {
        if (!bingoInput.isEmpty() && bingoInput.first().trim().isEmpty()) {
            bingoInput.pop()
        }

        if (bingoInput.isEmpty()) {
            return null
        }

        def board = new int[25]
        for (def rowIdx in 0..<5) {
            def row = bingoInput.pop().split(" ").findAll{ !it.trim().isEmpty() }.collect { it.toInteger() }
            for (colIdx in 0..<5) {
                board[rowIdx * 5 + colIdx] = row[colIdx]
            }
        }

        return board
    }

    def checkBoardWin(def board) {
        if (board == null) {
            return false
        }

        for (def idx in 0..<5) {
            if (((idx * 5)..<((idx + 1) * 5)).collect { board[it] }.every { it == drawMarker }) {
                return true
            }

            if (((0 + idx)..(20 + idx)).step(5).collect { board[it] }.every { it == drawMarker }) {
                return true
            }
        }
    }

    @Override
    def doPart1() {
        def bingoInput = this.readInputLines()
        def numbersToDraw = bingoInput.pop().split(",").collect { it.toInteger() }
        def boards = new ArrayList<int[]>()

        while (true) {
            def board = this.readBingoBoard(bingoInput)
            if (board == null) break
            boards.add(board)
        }

        def win = false
        for (def draw in numbersToDraw) {
            if (win) break

            boards.each {
                it.eachWithIndex{ int entry, int i ->
                    if (entry == draw) it[i] = drawMarker
                }

                if (this.checkBoardWin(it)) {

                    def finalScore = it.findAll { it != drawMarker }.sum() * draw
                    println "What will your final score be if you choose that board? $finalScore"
                    win = true
                }
            }
        }
    }

    @Override
    def doPart2() {
        def bingoInput = this.readInputLines()
        def numbersToDraw = bingoInput.pop().split(",").collect { it.toInteger() }
        def boards = new ArrayList<int[]>()

        while (true) {
            def board = this.readBingoBoard(bingoInput)
            if (board == null) break
            boards.add(board)
        }

        for (def draw in numbersToDraw) {
            boards.eachWithIndex{ int[] board, int boardIdx ->
                if (board == null) return

                board.eachWithIndex{ int entry, int i ->
                    if (entry == draw) board[i] = drawMarker
                }

                if (this.checkBoardWin(board)) {
                    def finalScore = board.findAll { it != drawMarker }.sum() * draw
                    println "Board ${boardIdx + 1} won with score $finalScore on draw $draw"
                }
            }

            boards = boards.collect { this.checkBoardWin(it) ? null : it }
        }
    }
}
