Vue.component("users", {
	data: function () {
		    return {
		      users : null
		    }
	},
	template: ` 
    <div>
        <nav-bar></nav-bar>
        <table border="1">
            <tr>
                <th>Ime</th><th>Prezime</th><th>Email</th></tr>
                <tr v-for="u in users">
                    <td>{{u.name}}</td>
                    <td>{{u.surname}}</td>
                    <td>{{u.email}}</td>
                </tr>
        </table>
    </div>	  
`
	, 
	methods : {

        
	},
	mounted () {
        axios
            .get('/getUsers')
            .then(response => {
                this.users = response.data
            });             
    }
});