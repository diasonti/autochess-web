<template>
    <div :style="{ background: 'url(/img/pieces/' + color + '/' + type + '.svg)', width: size, height: size }">
    </div>
</template>

<script>
    export default {
        name: 'Piece',
        props: {
            color: {
                required: true,
                validator: function (value) {
                    return ['white', 'black', 'empty'].indexOf(value) !== -1
                }
            },
            type: {
                required: true,
                validator(value) {
                    return ['king', 'queen', 'rook', 'bishop', 'knight', 'pawn', 'empty'].indexOf(value) !== -1
                }
            },
            size: {
                type: String,
                default: "1000px"
            },
        }
    }
</script>

<style scoped>
    div {
        width: 1000px;
        height: 1000px;
        background-repeat: no-repeat !important;
        background-size: cover !important;
    }
</style>
