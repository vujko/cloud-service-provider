Vue.component("categories", {
    data : function(){
        return {
            categories : null,
            selectedCategory : null
        }
    },
    template : `
    <div>
        <table class="table table-striped"  border="2">
            <thead class="thead-light"> 
            <tr>
                <th>Ime</th><th >Br. jezgara</th><th>RAM (GB)</th><th>Br. GPU jezgara</th></tr></thead>
                <tbody>
                <tr v-for="c in categories" :key="c.name" v-on:click="selectCategory(c)" v-bind:class="{selected : selectedCategory != null && selectedCategory.name===c.name}">
                    <td>{{c.name}}</td>
                    <td>{{c.cores}}</td>
                    <td>{{c.ram}}</td>
                    <td>{{c.gpus}}</td>
                </tr>
            </tbody>
        </table>
        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" v-on:click="addCategory()">
            Add Category
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="editCategory()" v-bind:disabled="selectedCategory==null">
            Edit Category
        </button>
        <button type="button" class="btn btn-primary btn-sm" v-on:click="deleteCategory()" v-bind:disabled="selectedCategory==null">
            Delete Category 
        </button>
        <!-- Modal -->
        <category-form ref="categoryForm"></category-form>

    </div>
    `,
    methods : {
        getCategories(){
            axios
            .get("/getCategories")
            .then(response => {
                this.categories = response.data;
            });
        },
        selectCategory : function(c){
            this.selectedCategory = c;
        },

        openCategoryModal(type){
            $("#categoryModal").modal("show");
            this.$refs.categoryForm.modal = type;
        },
        addCategory(){
            this.openCategoryModal('add');

        },
        editCategory(){
            this.openCategoryModal("edit");
            this.$refs.categoryForm.setEditedCategory(this.selectedCategory);
        },
        deleteCategory(){
            var self = this;
            axios
            .post('/deleteCategory',{"name": '' + this.selectedCategory.name})
            .then(function(response){
                if(response.data){
                    self.selectedCategory = null;
                    toast("Successfully deleted.");
                    self.getCategories();
                }
            })
            .catch(error =>{
                alert("Something wrong");
            })
        }


    },
    mounted(){
        this.getCategories();
    }
    
})