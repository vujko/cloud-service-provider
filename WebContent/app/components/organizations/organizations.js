Vue.component("organizations",{
    data : function(){
        return{
            selectedOrg : null,
            organizations : null,
            role : null
        }
    },
    template : `
    <div>
        <nav-bar></nav-bar>
        <table class="table table-striped table-responsive col px-md-2">
            <thead>
            <tr>
                <th scope="col">Ime</th><th scope="col">Opis</th><th scope="col">Logo</th></tr></thead>
                <tbody>
                <tr v-for="o in organizations" :key="o.name" v-on:click="selectOrganization(o)" v-bind:class="{selected : selectedOrg != null && selectedOrg.name===o.name}">
                    <td>{{ o.name }}</td>
                    <td>{{ o.description }}</td>
                    <td>{{ o.logo }}</td>
                </tr>
                </tbody>
        </table>
        <span>
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#orgModal">
                Add organization
            </button>
            <button type="button" class="btn btn-primary" v-on:click="editOrganization()" v-bind:disabled="selectedOrg==null">
                Edit organization
            </button>
        </span>
        <!-- Modals -->
        <organization-form></organization-form>
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
            axios
            .get("/getOrganizations")
            .then(response => {
                this.organizations = response.data
            });
        }
    },
    mounted () {
        this.getOrganizations();
        this.role = localStorage.getItem("role");

    }
})