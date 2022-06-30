package com.example.demo.web.ageDist.biz;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgeDistService {
    private final ProfileSlave ProfileSlave;

    @Autowired
    LocCodeComponent locCodeComponent;

    private final String[] cityOrder
            = {------------};
    public Map<String, Object> getLocMap(List<CodeObject.Code> listFromCodeCompo){
        return listFromCodeCompo.stream()
                .filter(l-> Arrays.asList(cityOrder).contains(l.getCode()))
                .collect(Collectors.toMap(CodeObject.Code::getCode,CodeObject.Code::getCodeName));
    }
    public Integer getIndex(String code){
        return Arrays.asList(cityOrder).indexOf(code);
    }




    public Map<Integer,Map<String, Object>> getLocalDist() {
        Map<Integer,Map<String, Object>> result= new HashMap<>();

        //지역별회원수
        List<Map<String, Object>> localDist = ------ProfileSlave.getLocalDistCnt();
        if (localDist == null || localDist.isEmpty()) { return null; }
        try {
            localDist.forEach(local -> {
                result.put(getIndex(local.get("mem_loc").toString()), local);
            });
            getLocMap(locCodeComponent.getListByCode()).forEach((code,locName)->{
                Map<String, Object> vo = new HashMap<>();
                vo.put("cnt", 0);
                vo.put("mem_loc", code);
                vo.put("mem_loc_name", locName);
                result.putIfAbsent(getIndex(code), vo);
            });
        }catch(Exception e){
            log.error("AgeDistService localDist -> {}",e.toString());
        }
        return result;
    }

    public HashMap<String, Object> getAgeDist(String code) throws Exception{
        HashMap<String,Object> result = new HashMap<>();

        List<Object> list= ProfileSlave.getAgeDistCnt(code);
        if(list==null || list.isEmpty()){return null;} //
        //남녀총프로필
        List<AgeDistCntVO> mfTotal = DBUtil.getList(list, AgeDistCntVO.class, 0);
        if (mfTotal.size() < 2) {               //빈데이터준비
            result.put("mTotal", 0);
            result.put("fTotal", 0);
        }
        try {
            if (!mfTotal.isEmpty()) {                 //DB데이터집계
                mfTotal.forEach(vo -> {
                    String tag = vo.getMemSex().equals("m") ? "mTotal" : "fTotal";
                    result.put(tag, vo.getCnt());
                });
            }
        }catch(Exception e){
            log.error("AgeDistService mfTotal -> {}",e.toString());
        }
        //나이분포
        List<AgeDistCntVO> mfList = DBUtil.getList(list, AgeDistCntVO.class, 1);

        List<AgeDistCntVO> mList = new ArrayList<>();
        List<AgeDistCntVO> fList = new ArrayList<>();

        for (int age = 20; age <= 80; age++) {  //빈데이터준비
            mList.add(new AgeDistCntVO(age));
            fList.add(new AgeDistCntVO(age));
        }
        try {
            if (!mfList.isEmpty()) {                  //DB데이터집계
                mfList.forEach(vo -> {
                    if (vo.getMemSex().equals("m")) {
                        mList.set(vo.getMemAge() - 20, vo);
                    } else {
                        fList.set(vo.getMemAge() - 20, vo);
                    }
                });
                //매칭가능남성집계
                mList.forEach(m -> {
                    int regCnt = m.getCnt();
                    int minAge = m.getMemAge() - 15;
                    int maxAge = m.getMemAge() + 5;
                    fList.stream()
                            .filter(fVO -> fVO.getMemAge() >= minAge && fVO.getMemAge() <= maxAge)
                            .forEach(fVO -> fVO.setMatchCnt(fVO.getMatchCnt() + regCnt));
                });
                //매칭가능여성집계
                fList.forEach(f -> {
                    int regCnt = f.getCnt();
                    int minAge = f.getMemAge() - 5;
                    int maxAge = f.getMemAge() + 15;
                    mList.stream()
                            .filter(mVO -> mVO.getMemAge() >= minAge && mVO.getMemAge() <= maxAge)
                            .forEach(mVO -> mVO.setMatchCnt(mVO.getMatchCnt() + regCnt));
                });
            }
        } catch (Exception e){
            log.error("AgeDistService mfList -> {}",e.toString());
        }
        result.put("mList", mList);
        result.put("fList", fList);

        return result;
    }
}