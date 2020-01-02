Vue.component("user-form", {
    data: function(){
            return{
                user_input : {
                    email : "",
                    password : "",
                    surname : "",
                    name : ""
            }
        }
    },
    template : `
        <div class="modal fade" ref="user-modal" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="userModalLabel">Add a new User</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                                
                <form id="userForm" class="form-signin" role="form">
                <fieldset>  
                    <div class="form-group">
                        <input class="form-control" id="us_email" placeholder="Email" name="email" type="email" v-model="user_input.email" required><p id="name_err"></p>
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="us_name" placeholder="Name" name="name" type="text" v-model="user_input.name" required>
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="us_surname" placeholder="Surname" name="surname" type="text" v-model="user_input.surname" required>
                    </div>
                    <div class="form-group">
                    <input class="form-control" id="us_password" placeholder="Password" name="password" type="password" v-model="user_input.password" required>
                    </div>
                </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" v-on:click="clearFields()">Cancel</button>
                <button type="button" class="btn btn-primary" v-on:click="addUser()" >Add user</button>
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
            $('#userModal').modal('hide');
            document.getElementById('us_email').style.borderColor = "";
            document.getElementById('name_err').innerHTML = "";
        },
        highlightNameField : function(){
            document.getElementById('us_email').style.borderColor = "red";
            document.getElementById('name_err').innerHTML = "User with that email already exsists.Please enter another.";
        },
        addUser : function(){
            var self = this;
            var $userForm = $("#userForm");
            if( ! $userForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($userForm).click().remove();
            }
            else{
                axios
                .post("/addUser",{"email" : '' + this.user_input.email, "name" : '' + this.user_input.name, "surname" : '' + this.user_input.surname, "organization" : null,
                            "role" : null, "password" : '' + this.user_input.password})
                .then(response => {
                    self.$parent.getUsers();
                    self.clearFields();
                })
                .catch(error =>{
                    self.highlightNameField();
                })
            }
        }
    }
})