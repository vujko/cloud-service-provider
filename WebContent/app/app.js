const LoginPage = {template : "<log-in></log-in>"}
const HomePage = { template : "<home-page></home-page>"}
const Users = {template: "<user-page></user-page>"}
const Organizations = { template : "<organization-page></organization-page>"}
const ProfilePage = { template : "<profile-page></profile-page>"}
const Categories = {template : "<category-page></category-page>"}
const Drives = {template : "<drive-page ></drive-page>"}

const router = new VueRouter({
    mode : 'hash',
    routes : [

        {path : "/", component : LoginPage},
        {path : "/homepage", component : HomePage}, 
        {path : "/users",component : Users},
        {path : "/organizations", component : Organizations},
        {path : "/profile", component : ProfilePage},
        {path : "/categories", component : Categories},
        {path : "/drives", component : Drives},
        {path : "/VM"}

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
            localStorage.setItem("page", to.path);
            next();
        }
    })
})
var EventBus = new Vue();
var app = new Vue({
    router,
    el : '#app'

}).$mount('#app');


