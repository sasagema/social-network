<template>

<div >
  <b-card class="col-md-8 offset-2 mb-3">
    <b-media no-body>
      <b-media-aside vertical-align="left">
        <b-img alt="Profilna slika" src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" blank-color="#ccc" width="256" height="256"></b-img>
      </b-media-aside>
        <b-media-body class="ml-3">
            <h5 class="mt-0">User profile</h5>
            <table class="table" >
                <tbody class="text-left">
                    <tr>
                        <td>First name:</td>
                        <td>{{user.firstName}}</td>
                    </tr>
                    <tr>
                        <td>Last name:</td>
                        <td>{{user.lastName}}</td>
                    </tr>
                    <tr>
                        <td>E-mail:</td>
                        <td>{{user.email}}</td>
                    </tr>
                    <tr>
                        <td>Info:</td>
                        <td>{{user.info}}</td>
                    </tr>
                </tbody>
            </table>
            </b-media-body>
    </b-media>
    <b-button align="right" class="float-sm-right" v-if="!friends.includes(user.id)  && loggedInUser != user.id" @click="addFriend(user.id)">Add friend</b-button>
    <b-button align="right" class="float-sm-right" v-if="friends.includes(user.id) && loggedInUser != user.id" @click="removeFriend(user.id)" >Remove friend</b-button>
    </b-card>

    <Post class="col-md-6 offset-3" v-bind:posts="posts" v-bind:userId="user.id" v-bind:isProfile="true"/>
</div>
</template>

<script>
import api from '../api'
import router from '../router'
import Post from '../components/Post.vue'

export default {
  props: ['userId', 'isFriend'],
  name: 'profile',
  components: {
    Post
  },
  data () {
    return {
      user: {},
      friend: true,
      loggedInUser: parseInt(localStorage.userId),
      posts: [],
      friends: []
    }
  },
  async created(){
    this.getUser(this.userId)
    this.getPosts(this.userId)
    this.getFriends(this.loggedInUser)
  },
  methods: {
    async getUser (id) {
      this.user = await api.getOne('user', id)
    },
    async getPosts (id) {
      this.posts = (await api.getAllByUser('usersPosts', id)).reverse()
    },
    async getFriends (id) {
      this.friends = (await api.getAllByUser('usersFriendsIds', id))
    },
    async addFriend(id){
      let data = {
        userOneId: this.loggedInUser,
        userTwoId: id,
        sent: true,
        accepted:true,
        blocked: true
      }
      alert(await api.create('addFriend',data))
      this.friends.push(id)
    },
    async removeFriend(id){
      let data = {
        userOneId: parseInt(localStorage.userId),
        userTwoId: id,
        sent: true,
        accepted:true,
        blocked: true
      }
      alert(await api.create('removeFriend',data))
      this.friends.pop(id)

    }

  }
}
</script>

<style scoped>

</style>
