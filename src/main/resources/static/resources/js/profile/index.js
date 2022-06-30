'use strict'

import moment from "moment/moment";
import {ProfileInfo} from "@/------/profileInfo";

$(()=>{
    new Profile();
})

export default class Profile {
    constructor() {
        this.profileInfo = new ProfileInfo();
        this.init();
        this.bindEvent();
    }

    init(){
        let sCal = new SearchCalendar();
        sCal.setTargetForm("form[name=srch]");
        sCal.setCalendar("#calendar");
    }
    bindEvent(){

        $(document).on('click','.mbsc-btn-e', ()=>{
            $('#srchForm').submit();
        });

        $(document).on('click','.evt-click-profile', (e)=>{
            const memNo=e.currentTarget.dataset.memno;
            this.profileInfo.openDialog(memNo)
        });

    }
}

export class SearchCalendar{

    constructor(){
    }

    setTargetForm(tgtFormId){
        this.$tgtForm = $(tgtFormId);
    }

    setCalendar(target){
        $(target).mobiscroll().calendar({
            display: 'bubble',
            touchUi: false,
            dateFormat: 'yy-mm-dd',
            lang: 'ko',
            max: new Date(),
            min: moment().subtract(6, 'months'),
        });


        $(document).off('change',target)
            .on('change',target,()=>{
                this.$tgtForm.submit();
            })

        $("#btn-today").on("click",()=>{
            const tDate = moment().format('YYYY-MM-DD');
            $(target).val(tDate);

            if(this.$tgtForm[0].pageNo) this.$tgtForm[0].pageNo.value = "1";
            this.$tgtForm.submit();
        })

        $("#btn-prev").on("click",()=>{
            const tDate = moment($("#calendar").val()).subtract(1,'days').format("YYYY-MM-DD");
            $(target).val(tDate);
            if(this.$tgtForm[0].pageNo) this.$tgtForm[0].pageNo.value = "1";
            this.$tgtForm.submit();
        })

        $("#btn-next").on("click",()=>{
            const tDate = moment($("#calendar").val()).add(1,'days');
            if(tDate.isBefore(moment())){
                $(target).val(tDate.format("YYYY-MM-DD"));
                if(this.$tgtForm[0].pageNo) this.$tgtForm[0].pageNo.value = "1";
                this.$tgtForm.submit();
            }else{
            }
        })
    }

}