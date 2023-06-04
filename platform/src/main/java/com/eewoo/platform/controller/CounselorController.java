package com.eewoo.platform.controller;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Session;
import com.eewoo.common.pojo.User;
import com.eewoo.common.util.R;
import com.eewoo.platform.pojo.vo.request.c_Evaluation;
import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.service.CounselorService;
import com.eewoo.platform.utils.ZipUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("hasAuthority('c')")
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    CounselorService counselorService;
//    @PostMapping("/chat/counselor-comment")
//    public R evaluateOnSession(@RequestBody c_Evaluation eva)
//    {
//        counselorService.createEvaluation(eva.getSessionId(), eva.getCounselorFeedback(),eva.getType());
//        return R.ok();
//    }

    /**
     * 获取除密码外的所有字段
     * @return
     */
    @GetMapping("/counselor-info")
    public R getCounselorInfo()
    {
        Counselor this_conselor = counselorService.getAllInfoExceptPassword();

        if(this_conselor != null)
            return R.ok(this_conselor);
        else
            return R.err("500","Error");
    }

    /**
     * 查询咨询记录列表，涉及到分页操作，前端传来page和size
     * @return
     */
    @GetMapping("/consult-list")
    public R getConsultList(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "20") Integer size)
    {
        List<Consult> list = counselorService.getConsult(page, size);
        if(list!= null)
        {
            if(list.size() >0)
                return R.ok(list);
            else //没有大小说明没有人
                return R.err("200","暂无已经咨询的用户");
        }
        else//为空说明出错了
            return R.err("code =500", "error");
    }

    /**
     * 获得总的咨询时长
     * @return
     */
    @GetMapping("/total-consult")
    public R getTotalConsultTime()
    {

        Integer time = counselorService.getTotalSessionTime();
        if(time != null)
            return R.ok(time);
        else
            return R.err("code =500", "error");
    }

    /**
     * 获得今日咨询数量
     * @return
     */
    @GetMapping("/today-consults")
    public R getTodayConsultsNum() {
        Integer time = counselorService.getTodaySessionNum();
        if (time != null)
            return R.ok(time);
        else
            return R.err("code =500", "error");
    }

    /**
     * 获得今日咨询总时长
     * @return
     */
    @GetMapping("/today-total-time")
    public R getTodayConsultsTime() {
        Integer time = counselorService.getTodaySessionTime();
        if (time != null)
            return R.ok(time);
        else
            return R.err("code =500", "error");
    }

    /**
     * 当前会话总数功能在chat那里，需要提供service接口。
     */




    /***
     *批量导出记录，本接口涉及到文件传输，需要给浏览器自动下载打包好的咨询记录列表
     *既然是给咨询师使用的功能，还要判断ID是否都是来自属于本咨询师的会话账号的。
     * 应该还需要chat那边给具体的聊天记录，毕竟我这边只有对话的小记录
     */
    @PostMapping("/export-consults")
    public R exportChat(@RequestParam("exportIDs[]") List<Integer> exportIDs) throws IOException {
        List<Session> sessions = new ArrayList<>();
        Integer cnt = exportIDs.size();
        //维护一个指针，看看是不是全部不是本咨询师的会话记录。
        for(Integer item : exportIDs)//需要判断前端来的ID是不是真的是本咨询师的会话记录。一个一个查验，最后再合并，看会不会null
        {
            Session session  = counselorService.fetchSessionIfAuthenticated(item);
            if(session != null)
            {
                sessions.add(session);
                cnt--;
            }
            else
                continue;
        }
        if(cnt != 0)
            return R.err("400","输入了非法的会话ID");
        //如何提供下载功能？
        //先进行对象的序列化
        try
        {
            String filePath ="src/main/resources/zipFile/file.txt";
            //记得把文件保存在整个项目相对位置
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(filePath)));
            Session[] obj = new Session[sessions.size()];
            sessions.toArray(obj);
            oos.writeObject(obj);
            //进行文件的压缩

            String filePath2 = "src/main/resources/zipFile/file.txt";
            FileOutputStream out = new FileOutputStream(new File(filePath2));
            ZipUtils.toZip(filePath, out,true);
            out.close();

            //进行资源的打包。
            try
            {
                Resource resource  = (Resource) new UrlResource(filePath2);
                if(resource != null)
                    return R.ok(resource);
                //真正的返回下载文件在这里
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        //在找到文件路径，下载

        return R.ok(sessions);
        //实在没有办法就在这里返回，反正也返回了。
    }




}
/**
 * 这两个任务在CounselorServiceImpl供Chat服务模块调用，不在本Controller做路由
 * 1.咨询师填写会话评价内容
 * 2.查找督导
 */