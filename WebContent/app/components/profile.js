Vue.component("profile", {
    data : function(){
        return {
            user : {
                email : "",
                name : "",
                surname : "",
                password : "",
                organization : {
                    name : ""
                }
            }
        }
    },
    template : `
    <div>
    <nav-bar></nav-bar>
        <h4 style="margin-left: 0.5rem;">Edit profile</h4>
    <div class="input-group" >
        
    <form id="userForm"  class="form-horizontal " role="form">
    <fieldset>  
        <div class="form-group form-inline">
            <label class="control-label col-sm-4" for="email">Email:</label>
            <input class="form-control" id="us_email" placeholder="Email" name="email" type="email" v-model="user.email" required><p id="name_err"></p>
        </div>
        <div class="form-group  form-inline">
            <label class="control-label col-sm-4" for="email">Name:</label>
            <input class="form-control" id="us_name" placeholder="Name" name="name" type="text" v-model="user.name" required>
        </div>
        <div class="form-group  form-inline">
            <label class="control-label col-sm-4" for="email">Surname:</label>
            <input class="form-control" id="us_surname" placeholder="Surname" name="surname" type="text" v-model="user.surname" required>
        </div>
        <div class="form-group  form-inline">
            <label class="control-label col-sm-4" for="email">Password:</label>
            <input class="form-control" id="us_password" placeholder="Password" name="password" type="text" v-model="user.password" required>
        </div>
        <div class="form-group  form-inline">
            <label class="control-label col-sm-4" for="email">Organization:</label>
            <input class="form-control" id="us_organization" placeholder="No organization" name="organization" type="text" v-model="user.organization.name" disabled>
        </div>
    </fieldset>
    </form>
    </div>
        <div style="margin-left: 0.5rem;">
            <button type="button" class="btn btn-secondary" v-on:click="clearFields()" >Cancel</button>
            <button type="button" class="btn btn-primary" v-on:click="addOrganization()" >Save changes</button>
        </div>

    </div>
    `,

    methods : {
        editProfile : function(){
            this.$refs.userForm.modal = 'edit';
            this.$refs.userForm.user = {...this.user};
            this.$refs.userForm.backup = {...this.user}; 
            $('#userModal').modal('show');
        }
    },

    mounted(){
        axios
        .get("/getUser/" + localStorage.getItem("email"))
        .then(response => {
          this.user = response.data;
        })
    }

})