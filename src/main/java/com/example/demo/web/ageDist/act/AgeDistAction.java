package com.example.demo.web.ageDist.act;

@Controller
@RequiredArgsConstructor
@RequestMapping("/-----/ageDist")
public class AgeDistAction {

    private final AgeDistService ageDistService;

    @RequestMapping("/index")
    public String justPage() { return "/ageDist/index"; }

    @ResponseBody
    @RequestMapping("/data")
    public Map<String, Object> ageDistData(@RequestParam(value = "locCode", required = false) String locCode, Model model) throws Exception{
        Map<String, Object> res = new HashMap<>();

        Map<Integer,Map<String, Object>> localDist = ageDistService.getLocalDist();
        if(localDist!=null){
            res.put("localDist",localDist);
            res.put("error","n");
        }else{
            res.put("error","y");
        }

        String code=(locCode==null)? "" : locCode;
        Map<String, Object> ageDist = ageDistService.getAgeDist(code);
        if(ageDist!=null){
            res.put("mTotal",ageDist.get("mTotal"));
            res.put("fTotal",ageDist.get("fTotal"));
            res.put("mList", ageDist.get("mList"));
            res.put("fList", ageDist.get("fList"));
            res.put("error","n");
        }else{
            res.put("error","y");
        }
        return res;

    }
}