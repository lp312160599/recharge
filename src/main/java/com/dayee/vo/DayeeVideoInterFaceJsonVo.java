package com.dayee.vo;

import java.util.List;



public class DayeeVideoInterFaceJsonVo {
    
    private List<CreditsArray> creditsArray;

    public static class CreditsArray{
        private Integer earlyWarningSeconds;
        
        private String externalKey;
        
        public String getExternalKey() {
            
            return externalKey;
        }
        
        public void setExternalKey(String externalKey) {
            
            this.externalKey = externalKey;
        }

        
        public Integer getEarlyWarningSeconds() {
        
            return earlyWarningSeconds;
        }

        
        public void setEarlyWarningSeconds(Integer earlyWarningSeconds) {
        
            this.earlyWarningSeconds = earlyWarningSeconds;
        }
        
    }
    
    public List<CreditsArray> getCreditsArray() {
    
        return creditsArray;
    }

    
    public void setCreditsArray(List<CreditsArray> creditsArray) {
    
        this.creditsArray = creditsArray;
    }
}
