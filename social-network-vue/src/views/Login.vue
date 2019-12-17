<template>
    <div class="col-md-4 offset-4">
    <form @submit.prevent="login()">
        <h1 class="h3 mb-3 mt-3 font-weight-normal">Log in</h1>
        <label for="email" class="sr-only" v-bind="email"> E-mail</label>

        <input type="text" id="email" class="form-control" @click=" error='' " v-model="user.email" placeholder="E-mail" required autofocus>
        <br>
        <label for="password" class="sr-only" v-bind="password">Password</label>
        <input type="password" id="password" class="form-control" @click=" error='' " v-model="user.password" placeholder="Lozinka" required>
        <p class="error">{{error}}</p>
        <br>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
    </form>
    <router-link to="/register" tag="b-button" class="btn btn-lg btn-block mt-sm-2" >Register</router-link>
    </div>
</template>

<script>
import http from '../http-common'
import router from '../router'
// eslint-disable-next-line
/* eslint-disable */
export default {
    data(){
        return{
            user: {
                email: '',
                password: ''
            },
            error: ''
        }
    },
    methods:{
        login(){
            console.log(this.user)
            http
                .post("/login", this.user)
                .then(response => {
                    var loggedUser = response.data
                    localStorage.setItem('userId', loggedUser.id)
                    localStorage.setItem('userEmail', loggedUser.email)
                    localStorage.setItem('userFullName', loggedUser.firstName + " " + loggedUser.lastName)
                    console.log(response)
                    //alert('Welcome ' + loggedUser.firstName + " " + loggedUser.lastName)
                    router.push({ path: '/' })
                    router.go()    
                })
                .catch(e => {
                    console.log(e)
                    this.error = "Wrong email/password."
                })   
        },
        register(){

        }
    }
}
</script>
<style>
.error{
    color: red;
}
</style>