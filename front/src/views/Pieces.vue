<template>
    <div>
        <Piece :color="cells[0].color" :type="cells[0].name" @click="set(0, {color: 'black', name: 'queen'})" size="100px"/>
        <Piece :color="cells[1].color" :type="cells[1].name" @click="set(1, {color: 'black', name: 'queen'})" size="100px"/>
        <Piece :color="cells[2].color" :type="cells[2].name" @click="set(2, {color: 'black', name: 'queen'})" size="100px"/>
        <button @click="set(0, {color: 'black', name: 'queen'})">Switch</button>
    </div>
</template>

<script>
    import Piece from '../components/Piece'

    export default {
        name: 'Index',
        components: {Piece},
        data() {
            return {
                cells: [{color: 'white', name: 'rook'},{color: 'white', name: 'rook'},{color: 'white', name: 'rook'}]
            }
        },
        mounted() {
            this.$set(this.cells, 0, {color: 'white', name: 'knight'});
            this.$set(this.cells, 1, {color: 'white', name: 'king'});
            this.$set(this.cells, 2, {color: 'white', name: 'pawn'});
        },
        methods: {
            set(index, value) {
                console.log('aa')
                this.$set(this.cells, index, value);
            }
        }
    }
</script>

<style scoped>
</style>
