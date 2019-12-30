Vue.component("users", {
	data: function () {
		    return {
		      users : null
		    }
	},
	template: ` 
    <div>
        <nav-bar></nav-bar>
        <table border="1">
            <tr>
                <th>Ime</th><th>Prezime</th><th>Email</th></tr>
                <tr v-for="u in users">
                    <td>{{u.name}}</td>
                    <td>{{u.surname}}</td>
                    <td>{{u.email}}</td>
                </tr>
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
    }
});