<template>
    <div>
        <GameHeader :stage="stage" />
        <div class="alert alert-secondary text-center mb-1" v-if="!state.default">
            <h4 class="m-0">Your color: <strong class="text-capitalize">{{ color }}</strong></h4>
        </div>
        <div class="alert alert-secondary text-center mb-1" v-if="!state.default">
            <h6 class="m-0">
                Your opponent: <strong>{{ opponentProfile.username }}</strong>
                <br/>
                Rank: <strong>{{ opponentProfile.rank }}</strong>
            </h6>
        </div>
        <Board :fen="board" :on-piece-move="attemptMove" :interactive-color="interactiveColor" class="mx-auto"/>

        <div class="modal" id="matchResultModal" tabindex="-1" role="dialog" aria-hidden="true"
             style="display: block;" v-if="result">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header justify-content-center">
                        <h5 class="modal-title" v-if="result.result === 'WIN'">WIN!</h5>
                        <h5 class="modal-title" v-if="result.result === 'LOSE'">Lose!</h5>
                        <h5 class="modal-title" v-if="result.result === 'TIE'">It's a tie!</h5>
                    </div>
                    <div class="modal-body text-center">
                        Your rank changed from {{result.rankBefore}} to {{result.rankAfter}}
                    </div>
                    <div class="modal-footer justify-content-center">
                        <button type="button" class="btn btn-secondary" @click="backToMenu">Back to search</button>
                    </div>
                </div>
            </div>
        </div>
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
        data() {
            return {
                showEndingModal: false,
            }
        },
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
            interactiveColor() {
                return this.stage === 'PLACEMENT' ? this.color : null
            },
            movablePieces() {
                return this.state.movablePieces
            },
            opponentProfile() {
                return this.state.opponentProfile
            },
            result() {
                return this.state.result
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
            backToMenu() {
                this.state.backToMenu()
            }
        }
    }
</script>

<style scoped>

</style>
