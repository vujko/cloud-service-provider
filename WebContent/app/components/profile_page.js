Vue.component("profile-page", {
	data: function () {
		return {
			role : null
		}
	},
	template: ` 
	   <layout-page>
	 		<profile></profile>  
	   </layout-page>
`
	, 
	mounted(){
		this.role = localStorage.getItem("role");
	}
});

