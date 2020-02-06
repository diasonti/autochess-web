<template>
    <div ondragstart="return false" ondrop="return false">
        <div style="display: none">
            <Piece v-for="piece in piecesMap" :key="piece.key" :color="piece.color" :type="piece.name" size="70px" :id="piece.color + '_' + piece.name" />
        </div>
        <table>
            <tr v-for="row in 8" :key="row">
                <td v-for="col in 8" :key="col" class="chess-board-cell" :class="cellNumber(row, col)"
                    @mouseenter="cellEnter" @mouseleave="cellLeave">
                </td>
            </tr>
        </table>
        <p>{{ fen }}</p>
        <p>{{ moveHistory }}</p>
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
                moveHistory: [
                    // { from: (CellNumber), to: (CellNumber) }
                ],
                cells: [
                    // cellNumber: $(td)
                ],
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
                    ' ': { key: ' ', name: 'empty',  color: 'empty', char: '&nbsp;',  template: null },
                },
                piecesOnBoard: {
                    // uid: { key: 'k', cellNumber: 0, element: $(piece) },
                },
                dragPiece: null,
            }
        },
        methods: {
            applyFen(fen) {
                this.fen = fen
                let pos = 0
                this.cells.forEach(cell => {
                    cell.empty()
                    let emptyPiece = this.piecesMap[' '].template.clone()
                    emptyPiece.removeAttr('id')
                    emptyPiece.css({'pointer-events': 'none'})
                    cell.append(emptyPiece)
                    cell.off('click')
                    cell.on('click', (e) => this.cellClick(e))
                })
                this.piecesOnBoard = []
                for (let i = 0; pos < 64; i++) {
                    const symbol = fen[i]
                    if (this.piecesMap[symbol]) {
                        const piece = this.piecesMap[symbol]
                        this.cells[pos].empty().empty()
                        this.cells[pos].off('click')
                        let newPiece = piece.template.clone()
                        this.cells[pos].append(newPiece)
                        newPiece.removeAttr('id')
                        const uid = Math.random().toString(36).substr(2, 9)
                        newPiece.attr('data-uid', uid)
                        newPiece.on('click', (e) => this.pieceClick(e))
                        this.piecesOnBoard[uid] = {
                            key: piece.key,
                            cellNumber: pos,
                            element: newPiece
                        }
                        pos++
                    } else if ("12345678".includes(symbol)) {
                        pos += parseInt(symbol)
                    }
                }
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
                    const pieceUid = $($(cell).children()[0]).data('uid')
                    if (pieceUid) {
                        if (empty > 0) {
                            fen += empty
                            empty = 0
                        }
                        const piece = this.piecesOnBoard[$($(cell).children()[0]).data('uid')]
                        fen += piece.key
                    } else {
                        empty++
                    }
                }
                return fen
            },
            cellEnter(e) {
                const target = $(e.target)
                const cellNumber = target.data('number')
                let allowedToPlace = true
                for (let uid in this.piecesOnBoard) {
                    if (this.piecesOnBoard[uid].cellNumber === cellNumber) {
                        allowedToPlace = false
                        break
                    }
                }
                if (this.dragPiece !== null && allowedToPlace) {
                    target.css({'background-color': 'green'})
                }
            },
            cellLeave(e) {
                const target = $(e.target)
                target.css({'background-color': ''})
            },
            cellClick(e) {
                const targetCellNumber = $(e.target).data('number')
                if (this.dragPiece !== null) {
                    const fromCellNumber = this.piecesOnBoard[this.dragPiece.data('uid')].cellNumber
                    this.movePiece(fromCellNumber, targetCellNumber)
                }
            },
            movePiece(fromCellNumber, toCellNumber) {
                const fromCell = this.cells[fromCellNumber]
                const toCell = this.cells[toCellNumber]
                fromCell.empty()
                fromCell.on('click', (event) => this.cellClick(event))
                this.dragPiece.css({'background-color': ''})
                toCell.empty()
                toCell.off('click')
                toCell.append(this.dragPiece)
                this.piecesOnBoard[this.dragPiece.data('uid')].cellNumber = toCell.data('number')
                this.dragPiece.on('click', (event) => this.pieceClick(event))
                this.dragPiece = null
                this.fen = this.extractFen()
                this.moveHistory.push({from: fromCellNumber, to: toCellNumber})
            },
            pieceClick(e) {
                const target = $(e.target)
                if (this.dragPiece === null) {
                    this.dragPiece = target
                    this.dragPiece.css({'background-color': 'blue'})
                } else if (this.dragPiece.data('uid') === target.data('uid')) {
                    this.dragPiece.css({'background-color': ''})
                    this.dragPiece = null
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
            cellNumber(row, col) {
                const cellNumber = (row - 1) * 8 + (col - 1)
                let classObject = {}
                classObject[cellNumber] = true
                return classObject
            }
        },
        mounted() {
            for (let i = 0; i < 64; i++) {
                const cell = $('td.chess-board-cell.' + i)
                cell.attr('data-number', i)
                this.cells[i] = cell
            }
            for (let key in this.piecesMap) {
                const id = this.piecesMap[key].color + '_' + this.piecesMap[key].name
                this.piecesMap[key].template = $('#' + id)
            }

            this.applyFen('rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR')
            // this.connect()
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
