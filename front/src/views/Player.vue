<template>
    <div v-cloak>
        <transition name="fade" mode="out-in" appear>
            <div v-if="status === 'LOADING'" :key="status">
                <FullScreenPreloader/>
            </div>

            <div v-if="status === 'MENU'" :key="status">
                <Menu :state="menuState"/>
            </div>

            <div v-if="status === 'GAME'" :key="status">
                <Game :state="gameState"/>
            </div>
        </transition>
    </div>
</template>

<script>
    import Menu from '../components/Menu'
    import Game from '../components/Game'
    import {apiMap} from '../core/config'
    import FullScreenPreloader from '../components/FullScreenPreloader'

    export default {
        name: 'Player',
        components: {FullScreenPreloader, Game, Menu},
        data() {
            return {
                actualStatus: 'LOADING',
                forcedStatus: null,
                menuState: {
                    profile: {
                        username: 'username',
                        rank: '1234',
                        matchHistory: [
                            {
                                opponentUsername: 'opponent1',
                                color: 'WHITE',
                                winner: 'WHITE',
                                rankDelta: 15,
                                finishedAt: [1, 2, 3, 4, 5, 6],
                            },
                            {
                                opponentUsername: 'opponent2',
                                color: 'WHITE',
                                winner: 'BLACK',
                                rankDelta: -20,
                                finishedAt: [1, 2, 3, 4, 5, 6],
                            },
                        ],
                    },
                    search: {
                        inProgress: false,
                        startedAt: 1588015898,
                    },
                    statistics: {
                        playersOnline: 322,
                    },
                },
                gameState: {
                    default: true,
                    stage: 'GAME',
                    board: 'qrp5/8/8/8/8/8/8/5NBB',
                    color: 'WHITE',
                    movablePieces: [
                        { id: 1, piece: 'B', position: 63 },
                        { id: 2, piece: 'B', position: 62 },
                        { id: 3, piece: 'K', position: 61 },
                    ],
                    opponentProfile: {
                        username: 'opponent1',
                        rank: '1234',
                    },
                    result: {
                        result: 'TIE',
                        rankBefore: 1000,
                        rankAfter: 1015
                    },
                },
                fetchStateInterval: null,
                fetchStateInProgress: false,
            }
        },
        computed: {
            status() {
                return this.forcedStatus ? this.forcedStatus : this.actualStatus
            }
        },
        methods: {
            fetchState() {
                if (this.fetchStateInProgress)
                    return
                this.fetchStateInProgress = true
                this.axios.get(apiMap.fetchState)
                    .then((response) => {
                        console.log(response.data)
                        let status = response.data.status
                        if (status === 'MENU') {
                            this.menuState = response.data
                            if (response.data.finishedGame) {
                                if (this.forcedStatus !== 'MENU')
                                    this.forcedStatus = 'GAME'
                                this.gameState = {...this.gameState,
                                    stage: 'FINISHED',
                                    result: response.data.finishedGame,
                                    backToMenu: this.backToMenuHandler,
                                }
                            } else {
                                this.forcedStatus = null
                            }
                            if (response.data.search.inProgress) {
                                this.forcedStatus = null
                            }
                        }
                        if (status === 'GAME') {
                            this.gameState = response.data
                            this.forcedStatus = null
                        }
                        if (status === 'ANONYMOUS') {
                            this.$router.replace('login')
                            clearInterval(this.fetchStateInterval)
                            this.forcedStatus = null
                            return
                        }
                        this.actualStatus = status
                    })
                    .catch((error) => {
                        console.error('fetchState error', error)
                    })
                    .finally(() => {
                        this.fetchStateInProgress = false
                    })
            },
            backToMenuHandler() {
                this.forcedStatus = 'MENU'
            },
        },
        mounted() {
            this.fetchStateInterval = setInterval(() => {
                this.fetchState()
            }, 100);
        },
    }
</script>

<style scoped>

</style>
