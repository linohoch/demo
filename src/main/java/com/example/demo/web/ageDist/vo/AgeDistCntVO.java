package com.example.demo.web.ageDist.vo;

@Data
public class AgeDistCntVO {

    private String memSex = "";
    private int memAge;
    private int cnt=0;
    private int matchCnt=0;

    //리턴1
    public AgeDistCntVO(String memSex, int cnt){
        this.memSex=memSex;
        this.cnt=cnt;
    }
    //리턴2
    public AgeDistCntVO(String memSex, int memAge, int cnt){
        this.memSex=memSex;
        this.memAge=memAge;
        this.cnt=cnt;
    }
    //리스트준비
    public AgeDistCntVO(int memAge){
        this.memAge=memAge;
    }
}
