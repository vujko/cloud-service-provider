Vue.component("activity-form",{
    data : function(){
        return {
            stavkaAktivnosti : "",
            backup : ""
        }
    },
    template : 
    `
    <div class="modal fade" ref="vm-modal" id="actModal" tabindex="-1" role="dialog" aria-labelledby="actModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="actModalLabel">Edit activity</h5>
            <button type="button" class="close" v-on:click="clearFields()" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">                         
            <form id="actForm" class="form-signin" role="form">
            <fieldset>  
                <div class="form-group">
                    <label>Stavka aktivnosti:</label>
                    <input class="form-control" id="vm_name" name="name" type="name" v-model="stavkaAktivnosti" disabled>
                </div>
                <div class="container">
                    <div class='col-md-5'>
                        <div class="form-group">
                            <div class='input-group date' id='datetimepicker6'>
                                <input type='text' class="form-control" />
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class='col-md-5'>
                        <div class="form-group">
                            <div class='input-group date' id='datetimepicker7'>
                                <input type='text' class="form-control" />
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>

                <form class="form-inline">
                   
                </form>              
            </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="updateAct()">Save changes</button>
            <button type="button" class="btn btn-secondary" v-on:click="cancelUpdate()">Cancel</button>
        </div>
        </div>
    </div>
    </div>
    `,
    methods : {
        
        setEdit : function(selectedActivity){
            this.backup = {...selectedActivity}
            this.stavkaAktivnosti = selectedActivity;
            
        },
        clearFields : function(){
            $("#actModal").modal('hide');
        },
        updateAct : function(){

        },
        cancelUpdate : function(){
            $("#actModal").modal('hide');
        }
    }
})