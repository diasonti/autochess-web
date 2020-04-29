<template>
    <div class="card">
        <transition name="fade" mode="out-in" appear>
            <h5 class="card-header text-center" v-if="stage === 'PRE_PLACEMENT'" :key="stage">
                <strong>Planning</strong>
            </h5>
            <h5 class="card-header text-center" v-if="stage === 'PLACEMENT'" :key="stage">
                <strong>Placement</strong>
            </h5>
            <h5 class="card-header text-center" v-if="stage === 'GAME'" :key="stage">
                <strong>Game</strong>
            </h5>
        </transition>
        <div class="card-body text-center">
            <transition name="fade" mode="out-in" appear>
                <p class="card-text" v-if="stage === 'PRE_PLACEMENT'" :key="stage">
                    Look at your pieces and think of possible strategy
                </p>
                <p class="card-text" v-if="stage === 'PLACEMENT'" :key="stage">
                    Move your pieces to the most advantageous positions <br/>
                    <small class="text-muted">Beware: your opponent will do the same</small>
                </p>
                <p class="card-text" v-if="stage === 'GAME'" :key="stage">
                    Observe your pieces playing for you
                </p>
            </transition>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'GameHeader',
        props: ['stage']
    }
</script>

<style scoped>

</style>
