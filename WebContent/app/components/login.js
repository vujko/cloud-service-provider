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
    <div id="relative">
    <div class="container">
        <div class="row vertical-offset-100"></div>
            <div class="col-md-4 col-md-offset-4"></div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">Sign in</h4>
                    </div>
                    <div class="panel-body">
                        <form id="login-form" class="form-signin" role="form">
                        <fieldset>  
                            <div class="form-group">
                                <input class="form-control" placeholder="E-mail" name="email" type="email"  v-model="input.email" required>
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="Password" name="password" type="password" value=""  v-model="input.password" required>
                            </div>
                            <button class="btn btn-lg btn-success btn-block" v-on:click="login()" type="button" value="Login">Login</button>
                        </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
` ,
    methods : {
        login : function(){
            var self = this;
            var $loginForm = $("#login-form");
            if( ! $loginForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($loginForm).click().remove();
            }
            else{
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
                        toast("Wrong email or password.Please try again.");
                    }
                    
                })
            }

            
        }
        
    }


});