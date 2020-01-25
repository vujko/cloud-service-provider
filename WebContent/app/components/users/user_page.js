Vue.component("user-page", {
	data: function () {
		return {
			role : null
		}
	},
	template: ` 
	   <layout-page>
	 		<users></users>  
	   </layout-page>
`
	, 
	mounted(){
		this.role = localStorage.getItem("role");
	}
});

