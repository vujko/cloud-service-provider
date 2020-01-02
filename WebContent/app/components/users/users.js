Vue.component("users", {
	data: function () {
		    return {
		      users : null
		    }
	},
	template: ` 
    <div>
        <nav-bar></nav-bar>
        <table class="table table-striped">
            <thread>
            <tr>
                <th>Ime</th><th scope="col">Prezime</th><th scope="col">Email</th></tr></thread>
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
    beforeCreate () {
		EventBus.$emit("ensureLogin");
    },
	mounted () {
        this.getUsers();       
    }
});