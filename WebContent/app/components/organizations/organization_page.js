Vue.component("organization-page", {
	data: function () {
		return {
			role : null
		}
	},
	template: ` 
	   <layout-page>
	 		<organizations></organizations>  
	   </layout-page>
`
	, 
	mounted(){
		this.role = localStorage.getItem("role");
	}
});

