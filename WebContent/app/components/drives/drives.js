Vue.component("drives",{
    data: function(){
        return{
            drives : null,
            selectedDrive : null,
            virtualMachines : null,
            role : null
        }
    },
    template : 
    `<div>
        <nav-bar></nav-bar>
        <table class="table table-striped">
            <thead class="thead-dark">
                <tr>
                <th>Ime</th>
                <th>Kapacitet(MB)</th>
                <th>Virtuelna masina</th></tr></thead>
                <tbody>
                <tr v-for="drive in drives" 
                v-on:click="selectDrive(drive)" v-bind:class="{selected : selectedDrive != null && selectedDrive.name===drive.name}">
                    <td>{{drive.name}}</td>
                    <td>{{drive.capacity}}</td>
                    <td>{{drive.vm.name}}</td>
                </tr>
                </tbody>
        </table>
        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" v-on:click="driveAdd" v-if="role!='USER'">
            Add drive
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="editDrive" v-bind:disabled="selectedDrive==null">
            Edit Drive
        </button>
        <button type="button" v-if="role!='USER'"
         class="btn btn-primary btn-sm" v-on:click="deleteDrive" v-bind:disabled="selectedDrive==null">
            Delete
            </button>
        <!-- Modal -->
        <add-drive-form @driveAdded="addDrive($event)" ref="addDriveForm"></add-drive-form>
    </div>
    `,

    methods : {
        getDrives : function(){
            axios
            .get("/getDrives")
            .then(response => {
                this.drives = response.data;
            });
            this.getVirtual();  
        },
        selectDrive : function(drive){
            this.selectedDrive = drive;
        },
        openDriveModal : function(mode){
            this.$refs.addDriveForm.modal = mode;
            $("#driveModal").modal('show');
        },
        driveAdd : function(){
            this.$refs.addDriveForm.virtualMachines = {...this.virtualMachines};
            this.openDriveModal('add');
        },
        addDrive : function(drive){
            this.drives.push(drive);
        },
        editDrive : function(){
            this.$refs.addDriveForm.virtualMachines = {...this.virtualMachines};
            this.$refs.addDriveForm.setEditedDrive(this.selectedDrive);
            this.openDriveModal('edit');
        },
        deleteDrive : function(){
            var self = this;
            axios
            .post("/deleteDrive",'' + this.selectedDrive.name)
            .then(function(response){
                if(response.data){
                    self.selectedDrive = null;
                    toast("Successfully added");
                    self.getDrives();
                }
            })
            .catch(error=>{
                alert("Something wrong");
            });
        },
        getVirtual : function(){
            axios
            .get("/getVirtual")
            .then(response => {
                this.virtualMachines = response.data;
            });
        },
    },
    mounted () {
        this.role = localStorage.getItem("role");
        this.getDrives(); 
    }
});