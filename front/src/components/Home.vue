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
                        <p>Estimated: 03:17</p>
                    </div>
                    <div class="col-sm-6 text-right">
                        <p>12 345 players online</p>
                        <p>1 234 players in game</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-3">
            <div class="col-sm-4">
                <div class="card" style="height: 100%">
                    <div class="card-body">
                        <h5 class="card-title">{{ player.username }}</h5>
                        <h6 class="card-subtitle mb-2 text-muted">Rank: {{ 322 }}</h6>
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
                                <th scope="col">ID</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(game, index) in gameHistory" :key="index" class="clickable"
                                @click="alert('TODO: Show game details page')">
                                <td>{{ game.opponent }}</td>
                                <td :class="{'text-success': game.win, 'text-danger': !game.win}">
                                    {{ game.win }}
                                </td>
                                <td :class="{'text-success': game.win, 'text-danger': !game.win}">
                                    {{ game.rankDelta > 0 ? '+' + game.rankDelta : game.rankDelta }} ({{ game.rank }})
                                </td>
                                <td>{{ game.id }}</td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="text-right">
                            <button class="btn btn-secondary" @click="alert('TODO: Show game history table page')">
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
    import {apiMap, baseUrl} from '../config'

    export default {
        name: 'Home',
        data() {
            return {
                searchTitle: 'Searching for an opponent',
                searchStopwatchSeconds: 5000,
                searchStopwatchInterval: null,
                gameHistory: [
                    {id: 21, opponent: 'Mark', win: true, rankDelta: 25, rank: 1225},
                    {id: 17, opponent: 'Jacob', win: false, rankDelta: -20, rank: 1200},
                ],
            }
        },
        computed: {
            player() {
                return this.$store.getters.authenticatedUserGetter
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
                this.$sse(baseUrl + apiMap.stream, {format: 'json', withCredentials: true}) // or {format: 'plain'}
                    .then((sse) => {
                        // Catch any errors (ie. lost connections, etc.)
                        sse.onError((e) => {
                            console.error('SSE Listening error', e)
                            sse.close()
                            setTimeout(() => this.listen(), 333)
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
                            console.info('SSE Received a message with event: keep.alive', data)
                        })

                        // Listen for messages without a specified event
                        sse.subscribe('', (data) => {
                            console.warn('SSE Received a message w/o an event!', data)
                        })
                    })
                    .catch((error) => {
                        // When this error is caught, it means the initial connection to the events server failed.
                        console.error('SSE Failed to connect to server', error)
                        setTimeout(() => this.listen(), 333)
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
                }, 700)
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
            alert: (message) => alert(message),
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.listen()
            })
        },
        filters: {
            time: function (value) {
                if (!value || value === 0)
                    return '00:00'
                const seconds = parseInt(value)
                let sec = seconds % 60
                if (sec < 10)
                    sec = '0' + sec

                let min = Math.floor(seconds / 60) % 60
                if (min < 10)
                    min = '0' + min

                let hr = Math.floor(seconds / 3600)
                if (hr > 0 && hr < 10)
                    hr = '0' + hr

                let time = min + ':' + sec
                if (hr > 0)
                    time = hr + ':' + time
                return time
            },
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
