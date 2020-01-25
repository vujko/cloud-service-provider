Vue.component("category-page", {
	data: function () {
		return {
			role : null
		}
	},
	template: ` 
	   <layout-page>
	 		<categories></categories>  
	   </layout-page>
`
	, 
	mounted(){
		this.role = localStorage.getItem("role");
	}
});

