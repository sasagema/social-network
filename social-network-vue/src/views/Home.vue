<template>
  <div class="home">
    <b-container>
      <b-row >
        <b-col cols="4" >
          <Friends />
        </b-col>
        <b-col cols="8">
          <Post v-bind:posts="posts" v-bind:userId="userId" v-bind:isProfile="false"/>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import Friends from '../components/Friends.vue'
import Post from '../components/Post.vue'
import api from '../api'

export default {
  name: 'home',
  components: {
    Friends,
    Post
  },
  data(){
    return {
      userId: parseInt(localStorage.userId),
      posts: []
    }
    
  },
  async created(){
    this.getPosts()
  },
  methods: {
    async getPosts(){
            this.posts = (await api.getAllByUser('usersFriendsPosts', parseInt(localStorage.userId))).reverse()
        }
    

  }
  
}
</script>
