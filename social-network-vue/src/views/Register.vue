<template>
<div class="col-md-4 offset-4">
    <form @submit.prevent="register">
        <h1 class="h3 mb-3 mt-3 font-weight-normal">Register</h1>
        <label for="firstName" class="sr-only" > First name</label>

        <input type="text" id="email" class="form-control" @click=" error='' " v-model="user.firstName" placeholder="First name" required autofocus>
        <br>
        <label for="lastName" class="sr-only"> Last name</label>

        <input type="text" id="email" class="form-control" @click=" error='' " v-model="user.lastName" placeholder="Last name" required autofocus>
        <br>
        <label for="info" class="sr-only">About you...</label>

        <input type="text" id="email" class="form-control" @click=" error='' " v-model="user.info" placeholder="About you..." required autofocus>
        <br>
        <label for="email" class="sr-only"> E-mail</label>

        <input type="email" id="email" class="form-control" @click=" error='' " v-model="user.email" placeholder="E-mail" required autofocus>
        <br>
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" class="form-control" @click=" error='' " v-model="user.password" placeholder="Password" required>
        <br>
        <label for="repeatPassword" class="sr-only">Repeat password</label>
        <input type="password" id="repeatPassword" class="form-control" @click=" error='' " v-model="repeatPassword" placeholder="Repeat password" required>
        <p class="error">{{error}}</p>
        <br>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Register</button>
    </form>
        <router-link to="/login" tag="b-button" class="btn btn-lg btn-block mt-sm-2" >Log in</router-link>

</div>
</template>

<script>
import http from '../http-common'
import router from '../router'
// eslint-disable-next-line
// /* eslint-disable */
export default {
    data(){
        return{
            user: {
                firstName: '',
                lastName: '',
                info: '',
                email: '',
                password: '',
            },
            error: '',
            repeatPassword: ''
        }
    },
    methods:{
        register(){


            var inValid = new RegExp('^[_A-z0-9]{6,}$');
            var value = this.user.password
            var isValid = inValid.test(value);

            if(this.user.password === this.repeatPassword && isValid){
                http
                .post("/register", this.user)
                .then(response => {

                    alert('Registration successful.')
                    router.push({ path: '/login' })
                    router.go()
                })
                .catch(e => {
                    console.log(e)
                    alert(e)

                })

            }else{
                this.error = "Passwords don't match."

            }


        }
    }
}
</script>
<style>
.error{
    color: red;
}
</style>
