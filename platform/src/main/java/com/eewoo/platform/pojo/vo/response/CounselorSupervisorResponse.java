package com.eewoo.platform.pojo.vo.response;


import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import lombok.Data;

@Data
public class CounselorSupervisorResponse {

    Integer counselorId;
    String counselorUsername;
    Integer counselorBanned;
    String counselorName;
    String counselorProfile;
    Integer counselorConsultDurationTotal;
    Integer counselorConsultCntTotal;
    Integer counselorConsultScoreTotal;
    Integer counselorConsultCntToday;
    Integer counselorConsultDurationToday;
    Integer counselorAge;
    String counselorIdCard;
    String counselorPhone;
    String counselorEmail;
    String counselorWorkPlace;
    String counselorTitle;

    Integer supervisorId;
    String supervisorUsername;
    Integer supervisorBanned;
    String supervisorName;
    String supervisorProfile;
    Integer supervisorConsultDurationTotal;
    Integer supervisorConsultCntToday;
    Integer supervisorConsultDurationToday;
    Integer supervisorAge;
    String supervisorIdCard;
    String supervisorPhone;
    String supervisorEmail;
    String supervisorWorkPlace;
    String supervisorTitle;
    String supervisorQualification;
    String supervisorQualificationNumber;



}
