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
    import Piece from './Piece'
    import {baseUrl} from '../config'
    import {piecesMap} from '../utils/chessPieces'
    import {cellNumbers} from '../utils/cellNumbers'

    export default {
        name: 'ChessBoard',
        components: {Piece},
        data() {
            return {
                moveHistory: [
                    // { from: (CellNumber), to: (CellNumber) }
                ],
                cells: [
                    // {key: 'b', name: 'bishop', color: 'white', class: {'selected': true}}, - for each cell
                ],
                draggedNumber: null,
            }
        },
        watch: {
            draggedNumber (newValue, oldValue) {
                if (oldValue) {
                    this.cells[oldValue].class['selected'] = false
                    this.$set(this.cells,oldValue, this.cells[oldValue])
                }
                if (newValue) {
                    this.cells[newValue].class['selected'] = true
                    this.cells[newValue].class['available-move-from'] = false
                    this.$set(this.cells, newValue, this.cells[newValue])
                }
            }
        },
        computed: {
            fen: {
                get() {
                    let fen = ''
                    let empty = 0
                    let column = 0
                    for (let i = 0; i < 64; i++) {
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
                        if (cell.key !== piecesMap.blank.key) {
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
                set(fen) {
                    let pos = 0
                    for (let i = 0; pos < 64; i++) {
                        const symbol = fen[i]
                        if (piecesMap[symbol]) {
                            this.$set(this.cells, pos, {...piecesMap[symbol], class: {}})
                            pos++
                        } else if ("12345678".includes(symbol)) {
                            const skip = parseInt(symbol)
                            for (let j = 0; j < skip; j++) {
                                this.$set(this.cells, pos, {...piecesMap.blank, class: {}})
                                pos++
                            }
                        }
                    }
                }
            },
        },
        methods: {
            cellClick(e) {
                const targetCellNumber = e.target.dataset.number
                const targetCell = this.cells[targetCellNumber]
                if (this.draggedNumber === null && targetCell.key !== piecesMap.blank.key) { // Clicked piece, no dragged
                    this.draggedNumber = targetCellNumber
                } else if (this.draggedNumber === targetCellNumber) { // Clicked dragged piece
                    this.draggedNumber = null
                } else if (targetCell.class['available-move-to']) { // Clicked cell with valid move
                    const fromCellNumber = this.draggedNumber
                    this.movePiece(fromCellNumber, targetCellNumber)
                    this.draggedNumber = null
                }
                this.highlightAvailableActions()
            },
            highlightAvailableActions() {
                    for (let i = 0; i < 64; i++) {
                        if (i == this.draggedNumber) // The piece being dragged -> ignore
                            continue

                        const cell = this.cells[i]
                        if (this.draggedNumber !== null) { // Some piece being dragged now
                            if (cell.key === piecesMap.blank.key) { // Empty cell -> possible move
                                cell.class['available-move-to'] = true
                            } else { // Not empty cell -> invalid move
                                cell.class['available-move-to'] = false
                                cell.class['available-move-from'] = false
                            }
                        } else { // No piece being dragged now
                            if (cell.key === piecesMap.blank.key) { // Empty cell -> can't drag this
                                cell.class['available-move-to'] = false
                                cell.class['available-move-from'] = false
                            } else { // Not empty cell -> can drag this
                                cell.class['available-move-from'] = true
                            }
                        }
                        this.$set(this.cells, i, cell)
                    }
            },
            movePiece(fromCellNumber, toCellNumber) {
                for (let i = 0; i < 64; i++) {
                    let cell = this.cells[i]
                    cell.class['last-move-from'] = false
                    cell.class['last-move-to'] = false
                    this.$set(this.cells, i, cell)
                }
                this.$set(this.cells, toCellNumber, {...this.cells[fromCellNumber], class: {'last-move-to': true}})
                this.$set(this.cells, fromCellNumber, {...piecesMap.blank, class: {'last-move-from': true}})
                this.moveHistory.unshift({from: fromCellNumber, to: toCellNumber})
            },
            connect() {
                const ctx = this
                // Server-side events receiving
                this.$sse(baseUrl + '/test/moves?startingFen=' + ctx.fen, {format: 'json'}) // or {format: 'plain'}
                    .then(sse => {
                        console.log('sse: ', sse)
                        // Catch any errors (ie. lost connections, etc.)
                        sse.onError(e => {
                            console.error('SSE Error', e);
                        });

                        sse.subscribe('move', data => {
                            console.info('Received a message with event: move', data);
                            ctx.movePiece(data.from, data.to)
                        });

                        sse.subscribe('close', data => {
                            console.info('Received a message with event: close', data);
                            sse.close();
                        });

                        // Listen for messages without a specified event
                        sse.subscribe('', data => {
                            console.warn('Received a message w/o an event!', data);
                        });
                    })
                    .catch(err => {
                        // When this error is caught, it means the initial connection to the
                        // events server failed.  No automatic attempts to reconnect will be made.
                        console.error('Failed to connect to server', err);
                    });
            },
            cellNumber(row, col) {
                return cellNumbers[row][col]
            }
        },
        created() {
            for (let i = 0; i < 64; i++) {
                this.cells[i] = {...piecesMap.blank, class: {}}
            }
        },
        mounted() {
            this.fen = 'rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR'
            this.highlightAvailableActions()
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
    .selected:hover {
        background: rgb(62, 128, 20) !important; /* Copy of available-move-to */
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
