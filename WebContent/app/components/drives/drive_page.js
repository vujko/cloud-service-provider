Vue.component("drive-page", {
	data: function () {
		return {
			role : null
		}
	},
	template: ` 
	   <layout-page>
	 		<drives></drives>  
	   </layout-page>
`
	, 

	methods : {
		search : function(event){
			alert(event);
		}
	},
	mounted(){
		this.role = localStorage.getItem("role");
	}
});

