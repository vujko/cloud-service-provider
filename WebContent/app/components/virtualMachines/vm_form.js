Vue.component("vm-form", {
    data : function(){
        return {
            modal : "add",
            backup : {
                name : "",
                category : {
                    name : "",    
                    cores : null,
                    ram : null,
                    gpus : null,
                },
                listOfActivities : []
            },
            dict : {
                add : {
                    name : "",
                    category : {
                        name : "",    
                        cores : null,
                        ram : null,
                        gpus : null,
                    },
                    organization : null

                },
                edit : {
                    name : "",
                    category : {    
                        name : "", 
                        cores : null,
                        ram : null,
                        gpus : null,
                    },
                    listOfActivities : [],
                    organization : null
                },
                show : {
                    name : "",
                    category : {    
                        name : "", 
                        cores : null,
                        ram : null,
                        gpus : null,
                    },
                    organization : null
                }

            },
            categories : null,
            drives : null,
            selectedDrives : null,
            selectedActivity : "",
            deleteItems : [],
            orgDrives : [],
            role : null,
            organizations : null,
            selectedOrgName : null
        }
    },

    template : `
    
    <div class="modal fade" ref="vm-modal" id="vmModal" tabindex="-1" role="dialog" aria-labelledby="vmModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="vmModalLabel" v-if="modal=='add'">Add a new Virtual Machine</h5>
            <h5 class="modal-title" id="vmModalLabel" v-if="modal=='edit'">Edit a Virtual Machine</h5>
            <h5 class="modal-title" id="vmModalLabel" v-if="modal=='show'">Virtual Machine Preview</h5>
            <button type="button" class="close" v-on:click="clearFields()" v-if="modal=='add'" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>

            <button type="button" class="close" v-on:click="cancelUpdate()" v-if="modal=='edit'" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>

            <button type="button" class="close" v-on:click="closeModal()" v-if="modal=='show'" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
                            
            <form id="vmForm" class="form-signin" role="form">
            <fieldset>  
                <div class="form-group">
                    <label>Name:</label>
                    <input class="form-control" id="vm_name" name="name" type="name" v-model="dict[modal].name   "   v-bind:disabled="modal == 'show'" required><p id="name_err"></p>
                </div>

                <div class="form-group" v-bind:hidden="!(role == 'SUPER_ADMIN')">
                    <label>Organization:</label>
                    <div>
                    <select class="mdb-select md-form form-control" id="organizationSelect" style="width:450px" v-on:change="setUpDisks()" v-bind:disabled="modal == 'edit'">
                        <option v-for="o in organizations" :value="o.name">{{o.name}}</option>
                    </select>
                    </div>
                </div>

                <div >  
                    <label>Avilable Disks From Organization:</label>
                    <div>
                    <select class="mdb-select md-form form-control" id="diskSelect" multiple  style="width:450px" v-if="modal=='add'">
                        <option v-for="d in orgDrives">{{d.name}}</option>
                    </select>

                    <select class="mdb-select md-form form-control" id="diskEditSelect" multiple  style="width:450px" v-else   v-bind:disabled="modal == 'show'">
                        <option :id="sd.name" v-for="sd in selectedDrives" v-bind:selected="modal != 'show'">{{sd.name}}</option>
                        <option :id="od.name" v-for="od in orgDrives">{{od.name}}</option>
                    </select>
                    </div>
                </div>
                               
                <div class="form-group">
                    <label>Category:</label>
                    <div>
                    <select class="mdb-select md-form form-control" id="categorySelect" style="width:450px" v-on:change="setCategoryParams()"   v-bind:disabled="modal == 'show'">
                        <option v-for="c in categories" :value="c.name">{{c.name}}</option>
                    </select>
                    </div>
                </div>

                <form class="form-inline">
                    <div class="form-group" style="margin-top : 10px">
                        <label style="margin-right: 10px;">Cores:</label>
                        <input class="form-control"  id="vm_cores" name="name" v-model="dict[modal].category.cores" style="width:70px; height : 35px; margin-right: 25px;" disabled>
                        <label style="margin-right: 10px;">RAM:</label>
                        <input class="form-control" id="vm_ram" name="name" v-model="dict[modal].category.ram" style="width:70px; height : 35px; margin-right: 25px;" disabled>
                        <label style="margin-right: 10px;">GPUS:</label>
                        <input class="form-control" id="vm_gpus" name="name" v-model="dict[modal].category.gpus" style="width:70px; height : 35px; margin-right: 25px;" disabled>
                    </div>
                </form>              
                <div v-if="modal == 'edit'" class="form-group" style="margin-top : 20px" >
                    <switch-button v-model="dict[modal].activity" @toggle="toggleEmit">
                    {{dict[modal].activity ? "Turned on" : "Turned off"}}
                    </switch-button>
                </div>
                <div v-if="role!='USER' && modal=='edit'">
                    <label>Lista aktivnosti:</label>
                    <div>
                    <select class="custom-select" id="selectAct" v-model="selectedActivity">
                        <option disabled value="">Please select one</option>
                        <option v-for="act in dict[modal].listOfActivities">
                            {{act.startActivity +'--' + (act.endActivity == undefined ? "" : act.endActivity)}}  </option>
                    </select>
                    </div>
                </div>
                <div v-if="role=='SUPER_ADMIN' && modal=='edit'" class="modal-footer">
                    
                    <button type="button" class="btn btn-sm btn-primary" 
                    v-bind:disabled="selectedActivity==''" v-on:click="deleteAct()">
                    Delete this activity</button>
                </div>
            </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="addVm()" v-if="modal == 'add'">Add Virtual Machine</button>
            <button type="button" class="btn btn-secondary" v-if=" modal == 'add'" v-on:click="clearFields()" > Cancel </button>
            <button type="button" class="btn btn-primary" v-on:click="updateVm()" v-if="modal == 'edit'">Save changes</button>
            <button type="button" class="btn btn-secondary" v-on:click="cancelUpdate()" v-if="modal =='edit'">Cancel</button>
            <button type="button" class="btn btn-secondary" v-on:click="closeModal()" v-if="modal =='show'">Close</button>
        </div>
        </div>
    </div>
    <activity-form ref="activity"></activity-form>
    </div>`
    ,
    methods : {
        editAct : function(){
            this.$refs.activity.setEdit(this.selectedActivity);
            $("#actModal").modal("show");
        },
        deleteAct : function() {
            var dateActivity = this.getSelectedActivity();
            var filtered = this.dict.edit.listOfActivities.filter(item => item.startActivity != dateActivity.startActivity);
            this.dict.edit.listOfActivities = filtered;
            this.deleteItems.push(dateActivity.startActivity);
            this.selectedActivity = "";
        },
        getSelectedActivity : function(){
            var splited = this.selectedActivity.split("--");
            var dateActivity = {"startActivity" : splited[0],"endActivity" : splited[1]};
            return dateActivity;
        },
        setEditedMachine : function(selectedMachine){
            this.backup = {...selectedMachine};
            this.dict.edit = selectedMachine;
            var categorySelect = document.getElementById("categorySelect");
            categorySelect.value = selectedMachine.category.name;
            var orgSelect = document.getElementById("organizationSelect");
            orgSelect.value = selectedMachine.organization.name;
            this.setCategoryParams();
            if('organization' in selectedMachine){
                this.getDrivesWithoutVM(selectedMachine.organization.name);
            }
            this.getSelectedDrives(selectedMachine.name);
            this.deleteItems = [];
            
        },

        closeModal : function(){
            $("#vmModal").modal('hide');
        },

        setUpForShowing : function(selectedMachine){
            this.dict.show = selectedMachine;
            var categorySelect = document.getElementById("categorySelect");
            categorySelect.value = selectedMachine.category.name;
            this.setCategoryParams();
            this.getSelectedDrives(selectedMachine.name);
        },

        getSelectedDrives : function(machineName){
            axios
            .get("/getSelectedDisks/" + machineName)
            .then(response => {
                this.selectedDrives = response.data;
            })
        },

        getDrivesWithoutVM : function(orgName){
            axios
            .get("/getDrivesWithoutVM/" + orgName)
            .then(response =>{
                this.orgDrives = response.data;
            })
        },

        setUpDisks : function(){
            var e = document.getElementById("organizationSelect");
            this.selectedOrgName = e.options[e.selectedIndex].text;
            this.getDrivesWithoutVM(this.selectedOrgName);
        },

        setCategoryParams : function(){
            var e = document.getElementById("categorySelect");
            var newCatName = e.options[e.selectedIndex].text;
            this.categories.forEach(element => {
                if(element.name == newCatName){
                    this.dict[this.modal].category = element;
                }
            });
        },

        resetNameField : function(){
            document.getElementById('vm_name').style.borderColor = "";
            document.getElementById('name_err').innerHTML = ""; 
        },

        clearFields : function(){
            this.dict.add.name = "";
            var e = document.getElementById("diskSelect");
            e.selectedIndex = -1;
            $("#vmModal").modal('hide');
            this.resetNameField();
        
        },

        cancelUpdate : function(){
            this.dict.edit.name = this.backup.name;
            this.dict.edit.activity = this.backup.activity;
            this.dict.edit.category = this.backup.category;
            this.dict.edit.listOfActivities = this.backup.listOfActivities;
            this.selectedDrives.forEach(element => {
                document.getElementById(element.name).selected = true;
            })
            this.orgDrives.forEach(element => {
                document.getElementById(element.name).selected = false;
            })

            $("#vmModal").modal('hide');
            this.resetNameField();
            this.$parent.selectedMachine = null;
        },

        highlightNameField : function(){
            document.getElementById('vm_name').style.borderColor = "red";
            document.getElementById('name_err').innerHTML = "Virtual machine with that name already exsists.Please enter another."; 
        },
        addVm : function(){
            var self = this;
            var $vmForm = $("#vmForm");
            if(!$vmForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($vmForm).click().remove();
            }
            else{
                var e = $("#diskSelect");
                var disks = e.val();
                if(disks == null){
                    disks = [];
                }
                axios
                .post("/addVM", {"name" : ''+ self.dict.add.name, "categoryName" : '' + self.dict.add.category.name, "disks" :  disks, "orgName" : ''+ self.selectedOrgName})
                .then(response => {
                    $("#vmModal").modal('hide');
                    self.$parent.getMachines();
                    self.clearFields();
                })
                .catch(error => {
                    self.highlightNameField();
                })
            }
        },

        updateVm : function(){
            var self = this;
            var $vmForm = $("#vmForm");
            if(!$vmForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($vmForm).click().remove();
            }
            else{
                var e = $("#diskEditSelect");
                var selectedDisks = e.val();
                if(selectedDisks == null){
                    selectedDisks = [];
                }

                axios
                .post("/updateMachine", {"oldName" : '' + self.backup.name, "newName" : '' + self.dict.edit.name, 
                        "categoryName" : '' + self.dict.edit.category.name, "disks" : selectedDisks,
                        "deletedItems" : self.deleteItems})
                .then(response => {
                    $("#vmModal").modal('hide');
                    self.deleteItems = []
                    self.resetNameField();
                    self.$parent.getMachines();
                    self.$parent.selectedMachine = null;
                    toast("Successfully updated virtual machine");
                })
                .catch(error => {
                    self.highlightNameField();
                })
            }
        },  

        getCategories : function(){
            axios
            .get("/getCategories")
            .then(response => {
                this.categories = response.data;
            });
        },

        getDrives : function(){
            axios
            .get("/getDrives/" + localStorage.getItem("email"))
            .then(response =>{
                this.drives = response.data;
            })
        },

        setUpForAdding : function(){            
            this.setCategoryParams();
            if(this.role == 'SUPER_ADMIN'){
                this.setUpDisks();
            }
        },

        getOrganizations : function(){
            axios
            .get("/getOrganizations")
            .then(response => {
                this.organizations = response.data;
            })
        },
        toggleEmit : function(){
            var self = this;
            axios
            .post("/changeActivity", {"name" : '' + self.backup.name, "activity" : ''+ self.dict.edit.activity})
            .then(response =>{
                self.dict.edit.listOfActivities = response.data;
                self.$parent.getMachines();
            })
        }
    },

    mounted(){
        this.getCategories();
        this.getOrganizations();
        this.role = localStorage.getItem("role");
        if(this.role == 'ADMIN'){
            axios
            .get("/getUsersDrivesWithoutVM")
            .then(response => {
                this.orgDrives = response.data;
            })
        }
    }
})