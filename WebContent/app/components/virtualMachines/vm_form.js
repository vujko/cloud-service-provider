Vue.component("vm-form", {
    data : function(){
        return {
            modal : "add",
            dict : {
                add : {
                    name : "",
                    cores : null,
                    ram : null,
                    gpus : null
                },
                edit : {
                    name : "",
                    cores : null,
                    ram : null,
                    gpus : null
                },

            },
            categories : null,
            disks : null
        }
    },

    template : `
    
    <div class="modal fade" ref="vm-modal" id="vmModal" tabindex="-1" role="dialog" aria-labelledby="vmModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="vmModalLabel" v-if="modal=='add'">Add a new Virtual Machine</h5>
            <h5 class="modal-title" id="vmModalLabel" v-if="modal=='edit'">Edit a Virtual Machine</h5>
            <button type="button" class="close" v-on:click="clearFields()" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
                            
            <form id="vmForm" class="form-signin" role="form">
            <fieldset>  
                <div class="form-group">
                    <label>Name:</label>
                    <input class="form-control" id="vm_name" name="name" type="name" v-model="dict[modal].name   " required><p id="name_err"></p>
                </div>
                <div >
                    <label>Disks:</label>
                    <div>
                    <select class="mdb-select md-form form-control" id="diskSelect" multiple  style="width:450px" >

                    </select>
                    </div>
                </div>

                <div >
                    <label>Category:</label>
                    <div>
                    <select multiple class="mdb-select md-form form-control" id="categorySelect" style="width:450px" >

                    </select>
                    </div>
                </div>

                <form class="form-inline">
                    <div class="form-group" style="margin-top : 10px">
                        <label style="margin-right: 10px;">Cores:</label>
                        <input class="form-control"  id="vm_name" name="name" type="name" v-model="dict[modal].cores" style="width:70px; height : 35px; margin-right: 25px;" disabled>
                        <label style="margin-right: 10px;">RAM:</label>
                        <input class="form-control" id="vm_name" name="name" type="name" v-model="dict[modal].ram" style="width:70px; height : 35px; margin-right: 25px;" disabled>
                        <label style="margin-right: 10px;">GPUS:</label>
                        <input class="form-control" id="vm_name" name="name" type="name" v-model="dict[modal].gpus" style="width:70px; height : 35px; margin-right: 25px;" disabled>
                    </div>
                </form>

                
                
                
            </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="addVm()" v-if="modal == 'add'">Add Virtual Machine</button>
            <button type="button" class="btn btn-secondary" v-if=" modal == 'add'" v-on:click="clearFields()" > Cancel </button>
            <button type="button" class="btn btn-primary" v-on:click="updateVm()" v-if="modal == 'edit'">Save changes</button>
            <button type="button" class="btn btn-secondary" v-on:click="cancelUpdate()" v-if="modal =='edit'">Cancel</button>
        </div>
        </div>
    </div>
    </div>`
    ,
    methods : {
        addVm : function(){
            var self = this;
            var $vmForm = $("#vmFrom");
            if(!$vmForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($vmForm).click().remove();
            }
            else{
                // axios
                // .post("/addVM", {"name"})
            }
        },

        getCategories : function(){
            axios
            .get("/getCategories")
            .then(response => {
                this.categories = response.data;
            });
        },

        getDrives : function(){

        }
    },

    mounted(){
        this.getCategories();
        this.getDrives();
    }
})