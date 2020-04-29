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
                status: 'LOADING',
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
                    stage: 'PRE_PLACEMENT',
                    board: 'qrp5/8/8/8/8/8/8/5NBB',
                    movablePieces: [
                        { id: 1, piece: 'B', position: 63 },
                        { id: 2, piece: 'B', position: 62 },
                        { id: 3, piece: 'K', position: 61 },
                    ],
                    opponentProfile: {
                        username: 'opponent1',
                        rank: '1234',
                    }
                },
                fetchStateInterval: null,
                fetchStateInProgress: false,
            }
        },
        methods: {
            fetchState() {
                if (this.fetchStateInProgress)
                    return
                this.fetchStateInProgress = true
                this.axios.get(apiMap.fetchState)
                    .then((response) => {
                        // console.log(response.data)
                        const status = response.data.status
                        if (status === 'MENU') {
                            this.menuState = response.data
                        }
                        if (status === 'GAME') {
                            this.gameState = response.data
                        }
                        if (status === 'ANONYMOUS') {
                            clearInterval(this.fetchStateInterval)
                            this.$router.replace('login')
                            this.$store.replace('login')
                        }
                        this.status = status
                    })
                    .catch((error) => {
                        console.error('fetchState error', error)
                    })
                    .finally(() => {
                        this.fetchStateInProgress = false
                    })
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
