Vue.component("edit-org-form",{
    data : function(){
        return{
            editedOrg : {
                name : "",
                description : "",
                logo : ""
            },
            oldName : "",
            backup : {
                name : "",
                description : "",
                logo : ""
            }
        }
    },

    template : `   
    <div class="modal fade" ref="org-modal" id="editOrgModal" tabindex="-1" role="dialog" aria-labelledby="editOrgModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="orgModalLabel">Edit organization</h5>
            <button type="button" class="close" v-on:click="cancelUpdate()" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
                            
            <form id="editOrgForm" class="form-signin" role="form">
            <fieldset>  
                <div class="form-group">
                    <input class="form-control" id="edit_org_name" placeholder="Organization name" name="name" type="text" v-model="editedOrg.name" required><p id="edit_name_err"></p>
                </div>
                <div class="form-group">
                    <input class="form-control" id="edit_org_desc" placeholder="Description" name="description" type="text" v-model="editedOrg.description" required>
                </div>
                <div class="form-group">
                    <input class="form-control" id="edit_org_logo"  placeholder="Logo" name="logo" type="text" v-model="editedOrg.logo" required>
                </div>
            </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" v-on:click="cancelUpdate()" >Cancel</button>
            <button type="button" class="btn btn-primary" v-on:click="changeOrganization()" >Save changes</button>
        </div>
        </div>
    </div>
    </div>`,

    methods : {
        cancelUpdate : function(){
            this.editedOrg.name = this.backup.name;
            this.editedOrg.description = this.backup.description;
            this.editedOrg.logo = this.backup.logo;
            $('#editOrgModal').modal('hide');
            this.resetNameField();
        },

        setOrg : function(selectedOrg){
            this.backup = {...selectedOrg};
            this.editedOrg = selectedOrg;
            this.oldName = selectedOrg.name;
            document.getElementById('edit_org_name').setAttribute("value",selectedOrg.name); 
            document.getElementById('edit_org_desc').setAttribute("value",selectedOrg.description); 
            document.getElementById('edit_org_logo').setAttribute("value",selectedOrg.logo); 
        },
        resetNameField : function(){
            document.getElementById('edit_org_name').style.borderColor = "";
            document.getElementById('edit_name_err').innerHTML = "";
        },

        highlightNameField : function(){
            document.getElementById('edit_org_name').style.borderColor = "red";
            document.getElementById('edit_name_err').innerHTML = "Organization with that name already exsists.Please enter another.";
        },
        changeOrganization : function(){
            var self = this;
            var $editOrgForm = $("#editOrgForm");
            if( !$editOrgForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($editOrgForm).click().remove();
            }
            else{
                axios
                .post("/updateOrganization", {"oldName" : self.oldName, "newName" : ''+ self.editedOrg.name, "description" : '' + self.editedOrg.description, "logo": '' + self.editedOrg.logo})
                .then(response => {
                    if(response.data){
                        $('#editOrgModal').modal('hide');
                        self.resetNameField();
                    }
                })
                .catch(error => {
                    self.highlightNameField();
                })
                
            }
            
        }
    }

    

})