Vue.component("log-in", {
    data: function () {
        return {
            verified : false,
            input: {
                email : "",
                password : ""
            }
            
        }
    },
    template :
    `
    <form>
        <table>
            <tr>
                <td>Enter email:</td>

                <td>
                    <input type="email" name="email" v-model="input.email" required>
                </td>
            </tr>
            <tr>
                <td>Enter password:</td>
                <td><input type="password" name="password" v-model="input.password" required></td>
            </tr>
            <tr>
                <td><button v-on:click="login()" type="button">Log in</button></td>
            </tr>
        </table>
    </form>

` ,
    methods : {
        login : function(){
            var self = this;
            var userinput = {"email" : '' + this.input.email, "password" : '' + this.input.password};
            axios
            .post("/verify", userinput)
            .then(function(response){ 
                this.verified = response.data;
                if(this.verified){
                    axios
                    .post("/login",userinput);
                    self.$router.replace("/homepage");
                }
                else{
                    toast("Bad login");
                }
                
            })
        }
        
    }


});