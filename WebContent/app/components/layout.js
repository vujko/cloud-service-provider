Vue.component("layout-page", {
	data: function () {
		return {
            role : null,
            title : null
		}
	},
	template: ` 
	<div>
	<nav-bar></nav-bar>

		<div class="container-fluid">			
			<div class="row">
				<side-bar></side-bar>
				<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
					<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
						<h3>{{title}}</h3>
					</div>				
					<slot></slot>
				</main>				
			</div>
			
		</div>
	</div>
`
	, 

	mounted(){
        this.role = localStorage.getItem("role");
        this.title = localStorage.getItem("page").substring(1).toUpperCase();
	}
});

