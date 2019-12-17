<template>
    <div class="col-md-4 offset-4">
        <ul class="list-unstyled">
            <b-media class="mb-2" tag="li" v-for="user in users" :key="user.id" >
            <b-img slot="aside" src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" blank-color="#abc" width="64" alt="placeholder"></b-img>
            <router-link :to="{ name: 'profile', params: { userId: user.id}}">
                            <h5 class="mt-0 mb-1" align="left">{{user.firstName}} {{user.lastName}}</h5>
            </router-link>
            <p class="mb-0" align="left">
                {{user.info}}
            </p>
            </b-media>
        </ul>
    </div>
</template>
<script>
import api from '../api'
export default {
  props: ['users'],
  data () {
    return {
      
      loggedInUser: parseInt(localStorage.userId)
    }
  },
  methods: {
   
  }

}
</script>>
