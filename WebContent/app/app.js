
window.EventBus = new Vue();

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
        ensureLogin(){
            var loggedIn;
            axios
            .get("/ensureLogin")
            .then(response => {
                loggedIn = response.data;
                if(!loggedIn){
                    router.replace("/");
                }
            })
        }
    },

    created() {
        EventBus.$on("ensureLogin", this.ensureLogin);
    }
}).$mount('#app');


// router.beforeEach((to, from, next) => {
//     if (!app.isAuthenticated()) next({path : "/"})
//     else next()
// })