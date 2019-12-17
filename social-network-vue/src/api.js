import Vue from 'vue'
import http from './http-common'
import { isUndefined } from 'util';
export default {
  async execute (method, resource, data) {
    return http({
      method,
      url: resource,
      data
    }).then(response => {
      if(response.data.isUndefined){
        console.log(response)
        return response
      }else{
        console.log(response.data)
        return response.data
      }
        
    }).catch(e => {
      console.log(e)
      return e
    })
  },
  getAll (resource) {
    return this.execute('get', '/' + resource)
  },
  getAllByUser (resource, userId) {
    return this.execute('get', '/' + resource + '/' + userId)
  },
  getOne (resource, id) {
    return this.execute('get', '/' + resource + '/' + id)
  },
  create (resource, data) {
    return this.execute('post', '/' + resource, data)
  },
  update (resource, data) {
    return this.execute('put', '/' + resource + '/' + data)
  },
  delete (resource, id) {
    return this.execute('delete', '/' + resource + '/' + id)
  }
}
