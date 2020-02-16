<template>
    <form @submit="submitLogin">
        <div class="alert alert-danger" role="alert" v-if="$store.getters.getError">
            Invalid username or password
        </div>
        <div class="form-group">
            <label for="loginUsernameInput">Username</label>
            <input type="text" class="form-control" id="loginUsernameInput" placeholder="Enter username"
                   v-model="loginUsername">
        </div>
        <div class="form-group">
            <label for="loginPasswordInput">Password</label>
            <input type="password" class="form-control" id="loginPasswordInput" placeholder="Enter password"
                   v-model="loginPassword">
        </div>
        <button type="submit" class="btn btn-primary">Sign in</button>
    </form>
</template>

<script>
    import {apiMap} from '../config'

    export default {
        name: 'Login',
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
                    return;
                const formData = new FormData()
                formData.append('username', this.loginUsername)
                formData.append('password', this.loginPassword)
                this.axios.post(apiMap.login, formData)
                    .then(response => {
                        this.$store.dispatch('setToken', {token: response.data.token})
                        // this.$router.push({path: 'home'})
                    })
                    .catch((error) => {
                        this.$store.dispatch('setLoginError', {error: error})
                    })
            },
        },
    }
</script>

<style scoped>

</style>
