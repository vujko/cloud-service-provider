Vue.component("side-bar",{
    data : function(){
        return {
            page : null,
            role : null,
            filter : {
                searchArg : "",
                coreFrom : null,
                coreTo : null,
                ramFrom : null,
                ramTo : null,
                gpuFrom : null,
                gpuTo : null
            },
            filterDrive : {
                searchArg : "",
                capFrom : null,
                capTo : null,
                type : []
            }
        }
    },
    template : `    
    <nav class="col-md-2 d-none d-md-block bg-light sidebar">
        <div class="sidebar-sticky">
            <ul class="nav flex-column">      
                <li class="nav-item">
                    <a class="nav-link active" href="#/homepage"><span data-feather="home"></span>
                        Home <span class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item" v-if="role!='USER'">
                    <a class="nav-link" href="#/users"><span data-feather="file"></span>
                        Users
                </a>
                </li>
                <li class="nav-item" v-if="role != 'USER'">
                    <a class="nav-link" href="#/organizations" ><span data-feather="shopping-cart"></span>
                        Organizations
                    </a>
                </li>
                <li class="nav-item" v-if="role == 'SUPER_ADMIN'">
                    <a class="nav-link" href="#/categories" ><span data-feather="categories"  ></span>
                        Categories
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#/drives"><span data-feather="bar-chart-2"></span>
                        Drives
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#/profile"><span data-feather="layers"></span>
                        Edit profile
                    </a>
                </li>
                &nbsp;
                <li v-if="page=='DRIVES'" class="nav-item">
                    <a href="#pageSubmenu2" data-toggle="collapse" aria-expanded="false" class="nav-link dropdown-toggle">Tip</a>
                    <ul class="collapse list-unstyled" id="pageSubmenu2">
                        <li><span class="fa fa-chevron-right mr-2"></span><input type="checkbox" id="HDD" value="HDD" v-model="filterDrive.type">HDD</li>
                        <li><span class="fa fa-chevron-right mr-2"></span><input type="checkbox" id="SSD" value="SSD" v-model="filterDrive.type">SSD</li>
                    </ul>
                </li>&nbsp;
                <li v-if="page=='DRIVES'" class="nav-item">
                    <div class="form-inline row">
                    <div class="col-md-3">
                        <label>Kapacitet</label>
                    </div>
                    <div class="col-md-4">
                        <input type="number" min="0" class="form-control" style="width:70px; height : 35px;" v-model="filterDrive.capFrom" placeholder="Min">
                    </div>
                    <div class="col-md-3">
                        <input type="number" min="0" class="form-control" style="width:70px; height : 35px;" v-model="filterDrive.capTo" placeholder="Max">
                    </div>
                    </div>&nbsp;
                </li>
                
            </ul>
            
            <ul class="navbar-nav mr-auto">
            <li v-if="page=='DRIVES' || page=='HOMEPAGE'" class="nav-item active">
                <input class="form-control form-control-dark w-100" type="text" 
                    placeholder="Search" v-model="filter.searchArg"  aria-label="Search">
            </li>
            &nbsp;
            <li v-if="page=='HOMEPAGE'">
                <form action="" class="form-horizontal">
                <div class="form-inline row">
                    <div class="col-md-3">
                        <label>Jezgra</label>
                    </div>
                    <div class="col-md-4">
                        <input type="number" min="0" class="form-control" style="width:75px; height : 35px;" v-model="filter.coreFrom" placeholder="Min">
                    </div>
                    <div class="col-md-3">
                        <input type="number" min="0" class="form-control" style="width:75px; height : 35px;" v-model="filter.coreTo" placeholder="Max">
                    </div>
                </div>&nbsp;
                <div class="form-inline row">
                    <div class="col-md-3">
                        <label>Ram</label>
                    </div>
                    <div class="col-md-4">
                        <input type="number" min="0" class="form-control" style="width:75px; height : 35px;" v-model="filter.ramFrom" placeholder="Min">
                    </div>
                    <div class="col-md-3">
                        <input type="number" min="0" class="form-control" style="width:75px; height : 35px;" v-model="filter.ramTo" placeholder="Max">
                    </div>
                </div>&nbsp;
                <div class="form-inline row">
                    <div class="col-md-3">
                        <label>Gpus</label>
                    </div>
                    <div class="col-md-4">
                        <input type="number" min="0" class="form-control" style="width:75px; height : 35px;" v-model="filter.gpuFrom" placeholder="Min">
                    </div>
                    <div class="col-md-3">
                        <input type="number" min="0" class="form-control" style="width:75px; height : 35px;" v-model="filter.gpuTo" placeholder="Max">
                    </div>
                </div>
                </form>
            </li>
            &nbsp;
            <li v-if="page=='DRIVES' || page=='HOMEPAGE'" class="nav-item active">
                <button type="button" v-if="page=='DRIVES'" class="btn btn-dark float-right btn-sm" @click="filterDriveMethod()"> Pretrazi </button>
                <button type="button" v-if="page=='DRIVES'" class="btn btn-danger float-right btn-sm" @click="ponistiDrive()"> Ponisti </button>
                <button type="button" v-if="page=='HOMEPAGE'" class="btn btn-dark float-right btn-sm" v-on:click="filterVM()"> Pretrazi </button>
                <button type="button" v-if="page=='HOMEPAGE'" class="btn btn-danger float-right btn-sm" v-on:click="ponistiVM()"> Ponisti </button>
            </li>
            </ul>
        </div>
    </nav>
    `,
    methods : {
        collectDriveCheck : function(){
            this.filterDrive.searchArg = this.filter.searchArg;
            if(this.filterDrive.capFrom == "")
                this.filterDrive.capFrom = null;
            if(this.filterDrive.capTo == "")
                this.filterDrive.capTo = null;
        },
        filterDriveMethod : function(){
            this.collectDriveCheck();
            EventBus.$emit("filterCapacity",this.filterDrive);  
        },
        valitateFilterVm : function(){
            if(this.filter.coreFrom == "")
                this.filter.coreFrom = null;
            if(this.filter.coreTo == "")
                this.filter.coreTo = null;
            if(this.filter.ramFrom == "")
                this.filter.ramFrom = null;
            if(this.filter.ramTo == "")
                this.filter.ramTo = null;
            if(this.filter.gpuFrom == "")
                this.filter.gpuFrom = null;
            if(this.filter.gpuTo == "")
                this.filter.gpuTo = null;
        },
        filterVM : function(){
            this.valitateFilterVm();
            EventBus.$emit("filterVM",this.filter);  
        },
        ponistiDrive : function(){
            this.filterDrive.searchArg = "";
            this.filter.searchArg = "";
            this.filterDrive.capFrom = null;
            this.filterDrive.capTo = null;
            this.filterDrive.type.forEach(tip => document.getElementById(tip).checked = false);
            this.filterDrive.type = [];
            EventBus.$emit("refresh");
        },
        ponistiVM : function(){
            this.filter.searchArg = "";
            this.filter.coreFrom = null;
            this.filter.coreTo = null;
            this.filter.ramFrom = null;
            this.filter.ramTo = null;
            this.filter.gpuFrom = null;
            this.filter.ramTo = null;
            EventBus.$emit("refreshVM");
        }
    },
    mounted(){
        this.page = localStorage.getItem("page").substring(1).toUpperCase();
        this.role = localStorage.getItem("role");
    }

})