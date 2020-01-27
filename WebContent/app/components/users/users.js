Vue.component("users", {
	data: function () {
		    return {
              users : [],
              verified : false,
              role : null,
              selectedUser : null,
              noRes : false
		    }
	},
	template: ` 
    <div>
        
        <table v-if="noRes==false" class="table table-striped">
            <thead class="thead-dark"> 
            <tr>
                <th>Ime</th><th >Prezime</th><th>Email</th><th v-if="role=='SUPER_ADMIN' ">Organization</th></tr></thead>
                <tbody>
                <tr v-for="u in users" :key="u.email" v-on:click="selectUser(u)" v-bind:class="{selected : selectedUser != null && selectedUser.email===u.email}">
                    <td>{{u.name}}</td>
                    <td>{{u.surname}}</td>
                    <td>{{u.email}}</td>
                    <td v-if="role=='SUPER_ADMIN' ">{{u.organization.name}}</td>
                </tr>
            </tbody>
        </table>
        <h4 align="center" v-if="noRes==true">Nema rezultata</h4>    
        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" v-on:click="userAdd">
            Add User
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="editUser" v-bind:disabled="selectedUser==null">
            Edit User
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="deleteUser" v-bind:disabled="selectedUser==null">
            Delete User 
        </button>
         <!-- Modal -->
        <user-form @userAdded="addUser($event)" ref="userForm"></user-form>
    </div>	  
`
	, 
	methods : {
        getUsers : function(){
            axios
            .get('/getUsers/' + this.role)
            .then(response => {
                if(!response.data.length)
                    this.noRes =true;
                else
                    this.users = response.data;
            });
        },
        selectUser : function(user){
            this.selectedUser = user;
        },
        editUser : function(){
            this.$refs.userForm.modal = 'edit';
            this.$refs.userForm.user_input = {...this.selectedUser};   //kad odemo u addUser samo clearFields
            this.$refs.userForm.backup = {...this.selectedUser};      //postavimo bekap zbog cancela
            if(this.selectedUser.role === "ADMIN"){    //ovo mozda i nece trebati
                this.$refs.userForm.picked = "ADMIN";
            }else if(this.selectedUser.role === "USER"){
                this.$refs.userForm.picked = "USER";
            }
            $('#userModal').modal('show');
            
        },
        userAdd : function(){
            this.$refs.userForm.user_input.name = "";
            this.$refs.userForm.user_input.surname = "";
            this.$refs.userForm.user_input.password = "";
            this.$refs.userForm.user_input.email = "";
            this.$refs.userForm.user_input.organization.name = "";
            this.$refs.userForm.modal = 'add';
            $('#userModal').modal('show');       
        },
        addUser : function(user){      
            this.users.push(user);
            this.noRes = false;
        } 
        ,
        deleteUser : function(){
            var self = this;
            axios
            .post('/deleteUser',{"email" : '' + this.selectedUser.email})
            .then(function(response){
                self.verified = response.data;
                if(self.verified){
                    self.selectedUser = null;
                    toast("Successfully deleted.")
                    self.getUsers();
                }      
            })
            .catch(error =>{
                alert("You can't delete yourself.")
            })           
        }
    },
	mounted () {
        this.role = localStorage.getItem("role");
        this.getUsers();           
        this.$refs.userForm.role = this.role;
    }
});