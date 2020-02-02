Vue.component("vm", {
    data : function(){
        return {
            role : null,
            machines : null,
            selectedMachine : null,
            organizations : null,
            orgName : {

            }
        }
        
    },
    template : `<div>
    <table class="table table-striped col px-md-2" border="2">
        <thead class="thead-light">
        <tr>
            <th scope="col">Ime</th><th scope="col">Br. jezgara</th><th scope="col">RAM (GB)</th><th scope="col">GPU</th><th scope="col">Active</th><th scope="col" v-if="role == 'SUPER_ADMIN'" >Organizacija</th></tr></thead>
            <tbody>
            <tr v-for="m in machines" :key="m.name" v-on:click="selectMachine(m)" v-bind:class="{selected : selectedMachine != null && selectedMachine.name===m.name}">
                <td>{{ m.name }}</td>
                <td>{{ m.category.cores }}</td>
                <td>{{ m.category.ram }}</td>
                <td>{{ m.category.gpus }}</td>
                <td>{{ m.activity ? "On" : "Off" }}</td>
                <td v-if="'organization' in m && role == 'SUPER_ADMIN'">{{ m.organization.name}} </td>

            </tr>
            </tbody>
    </table>
    <span v-if="role != 'USER'">
        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" v-on:click="addVM()">
            Add Virtual Machine
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="editVM()" v-bind:disabled="selectedMachine==null">
            Edit Virtual Machine
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="deleteVM()" v-bind:disabled="selectedMachine==null">
            Delete Virtual Machine 
        </button>
    </span>

    <button type="button" class="btn btn-primary btn-sm" v-on:click="showVM()" v-bind:disabled="selectedMachine==null" v-if="role == 'USER'">
        Show details
    </button>

    <vm-form ref="vmForm"></vm-form>

    </div>
    `,
    methods : {

        openMachineModal : function(type){
            $("#vmModal").modal("show");
            this.$refs.vmForm.modal = type;
        },

        selectMachine : function(m){
            this.selectedMachine = m;
        },

        addVM : function(){
            this.openMachineModal('add');
            this.$refs.vmForm.setUpForAdding();
        },

        editVM : function(){
            this.openMachineModal("edit");

            this.$refs.vmForm.setEditedMachine(this.selectedMachine);
        },

        showVM : function(){
            this.openMachineModal("show");
            this.$refs.vmForm.setUpForShowing(this.selectedMachine);
        },

        refresh : function(){
            this.getMachines();
        },
        deleteVM : function(){
            var self = this;
            axios
            .post("/deleteMachine", '' + this.selectedMachine.name)
            .then(response =>{
                self.selectedMachine = null;
                toast("Successfully deleted.");
                self.getMachines();
            })
            .catch(error =>{
                alert("Unable to delete machine.")
            })
        },
        getMachines : function(){
            axios
            .get("/getMachines/" + localStorage.getItem("email"))
            .then(response => {
                this.machines = response.data;
            })
        },
        filter : function(filter){
            var self = this; 
            axios
            .post("/VMfilter", filter)
            .then(function(response){
                self.machines = response.data;
            })
            .catch(function(response){
                self.machines = response.data;
            })
        }
    },

    mounted(){
        this.getMachines();
        this.role = localStorage.getItem("role");
        EventBus.$on('refreshVM', this.refresh);
        EventBus.$on('filterVM', this.filter);

    }
})