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
        }
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

    [v-cloak] {
        display: none
    }
</style>
