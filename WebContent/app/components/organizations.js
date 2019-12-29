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
                <tr v-for="o in organizations">
                    <td>{{ o.name }}</td>
                    <td>{{ o.description }}</td>
                    <td>{{ o.logo }}</td>

                </tr>
        </table>
    </div>	 
    
    `,

    mounted () {
        axios
        .get("/getOrganizations")
        .then(response => {
            this.organizations = response.data
        });
    }
})