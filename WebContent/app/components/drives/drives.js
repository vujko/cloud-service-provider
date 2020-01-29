Vue.component("drives",{
    data: function(){
        return{
            drives : null,
            selectedDrive : null,
            virtualMachines : null,
            organizations : null,
            role : null
        }
    },
    template : 
    `<div>
        <table class="table table-striped">
            <thead class="thead-dark">
                <tr>

                <th>Ime</th>
                <th>Kapacitet(GB)</th>
                <th>Organizacija</th>
                <th>Virtuelna masina</th>

                </tr>                
                </thead>
                <tbody>
                <tr v-for="drive in drives" 
                v-on:click="selectDrive(drive)" v-bind:class="{selected : selectedDrive != null && selectedDrive.name===drive.name}">
                    <td>{{drive.name}}</td>
                    <td>{{drive.capacity}}</td>
                    <td v-if="'organization' in drive" > {{drive.organization.name}}</td>
                    <td v-if="'vm' in drive" >{{drive.vm.name}}</td>
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
            .get("/getDrives/" + localStorage.getItem("email"))
            .then(response => {
                this.drives = response.data;
            });
        },
        selectDrive : function(drive){
            this.selectedDrive = drive;
        },
        openDriveModal : function(mode){
            this.$refs.addDriveForm.modal = mode;
            this.$refs.addDriveForm.role = this.role;
            $("#driveModal").modal('show');
        },
        driveAdd : function(){
            this.openDriveModal('add');
            this.$refs.addDriveForm.setUpForAdding();

        },
        addDrive : function(drive){
            //this.drives.push(drive);
            this.getDrives();
        },
        search : function(drive){           
            var self = this;
            axios
            .post("/searchDrives",'' + drive)
            .then(function(response){
                self.drives = response.data;
            })
            .catch(function(response){
                self.drives = response.data;
                alert("Nema rezultata pretrage");
            })
        },
        filter : function(capacity,type){
            var self = this;
            var filter = capacity.concat(type); 
            axios
            .post("/driveFilterCapacity",filter)
            .then(function(response){
                self.drives = response.data;
            })
            .catch(function(response){
                self.drives = response.data;
            })
        },
        editDrive : function(){
            
            axios
            .get("getSelectedMachines/" + this.selectedDrive.organization.name)
            .then(response =>{
                this.$refs.addDriveForm.virtualMachines = response.data;
            })
            this.$refs.addDriveForm.setEditedDrive(this.selectedDrive);
            this.openDriveModal('edit');
        },
        deleteDrive : function(){
            var self = this;
            axios
            .post("/deleteDrive",{"name":'' + this.selectedDrive.name})
            .then(function(response){
                if(response.data){
                    self.selectedDrive = null;
                    toast("Successfully deleted");
                    self.getDrives();
                }
            })
            .catch(error=>{
                alert("Something wrong");
            });
        },
        getVirtual : function(){
            axios
            .get("/getMachines/" + localStorage.getItem("email"))
            .then(response => {
                this.virtualMachines = response.data;
            });
        },
        getOrganizations : function(){
            axios
            .get("/getOrganizations")
            .then(response => {
                this.organizations = response.data
            });
        }
    },
    mounted () {
        this.role = localStorage.getItem("role");
        this.getDrives(); 
        this.getVirtual();
        EventBus.$on('searched', this.search);
        EventBus.$on('filterCapacity', this.filter);
    }
});