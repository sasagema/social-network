<template>
    <div>
      <b-card bg-variant="light" header="Friends">
        <ul class="list-unstyled">
           
            <b-media class="mb-2" tag="li" v-for="friend in friends" :key="friend.id" >
               
              <b-img slot="aside" src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" blank-color="#abc" width="64" alt="placeholder"></b-img>
              <router-link :to="{ name: 'profile', params: { userId: friend.id, isFriend: true}}">
                            <h5 class="mt-0 mb-1" align="left">{{friend.firstName}} {{friend.lastName}}</h5>
              </router-link>
              <p class="mb-0" align="left">
                {{friend.info}}
              </p>
              
            </b-media>
           
        </ul>
      </b-card>
</div>
</template>
<script>
import api from '../api'
export default {

  data () {
    return {
      friends: [],
      loggedInUser: parseInt(localStorage.userId)
    }
  },
  mounted () {
    this.getFriends()
  },
  methods: {
    async getFriends () {
      this.friends = await api.getAllByUser('usersFriends', this.loggedInUser)
    }
  }

}
</script>>
