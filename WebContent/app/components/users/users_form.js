Vue.component("user-form", {
    data: function(){
            return{
                user_input : {
                    email : "",
                    password : "",
                    surname : "",
                    name : "",
                    organization : {name : ""}
                },
                modal : null,
                backup : {
                    email : "",
                    password : "",
                    surname : "",
                    name : ""
                },
                role : null,
                picked : ""
        }
    },
    template : `
        <div class="modal fade" ref="user-modal" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="userModalLabel" v-if="modal=='add'">Add a new User</h5>
                <h5 class="modal-title" id="userModalLabel" v-if="modal=='edit'">Edit a User</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                                
                <form id="userForm" class="form-signin" role="form">
                <fieldset>  
                    <div class="form-group">
                        <input class="form-control" v-bind:disabled="modal=='edit'" id="us_email" placeholder="Email" name="email" type="email" v-model="user_input.email" required><p id="name_err"></p>
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="us_name" placeholder="Name" name="name" type="text" v-model="user_input.name" required>
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="us_surname" placeholder="Surname" name="surname" type="text" v-model="user_input.surname" required>
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="us_password" placeholder="Password" name="password" type="text" v-model="user_input.password" required>
                    </div>
                    <div class="form-group">
                        <input class="form-control" v-if="role=='SUPER_ADMIN'" v-bind:disabled="modal=='edit'" id="us_organization" placeholder="Organization" name="organization" type="text"  v-model="user_input.organization.name" required><p id="org_err"></p>
                    </div>
                    <div class="form-check">
                        <input type="radio" name="role" id="user" value="USER" v-model="picked" required>
                        <label for="user">User</label>
                        <input type="radio" name="role" id="adm" value="ADMIN" v-model="picked">
                        <label for="adm">Admin</label>               
                    </div>
                </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" v-on:click="addUser()" v-if="modal == 'add'">Add user</button>
                <button type="button" class="btn btn-secondary" v-if="role=='SUPER_ADMIN' && modal=='add'" v-on:click="clearFieldsSuperAdmin()" > Cancel </button>
                <button type="button" class="btn btn-secondary" v-if="role=='ADMIN' && modal == 'add'" v-on:click="clearFieldsAdmin()" > Cancel </button>
                <button type="button" class="bnt btn-primary" v-on:click="updateUser()" v-if="modal == 'edit'">Save changes</button>
                <button type="button" class="bnt btn-secondary" v-on:click="cancelUpdate()" v-if="modal =='edit'">Cancel</button>
            </div>
            </div>
        </div>
        </div>
    `,

    methods : {
        clearFields : function(){
            this.user_input.name = "";
            this.user_input.surname = "";
            this.user_input.email = "";
            this.user_input.password = "";
            this.user_input.organization.name = "";
        },
        clearFieldsAdmin : function(){
            this.clearFields();
            $('#userModal').modal('hide');
            this.resetNameField();
        },
        clearFieldsSuperAdmin : function(){
            this.clearFields();
            $('#userModal').modal('hide');
            this.resetNameField();
            this.resetOrgField();
        },
        cancelUpdate : function(){
            this.user_input.name = this.backup.name;
            this.user_input.surname = this.backup.surname;
            this.user_input.password = this.backup.password;
            this.user_input.email = this.backup.email;
            $('#userModal').modal('hide');
            //this.$parent.selectedUser = null;        
        },
        resetNameField : function(){
            document.getElementById('us_email').style.borderColor = ""; 
            document.getElementById('name_err').innerHTML = "";
        },
        resetOrgField : function(){
            document.getElementById('us_organization').style.borderColor ="";
            document.getElementById('org_err').innerHTML = "";
        },
        highlightNameField : function(){
            document.getElementById('us_email').style.borderColor = "red";
            document.getElementById('name_err').innerHTML = "User with that email already exsists.Please enter another.";
        },
        highlightOrgField : function(){
            document.getElementById('us_organization').style.borderColor ="red";
            document.getElementById('org_err').innerHTML = "Organization doesn't exsists.Please enter another.";
        },
        addUser : function(){
            //this.clearFields();
            var self = this;
            
            var $userForm = $("#userForm");
            if( ! $userForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($userForm).click().remove();
            }
            else{
                axios
                .post("/addUser",{"email" : '' + this.user_input.email, "name" : '' + this.user_input.name, "surname" : '' + this.user_input.surname, "organization" : this.user_input.organization,
                            "role" : this.picked, "password" : '' + this.user_input.password})

                .then(response => {
                    self.$parent.getUsers();
                    if(self.role == "SUPER_ADMIN"){
                        self.clearFieldsSuperAdmin();
                    }else{
                        self.clearFieldsAdmin();
                    }
                   
                })
                .catch(error =>{
                    axios
                    .post("/checkEmail",{"email" : '' + this.user_input.email})
                    .then(response =>{
                        if(response.data){
                            self.highlightOrgField();
                        }else{
                            self.highlightNameField();
                        }
                    })        
                })
            }
        },
        updateUser : function(){
            var self = this;
            var $userForm = $("#userForm");
            if( ! $userForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($userForm).click().remove();
            }
            else {
                axios
                .post("/updateUser",{"oldEmail" : '' +this.backup.email, "newEmail" : '' + this.user_input.email,  "name" : '' + this.user_input.name, "surname" : '' + this.user_input.surname, 
                 "pass" : '' + this.user_input.password})
                .then(response =>{
                    if(response.data){
                        $('#userModal').modal('hide');
                        toast("Successfully updated.");
                        self.$parent.getUsers();
                    }
                })
                
            }
        },
    }
})