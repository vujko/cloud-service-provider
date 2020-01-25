Vue.component("side-bar",{
    data : function(){
        return {
            page : null,
            checkedDrives : [],
            type : []
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
                <li class="nav-item">
                    <a class="nav-link" href="#/users"><span data-feather="file"></span>
                        Users
                </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#/organizations"><span data-feather="shopping-cart"></span>
                        Organizations
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#/categories"><span data-feather="categories"></span>
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
                    <a href="#pageSubmenu1" data-toggle="collapse" aria-expanded="false" class="nav-link dropdown-toggle">Kapacitet</a>
                    <ul class="collapse list-unstyled" id="pageSubmenu1">
                        <li><span class="fa fa-chevron-right mr-2"></span><input type="checkbox" id="Darg1"> Od 200 do 500GB</li>
                        <li><span class="fa fa-chevron-right mr-2"></span><input type="checkbox" id="Darg2"> Od 500GB do 1TB</li>
                        <li><span class="fa fa-chevron-right mr-2"></span><input type="checkbox" id ="Darg3"> Preko 1TB</li>
                    </ul>
                </li>
                <li v-if="page=='DRIVES'" class="nav-item">
                    <a href="#pageSubmenu2" data-toggle="collapse" aria-expanded="false" class="nav-link dropdown-toggle">Tip</a>
                    <ul class="collapse list-unstyled" id="pageSubmenu2">
                        <li><span class="fa fa-chevron-right mr-2"></span><input type="checkbox" id="HDD">HDD</li>
                        <li><span class="fa fa-chevron-right mr-2"></span><input type="checkbox" id ="SSD">SSD</li>
                    </ul>
                </li>
                &nbsp;
                <li v-if="page=='DRIVES' || page=='HOMEPAGE'"> 
                    <button type="button" class="btn btn-dark float-right" v-on:click="filter()">Filtriraj</button>
                </li>
            </ul>

        </div>
    </nav>
    `,
    methods : {
        colectDriveCheck : function(){
            var Darg1 = document.getElementById("Darg1").checked;
            var Darg2 = document.getElementById("Darg2").checked;
            var Darg3 = document.getElementById("Darg3").checked;
            this.checkedDrives[0] = Darg1; 
            this.checkedDrives[1] = Darg2; 
            this.checkedDrives[2] = Darg3;
            var hdd = document.getElementById("HDD").checked; this.type[0] = hdd;
            var ssd = document.getElementById("SSD").checked; this.type[1] = ssd;
        },
        filter : function(){
            this.colectDriveCheck();
            EventBus.$emit("filterCapacity",this.checkedDrives,this.type);  
        }
    },
    mounted(){
        this.page = localStorage.getItem("page").substring(1).toUpperCase();
    }

})