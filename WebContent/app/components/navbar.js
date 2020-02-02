Vue.component("nav-bar",{
    data : function(){
      return {
        page : null,
        input : null
      }
    },
    template :`
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <p class="navbar-brand col-sm-3 col-md-2 mr-3" style="margin-top:15px">Cloud service provider</p>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>     
        <ul class="navbar-nav flex-row ml-md-auto d-none d-md-flex px-3">
          <li class="nav-item">
              <a class="nav-link" v-on:click="logout()">Logout</a>
          </li>
        </ul>
    </nav>
    `,
    methods : {	
      logout : function(){
        var self = this;
        axios
        .post("/logout")
        .then(function(response){
          self.$router.replace("/");
        })
      },
      searchDrive : function(){
        EventBus.$emit("searched",this.input);        
      },
      searchVM : function(){
        EventBus.$emit("searchedVM",this.input);
      }
    },
    mounted(){
      this.page = localStorage.getItem("page").substring(1).toUpperCase();
    }
   
})
