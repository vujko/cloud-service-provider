Vue.component("organization-form", {
    data : function(){
        return {
            org_input : {
                name : "",
                description : "",
                logo : ""
            },

            logo : null,
            disks : null
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
                            
            <form id="orgForm" class="form-signin" role="form">
            <fieldset>  
                <div class="form-group">
                    <input class="form-control" id="org_name" placeholder="Organization name" name="name" type="text" v-model="org_input.name" required><p id="name_err"></p>
                </div>
                <div class="form-group">
                    <input class="form-control" id="org_desc" placeholder="Description" name="description" type="text" v-model="org_input.description" required>
                </div>

                <!-- <div >
                    <label>Avilable Disks:</label>
                    <div>
                    <select class="mdb-select md-form form-control" id="diskSelect" multiple  style="width:450px" >
                        <option v-for="d in disks">{{d.name}}</option>
                    </select>
                    </div>
                </div> -->


                <div class="border">
                    <label class="form-group form-control" >Logo</label>
                    <div class="form-group text-center">
                        
                        <div v-if="!logo">
                            <input id="org_logo" placeholder="Organization logo" name="logo" @change="onFileChange" type="file">
                        </div>
                        <div v-else>
                            <img :src="logo" style="width:100px;height:120px;">
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
            <button type="button" class="btn btn-secondary" v-on:click="clearFields()" >Cancel</button>
            <button type="button" class="btn btn-primary" v-on:click="addOrganization()" >Add organization</button>
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
                self.logo = e.target.result;
            };
            reader.readAsDataURL(file);
        },

        removeLogo : function(e){
            this.logo = null;
        },


        clearFields : function(){
            this.org_input.name = "";
            this.org_input.description = "";
            this.logo = null;  
            $('#orgModal').modal('hide');  
            document.getElementById('org_name').style.borderColor = "";
            document.getElementById('name_err').innerHTML = ""; 
        },
        highlightNameField : function(){
            document.getElementById('org_name').style.borderColor = "red";
            document.getElementById('name_err').innerHTML = "Organization with that name already exsists.Please enter another.";
        },
        addOrganization : function(){
            var self = this;
            var $orgForm = $("#orgForm");
            this.org_input.logo = this.logo;
            
            if( ! $orgForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($orgForm).click().remove();
            }
            else{
                var e = $("#diskSelect");
                var selectedDisks = e.val();
                if(selectedDisks == null){
                    selectedDisks = [];
                }
                axios
                .post("/addOrganization", {"name" : '' + this.org_input.name, "description" : '' + this.org_input.description, "logo" : '' + this.org_input.logo, "disks" : selectedDisks})
                .then(response =>{
                    self.$parent.getOrganizations();
                    self.$parent.getDisks();
                    self.clearFields();          
                })
                .catch(error =>{
                    self.highlightNameField();
                })
            }
            
        }
    },
})