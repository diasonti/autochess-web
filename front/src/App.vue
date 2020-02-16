<template>
    <div id="app">
        <Header/>
        <main class="container-fluid mt-4">
            <div class="row">
                <div class="col-sm-9 m-auto">
                    <router-view/>
                </div>
            </div>
        </main>
        <Footer/>
    </div>
</template>

<script>
    import Header from './components/Header'
    import Footer from './components/Footer'

    export default {
        name: 'app',
        components: {
            Footer,
            Header,
        },
        created() {
            const tokenRefreshInterval = 300 // 300 seconds = 5 minutes
            setInterval(() => {
                if (this.$store.getters.tokenGetter) {
                    this.$store.dispatch('refreshTokenAction')
                    if (!this.$store.getters.isAuthenticatedGetter) {
                        this.$router.push({path: '/login', query: {back: 'history'}})
                    }
                }
            }, tokenRefreshInterval * 1000)
        },
    }
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }

    .hidden {
        visibility: hidden;
    }
</style>
