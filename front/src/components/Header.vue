<template>
    <header>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <router-link to="/" class="navbar-brand">Autochess</router-link>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ml-auto">
                    <router-link tag="li" to="/login" class="nav-item" v-if="!isAuthenticated">
                        <a class="nav-link" href="#">
                            <font-awesome-icon icon="sign-in-alt"/>
                            Sign in</a>
                    </router-link>

                    <li class="nav-item dropdown" v-if="isAuthenticated">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <font-awesome-icon icon="user-circle"/>
                            Profile
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <router-link to="/profile" class="dropdown-item">My profile</router-link>
                            <router-link to="/profile" class="dropdown-item">Settings</router-link>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="#" @click="logout">
                                <font-awesome-icon icon="sign-out-alt"/>
                                Log out
                            </a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
    </header>
</template>

<script>
    export default {
        name: 'Header',
        computed: {
            isAuthenticated() {
                return this.$store.getters.isAuthenticatedGetter
            },
        },
        methods: {
            logout() {
                this.$store.dispatch('logoutAction')
                this.$router.push('/')
            },
        },
    }
</script>

<style scoped>

</style>
