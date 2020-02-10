<template>
    <div ondragstart="return false" ondrop="return false">
        <table>
            <tr v-for="row in 8" :key="row">
                <td v-for="col in 8" :key="col" class="chess-board-cell" :data-number="cellNumber(row, col)"
                    :class="cells[cellNumber(row, col)].class" @click="cellClick">
                    <Piece :color="cells[cellNumber(row, col)].color" :type="cells[cellNumber(row, col)].name"
                           size="70px" />
                </td>
            </tr>
        </table>
        <input class="form-control" type="text" v-model="fen" readonly>
        <button type="button" class="btn btn-primary" @click="connect">Connect WS</button>
        <h5>Move history</h5>
        <ol reversed>
            <li v-for="(move, index) in moveHistory" :key="index"><code>{{ move }}</code></li>
        </ol>
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
                fen: '', // rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR
                moveHistory: [
                    // { from: (CellNumber), to: (CellNumber) }
                ],
                cells: [
                    // {key: 'b', name: 'bishop', color: 'white', class: {'selected': true}}, - for each cell
                ],
                piecesMap: {
                    'Q': { key: 'Q', name: 'queen',  color: 'white'},
                    'R': { key: 'R', name: 'rook',   color: 'white'},
                    'B': { key: 'B', name: 'bishop', color: 'white'},
                    'N': { key: 'N', name: 'knight', color: 'white'},
                    'P': { key: 'P', name: 'pawn',   color: 'white'},
                    'k': { key: 'k', name: 'king',   color: 'black'},
                    'q': { key: 'q', name: 'queen',  color: 'black'},
                    'r': { key: 'r', name: 'rook',   color: 'black'},
                    'b': { key: 'b', name: 'bishop', color: 'black'},
                    'n': { key: 'n', name: 'knight', color: 'black'},
                    'p': { key: 'p', name: 'pawn',   color: 'black'},
                    'K': { key: 'K', name: 'king',   color: 'white'},
                    ' ': { key: ' ', name: 'empty',  color: 'empty'},
                },
                draggedNumber: null,
            }
        },
        methods: {
            applyFen(fen) {
                this.fen = fen
                let pos = 0
                const blank = this.piecesMap[' ']
                for (let i = 0; pos < 64; i++) {
                    const symbol = fen[i]
                    if (this.piecesMap[symbol]) {
                        const piece = this.piecesMap[symbol]
                        this.$set(this.cells, pos, {key: piece.key, name: piece.name, color: piece.color, class: {}})
                        pos++
                    } else if ("12345678".includes(symbol)) {
                        const skip = parseInt(symbol)
                        for (let j = 0; j < skip; j++) {
                            this.$set(this.cells, pos, {key: blank.key, name: blank.name, color: blank.color, class: {}})
                            pos++
                        }
                    }
                }
                this.highlightAvailableActions()
            },
            extractFen() {
                let fen = ''
                let empty = 0
                let column = 0
                for (let i = 0; i < this.cells.length; i++) {
                    if (column === 8) {
                        if (empty > 0) {
                            fen += empty
                            empty = 0
                        }
                        fen += '/'
                        column = 0
                    }
                    column++
                    const cell = this.cells[i]
                    if (cell.key !== ' ') {
                        if (empty > 0) {
                            fen += empty
                            empty = 0
                        }
                        fen += cell.key
                    } else {
                        empty++
                    }
                }
                if (empty > 0) {
                    fen += empty
                }
                return fen
            },
            cellClick(e) {
                const targetCellNumber = e.target.dataset.number
                if (this.draggedNumber === null && this.cells[targetCellNumber].key !== ' ') {
                    this.draggedNumber = targetCellNumber
                    let draggedPiece = this.cells[this.draggedNumber]
                    draggedPiece.class['selected'] = true
                    this.$set(this.cells, this.draggedNumber, draggedPiece)
                } else if (this.draggedNumber === targetCellNumber) {
                    let draggedPiece = this.cells[this.draggedNumber]
                    draggedPiece.class['available-move-to'] = false
                    draggedPiece.class['available-move-from'] = false
                    draggedPiece.class['selected'] = false
                    this.$set(this.cells, this.draggedNumber, draggedPiece)
                    this.draggedNumber = null
                } else if (this.cells[targetCellNumber].class['available-move-to']) {
                    const fromCellNumber = this.draggedNumber
                    this.movePiece(fromCellNumber, targetCellNumber)
                    this.draggedNumber = null
                }
                this.highlightAvailableActions()
            },
            highlightAvailableActions() {
                    for (let i = 0; i < 64; i++) {
                        if (i == this.draggedNumber)
                            continue

                        let cell = this.cells[i]
                        if (this.draggedNumber !== null) {
                            if (cell.key === ' ') {
                                cell.class['available-move-to'] = true
                                this.$set(this.cells, i, cell)
                            } else {
                                cell.class['available-move-to'] = false
                                cell.class['available-move-from'] = false
                                cell.class['selected'] = false
                                this.$set(this.cells, i, cell)
                            }
                        } else {
                            if (cell.key === ' ') {
                                cell.class['available-move-to'] = false
                                cell.class['available-move-from'] = false
                                cell.class['selected'] = false
                                this.$set(this.cells, i, cell)
                            } else {
                                cell.class['available-move-from'] = true
                                this.$set(this.cells, i, cell)
                            }
                        }
                    }
            },
            movePiece(fromCellNumber, toCellNumber) {
                const blank = this.piecesMap[' ']
                const fromCell = this.cells[fromCellNumber]
                for (let i = 0; i < 64; i++) {
                    let cell = this.cells[i]
                    cell.class['last-move-from'] = false
                    cell.class['last-move-to'] = false
                    this.$set(this.cells, i, cell)
                }
                this.$set(this.cells, toCellNumber,
                    {key: fromCell.key, name: fromCell.name, color: fromCell.color, class: {'last-move-to': true}})
                this.$set(this.cells, fromCellNumber,
                    {key: blank.key, name: blank.name, color: blank.color, class: {'last-move-from': true}})
                this.fen = this.extractFen()
                this.moveHistory.unshift({from: fromCellNumber, to: toCellNumber})
            },
            connect() {
                const ctx = this
                const gameSocketUrl = '/topic/test'
                const socket = new SockJs('http://localhost:5000/ws')
                const stompClient = Stomp.over(socket)
                stompClient.connect({}, function (frame) {
                    console.log('Connected: ' + frame)
                    stompClient.subscribe(gameSocketUrl, function (message) {
                        if (message.body.startsWith('BOARD:')) {
                            ctx.applyFen(message.body.substring(6));
                        } else if (message.body.startsWith('MOVE:')) {
                            ctx.movePiece(message.body.substring(5).split(',')[0], message.body.substring(5).split(',')[1])
                        }
                    })
                })
            },
            cellNumber(row, col) {
                return (row - 1) * 8 + (col - 1)
            }
        },
        created() {
            const blank = this.piecesMap[' ']
            for (let i = 0; i < 64; i++) {
                this.cells[i] = {key: blank.key, name: blank.name, color: blank.color, class: {}}
            }
        },
        mounted() {
            this.applyFen('rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR')
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
    .selected {
        background: rgba(247, 238, 35, 0.69) !important;
        cursor: pointer;
    }
    .available-move-from:hover {
        background: rgba(0, 128, 0, 0.2) !important;
        cursor: pointer;
    }
    .available-move-to:hover {
        background: rgb(62, 128, 20) !important;
        cursor: pointer;
    }
    .last-move-from {
        background: rgb(128, 85, 115) !important;
    }
    .last-move-to {
        background: rgb(128, 39, 105) !important;
    }
</style>
