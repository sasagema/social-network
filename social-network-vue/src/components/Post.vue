<template>
    <div>
        <div v-if="loggedInUser === userId">
        <b-card class="mb-2" align="left">
            <b-media>
                <b-img slot="aside" src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" blank-color="#ccc" width="64" alt="placeholder"></b-img>
                <router-link :to="{ name: 'profile', params: { userId: newPost.userId}}">
                    <h5 class="mt-0">{{newPost.userFullName}}</h5>
                </router-link>
                <b-form-textarea
                    id="content"
                    v-model="newPost.content"
                    placeholder="Share something with your friends..."
                    rows="3"
                    max-rows="6"
                ></b-form-textarea>
            </b-media>
            <br>
            <b-button  align="right" v-on:click="createPost()" class="float-sm-right btn-sm">Add post</b-button>
        </b-card>
        </div>
        <b-card  class="mb-2" align="left" v-for="post in posts" :key="post.id">
            <div align='right' class="date">
               {{parseDate(post.date)}}
            </div>
            <b-media>
                <b-img src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"  slot="aside" blank-color="#ccc" width="64" alt="Profile picture"></b-img>
                <router-link :to="{ name: 'profile', params: { userId: post.userId}}">
                    <h5 class="mt-0">{{post.userFullName}}</h5>
                </router-link>
                <br>
            <b-card bg-variant="light" class=" mb-2" >
                <b-card-text>{{post.content}}</b-card-text>
            </b-card>
            
            </b-media>
            <div>
                <div  align="right">
                    <b-button align="left" class="float-sm  btn-sm btn-danger " @click="deletePost(post.id)" v-if="userId == loggedInUser && isProfile">Delete</b-button>
                    <b-button align="left" class="float-sm ml-2 mr-3 btn-sm btn-warning" @click="showModal(post)" v-if="userId == loggedInUser && isProfile">Edit</b-button>

                    <label class="likes mr-3 label-align-right" >{{post.likesCount}}</label>
                    <b-button   @click="like($event, post)"   variant="outline-success" class="float-sm-right btn-sm like" v-if="!likedPosts.includes(post.id)">Like</b-button>
                    <b-button   @click="dislike($event, post)"   variant="outline-success" class="float-sm-right btn-sm like active" v-if="likedPosts.includes(post.id)">Like</b-button>
                </div>
            </div> 
            </b-card>
    <b-modal ref="my-modal" hide-footer title="Edit post">
      <div class="d-block text-center">
         
            <b-media>
                <b-form-textarea
                    id="editContent"
                    v-model="postForEdit.content"
                    rows="3"
                    max-rows="6"
                ></b-form-textarea>
            </b-media>
            <br>
             <b-button variant="secondary" align="right" v-on:click="hideModal" class="float-sm-right btn-mr-2 btn-sm">Close</b-button>

            <b-button variant="primary" align="right" v-on:click="editPost()" class="float-sm-right mr-2 btn-sm">Save post</b-button>
       
      </div>
      

    </b-modal>
    </div>
    </template>
<script>
import api from '../api'
// eslint-disable-next-line
/* eslint-disable */
export default {
    props: ['posts', 'userId', 'isProfile'],
    data(){
        return{
            loggedInUser: parseInt(localStorage.userId),
            likedPosts: [],
            newPost: {
                userId: parseInt(localStorage.userId),
                userFullName: localStorage.userFullName,
                content: '',
                likesCount: 0,
                dislikesCount: 0,
                date: '',
            },
            neWLikeDislike: {
                userId: 0,
                postId: 4,
                like: false,
                dislike: false
            },
            postForEdit: {
                content: ""
            },
            
        }
    },
    mounted () {
        console.log(this.posts)  
    },
    async created(){
        this.getPosts()
    },
    methods: {
        parseDate(value) {
                return `${value.split('T')[0]} ${value.slice(11, 16)} `
        },
        buttonClicked(event, post){
            let btn = e.srcElement
            console.log(btn.classList.contains('active'))
            post.likesCount += 1
            if(btn.classList.contains('dislike') && btn.classList.contains('active')){
                btn.classList.remove('active')
                //console.log(btn)
            }else   if(btn.classList.contains('like') && btn.classList.contains('active')){
                btn.classList.remove('active')            
            }else{
                btn.classList.add('active')
            }
        },
        async getPosts(){
            //this.posts = (await api.getAllByUser('usersFriendsPosts', parseInt(localStorage.userId))).reverse()
            this.likedPosts = (await api.getAllByUser('likedposts',parseInt(localStorage.userId)))
        },
        isLiked(post, index){
            if(this.likedPosts.find(post.id) !== undefined){
                console.log(post.id)
                post.liked = true
            }
        },
         async createPost(){
             this.newPost.date = new Date()
            console.log(this.newPost)
            if(this.newPost.content !== ''){
                this.posts.splice(0, 0, await api.create('post', this.newPost))
            } 
        },
        async deletePost(id) {
            console.log(id)
            api.delete('post', id).resource
            let deleted = this.posts.find(obj => {
                        console.log(obj)
                    return obj.id === id
            })
            this.posts.pop(deleted)

        },
        async editPost() {
            if(this.postForEdit.content !== ""){
                let response = await api.create('editpost',this.postForEdit)
                if(response === "Post updated."){
                    let edited = this.posts.find(obj => {
                    return obj.id === this.postForEdit.id
                    })
                    edited.content = this.postForEdit.content
                    alert(response)
                    this.hideModal()
                }else{
                    alert("error")
                }
            }
            
            
        },
        async like(event, post){
            console.log("Like")
                        console.log(event)

            let data = {
                userId: parseInt(localStorage.userId),
                postId: post.id,
                like: true,
                dislike: false
            }

            post.likesCount += 1
            this.likedPosts.push(post.id)
            console.log(this.likedPosts)
            //event.srcElement.classList.add('active')
            await api.create('like',data)
            console.log(post)
            
        },
        async dislike(event, post){
            console.log("DISLKE")
            console.log(event)
            let data = {
                userId: parseInt(localStorage.userId),
                postId: post.id,
                like: false,
                dislike: true
            }
            post.likesCount -= 1
            //this.likedPosts.pop(post.id)
            console.log("izbacio " + post.id)
            console.log(post.id)
            console.log(this.likedPosts)
            var index = this.likedPosts.indexOf(post.id);
            console.log("Index je " + index)

            if (index > -1) {
                this.likedPosts.splice(index, 1);
            }
            //event.srcElement.classList.remove('active')
            await api.create('dislike',data)

            console.log(post)
        },
        showModal (post) {
            console.log(post)
            this.postForEdit.id = post.id
            this.postForEdit.userId = post.userId
            this.postForEdit.userFullName = post.userFullName,
            this.postForEdit.content = post.content 
            this.postForEdit.likesCount = post.likesCount
            this.postForEdit.dislikesCount = post.dislikesCount
            this.postForEdit.date= post.date
            console.log(this.postForEdit)
            this.$refs['my-modal'].show(post)
        },
        hideModal () {
            //this.postForEdit = {}
            this.$refs['my-modal'].hide()
        },
        toggleModal () {
            this.$refs['my-modal'].toggle('#toggle-btn')
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
.date{
    font-size: 12px;
}
</style>