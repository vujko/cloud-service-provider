Vue.component("bills", {
	data: function () {
		return {
            role : null,
            bills : null,
            start : null,
            end : null,
            total : 0
		}
	},
    template: ` 
    <div>
        <div>
            <label>Start date</label>
            <input type="date" class="form-control" name="birthday" id="start" placeholder="Start date">
        </div>
        <div style="margin-top :10px">
            <label>End date</label>
            <input type="date" class="form-control" name="birthday" id="end" placeholder="Start date">
            
        </div>
        <button style="margin-top : 10px" type="button" class="btn btn-primary" v-on:click="getBills()" >Get Bills</button>
        <table class="table table-striped"  border="2" v-if="bills != null" style="margin-top : 15px">
            <thead class="thead-light"> 
            <tr>
                <th>Resurs</th><th >Cena (EUR)</th></tr></thead>
                <tbody>
                <tr v-for="b in bills" :key="b.resourceName">
                    <td>{{b.resourceName}}</td>
                    <td>{{b.price}}</td>
                </tr>
                <tr style="border-top: 3px solid black;">
                    <td><b>Total</b></td>
                    <td><b>{{total}}</b></td>
                </tr>
            </tbody>
        </table>
    </div>
`
    , 
    methods : {
        getBills : function(){
            this.start = document.getElementById('start').value;
            this.end = document.getElementById('end').value;
            if(this.start == "" || this.end == ""){
                alert("Please enter both dates");
                return;
            }
            var self = this;
            axios
            .post("/getBills", {"startDate" :''+ self.start, "endDate" :  '' + self.end})
            .then(response => {
                this.total = 0.0;
                this.bills = response.data;
                this.bills.forEach(bill => {
                    this.total += parseFloat(bill.price);
                });
                this.total = Math.round(this.total * 100) / 100
            })
        }
    },
	mounted(){
		this.role = localStorage.getItem("role");
	}
});