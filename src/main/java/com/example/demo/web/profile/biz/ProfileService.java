package com.example.demo.web.profile.biz;
@Service
public class ProfileService {

    @Autowired
    private ProfileSlave ProfileSlave;

    public Map<String, Object> getProfileOpenLog (HashMap<String, Object> param){
        HashMap<String,Object> result = new HashMap<>();

        String memId = MapUtils.getString(param, "memId");
        if (!StringUtils.isBlank(memId)) {
            Integer memNo = noblesseProfileSlave.getMemberNoById(param);
            if (memNo == null || memNo < 0) {
                result.put("errCode", "1000");
                result.put("errMsg", "회원이 존재하지 않습니다.");
                return result;
            }
            param.put("memNo", memNo);
        }else{
            param.put("memNo", -1);
        }

        List<Object> data = ProfileSlave.getProfileViewList(param);
        ProfileItemCntVO counts = DBUtil.getData(data, ProfileItemCntVO.class);
        List<ProfileVO> listVO= DBUtil.getList(data, ProfileVO.class);
        assert counts != null;
        result.put("total_num", counts.getCnt());
        result.put("total_item_num", counts.getItemMemCnt());
        result.put("list", listVO);

        HashMap<String, Integer>  pfViewStat = ProfileSlave.getPfViewStat(param);

        if(MapUtils.isEmpty(pfViewStat)){
            String[] CNT_TAG= new String[]{"vmfCnt", "vfmCnt", "imfCnt", "ifmCnt", "fmfCnt", "ffmCnt"};
            HashMap<String, Integer> fornull = new HashMap<>();
            for( String tag : CNT_TAG) {fornull.put(tag, 0);}
            result.put("pfViewStat", fornull);
        }else{
            result.put("pfViewStat", pfViewStat);
        }

        return result;
    }
}