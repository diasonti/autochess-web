<template>
    <div>
        <Board :fen="board" :on-piece-move="attemptMove"/>
        <input class="form-control" type="text" v-model="board">
    </div>
</template>

<script>
    import Board from './Board'
    import {apiMap} from '../core/config'

    export default {
        name: 'Game',
        components: {Board},
        props: ['state'],
        computed: {
            stage() {
                return this.state.stage
            },
            board() {
                return this.state.board
            },
            movablePieces() {
                return this.state.movablePieces
            },
            opponentProfile() {
                return this.state.opponentProfile
            },
        },
        methods: {
            attemptMove(from, to) {
                if (this.stage !== 'PLACEMENT')
                    return;
                console.log("attemptMove", from, to)
                const formData = new FormData()
                formData.append('fromCell', from)
                formData.append('toCell', to)
                this.axios.post(apiMap.moveIntent, formData)
            },
        }
    }
</script>

<style scoped>

</style>
