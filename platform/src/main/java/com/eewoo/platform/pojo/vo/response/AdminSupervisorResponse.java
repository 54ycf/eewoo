package com.eewoo.platform.pojo.vo.response;


import lombok.Data;

import java.util.List;

@Data
public class AdminSupervisorResponse {
    private String name;
    private String title="督导";
    private List<String> counselors;
    private List<Integer> schedule;
}
