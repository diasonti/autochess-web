<template>
    <div class="container-fluid">
        <div class="col-sm-8 px-0 mx-auto card">
            <div class="card-header text-center">
                <strong>Sign up</strong>
            </div>
            <div class="card-body">
                <div v-if="page === 'form'" :key="page">
                    <form @submit="submitRegistration">
                        <div class="form-group">
                            <label for="usernameInput" class="mb-1"><strong>Username</strong></label>
                            <input type="text" class="form-control" id="usernameInput"
                                   placeholder="Enter username" maxlength="255"
                                   v-model="username" :class="{ 'is-invalid': errors.username.length > 0 }">
                            <div class="invalid-feedback" v-if="errors.username.indexOf('invalid') !== -1">
                                Please enter valid username.
                            </div>
                            <div class="invalid-feedback" v-if="errors.username.indexOf('taken') !== -1">
                                This username is already taken.
                            </div>
                            <small class="form-text text-muted">It will also be your display name.</small>
                        </div>
                        <div class="form-group">
                            <label for="emailInput" class="mb-1"><strong>Email</strong></label>
                            <input type="email" class="form-control" id="emailInput"
                                   placeholder="Enter your email" maxlength="255"
                                   v-model="email" :class="{ 'is-invalid': errors.email.length > 0 }">
                            <div class="invalid-feedback" v-if="errors.email.indexOf('invalid') !== -1">
                                Please enter valid email address.
                            </div>
                            <div class="invalid-feedback" v-if="errors.email.indexOf('taken') !== -1">
                                This email is already taken.
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="passwordInput" class="mb-1"><strong>Password</strong></label>
                            <input type="password" class="form-control" id="passwordInput"
                                   placeholder="Enter password" maxlength="255"
                                   v-model="password" :class="{ 'is-invalid': errors.password.length > 0 }">
                            <div class="invalid-feedback" v-if="errors.password.indexOf('invalid') !== -1">
                                Please enter valid password.
                            </div>
                            <div class="invalid-feedback" v-if="errors.password.indexOf('weak') !== -1">
                                The password is too weak. Make sure it's longer than 5 characters.
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="passwordRepeatInput" class="mb-1"><strong>Repeat password</strong></label>
                            <input type="password" class="form-control" id="passwordRepeatInput"
                                   placeholder="Repeat password" maxlength="255"
                                   v-model="passwordRepeat" :class="{ 'is-invalid': errors.passwordRepeat.length > 0 }">
                            <div class="invalid-feedback" v-if="errors.passwordRepeat.indexOf('not-match') !== -1">
                                Passwords are not same.
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Sign up</button>
                    </form>
                </div>

                <div v-if="page === 'confirmationPrompt'" :key="page">
                    <div class="alert alert-secondary" role="alert">
                      Registration is almost complete.
                    </div>
                    <p>We sent you an email with a link, click on it, and you're good to go.</p>
                </div>

                <div v-if="page === 'confirmationSuccess'" :key="page">
                    <div class="alert alert-success" role="alert">
                        Registration is complete - you can
                        <router-link to="player">play autochess</router-link>
                        now
                    </div>
                </div>
            </div>
        </div>
        <div v-if="loading">
            <FullScreenPreloader />
        </div>
    </div>
</template>

<script>
    import {apiMap} from '../core/config'
    import FullScreenPreloader from '../components/FullScreenPreloader'

    export default {
        name: 'Login',
        components: {FullScreenPreloader},
        props: ['confirmationEmail', 'token'],
        data() {
            return {
                page: 'form',
                loading: false,

                username: '',
                email: '',
                password: '',
                passwordRepeat: '',

                errors: {
                    username: [],
                    email: [],
                    password: [],
                    passwordRepeat: [],
                },
            }
        },
        watch: {
            username(newValue) {
                this.errors = {...this.errors, username: []}
            },
            email(newValue) {
                this.errors = {...this.errors, email: []}
            },
            password(newValue) {
                let errors = []
                if (newValue !== '' && newValue.length < 6)
                    errors.push('weak')
                this.errors = {...this.errors, password: errors}
                if (errors.length === 0)
                    this.errors = {...this.errors, passwordRepeat: []}
            },
            passwordRepeat(newValue) {
                let errors = []
                if (this.password !== '' && this.errors.password.length === 0 && newValue !== this.password)
                    errors.push('not-match')
                this.errors = {...this.errors, passwordRepeat: errors}
            },
        },
        methods: {
            submitRegistration(e) {
                e.preventDefault()

                this.loading = true

                const formData = new FormData()
                formData.append('username', this.username)
                formData.append('email', this.email)
                formData.append('password', this.password)
                this.axios.post(apiMap.registration, formData)
                    .then((response) => {
                        const responseErrors = response.data.errors
                        this.errors = {...this.errors, ...responseErrors}
                        console.log(responseErrors)
                        if ($.isEmptyObject(responseErrors)) {
                            this.page = 'confirmationPrompt'
                        }
                    })
                    .catch((error) => {
                        console.error(error)
                    })
                    .finally(() => {
                        this.loading = false
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
            attemptEmailConfirmation() {
                this.page = ''
                this.loading = true

                const formData = new FormData()
                formData.append('email', this.confirmationEmail)
                formData.append('token', this.token)
                this.axios.post(apiMap.emailConfirmation, formData)
                    .then((response) => {
                        console.log(response.data)
                        this.page = 'confirmationSuccess'
                    })
                    .catch((error) => {
                        console.error(error)
                        this.page = 'form'
                    })
                    .finally(() => {
                        this.loading = false
                    })
            }
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.$store.dispatch('clearAuthErrorAction')
                if (vm.confirmationEmail && vm.token)
                    vm.attemptEmailConfirmation()
            })
        },
    }
</script>

<style scoped>

</style>
