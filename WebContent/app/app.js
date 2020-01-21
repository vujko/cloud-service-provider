const LoginPage = {template : "<log-in></log-in>"}
const HomePage = { template : "<home-page></home-page>"}
const Users = {template: "<users></users>"}
const Organizations = { template : "<organizations></organizations>"}
const ProfilePage = { template : "<profile></profile>"}
const Drives = {template : "<drives></drives>"}

const router = new VueRouter({
    mode : 'hash',
    routes : [

        {path : "/", component : LoginPage},
        {path : "/homepage", component : HomePage}, 
        {path : "/users",component : Users},
        {path : "/organizations", component : Organizations},
        {path : "/profile", component : ProfilePage},
        {path : "/drives", component : Drives}

    ]
});

router.beforeEach((to, from , next) => {
    //ensuring login and setting role
    var self = this;
    var isLoggedIn;
    axios
    .get("/ensureLogin")
    .then(response => {
        isLoggedIn = response.data.isLoggedIn;
        if(!isLoggedIn && to.path !== "/"){
            next("/");
            
        }
        else{
            localStorage.setItem("role", response.data.role);
            localStorage.setItem("email", response.data.email);
            next();
        }
    })
})

var app = new Vue({
    router,
    el : '#app'

}).$mount('#app');


