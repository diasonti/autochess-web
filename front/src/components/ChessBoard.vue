<template>
    <table>
        <tr v-for="row in 8" :key="row">
            <td v-for="col in 8" :key="col" class="chess-board-cell" :class="cellClass(row, col)"></td>
        </tr>
    </table>
</template>

<script>
    import SockJs from 'sockjs-client'
    import Stomp from 'stompjs'

    export default {
        name: 'ChessBoard',
        data() {
            return {
                fen: '',
                fenHistory: [],
                cells: [],
                piecesMap: {
                    'K': { key: 'K', name: 'KING',   color: 'WHITE', char: '&#9812;'},
                    'Q': { key: 'Q', name: 'QUEEN',  color: 'WHITE', char: '&#9813;'},
                    'R': { key: 'R', name: 'ROOK',   color: 'WHITE', char: '&#9814;'},
                    'B': { key: 'B', name: 'BISHOP', color: 'WHITE', char: '&#9815;'},
                    'N': { key: 'N', name: 'KNIGHT', color: 'WHITE', char: '&#9816;'},
                    'P': { key: 'P', name: 'PAWN',   color: 'WHITE', char: '&#9817;'},
                    'k': { key: 'k', name: 'KING',   color: 'BLACK', char: '&#9818;'},
                    'q': { key: 'q', name: 'QUEEN',  color: 'BLACK', char: '&#9819;'},
                    'r': { key: 'r', name: 'ROOK',   color: 'BLACK', char: '&#9820;'},
                    'b': { key: 'b', name: 'BISHOP', color: 'BLACK', char: '&#9821;'},
                    'n': { key: 'n', name: 'KNIGHT', color: 'BLACK', char: '&#9822;'},
                    'p': { key: 'p', name: 'PAWN',   color: 'BLACK', char: '&#9823;'},
                },
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
                for (let i = 0; pos < 64; i++) {
                    const symbol = fen[i]
                    if (this.piecesMap[symbol]) {
                        const piece = this.piecesMap[symbol]
                        this.cells[pos].html(piece.char)
                        pos++;
                    } else if ("12345678".includes(symbol)) {
                        pos += parseInt(symbol)
                    }
                }
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
            cellClass(row, col) {
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
            this.fen = 'rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR'
            // this.connect();
        },
    }
</script>

<style scoped>
    table {
        margin: auto;
        background: white;
        border: 25px solid #333;
        border-collapse: collapse;
    }
    td {
        width: 70px; height: 70px;
        border: 2px solid #333;
        font-size: 36px;
        text-align: center;
        cursor: default;
    }
    tr:nth-child(odd) td:nth-child(even),
    tr:nth-child(even) td:nth-child(odd) {
        background: #999;
    }
</style>
