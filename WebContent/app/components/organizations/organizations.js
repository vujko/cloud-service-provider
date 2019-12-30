Vue.component("organizations",{
    data : function(){
        return{
            organizations : null
        }
    },
    template : `
    <div>
        <nav-bar></nav-bar>
        <table border="1">
            <tr>
                <th>Ime</th><th>Opis</th><th>Logo</th></tr>
                <tr v-for="o in organizations" :key="o.name">
                    <td>{{ o.name }}</td>
                    <td>{{ o.description }}</td>
                    <td>{{ o.logo }}</td>

                </tr>
        </table>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#orgModal">
            Add organization
        </button>

         <!-- Modal -->
        <organization-form></organization-form>
    </div>	 
    
    `,
    methods : {
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
    }
})