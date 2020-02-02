Vue.component("category-form",{
    data : function(){
        return {
            modal : "add",
            backup : {
                name : "",
                cores : null,
                ram : null,
                gpus : null
            },
            oldName : "",
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

            }


        }
    },
    template : `
    <div class="modal fade" ref="category-modal" id="categoryModal" tabindex="-1" role="dialog" aria-labelledby="categoryModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="categoryModalLabel" v-if="modal=='add'">Add a new category</h5>
            <h5 class="modal-title" id="categoryModalLabel" v-if="modal=='edit'">Edit a category</h5>
            <button type="button" class="close" v-on:click="clearFields()" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
                            
            <form id="categoryForm" class="form-signin" role="form">
            <fieldset>  
                <div class="form-group">
                    <input class="form-control" id="cat_name" placeholder="Name" name="name" type="name" v-model="dict[modal].name   " required><p id="name_err"></p>
                </div>
                <div class="form-group">
                    <input class="form-control" id="cat_cores" placeholder="Number of cores" min="0" type="number" @keydown="validate" v-model="dict[modal].cores" required>
                </div>
                <div class="form-group">
                    <input class="form-control" id="cat_ram" placeholder="RAM in GB" min="0" type="number" @keydown="validate" v-model="dict[modal].ram" required>
                </div>
                <div class="form-group">
                    <input class="form-control" id="cat_gpus" placeholder="Number of GPU cores" min="0" @keydown="validate" type="number" v-model="dict[modal].gpus" required>
                </div>
                
                
            </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="addCategory()" v-if="modal == 'add'">Add category</button>
            <button type="button" class="btn btn-secondary" v-if=" modal == 'add'" v-on:click="clearFields()" > Cancel </button>
            <button type="button" class="btn btn-primary" v-on:click="updateCategory()" v-if="modal == 'edit'">Save changes</button>
            <button type="button" class="btn btn-secondary" v-on:click="cancelUpdate()" v-if="modal =='edit'">Cancel</button>
        </div>
        </div>
    </div>
    </div>
    `,

    methods : {
        setEditedCategory : function(selectedCategory){
            this.backup = {...selectedCategory};
            this.dict.edit = selectedCategory;
            this.oldName = selectedCategory.name;

        },
        highlightNameField : function(){
            document.getElementById('cat_name').style.borderColor = "red";
            document.getElementById('name_err').innerHTML = "Category with that name already exsists.Please enter another.";
        },

        resetNameField : function(){
            document.getElementById('cat_name').style.borderColor = "";
            document.getElementById('name_err').innerHTML = "";
        },

        clearFields : function(){
            this.dict.add.name = "";
            this.dict.add.cores = "";
            this.dict.add.gpus = "";
            this.dict.add.ram = "";


            $('#categoryModal').modal('hide');
            this.resetNameField();
        
        },

        cancelUpdate : function(){
            this.dict.edit.name = this.backup.name;
            this.dict.edit.gpus = this.backup.gpus;
            this.dict.edit.ram = this.backup.ram;
            this.dict.edit.cores = this.backup.cores;
            $('#categoryModal').modal('hide'); 
            this.resetNameField();
        },

        addCategory(){
            var self = this;
            var $categoryForm = $("#categoryForm");
            if(! $categoryForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($categoryForm).click().remove();
            }
            else{
                axios
                .post("/addCategory", {"name" : '' + self.dict[self.modal].name, "cores" : parseInt(self.dict[self.modal].cores), "ram" : parseInt(self.dict[self.modal].ram ), "gpus" : parseInt(self.dict[self.modal].gpus)})
                .then(response =>{
                    self.$parent.getCategories();
                    self.clearFields();
                })
                .catch(error =>{
                    if(error.response.data != true && error.response.data != false){
                        alert(error.response.data)
                        return;
                    }
                    self.highlightNameField();
                })
            }
        },
        validate(event) {
            if (event.keyCode == 189 || event.keyCode == 190) {
                event.preventDefault();
            }
        },

        updateCategory(){
            var self = this;
            var $categoryForm = $("#categoryForm");

            if(! $categoryForm[0].checkValidity()){
                $('<input type="submit">').hide().appendTo($categoryForm).click().remove();
            }
            else{
                axios
                .post("/updateCategory", {"oldName" : ''+self.oldName, "newName" : '' + self.dict[self.modal].name, "cores" : parseInt(self.dict[self.modal].cores), "ram" : parseInt(self.dict[self.modal].ram ), "gpus" : parseInt(self.dict[self.modal].gpus)})
                .then(response =>{
                    if(response.data){
                        $('#categoryModal').modal('hide'); 
                        self.resetNameField();
                        toast("Successfully updated category"); 
                    }
                })
                .catch(error =>{
                    if(error.response.data != true && error.response.data != false){
                        alert(error.response.data)
                        return;
                    }
                    self.highlightNameField();
                })
            }
        }
    }

})