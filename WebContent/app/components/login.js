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
    
    <div class = "container fixed-top" style="width: 18rem;">
	<div class="wrapper">
		<form id="login-form" name="Login_Form" class="form-signin">       
		    <h3 class="form-signin-heading">Please Sign In</h3>
			  <hr class="colorgraph"><br>
			  
			  <input class="form-control" placeholder="E-mail" name="email" type="email"  v-model="input.email" required>
			  <input class="form-control" placeholder="Password" name="password" type="password" value=""  v-model="input.password" required>   		  
			 
			  <button class="btn btn-lg btn-success btn-block" v-on:click="login()" type="button" value="Login">Login</button>  			
		</form>			
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