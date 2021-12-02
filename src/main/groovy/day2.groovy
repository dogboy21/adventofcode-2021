class Day2 extends DayCommon {

    def executeCommands(Map<String, Closure<?>> commands) {
        def input = this.readInput()

        input.each {
            def commandData = it.split(" ")
            if (commandData.length != 2) return

            if (!commands.containsKey(commandData[0])) {
                println "Invalid command: ${commandData[0]}"
                return
            }

            commands[commandData[0]].call(commandData[1].toInteger())
        }
    }

    @Override
    def doPart1() {
        def posHorizontal = 0
        def posDepth = 0

        this.executeCommands([
                "forward": { x -> posHorizontal += x },
                "down": { x -> posDepth += x},
                "up" : { x -> posDepth -= x},
        ])

        println "What do you get if you multiply your final horizontal position by your final depth? ${posHorizontal * posDepth}"
    }

    @Override
    def doPart2() {
        def posHorizontal = 0
        def posDepth = 0
        def aim = 0

        this.executeCommands([
                "forward": { x ->
                    posHorizontal += x
                    posDepth += aim * x
                },
                "down": { x -> aim += x},
                "up" : { x -> aim -= x},
        ])

        println "What do you get if you multiply your final horizontal position by your final depth? ${posHorizontal * posDepth}"
    }

}