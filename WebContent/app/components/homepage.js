Vue.component("home-page", {
	data: function () {
		return {
			role : null
		}
	},
	template: ` 
	<div>
		<nav-bar></nav-bar>
		
		
		<div class="container">
		<h3>{{role}}</h3>
		<vm></vm>
		</div>
	</div>
`
	, 
	mounted(){
		this.role = localStorage.getItem("role");
	}
});

