<template>
    <div class="container-fluid">
        <div class="row mt-3">
            <div class="col-sm-4">
                <button class="btn btn-primary find-game-button" v-if="!searchInProgress" @click="startSearch">
                    Find game
                </button>
                <button class="btn btn-danger find-game-button" v-else @click="stopSearch">Stop search</button>
            </div>
            <div class="col-sm-8">
                <h3 :class="{'hidden': !searchInProgress}">{{ searchTitle }}</h3>
                <div class="row">
                    <div class="col-sm-6" :class="{'hidden': !searchInProgress}">
                        <h4>Elapsed: {{ searchStopwatchSeconds | time }}</h4>
                    </div>
                    <div class="col-sm-6 text-right">
                        <p>{{ playersOnline }} players online</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-3">
            <div class="col-sm-4">
                <div class="card" style="height: 100%">
                    <div class="card-body">
                        <h5 class="card-title">{{ player.username }}</h5>
                        <h6 class="card-subtitle mb-2 text-muted">Rank: {{ player.rank }}</h6>
                    </div>
                </div>
            </div>
            <div class="col-sm-8">
                <div class="card" style="height: 100%">
                    <div class="card-body">
                        <h5 id="game_history_title" class="card-title">Game history</h5>
                        <table class="table table-bordered table-hover" aria-describedby="game_history_title">
                            <thead>
                            <tr>
                                <th scope="col">Opponent</th>
                                <th scope="col">Result</th>
                                <th scope="col">Rank</th>
                                <th scope="col">Date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(match, index) in matchHistory" :key="index" class="clickable"
                                @click="alert('TODO: Show game details page')">
                                <td>{{ match.opponentUsername }}</td>
                                <td :class="{'text-success': match.winner === match.color, 'text-danger': match.winner !== match.color}">
                                    {{ match.winner === match.color ? 'Win' : 'Lose' }}
                                </td>
                                <td :class="{'text-success': match.rankDelta >= 0, 'text-danger': match.rankDelta < 0}">
                                    {{ match.rankDelta > 0 ? '+' + match.rankDelta : match.rankDelta }}
                                </td>
                                <td>{{ match.finishedAt | localDateTime }}</td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="text-right">
                            <button class="btn btn-secondary" v-show="matchHistoryPage >= 0" @click="loadMoreMatchHistory">
                                Show more
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {apiMap, baseUrl} from '../core/config'

    export default {
        name: 'Home',
        data() {
            return {
                searchTitle: 'Searching for an opponent',
                searchStopwatchSeconds: 5000,
                searchStopwatchInterval: null,
            }
        },
        computed: {
            player() {
                return this.$store.getters.authenticatedUserGetter
            },
            matchHistory() {
                return this.$store.getters.matchHistoryGetter
            },
            matchHistoryPage() {
                return this.$store.getters.matchHistoryPageGetter
            },
            playersOnline() {
                return this.$store.getters.playersOnlineGetter
            },
            searchInProgress() {
                return this.searchStopwatchInterval !== null
            },
        },
        methods: {
            startSearch() {
                this.axios.post(apiMap.searchStart)
            },
            stopSearch() {
                this.axios.post(apiMap.searchStop)
            },
            listen() {
                const retryInterval = 1000;
                this.$sse(baseUrl + apiMap.stream, {format: 'json', withCredentials: true}) // or {format: 'plain'}
                    .then((sse) => {
                        // Catch any errors (ie. lost connections, etc.)
                        sse.onError((e) => {
                            console.error('SSE Listening error', e)
                            sse.close()
                            setTimeout(() => this.listen(), retryInterval)
                        })

                        sse.subscribe('search.started', (data) => {
                            console.info('SSE Received a message with event: search.started', data)
                            this.handleSearchStarted()
                        })

                        sse.subscribe('search.stopped', (data) => {
                            console.info('SSE Received a message with event: search.stopped', data)
                            this.handleSearchStopped()
                        })

                        sse.subscribe('game.found', (data) => {
                            console.info('SSE Received a message with event: game.found', data)
                            this.handleGameFound(data['gameId'])
                        })

                        sse.subscribe('keep.alive', (data) => {
                            // console.info('SSE Received a message with event: keep.alive', data)
                        })

                        // Listen for messages without a specified event
                        sse.subscribe('', (data) => {
                            console.warn('SSE Received a message w/o an event!', data)
                        })
                    })
                    .catch((error) => {
                        // When this error is caught, it means the initial connection to the events server failed.
                        console.error('SSE Failed to connect to server', error)
                        setTimeout(() => this.listen(), retryInterval)
                    })
            },
            handleSearchStarted() {
                this.searchStopwatchSeconds = 0
                this.searchTitle = 'Searching for an opponent'
                this.searchStopwatchInterval = setInterval(() => {
                    this.searchStopwatchSeconds++
                    if (this.searchTitle.endsWith('.....')) {
                        this.searchTitle = 'Searching for an opponent'
                    } else {
                        this.searchTitle += '.'
                    }
                }, 1000)
            },
            handleSearchStopped() {
                clearInterval(this.searchStopwatchInterval)
                this.searchStopwatchInterval = null
            },
            handleGameFound(gameId) {
                console.log('Game found ', gameId)
                this.handleSearchStopped()
                // TODO: Redirect to the game component
            },
            loadMoreMatchHistory() {
                this.$store.dispatch('fetchMatchHistoryAction')
            },
            alert: (message) => alert(message),
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.listen()
                if (vm.matchHistoryPage === 0) {
                    vm.$store.dispatch('fetchMatchHistoryAction')
                }
            })
        },
    }
</script>

<style scoped>
    .find-game-button {
        height: 100px;
        width: 100%;
        font-size: 35px;
    }

    .clickable:hover {
        cursor: pointer;
    }
</style>
