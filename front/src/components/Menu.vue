<template>
    <div class="container-fluid">
        <div class="row mt-3">
            <div class="col-sm-4">
                <button class="btn btn-primary find-game-button" v-if="!search.inProgress" @click="startSearch">
                    Find game
                </button>
                <button class="btn btn-danger find-game-button" v-else @click="stopSearch">Stop search</button>
            </div>
            <transition name="fade" mode="out-in">
                <div class="col-sm-8" v-show="search.inProgress">
                    <h3>Searching for an opponent</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <h4>Elapsed: {{ currentSecond - search.startedAt | time }}</h4>
                        </div>
                    </div>
                </div>
            </transition>
        </div>


        <div class="row mt-3">

            <div class="col-sm-4">
                <div class="card" style="height: 100%">
                    <div class="card-body">
                        <h5 class="card-title">{{ profile.username }}</h5>
                        <h6 class="card-subtitle mb-2 text-muted">Rank: {{ profile.rank }}</h6>
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
                            <tr v-for="(match, index) in profile.matchHistory" :key="index" class="clickable"
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
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {apiMap} from '../core/config'

    export default {
        name: 'Menu',
        props: ['state'],
        data() {
            return {
                searchStopwatchInterval: null,
                currentSecond: Math.round(new Date().getTime() / 1000),
            }
        },
        computed: {
            profile() {
                return this.state.profile
            },
            search() {
                return this.state.search
            },
            statistics() {
                return this.state.statistics
            },
        },
        methods: {
            startSearch() {
                this.axios.post(apiMap.searchStart)
            },
            stopSearch() {
                this.axios.post(apiMap.searchStop)
            },
        },
        created() {
            this.searchStopwatchInterval = setInterval(() => {
                this.currentSecond++
            }, 1000)
        },
        beforeDestroy() {
            clearInterval(this.searchStopwatchInterval)
            this.stopSearch()
        }
    }
</script>

<style scoped>
    .find-game-button {
        height: 100px;
        width: 100%;
        font-size: 30px;
    }

    .clickable:hover {
        cursor: pointer;
    }
</style>
