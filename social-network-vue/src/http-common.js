import axios from 'axios'

export default axios.create({
  withCredentials: true,
  baseURL: 'http://localhost:9000/',
  headers: {
    'Content-type': 'application/json',
    'Accept': 'application/json'
  }
})
