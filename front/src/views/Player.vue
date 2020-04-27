<template>
    <div>
        <Board :fen="fen" :on-piece-move="logMove" />
        <input class="form-control" type="text" v-model="fen">
    </div>
</template>

<script>
    import Board from '../components/Board'

    export default {
        name: 'Player',
        components: {Board},
        data() {
            return {
                fen: 'rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR'
            }
        },
        methods: {
            logMove(from, to) {
                console.log("Move", from, to)
            }
        }
    }
</script>

<style scoped>

</style>
