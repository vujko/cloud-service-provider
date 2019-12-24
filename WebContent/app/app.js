const Homepage = {template: '<home-page></home-page>'}
const Users = {template: 'users'}

const router = new VueRouter({
    mode : 'hash',
    routes : [
        {path : '/', component : Homepage},
        {path : '/users',component : Users}
    ]
})

var app = new Vue({
    router,
    el : '#app'
})