Vue.component("organization-form", {
    data : function(){
        return {
            org_input : {
                name : "",
                description : "",
                logo : ""
            }
        }
    },
    template : `
    <div class="modal fade" ref="org-modal" id="orgModal" tabindex="-1" role="dialog" aria-labelledby="orgModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="orgModalLabel">Add a new organization</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
                            
            <form class="form-signin" role="form">
            <fieldset>  
                <div class="form-group">
                    <input class="form-control" placeholder="Organization name" name="name" type="text" v-model="org_input.name" required>
                </div>
                <div class="form-group">
                    <input class="form-control" placeholder="Description" name="description" type="text" v-model="org_input.description" required>
                </div>
                <div class="form-group">
                    <input class="form-control" placeholder="Organization logo" name="name" type="text" v-model="org_input.logo" required>
                </div>
            </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
            <button type="button" class="btn btn-primary" v-on:click="addOrganization()" >Add organization</button>
        </div>
        </div>
    </div>
    </div>`,

    methods : {
        addOrganization : function(){
            var self = this;
            axios
            .post("/addOrganization", {"name" : '' + this.org_input.name, "description" : '' + this.org_input.description, "logo" : '' + this.org_input.logo, "users" : [], "resources" : []})
            .then(response =>{
                self.$parent.getOrganizations();
                self.org_input.name = "";
                self.org_input.description = "";
                self.org_input.logo = "";  
                $('#orgModal').modal('hide');
            })
        }
    }
})