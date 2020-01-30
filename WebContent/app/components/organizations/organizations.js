Vue.component("organizations",{
    data : function(){
        return{
            selectedOrg : null,
            organizations : null,
            role : null,
            logos : null
        }
    },
    template : `
    <div>
        <table class="table table-striped col px-md-2">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Ime</th><th scope="col">Opis</th><th scope="col">Logo</th></tr></thead>
                <tbody>
                <tr v-for="o in organizations" :key="o.name" v-on:click="selectOrganization(o)" v-bind:class="{selected : selectedOrg != null && selectedOrg.name===o.name}">
                    <td>{{ o.name }}</td>
                    <td>{{ o.description }}</td>
                    <td><img :src="o.logo" style="width:50px;height:60px;"></td>
                </tr>
                </tbody>
        </table>
        <span>
            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#orgModal" v-if="role == 'SUPER_ADMIN'">
                Add organization
            </button>
            <button type="button" class="btn btn-primary btn-sm" v-on:click="editOrganization()"  v-bind:disabled="selectedOrg==null">

                Edit organization
            </button>
            <!-- <button type="button" class="btn btn-primary btn-sm" v-on:click="deleteOrganization" v-bind:disabled="selectedOrg==null">
            Delete organization 
            </button> -->
        </span>
        <!-- Modals -->
        <organization-form ref="addForm"></organization-form>
        <edit-org-form ref="editForm"></edit-org-form>
    </div>	 
    
    `,
    methods : {
        
        editOrganization : function(){
            $('#editOrgModal').modal('show'); 
            this.$refs.editForm.setOrg(this.selectedOrg);
        },

        selectOrganization : function(org){
            this.selectedOrg = org;
        },
        getOrganizations : function(){
            var self = this;
            axios
            .get("/getOrganizations")
            .then(response => {
                self.organizations = response.data;
                if(response.data.length == 1){
                    self.selectedOrg = self.organizations[0];
                }
            });
        },
        
        getDisks : function(){
            axios
            .get("/getAvilableDisks")
            .then(response => {
                this.$refs.addForm.disks = response.data;
                // this.$refs.editForm.avilableDisks = response.data;
            })
        },
        deleteOrganization : function(){
            var self = this;
            axios
            .post('/deleteOrganization',{"name" : '' + this.selectedOrg.name})
            .then(function(response){
                if(response.data){
                    self.selectedOrg = null;
                    toast("Successfully deleted.");
                    self.getOrganizations();
                    self.getDisks();
                }
            })
            .catch(error =>{
                alert("Something wrong");
            })
        }
    },
    mounted () {
        this.getOrganizations();
        this.getDisks();
        this.role = localStorage.getItem("role");

    }
})