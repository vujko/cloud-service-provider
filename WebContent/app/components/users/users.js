Vue.component("users", {
	data: function () {
		    return {
              users : null,
              role : null
		    }
	},
	template: ` 
    <div>
        <nav-bar></nav-bar>
        <table class="table table-striped table-responsive" >
            <thead>
            <tr>
                <th scope="col">Ime</th><th scope="col" >Prezime</th><th scope="col">Email</th></tr></thead>
                <tbody>
                <tr v-for="u in users">
                    <td>{{u.name}}</td>
                    <td>{{u.surname}}</td>
                    <td>{{u.email}}</td>
                </tr>
            </tbody>
        </table>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#userModal">
            Add User
        </button>

         <!-- Modal -->
        <user-form></user-form>
    </div>	  
`
	, 
	methods : {
        getUsers : function(){
            axios
            .get('/getUsers')
            .then(response => {
                this.users = response.data
            });
        }
        
    },
	mounted () {
        this.getUsers();         
        this.role = localStorage.getItem("role");
             
    }
});