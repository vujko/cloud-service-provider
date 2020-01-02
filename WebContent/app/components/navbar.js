Vue.component("nav-bar",{
    template :`
    <nav class="navbar navbar-expand-lg navbar-light bg-light mb-3">

  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item active">
        <a class="nav-link" href="#/homepage">Home <span class="sr-only">(current)</span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#/users">Users</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#/organizations">Organizations</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" v-on:click="logout()">Logout</a>
      </li>
      
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