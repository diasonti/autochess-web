<template>
    <div ondragstart="return false;" ondrop="return false;">
        <div style="display: none;">
            <Piece v-for="piece in piecesMap" :key="piece.key" :color="piece.color" :type="piece.name" size="70px" :id="piece.color + '_' + piece.name" />
        </div>
        <table>
            <tr v-for="row in 8" :key="row">
                <td v-for="col in 8" :key="col" class="chess-board-cell" :class="cellNumber(row, col)"
                    @mouseenter="cellEnter" @mouseleave="cellLeave">
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
                },
                piecesOnBoard: {
                    // uid: { key: 'k', cellNumber: 0, element: $(piece) },
                },
                dragPiece: null,
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
                this.cells.forEach(cell => {
                    cell.empty()
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
                        const uid = Math.random().toString(36).substr(2, 9);
                        newPiece.attr('data-uid', uid)
                        newPiece.on('click', (e) => this.pieceClick(e))
                        this.piecesOnBoard[uid] = {
                            key: piece.key,
                            cellNumber: pos,
                            element: newPiece
                        }
                        pos++;
                    } else if ("12345678".includes(symbol)) {
                        pos += parseInt(symbol)
                    }
                }
            },
            cellEnter(e) {
                const target = $(e.target)
                const cellNumber = target.data('number');
                let allowedToPlace = true;
                for (let uid in this.piecesOnBoard) {
                    if (this.piecesOnBoard[uid].cellNumber === cellNumber) {
                        allowedToPlace = false;
                        break;
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
                const target = $(e.target)
                if (this.dragPiece !== null) {
                    const fromCell = this.cells[this.piecesOnBoard[this.dragPiece.data('uid')].cellNumber]
                    fromCell.empty()
                    fromCell.on('click', (event) => this.cellClick(event))
                    this.dragPiece.css({'background-color': ''})
                    target.empty()
                    target.off('click')
                    target.append(this.dragPiece)
                    this.piecesOnBoard[this.dragPiece.data('uid')].cellNumber = target.data('number')
                    this.dragPiece.on('click', (event) => this.pieceClick(event))
                    this.dragPiece = null
                }
            },
            pieceClick(e) {
                const target = $(e.target)
                if (this.dragPiece === null) {
                    this.dragPiece = target
                    this.dragPiece.css({'background-color': 'blue'})
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
