Vue.component("add-drive-form",{
    data : function(){
        return{
            drive_input : {
                name : "",
                type : "",
                capacity : "",
                vm : {name : ""}
            },
            role : null,
            virtualMachines : null,
            selectedVM : "",
            selectedType : ""
        }
    },
    template :
    `
    <div class="modal fade" ref="drive-modal" id="driveModal" tabindex="-1" role="dialog" aria-labelledby="driveModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="driveModalLabel">Add a new Drive</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
        <form id="driveForm" class="form-signin" role="form">
        <fieldset>  
            <div class="form-group">
                <label for="naziv">Naziv:</label>
                <input class="form-control" id="naziv" placeholder="Name" name="naziv" type="text" v-model="drive_input.name" required><p id="name_err"></p>
            </div>
            <div class="form-group">
                <label for="type">Tip:</label>
                <select class="form-control" id="type" name="type" type="text" v-model="selectedType" placeholder="asdasd" required>
                <option>SSD</option>
                <option>HDD</option>
                </select>
            </select>
            </div>
            <div class="form-group">
                <label for="capacity">Kapacitet</label>
                <input class="form-control" id="capacity" placeholder="Kapcitet" name="capacity" type="text" v-model="drive_input.capacity" required>
            </div>
            <div class="form-group">
                <label for="masina">Virtuelna masina:</label>
                <select class="form-control" id="masina" name="masina" type="text" v-model="selectedVM" required>
                    <option v-for="virtual in virtualMachines">{{virtual.name}}</option>
                </select>
            </div>
        </fieldset>
        </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="addDrive()">Add</button>
            <button type="button" class="btn btn-secondary" v-on:click="cancel()">Cancel</button>
        </div>
    </div>
    </div>
    </div>
    `,
    methods : {
        addDrive : function(){
            var self = this;
            var $driveForm = $("#driveForm");
            if( ! $driveForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($driveForm).click().remove();
            }
            else{
                axios
                .post("/addDrive", {"name" : '' + this.drive_input.name, "type" : '' + this.selectedType,
                       "capacity" : '' + this.drive_input.capacity, "vm" : '' + this.selectedVM})
                .then(response =>{
                    self.$parent.getDrives();
                    toast("Succesfully added");
                })
                .catch(error =>{
                    toast("Name already exists!");
                })
            }

        },
        cancel : function(){
            this.drive_input.name = "";
            this.selected = "";
            $("#driveModal").modal('hide');
        },
        
        mounted(){
            
        }
    }
})