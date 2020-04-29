<template>
    <div>
        <GameHeader :stage="stage" />
        <div class="alert alert-secondary text-center">
            <h4 class="m-0">Your color: <strong class="text-capitalize">{{ color }}</strong></h4>
        </div>
        <Board :fen="board" :on-piece-move="attemptMove" :interactive-color="color" class="mx-auto"/>
    </div>
</template>

<script>
    import Board from './Board'
    import {apiMap} from '../core/config'
    import GameHeader from './GameHeader'

    export default {
        name: 'Game',
        components: {GameHeader, Board},
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
