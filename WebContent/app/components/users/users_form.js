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
                                
                <form class="form-signin" role="form">
                <fieldset>  
                    <div class="form-group">
                        <input class="form-control" placeholder="Email" name="email" type="text" v-model="user_input.email" required>
                    </div>
                    <div class="form-group">
                        <input class="form-control" placeholder="Name" name="name" type="text" v-model="user_input.name" required>
                    </div>
                    <div class="form-group">
                        <input class="form-control" placeholder="Surname" name="surname" type="text" v-model="user_input.surname" required>
                    </div>
                    <div class="form-group">
                    <input class="form-control" placeholder="Password" name="password" type="text" v-model="user_input.password" required>
                    </div>
                </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" v-on:click="addUser()" >Add user</button>
            </div>
            </div>
        </div>
        </div>
    `,

    methods : {
        addUser : function(){
            var self = this;
            axios
            .post("/addUser",{"email" : '' + this.user_input.email, "name" : '' + this.user_input.name, "surname" : '' + this.user_input.surname, "organization" : null,
                            "role" : null, "password" : '' + this.user_input.password})
            .then(response => {
                self.$parent.getUsers();
                self.user_input.email = "";
                self.user_input.name = "";
                self.user_input.surname = "";
                self.user_input.password = "";
                $('#userModal').modal('hide');
            })
        }
    }
})