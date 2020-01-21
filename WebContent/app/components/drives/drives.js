Vue.component("drives",{
    data: function(){
        return{
            drives : null,
            selectedDrive : null,
            virtualMachines : null
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
                    <td>drive.vm.name</td>
                </tr>
                </tbody>
        </table>
        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" v-on:click="driveAdd">
            Add drive
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="editDrive" v-bind:disabled="selectedDrive==null">
            Edit Drive
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="deleteDrive" v-bind:disabled="selectedDrive==null">
            Delete
            </button>
        <!-- Modal -->
        <add-drive-form ref="addDriveForm"></add-drive-form>
    </div>
    `,

    methods : {
        getDrives : function(){
            axios
            .get("/getDrives")
            .then(response => {
                this.drives = response.data;
            });
        },
        selectDrive : function(drive){
            this.selectedDrive = drive;
        },
        driveAdd : function(){
            this.$refs.addDriveForm.virtualMachines = {...this.virtualMachines};
            $("#driveModal").modal('show');
        },
        editDrive : function(){

        },
        deleteDrive : function(){

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
        this.getDrives();
        this.getVirtual();  
    }
});