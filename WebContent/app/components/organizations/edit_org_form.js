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
            },
            avilableMachines : null,
            selectedMachines : null
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
                            
            <form id="editOrgForm" class="form-signin" role="form" name="forma">
            <fieldset>  
                <div class="form-group">
                    <input class="form-control" id="edit_org_name" placeholder="Organization name" name="name" type="text" v-model="editedOrg.name" required><p id="edit_name_err"></p>
                </div>
                <div class="form-group">
                    <input class="form-control" id="edit_org_desc" placeholder="Description" name="description" type="text" v-model="editedOrg.description" required>
                </div>

                <div >
                    <label>Avilable Virtual machines:</label>
                    <div>
                    <select class="mdb-select md-form form-control" id="machineEditSelect" multiple  style="width:450px" >
                        <option v-for="m in selectedMachines" selected="selected">{{m.name}}</option>
                        <option v-for="am in avilableMachines">{{am.name}}</option>
                    </select>
                    </div>
                </div>


                <div class="border">
                <label id="label" class="form-group form-control" >Logo</label>
                <div class="form-group text-center">
                    
                    <div>
                        <input id="edit_org_logo" v-bind:hidden="editedOrg.logo" @change="onFileChange" type="file">
                    </div>
                    <div v-bind:hidden="!editedOrg.logo">
                        <img :src="editedOrg.logo" style="width:100px;height:120px;">
                        <div>
                            <button type="button" @click="removeLogo">Remove logo</button>
                        </div>
                    </div>

                </div>
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
        onFileChange(e){
            var files = e.target.files || e.dataTransfer.files;
            if(!files.length)
                return 
            this.createLogo(files[0]);
        },

        createLogo(file){
            var logo = new Image();
            var reader = new FileReader();
            var self = this;

            reader.onload = (e) => {
                self.editedOrg.logo = e.target.result;
            };
            reader.readAsDataURL(file);
        },

        removeLogo : function(e){
            this.editedOrg.logo = null;
        },


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
            axios
            .get("/getSelectedMachines/" + selectedOrg.name)
            .then(response => {
                this.selectedMachines = response.data;
            })

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
                var e = $("#machineEditSelect");
                var selectedMachines = e.val();
                if(selectedMachines == null){
                    selectedMachines = [];
                }

                axios
                .post("/updateOrganization", {"oldName" : self.oldName, "newName" : ''+ self.editedOrg.name, "description" : '' + self.editedOrg.description, "logo": '' + self.editedOrg.logo, "machines" : selectedMachines})
                .then(response => {
                    if(response.data){
                        $('#editOrgModal').modal('hide');
                        self.resetNameField();
                        //this.$parent.getMachines();
                        toast("Successfully updated organization"); 
                    }
                })
                .catch(error => {
                    self.highlightNameField();
                })
                
            }
            
        }
    }

    

})