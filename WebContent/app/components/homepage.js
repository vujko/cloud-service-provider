Vue.component("home-page", {

	data: function () {
		    return {
		      data : null
		    }
	},
	template: ` 
<div>
	<a href= "#/users">Users</a>
	<a v-on:click="logout()">Logout</a>
	
</div>		  
`
	, 
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
	mounted () {
    }
});

