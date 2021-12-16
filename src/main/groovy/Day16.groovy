class Day16 extends DayCommon {
    def operatorPacketName = 'OPERATOR_PACKET'
    def literalValueName = 'LITERAL_VALUE'

    class BinaryStringReader {
        String input
        int index = 0

        BinaryStringReader(String input) {
            this.input = input
        }

        def readString(int numBits) {
            def value = this.input.substring(this.index, this.index + numBits)
            this.index += numBits
            return value
        }

        def readInt(int numBits) {
            def value = Integer.parseInt(this.input.substring(this.index, this.index + numBits), 2)
            this.index += numBits
            return value
        }

        def getRemaining() {
            return this.input.substring(this.index)
        }

    }

    def parseBinaryPacket(BinaryStringReader reader) {
        def packetVersion = reader.readInt(3)
        def packetType = reader.readInt(3)

        if (packetType == 4) { // Literal Value
            def val = ""

            while (true) {
                def hasNext = reader.readInt(1)
                val += reader.readString(4)
                if (!hasNext) break
            }

            return [
                    version: packetVersion,
                    type: this.literalValueName,
                    typeId: packetType,
                    value: Long.parseLong(val, 2)
            ]
        } else { // Operator Packet
            def lengthTypeId = reader.readInt(1)
            if (lengthTypeId == 0) { // next 15 bits are a number that represents the total length in bits of the sub-packets
                def lengthSubPackets = reader.readInt(15)
                def subPacketReader = new BinaryStringReader(reader.readString(lengthSubPackets))
                def subPackets = []

                while (!subPacketReader.getRemaining().isEmpty()) {
                    subPackets += this.parseBinaryPacket(subPacketReader)
                }

                return [
                        version: packetVersion,
                        type: this.operatorPacketName,
                        typeId: packetType,
                        lengthSubPackets: lengthSubPackets,
                        subPackets: subPackets,
                ]
            } else if (lengthTypeId == 1) { // next 11 bits are a number that represents the number of sub-packets
                def numSubPackets = reader.readInt(11)
                return [
                        version: packetVersion,
                        type: this.operatorPacketName,
                        typeId: packetType,
                        numSubPackets: numSubPackets,
                        subPackets: (0..<numSubPackets).collect { parseBinaryPacket(reader) },
                ]
            } else {
                throw new IllegalArgumentException("Invalid length type id: $lengthTypeId")
            }
        }
    }

    def parseBinaryPacket(String packetString) {
        def reader = new BinaryStringReader(packetString)
        return this.parseBinaryPacket(reader)
    }

    def hexToBinary(def hexString) {
        return hexString.split("").collect { new BigInteger(it, 16).toString(2).padLeft(4, '0') }.join("")
    }

    def getPacketVersionSum(def packet) {
        if (packet['type'] == this.literalValueName) {
            return packet['version']
        } else if (packet['type'] == this.operatorPacketName) {
            return packet['version'] + packet['subPackets'].collect { this.getPacketVersionSum(it) }.sum()
        } else {
            throw new IllegalArgumentException("Invalid packet type: ${packet['type']}")
        }
    }

    def evaluatePacket(def packet) {
        if (packet['type'] == this.literalValueName) {
            return packet['value']
        }

        switch (packet['typeId']) {
            case 0: // Sum
                return packet['subPackets'].collect { this.evaluatePacket(it) }.sum()
            case 1: // Product
                return packet['subPackets'].collect { this.evaluatePacket(it) }.inject(1, { a, b -> a * b })
            case 2: // Min
                return packet['subPackets'].collect { this.evaluatePacket(it) }.min()
            case 3: // Max
                return packet['subPackets'].collect { this.evaluatePacket(it) }.max()
            case 5: // Greater Than
                return this.evaluatePacket(packet['subPackets'][0]) > this.evaluatePacket(packet['subPackets'][1]) ? 1 : 0
            case 6: // Less Than
                return this.evaluatePacket(packet['subPackets'][0]) < this.evaluatePacket(packet['subPackets'][1]) ? 1 : 0
            case 7: // Equal To
                return this.evaluatePacket(packet['subPackets'][0]) == this.evaluatePacket(packet['subPackets'][1]) ? 1 : 0
            default:
                throw new IllegalArgumentException("Invalid operator type: ${packet['typeId']}")
        }
    }

    @Override
    def doPart1() {
        def versionSum = this.getPacketVersionSum(this.parseBinaryPacket(this.hexToBinary(this.readInput())))
        println "What do you get if you add up the version numbers in all packets? $versionSum"
    }

    @Override
    def doPart2() {
        def result = this.evaluatePacket(this.parseBinaryPacket(this.hexToBinary(this.readInput())))
        println "What do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission? $result"
    }
}
