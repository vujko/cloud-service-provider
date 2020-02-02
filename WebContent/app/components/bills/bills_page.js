Vue.component("bills-page", {
	data: function () {
		return {
			role : null
		}
	},
	template: ` 
	   <layout-page>
	 		<bills></bills>  
	   </layout-page>
`
	, 
	mounted(){
		this.role = localStorage.getItem("role");
	}
});