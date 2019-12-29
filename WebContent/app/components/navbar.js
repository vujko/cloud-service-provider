Vue.component("nav-bar",{
    template :`
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
            <a class="navbar-brand" href="#/homepage">Home</a>
            </div>
            <ul class="nav navbar-nav">
            <li><a href="#/users">Users</a></li>
            <li><a href="#/organizations">Organizations</a></li>
            <li><a v-on:click="logout()" >Logout</a></li>
            </ul>
        </div>
    </nav>
    `,
    methods : {
		
		logout : function(){
			var self = this;
			axios
			.post("/logout")
			.then(function(response){
				self.$router.replace("/");
			})
		}

		
	},
})