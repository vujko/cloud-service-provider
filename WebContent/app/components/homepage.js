Vue.component("home-page", {

	data: function () {
		    return {
		      data : null
		    }
	},
	template: ` 
	<div>
		<nav-bar></nav-bar>
		
		
		<div class="container">
		<h3>VM</h3>
		<p>Prikaz VM</p>
		</div>
	</div>
`
	, 
	methods : {

	},
	beforeCreate () {
		EventBus.$emit("ensureLogin");
    }
});

