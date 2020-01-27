Vue.component("add-drive-form",{
    data : function(){
        return{
            dict : {
                add : {
                    name : "",
                    type : "",
                    capacity : "",
                    vm : {name : ""}
                },
                edit : {
                    name : "",
                    type : "",
                    capacity : "",
                    vm : {name : ""}
                }
            },
            backup : {
                name : "",
                type : "",
                capacity : "",
                vm : {name : ""}
            },
            role : null,
            virtualMachines : null,
            selectedVM : "",
            selectedType : "",
            modal : "add"
        }
    },
    template :
    `
    <div class="modal fade" ref="drive-modal" id="driveModal" tabindex="-1" role="dialog" aria-labelledby="driveModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="driveModalLabel" v-if="modal=='add'">Add a new Drive</h5>
            <h5 class="modal-title" id="driveModalLabel" v-if="modal=='edit'">Edit   Drive</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
        <form id="driveForm" class="form-signin" role="form">
        <fieldset>  
            <div class="form-group">
                <label for="naziv">Naziv:</label>
                <input class="form-control" id="naziv" placeholder="Name" name="naziv" type="text" 
                   v-bind:disabled="role=='USER'"  v-model="dict[modal].name" required><p id="name_err"></p>
            </div>
            <div class="form-group">
                <label for="type">Tip:</label>
                <select class="form-control" v-bind:disabled="role=='USER'" id="type" name="type" type="text" v-model="dict[modal].type" required>
                <option>SSD</option>
                <option>HDD</option>
                </select>
            </select>
            </div>
            <div class="form-group">
                <label for="capacity"> Kapacitet: </label>
                <input class="form-control" id="capacity" placeholder="Kapcitet" name="capacity" type="text" 
                   v-bind:disabled="role=='USER'"  v-model="dict[modal].capacity" required>
                <p id="cap_err"> </p>
            </div>
            <div class="form-group">
                <label for="masina">Virtuelna masina:</label>
                <select class="form-control" id="masina" name="masina" type="text" 
                  v-bind:disabled="role=='USER'" v-model="dict[modal].vm.name">
                    <option v-for="virtual in virtualMachines">{{virtual.name}}</option>
                </select>
            </div>
        </fieldset>
        </form>
        </div>
        <div class="modal-footer">
            <button type="button" v-if="modal=='add'" class="btn btn-primary" v-on:click="addDrive()">Add</button>
            <button type="button" v-if="modal=='edit' & role!='USER'" class="btn btn-primary" v-on:click="editDrive()">Save changes</button>
            <button type="button" class="btn btn-secondary" v-on:click="cancel(modal)">Cancel</button>
        </div>
    </div>
    </div>
    </div>
    `,
    methods : {
        setEditedDrive : function(selectedDrive){
            this.backup = {...selectedDrive};
            this.dict.edit = selectedDrive;
            this.dict.edit.vm.name = selectedDrive.vm.name;
        },
        addDrive : function(){
            var self = this;
            var $driveForm = $("#driveForm");
            if( ! $driveForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($driveForm).click().remove();
            }
            else{
                if(!this.checkNumber(this.dict.add.capacity)){
                    return;
                }
                axios
                .post("/addDrive", {"name" : '' + this.dict.add.name, "type" : '' + this.dict.add.type,
                       "capacity" : '' + this.dict.add.capacity, "vm" : '' + this.dict.add.vm.name})
                .then(response =>{
                    this.$emit("driveAdded",response.data);
                    toast("Succesfully added");
                    self.clearForm();
                })
                .catch(error =>{
                    self.highlightNameFields();                                     
                })
            }
        },
        editDrive : function(){
            var self = this;
            var $driveForm = $("#driveForm");
            if( ! $driveForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($driveForm).click().remove();
            }
            else{
                if(!this.checkNumber(this.dict.edit.capacity)){
                    return;
                }
                axios
                .post("/updateDrive",{"oldName" : '' + this.backup.name,"newName" : '' + this.dict.edit.name, "type" : '' + this.dict.edit.type,
                "capacity" : '' + this.dict.edit.capacity, "vm" : '' + this.dict.edit.vm.name})
                .then(response =>{
                    self.resetFields();
                    $("#driveModal").modal('hide');
                    toast("Successfully updated.");
                    self.$parent.selectedDrive = null;
                })
                .catch(error =>{
                    self.highlightNameFields();
                })
            }
        },
        clearForm : function(){
            this.dict.add.name = "";
            this.dict.add.capacity = "";
            this.dict.add.type = "";
            this.dict.add.vm.name = "";
            $("#driveModal").modal('hide');
            this.resetFields();
        }, 
        cancel : function(modal){
            if(modal == "edit")
                this.cancelEdit();
            else
                this.clearForm();
        },
        cancelEdit : function(){
            this.dict.edit.name = this.backup.name;
            this.dict.edit.type = this.backup.type;
            this.dict.edit.capacity = this.backup.capacity;
            this.dict.edit.vm.name = this.backup.vm.name;
            this.resetFields();
            $("#driveModal").modal('hide');
        },
        highlightNameFields : function(){
            document.getElementById("naziv").style.borderColor = "red";
            document.getElementById("name_err").innerHTML = "Drive with that name already exists.";
        },
        resetFields : function(){
            document.getElementById("naziv").style.borderColor = "";
            document.getElementById("name_err").innerHTML = "";
            document.getElementById("capacity").style.borderColor = "";
            document.getElementById("cap_err").innerHTML = "";
        },
        checkNumber : function(number){
            if (isNaN(number) || +number < 0) {
                document.getElementById("capacity").style.borderColor = "red";
                document.getElementById("cap_err").innerHTML = "Raspolozivo stanje mora biti pozitivan broj.";
                return false;
            }
            return true;
        },
        mounted(){
            
        }
    }
})