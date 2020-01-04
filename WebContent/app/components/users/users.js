Vue.component("users", {
	data: function () {
		    return {
              users : null,
              role : null
              selectedUser : null
		    }
	},
	template: ` 
    <div>
        <nav-bar></nav-bar>
        <table class="table table-striped table-responsive col px-md-2">
            <thead> 
            <tr>
                <th scope="col">Ime</th><th scope="col" >Prezime</th><th scope="col">Email</th></tr></thead>
                <tbody>
                <tr v-for="u in users" :key="u.email" v-on:click="selectUser(u)" v-bind:class="{selected : selectedUser != null && selectedUser.email===u.email}">
                    <td>{{u.name}}</td>
                    <td>{{u.surname}}</td>
                    <td>{{u.email}}</td>
                </tr>
            </tbody>
        </table>
        <button type="button" class="btn btn-primary " data-toggle="modal" v-on:click="userAdd">
            Add User
        </button>
        <button type="button" class="btn btn-primary" v-on:click="editUser" v-bind:disabled="selectedUser==null">
            Edit User
        </button>
         <!-- Modal -->
        <user-form ref="userForm"></user-form>
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
        },
        selectUser : function(user){
            this.selectedUser = user;
        },
        editUser : function(){
            this.$refs.userForm.modal = 'edit';
            this.$refs.userForm.user_input = {...this.selectedUser};   //kad odemo u addUser samo clearFields
            this.$refs.userForm.backup = {...this.selectedUser};      //postavimo bekap zbog cancela
            $('#userModal').modal('show');
            
        },
        userAdd : function(){
            this.$refs.userForm.user_input.name = "";
            this.$refs.userForm.user_input.surname = "";
            this.$refs.userForm.user_input.password = "";
            this.$refs.userForm.user_input.email = "";
            this.$refs.userForm.modal = 'add';
            $('#userModal').modal('show');
        }
    },
	mounted () {
        this.getUsers();         
        this.role = localStorage.getItem("role");
             
    }
});