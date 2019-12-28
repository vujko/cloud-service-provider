Vue.component("home-page", {

	data: function () {
		    return {
		      data : null
		    }
	},
	template: ` 
	<div>
		<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
			<a class="navbar-brand" href="#/homepage">Home</a>
			</div>
			<ul class="nav navbar-nav">
			<li><a href="#/users">Users</a></li>
			<li><a v-on:click="logout()" >Logout</a></li>
			</ul>
		</div>
		</nav>
		
		<div class="container">
		<h3>VM</h3>
		<p>Prikaz VM</p>
		</div>
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

