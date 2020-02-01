<template>
    <div>
        <div style="display: none;">
            <Piece v-for="piece in piecesMap" :key="piece.key" :color="piece.color" :type="piece.name" size="70px" :id="piece.color + '_' + piece.name" />
        </div>
        <table>
            <tr v-for="row in 8" :key="row">
                <td v-for="col in 8" :key="col" class="chess-board-cell" :class="cellNumber(row, col)" @click="cellClick">
                </td>
            </tr>
        </table>
    </div>
</template>

<script>
    import SockJs from 'sockjs-client'
    import Stomp from 'stompjs'
    import Piece from './Piece'

    export default {
        name: 'ChessBoard',
        components: {Piece},
        data() {
            return {
                fen: '',
                fenHistory: [],
                cells: [],
                piecesMap: {
                    'K': { key: 'K', name: 'king',   color: 'white', char: '&#9812;', template: null },
                    'Q': { key: 'Q', name: 'queen',  color: 'white', char: '&#9813;', template: null },
                    'R': { key: 'R', name: 'rook',   color: 'white', char: '&#9814;', template: null },
                    'B': { key: 'B', name: 'bishop', color: 'white', char: '&#9815;', template: null },
                    'N': { key: 'N', name: 'knight', color: 'white', char: '&#9816;', template: null },
                    'P': { key: 'P', name: 'pawn',   color: 'white', char: '&#9817;', template: null },
                    'k': { key: 'k', name: 'king',   color: 'black', char: '&#9818;', template: null },
                    'q': { key: 'q', name: 'queen',  color: 'black', char: '&#9819;', template: null },
                    'r': { key: 'r', name: 'rook',   color: 'black', char: '&#9820;', template: null },
                    'b': { key: 'b', name: 'bishop', color: 'black', char: '&#9821;', template: null },
                    'n': { key: 'n', name: 'knight', color: 'black', char: '&#9822;', template: null },
                    'p': { key: 'p', name: 'pawn',   color: 'black', char: '&#9823;', template: null },
                },
                piecesOnBoard: [
                    // { key: '', element: $(piece) },
                ],
            };
        },
        watch: {
            fen(newFen, oldFen) {
                this.applyFen(newFen)
                this.fenHistory.push(oldFen)
            }
        },
        methods: {
            applyFen(fen) {
                let pos = 0
                this.cells.forEach(cell => cell.html(''))
                this.piecesOnBoard = []
                for (let i = 0; pos < 64; i++) {
                    const symbol = fen[i]
                    if (this.piecesMap[symbol]) {
                        const piece = this.piecesMap[symbol]
                        this.cells[pos].html('')
                        let newElement = piece.template.clone()
                        newElement.removeAttr('id')
                        this.cells[pos].append(newElement)
                        this.piecesOnBoard.push({
                            key: piece.key,
                            element: newElement
                        })
                        pos++;
                    } else if ("12345678".includes(symbol)) {
                        pos += parseInt(symbol)
                    }
                }
            },
            cellClick(e) {
                const target = $(e.target)
                console.log(target.html())
            },
            connect() {
                const ctx = this
                const gameSocketUrl = '/topic/test'
                const socket = new SockJs('/ws')
                const stompClient = Stomp.over(socket)
                stompClient.connect({}, function (frame) {
                    console.log('Connected: ' + frame)
                    stompClient.subscribe(gameSocketUrl, function (message) {
                        ctx.fen = message.body
                    })
                })
            },
            cellNumber(row, col) {
                const cellNumber = (row - 1) * 8 + (col - 1)
                let classObject = {}
                classObject[cellNumber] = true
                return classObject
            }
        },
        mounted() {
            for (let i = 0; i < 64; i++) {
                this.cells[i] = $('td.chess-board-cell.' + i)
            }
            for (let key in this.piecesMap) {
                const id = this.piecesMap[key].color + '_' + this.piecesMap[key].name
                this.piecesMap[key].template = $('#' + id)
            }


            this.fen = 'rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR'
            // this.connect();
        },
    }
</script>

<style scoped>
    table {
        background: #f0d9b5;
    }
    td {
        width: 70px; height: 70px;
        font-size: 36px;
        text-align: center;
        cursor: default;
    }
    tr:nth-child(odd) td:nth-child(even),
    tr:nth-child(even) td:nth-child(odd) {
        background: #b58863;
    }
</style>
