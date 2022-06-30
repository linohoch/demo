import {Filters} from "@/module/art-filter/filters";

$(()=>{
    new ageDist();
    new Filters().numberPointToFixedTo(0);
})

export default class ageDist{

    constructor() {
        this.loadTable();
        this.eventBinding();
    }
    loadTable(code = " "){
        const template = require('../../------/template/localDistTable.html');

        axios.post(`/------/ageDist/data?locCode=${code}`)
            .then((res) => {
                $('.tableArea').html(template(res.data));
                document.querySelector(`[data-loc="${code}"]`).classList.add('slcted')
            })
    }
    eventBinding(){
        $(document).on('click','.slct_local',(e)=>{
            this.loadTable(e.currentTarget.dataset.loc);
        })
    }

}