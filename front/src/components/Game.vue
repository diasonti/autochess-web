<template>
    <div>
        <h3>Current stage: {{stage}}</h3>
        <h3>Your color: {{color}}</h3>
        <Board :fen="board" :on-piece-move="attemptMove" :interactive-color="color" class="m-auto"/>
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
            color() {
                return this.state.color ? this.state.color.toLowerCase() : null
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
