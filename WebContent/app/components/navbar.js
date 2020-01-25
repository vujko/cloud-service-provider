Vue.component("nav-bar",{
    data : function(){
      return {
        page : null,
        input : null
      }
    },
    template :`
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <p class="navbar-brand col-sm-3 col-md-2 mr-3">Cloud service provider</p>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li v-if="page=='DRIVES' || page=='HOMEPAGE'" class="nav-item active">
              <input class="form-control form-control-dark w-100" type="text" 
                  placeholder="Search" v-model="input" aria-label="Search">
          </li>
          &nbsp;
          <li v-if="page=='DRIVES' || page=='HOMEPAGE'" class="nav-item active">
              <button type="button" class="btn btn-light" v-on:click="search()"> Pretrazi </button>
          </li>
        </ul>
        <ul class="navbar-nav px-3">
          <li class="nav-item">
              <a class="nav-link" v-on:click="logout()">Logout</a>
          </li>
        </ul>

      </div>

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
      search : function(){
        EventBus.$emit("searched",this.input);        
      }
    },
    mounted(){
      this.page = localStorage.getItem("page").substring(1).toUpperCase();
    }
   
})