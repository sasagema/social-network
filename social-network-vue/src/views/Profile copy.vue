<template>
<div class="col-md-4 offset-4">
  <b-card>
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
    <b-button align="right" class="float-sm-right" v-if="!friend && loggedInUser != user.id" @click="addFriend(user.id)">Add friend</b-button>
    <b-button align="right" class="float-sm-right" v-if="friend && loggedInUser != user.id" @click="removeFriend(user.id)" >Remove friend</b-button>
    </b-card>
    <div v-if="loggedInUser === user.id">
    <b-card class="mb-2" align="left">
            <b-media>
                <b-img slot="aside" src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" blank-color="#ccc" width="64" alt="placeholder"></b-img>
                <router-link :to="{ name: 'profile', params: { userId: user.userId}}">
                    <h5 class="mt-0">{{newPost.userFullName}}</h5>
                </router-link>
                <b-form-textarea
                    id="content"
                    v-model="newPost.content"
                    placeholder="Podeli nesto sa prijateljima..."
                    rows="3"
                    max-rows="6"
                ></b-form-textarea>
            </b-media>          
            <br>
            <b-button  variant="primary" align="right" @click="createPost()" class="float-sm-right">Add post</b-button>
    </b-card>
    </div>
      <b-card class="mb-2" align="left" v-for="post in posts" :key="post.id">
              <div align='right'>
                  <label class="dislikes">{{post.dislikesCount}}</label>
                  <label>|</label>
                  <label class="likes">{{post.likesCount}}</label>
                  </div>
              <b-media>
                  <b-img src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"  slot="aside" blank-color="#ccc" width="64" alt="Profilna slika"></b-img>
                  <router-link :to="{ name: 'profile', params: { userId: post.userId}}">
                      <h5 class="mt-0">{{post.userFullName}}</h5>
                  </router-link>
                  <br>
              <p>{{post.content}}</p>
              </b-media>
              <!--<b-button align="right" class="float-sm btn-sm" >Izmeni</b-button> -->
              <b-button align="right" class="float-sm  btn-sm btn-danger" @click="deletePost(post.id)" v-if="user.id == loggedInUser">Delete</b-button>

              <b-button  @click="buttonClicked"   variant="outline-success" align="right" class="float-sm-right btn-sm like"><i class="arrow up"></i></b-button>
              <b-button  @click="buttonClicked"    variant="outline-danger" align="right" class="float-sm-right btn-sm dislike"><i class="arrow down"></i></b-button>
      </b-card>
</div>
</template>

<script>
import api from '../api'
import router from '../router'
export default {
  props: ['userId', 'isFriend'],
  name: 'profile',

  data () {
    return {
      user: {},
      friend: this.isFriend,
      loggedInUser: parseInt(localStorage.userId),
      posts: [],
      newPost: {
                userId: this.loggedInUser,
                userFullName: localStorage.userFullName,
                likesCount: 0,
                dislikesCount: 0,
                content: '',
                date: '',
      }
    }
  },
  async created(){
    this.getUser(this.userId)
    this.getPosts(this.userId)
  },
  methods: {
    async getUser (id) {
      this.user = await api.getOne('user', id)
    },
    async getPosts (id) {
      this.posts = (await api.getAllByUser('usersPosts', id)).reverse()
    },
    async createPost(){
        this.newPost.date = new Date()
      console.log(this.newPost)
      if(this.newPost.content !== ''){
        this.posts.splice(0,0, await api.create('post', this.newPost))
      }
    },
    async deletePost(id){
      console.log(api.delete('post', id).resource)
      return posts.filter(function(post){
        return post.id != id;
      });
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
      this.friend = true
    },
    async removeFriend(id){
      let data = {
        userOneId: parseInt(localStorage.userId),
        userTwoId: id,
        sent: true,
        accepted:true,
        blocked: true
      }
      console.log(data)
      alert(await api.create('removeFriend',data))
      this.friend = false
    }

  }
}
</script>

<style scoped>
.likes{
    color: green;
    padding: 2px;
}
.dislikes{
    color: red;
        padding: 2px;

}
i {
  border: solid black;
  border-width: 0 3px 3px 0;
  display: inline-block;
  padding: 3px;
}


.up {
  transform: rotate(-135deg);
  -webkit-transform: rotate(-135deg);
}

.down {
  transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
}
</style>
