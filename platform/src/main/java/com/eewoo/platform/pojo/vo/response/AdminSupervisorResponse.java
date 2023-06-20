package com.eewoo.platform.pojo.vo.response;


import com.eewoo.common.pojo.Counselor;
import lombok.Data;

import java.util.List;

@Data
public class AdminSupervisorResponse {
    private  Integer supervisorId;
    private String name;
    private String title="督导";
    private List<Counselor> counselors;
    private List<Integer> schedule;
    private Integer banned;
}
