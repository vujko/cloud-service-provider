Vue.component("admin-form",{
    data: function(){
        return{
            user_input : {
                email : "",
                password : "",
                surname : "",
                name : "",
                organization : {name : ""}
            },
            modal : null,
            backup : {
                email : "",
                password : "",
                surname : "",
                name : ""
            },
            role : null,
            picked : ""
    }
},
template : ``

})