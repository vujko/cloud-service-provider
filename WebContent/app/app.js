const LoginPage = {template : "<log-in></log-in>"}
const HomePage = { template : "<home-page></home-page>"}
const Users = {template: "<user-page></user-page>"}
const Organizations = { template : "<organization-page></organization-page>"}
const ProfilePage = { template : "<profile-page></profile-page>"}
const Categories = {template : "<category-page></category-page>"}
const Drives = {template : "<drive-page ></drive-page>"}
const Bills = {template : "<bills-page></bills-page>"}

const router = new VueRouter({
    mode : 'hash',
    routes : [

        {path : "/", component : LoginPage, meta: {allow :['USER', 'ADMIN', 'SUPER_ADMIN']}},
        {path : "/homepage", component : HomePage, meta: {allow :['USER', 'ADMIN', 'SUPER_ADMIN']}}, 
        {path : "/users",component : Users, meta: {allow :['ADMIN', 'SUPER_ADMIN']}},
        {path : "/organizations", component : Organizations, meta: {allow :['ADMIN', 'SUPER_ADMIN']}},
        {path : "/profile", component : ProfilePage, meta: {allow :['USER', 'ADMIN', 'SUPER_ADMIN']}},
        {path : "/categories", component : Categories, meta: {allow :['SUPER_ADMIN']}},
        {path : "/drives", component : Drives, meta: {allow :['USER', 'ADMIN', 'SUPER_ADMIN']}},
        {path : "/VM", meta: {allow :['USER', 'ADMIN', 'SUPER_ADMIN']}},
        {path : "/bills", component : Bills, meta : {allow : ["ADMIN"]}}

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
        if(!isLoggedIn){
            if(to.path === "/"){
                next();
            }
            else{
                next("/");                
            }
            
        }
        else if(to.path === "/"){
            next("/homepage");
        }
        else if(to.meta.allow.includes(response.data.role)){
            localStorage.setItem("role", response.data.role);
            localStorage.setItem("email", response.data.email);
            localStorage.setItem("page", to.path);
            next();
        }
        else{
            next(from.path);
        }


    })
})
var EventBus = new Vue();
var app = new Vue({
    router,
    el : '#app'

}).$mount('#app');


