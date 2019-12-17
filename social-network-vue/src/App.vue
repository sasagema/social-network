<template>
  <div id="app">
    <div id="nav" v-if="loggedIn">
    <b-navbar toggleable="lg" type="dark" variant="info">
        <b-navbar-brand>
          <router-link to="/">Home</router-link>
        </b-navbar-brand>
        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
        <b-collapse id="nav-collapse" is-nav>
        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto " >
            <b-nav-form v-on:submit.prevent="search()">
              <b-form-input size="sm" class="mr-sm-2" v-model="searchQuery" placeholder="Search"></b-form-input>
              <b-button size="sm" class="my-2 my-sm-0 mr-sm-5" type="submit">Search</b-button>
            </b-nav-form>

            <b-nav-item-dropdown right>
            <!-- Using 'button-content' slot -->
            <template slot="button-content">
              <em >{{userFullName}}</em></template>
            <b-dropdown-item>
              <router-link  :to="{ name: 'profile', params: { userId: loggedInUser }}">Profile</router-link>
            </b-dropdown-item>
            <b-dropdown-item>
              <em @click="logout">Log out</em>
            </b-dropdown-item>
            </b-nav-item-dropdown>
        </b-navbar-nav>
        </b-collapse>
    </b-navbar>
    </div>
    <router-view/>
    <b-modal ref="my-modal" hide-footer title="Search results">
      <div class="d-block text-center">
        <ul class="list-unstyled">
          <b-media class="mb-2" tag="li" v-for="user in searchResult" :key="user.id" >
            <b-img slot="aside" src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" blank-color="#abc" width="64" alt="placeholder"></b-img>
            <router-link :to="{ name: 'profile', params: { userId: user.id, isFriend: false}}">
              <h5 @click="hideModal()" class="mt-0 mb-1" align="left">{{user.firstName}} {{user.lastName}}</h5>
            </router-link>
            <p class="mb-0" align="left">
              {{user.info}}
            </p>
    </b-media>
  </ul>
      </div>
      <b-button class="mt-3" variant="outline-danger" block @click="hideModal">Close</b-button>
    </b-modal>
  </div>
</template>

<script>
import router from './router'
import api from './/api'
export default {
  data () {
    return {
      userFullName: '',
      loggedIn: false,
      loggedInUser: 0,
      searchQuery: '',
      searchResult: [],
      users: []
    }
  },
  mounted () {
    if (localStorage.userFullName) {
      this.userFullName = localStorage.userFullName
      this.loggedIn = true
      this.loggedInUser = localStorage.userId
    } else {
      this.userFullName = 'Prijavi se'
      this.loggedIn = false
      router.push({ path: 'login' })
    }
  },
  methods: {
    logout: function () {
      localStorage.removeItem('userId')
      localStorage.removeItem('userEmail')
      localStorage.removeItem('userFullName')
      // router.replace({ path: '/' })
      router.push({ path: '/' })
      router.go()
    },
    async search () {
       
      console.log(this.searchQuery)

      this.users = await api.getAll('user')
      console.log(this.users)
      if(this.searchQuery === ""){
        this.searchResult = this.users
        router.push({ path: '/' })
        this.showModal()

      }else{
        this.searchResult = this.users.filter(user => 
        user.firstName.toLowerCase() === this.searchQuery.toLowerCase() || 
        user.lastName.toLowerCase() === this.searchQuery.toLowerCase() ||
        this.searchQuery.toLowerCase() === user.firstName.toLowerCase() + ' ' + user.lastName.toLowerCase())
        console.log(this.searchResult)
        router.push({ path: '/' })
        this.showModal()
      }
    },
    showModal () {
      this.$refs['my-modal'].show()
    },
    hideModal () {
      this.$refs['my-modal'].hide()
      //window.location.reload()
    },
    toggleModal () {
      // We pass the ID of the button that we want to return focus to
      // when the modal has hidden
      this.$refs['my-modal'].toggle('#toggle-btn')
    }

  }
}

</script>
<style lang="scss">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #f8f9fa
}
#nav {
  padding: 20px;
  //background-color: #6c757d;
  a {
    font-weight: bold;
    color: #2c3e50;
    &.router-link-exact-active {
      color: #2c3e50 ;
    }
  }
}
</style>
