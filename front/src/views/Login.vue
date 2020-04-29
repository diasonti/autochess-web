<template>
    <div class="container-fluid">
        <div class="col-sm-8 px-0 mx-auto card">
            <div class="card-header text-center">
                <strong>Sign in</strong>
            </div>
            <div class="card-body">
                <form @submit="submitLogin">
                    <div class="alert alert-danger" role="alert" v-if="$store.getters.authErrorGetter">
                        Invalid username or password
                    </div>
                    <div class="form-group">
                        <label for="loginUsernameInput" class="mb-1"><strong>Username</strong></label>
                        <input type="text" class="form-control" id="loginUsernameInput" placeholder="Enter username"
                               v-model="loginUsername">
                    </div>
                    <div class="form-group">
                        <label for="loginPasswordInput" class="mb-1"><strong>Password</strong></label>
                        <input type="password" class="form-control" id="loginPasswordInput" placeholder="Enter password"
                               v-model="loginPassword">
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Sign in</button>
                </form>
                <p class="mt-3 mb-0 text-center">
                    Don't have an account? <router-link to="/registration">Sign up</router-link>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
    import {apiMap} from '../core/config'

    export default {
        name: 'Login',
        props: ['back'],
        data() {
            return {
                loginUsername: '',
                loginPassword: '',
            }
        },
        methods: {
            submitLogin(e) {
                e.preventDefault()
                if (this.loginUsername.length === 0 || this.loginPassword.length === 0)
                    return
                const formData = new FormData()
                formData.append('username', this.loginUsername)
                formData.append('password', this.loginPassword)
                this.axios.post(apiMap.login, formData)
                    .then((response) => {
                        this.$store.dispatch('loginSuccessAction')
                        this.handleSuccessLogin()
                    })
                    .catch((error) => {
                        this.$store.dispatch('setAuthErrorAction', {error: error.response})
                    })
            },
            handleSuccessLogin() {
                if (this.back === 'history') {
                    this.$router.go(-1)
                } else if (this.back) {
                    this.$router.go(this.back)
                } else {
                    this.$router.push('/player')
                }
            },
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.$store.dispatch('clearAuthErrorAction')
            })
        },
    }
</script>

<style scoped>

</style>
