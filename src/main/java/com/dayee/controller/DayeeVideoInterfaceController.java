package com.dayee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.dayee.exception.CreditsException;
import com.dayee.exception.FileSizeException;
import com.dayee.model.Company;
import com.dayee.model.CreditsLog;
import com.dayee.model.DayeeVideoConsumptionLog;
import com.dayee.model.PaidAccount;
import com.dayee.service.CompanySystemService;
import com.dayee.service.DayeeVideoService;
import com.dayee.service.PaidAccountService;
import com.dayee.utils.NumberUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.CorpInfo;
import com.dayee.vo.DurationSummary;


@Controller
@RequestMapping("/dayee/video/api")
public class DayeeVideoInterfaceController extends BaseController{

    @Resource
    private CompanySystemService companySystemService;
    @Resource
    private PaidAccountService paidAccountService;
    @Resource
    private DayeeVideoService dyeeVideoService;
    
    
    /***
     * 信用额度扣除
     * @return
     * @throws Exception 
     */
    @RequestMapping("deductingCredit")
    @ResponseBody
    public HashMap<String,Object> deductingCredits(String dataJson) throws Exception{

        log.info("dataJson============>" + dataJson);
        JSONArray dayeeVideInterFaceJsonVo = JSONArray.parseArray(dataJson);
        List<HashMap<String, String>> errorList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < dayeeVideInterFaceJsonVo.size(); i++) {
            Integer creditLimitSeconds = dayeeVideInterFaceJsonVo.getJSONObject(i).getInteger("creditLimitSeconds");
            String externalKey = dayeeVideInterFaceJsonVo.getJSONObject(i).getString("externalKey");
            String accountId = dayeeVideInterFaceJsonVo.getJSONObject(i).getString("accountId");
            Integer paidAccountId = dayeeVideInterFaceJsonVo.getJSONObject(i).getInteger("paidAccountId");
            try {
                companySystemService.deductingCredits(companySystem, creditLimitSeconds, paidAccountId,
                                                      CreditsLog.TYPE_DEDUCTION);
            } catch (CreditsException e) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("externalKey", externalKey);
                map.put("accountId", accountId);
                map.put("error", e.getCode());
                map.put("msg", e.getMessage());
                errorList.add(map);
            }
        }
        if (errorList.size() == 0) {
            return returnSuccessResult();
        } else {
            HashMap<String, Object> map = returnResult("-1", "");
            map.put("errorList", errorList);
            return map;
        }
    }
    
    /***
     * 信用额度恢复
     * @return
     * @throws Exception 
     */
    @RequestMapping("restoreCredit")
    @ResponseBody
    public HashMap<String,Object> restoreCredits(String dataJson) throws Exception{

        log.info("dataJson============>" + dataJson);
        JSONArray dayeeVideInterFaceJsonVo = JSONArray.parseArray(dataJson);
        List<HashMap<String, String>> errorList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < dayeeVideInterFaceJsonVo.size(); i++) {
            Integer creditLimitSeconds = dayeeVideInterFaceJsonVo.getJSONObject(i).getInteger("creditLimitSeconds");
            String externalKey = dayeeVideInterFaceJsonVo.getJSONObject(i).getString("externalKey");
            String accountId = dayeeVideInterFaceJsonVo.getJSONObject(i).getString("accountId");
            Integer paidAccountId = dayeeVideInterFaceJsonVo.getJSONObject(i).getInteger("paidAccountId");
            try {
                companySystemService.deductingCredits(companySystem, creditLimitSeconds, paidAccountId,
                                                      CreditsLog.TYPE_RESTORE);
            } catch (CreditsException e) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("externalKey", externalKey);
                map.put("accountId", accountId);
                map.put("error", e.getCode());
                map.put("msg", e.getMessage());
                errorList.add(map);
            }
        }
        if (errorList.size() == 0) {
            return returnSuccessResult();
        } else {
            HashMap<String, Object> map = returnResult("-1", "");
            map.put("errorList", errorList);
            return map;
        }
    }
    
    /***
     * 空间大小进行恢复
     * @return
     * @throws Exception 
     */
    @RequestMapping("restoreFileSize")
    @ResponseBody
    public HashMap<String,Object> restoreFileSize(String dataJson) throws Exception{

        log.info("dataJson============>" + dataJson);
        JSONArray dayeeVideInterFaceJsonVo = JSONArray.parseArray(dataJson);
        List<HashMap<String, String>> errorList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < dayeeVideInterFaceJsonVo.size(); i++) {
            String externalKey = dayeeVideInterFaceJsonVo.getJSONObject(i).getString("externalKey");
            ;
            String accountId = dayeeVideInterFaceJsonVo.getJSONObject(i).getString("accountId");
            ;
            Integer paidAccountId = dayeeVideInterFaceJsonVo.getJSONObject(i).getInteger("paidAccountId");
            try {
                companySystemService.restoreFileSize(companySystem, externalKey, paidAccountId,
                                                     CreditsLog.TYPE_RESTORE);
            } catch (FileSizeException e) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("externalKey", externalKey);
                map.put("accountId", accountId);
                map.put("error", e.getCode());
                map.put("msg", e.getMessage());
                errorList.add(map);
            }
        }
        if (errorList.size() == 0) {
            return returnSuccessResult();
        } else {
            HashMap<String, Object> map = returnResult("-1", "");
            map.put("errorList", errorList);
            return map;
        }

    }
    
    
    @RequestMapping("recordingVideoWater")
    @ResponseBody
    public HashMap<String,Object> recordingVideoWater(String dataJson) throws Exception{
            log.info("dataJson============>"+dataJson);
            DayeeVideoConsumptionLog  dayeeVideoConsumptionLog = JSONArray.parseObject(dataJson,DayeeVideoConsumptionLog.class);
            companySystemService.recordingVideoWater(companySystem, dayeeVideoConsumptionLog);
            return returnSuccessResult();
    }
    
    @RequestMapping("paidAccountList")
    @ResponseBody
    public HashMap<String,Object> getPaidAccountList() throws Exception{
            List<PaidAccount>  list = paidAccountService.query(QueryUtil.createQuery()
                                                               .eq("companySystemId",companySystem.getId())
                                                               .eq("state","有效"));
            HashMap<String,Object> map = returnSuccessResult();
            map.put("data", list);
            return map;

    }
    
    @RequestMapping("durationSummary")
    @ResponseBody
    public HashMap<String,Object> getDurationSummary(){
            DurationSummary  durationSummary  = paidAccountService.queryDurationSummary(companySystem);
            HashMap<String,Object> map = returnSuccessResult();
            double fileSize =  NumberUtil.division(companySystem.getFileSize(),1024*1024);
            double useFileSize =  NumberUtil.division(companySystem.getUseFileSize(),1024*1024);
            durationSummary.setFileSize(fileSize);
            durationSummary.setUseFileSize(useFileSize);
            durationSummary.setRechargeMoney(companySystem.getRechargeMoney());
            map.put("durationSummary", durationSummary);
            return map;
    }
    
    @RequestMapping("corpSynchronize")
    @ResponseBody
    public HashMap<String,Object> corpSynchronize( String corpInfo) throws Exception{
            log.info("corpInfo============>"+corpInfo);
            List<CorpInfo> corpInfoList = JSONArray.parseArray(corpInfo, CorpInfo.class);
            HashMap<String, Object>  map = returnSuccessResult();
            map.put("corpInfoList", corpInfoList);
            dyeeVideoService.corpSynchronize(corpInfoList);
            return map;
    }
    
    @RequestMapping("getOpenVideoCompany")
    @ResponseBody
    public HashMap<String,Object> getOpenVideoCompany(){
            List<Company> company = dyeeVideoService.getOpenVideoCompany();
            HashMap<String, Object>  map = returnSuccessResult();
            map.put("companyList", company);
            return map;
    }
}