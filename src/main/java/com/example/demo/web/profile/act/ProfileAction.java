package com.example.demo.web.profile.act;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/---/------")
public class ProfileAction {


    @Autowired
    private ProfileService ProfileService;


    @RequestMapping("/profile")
    public String profileLog(@RequestParam HashMap<String, Object> param, Model model) {
        this.initParam(param);

        if(param.get("memId") != null){ param.put("memId", param.get("memId").toString().trim()); };

        Map<String, Object> data = ProfileService.getProfileLog(param);

        if (data.get("errMsg") != null) {
            model.addAttribute("errMsg", "회원이 존재하지 않습니다.");
            return "/------/alertPage";
        }
        HashMap<String, Object> pagination = PaginationUtil.pagination(
                (int) param.get("pageNo"),
                (int) param.get("limit"),
                10,
                NumberUtils.toInt("" + data.get("total_num"), 0));

        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyy");
        String thisYear = simpleDateFormat2.format(new Date());
        model.addAttribute("thisYear", thisYear);
        model.addAttribute("pagination", pagination);
        model.addAttribute("data", data);
        model.addAttribute("paramMap", param);


        return "------/index";
    }


    //------------------------

    private void initParam(Map<String, Object> param){

        int pageNo = MapUtils.getIntValue(param,"pageNo",1);
        int limit = 100;
        int start = ( pageNo - 1) * limit;
        param.put("pageNo", pageNo ) ;
        param.put("start" , start );
        param.put("limit" , limit );
        param.put("pagePerCnt", limit);
        //tDate
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());

        param.put("enDate", StringUtils.defaultIfBlank( MapUtils.getString( param, "tDate" ) ,today) );
        param.put("stDate", param.get("enDate"));
        param.put("tDate", param.get("enDate"));
        param.put("orderSlct" , StringUtils.defaultIfBlank(MapUtils.getString( param, "orderSlct"),"a"));

    }
}