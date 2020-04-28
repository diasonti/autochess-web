<template>
    <div v-cloak>
        <div v-if="status === 'MENU'">
            <Menu :state="menuState" />
        </div>

        <div v-if="status === 'GAME'">
            <Game :state="gameState" />
        </div>
    </div>
</template>

<script>
    import Menu from '../components/Menu'
    import Game from '../components/Game'
    import {apiMap} from '../core/config'

    export default {
        name: 'Player',
        components: {Game, Menu},
        data() {
            return {
                // status: 'MENU',
                status: 'GAME',
                menuState: {
                    profile: {
                        username: 'PlayerBish',
                        rank: '1234',
                        matchHistory: [
                            {
                                opponentUsername: 'OpponentBish',
                                color: 'WHITE',
                                winner: 'WHITE',
                                rankDelta: 15,
                                finishedAt: [1, 2, 3, 4, 5, 6],
                            },
                            {
                                opponentUsername: 'otherBish',
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
                    stage: 'PLACEMENT',
                    board: 'qrp5/8/8/8/8/8/8/5NBB',
                    movablePieces: [
                        { id: 1, piece: 'B', position: 63 },
                        { id: 2, piece: 'B', position: 62 },
                        { id: 3, piece: 'K', position: 61 },
                    ],
                    opponentProfile: {
                        username: 'OpponentBish',
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
