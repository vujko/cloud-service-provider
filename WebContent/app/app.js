
const LoginPage = {template : "<log-in></log-in>"}
const HomePage = {template : "<home-page></home-page>"}
const Users = {template: '<users></users>'}
const Organizations = { template : "<organizations></organizations>"}

const router = new VueRouter({
    mode : 'hash',
    routes : [

        {path : "/", component : LoginPage},
        {path : "/homepage", component : HomePage},
        {path : '/users',component : Users},
        {path : "/organizations", component : Organizations}

    ]
});

var app = new Vue({
    router,
    el : '#app',
    methods :{
        someMethod(){
            
        }
    }
}).$mount('#app');