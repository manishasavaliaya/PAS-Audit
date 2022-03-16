package com.passurvey.model;

/**
 * Created by Ravi on 29/07/15.
 */
public class ProfileModel {
    String ProfileID = "ProfileID";
    String UserID = "UserID";
    String SurveyID= "SurveyID";
    String Fund = "Fund";
    String COManagingAgent = "COManagingAgent";
    String ManagementSurveor = "ManagementSurveor";
    String FacilitiesManager = "FacilitiesManager";
    // String SiteAddress = "SiteAddress";
    String ReportPreparedby = "ReportPreparedby";
    String ReportCheckedby = "ReportCheckedby";
    String AuditDate = "AuditDate";
    String DateofIssue = "DateofIssue";
    String AuditVisit = "AuditVisit";
    String ContractorsPerfomance = "ContractorsPerfomance";
    String StatutoryAuditScore = "StatutoryAuditScore";
//    String ContractorImprovePerfomance = "ContractorImprovePerfomance";
//    String ConsultantsComments = "ConsultantsComments";
//    String ClientsComments = "ClientsComments";
    String ContractorImprovePerfomance = "";
    String ConsultantsComments = "";
    String ClientsComments = "";
    String CreatedDate = "CreatedDate";
    String CreatedTime = "CreatedTime";

    String SiteName = "SiteName";
    String SiteAddress1 = "SiteAddress1";
    String SiteAddress2 = "SiteAddress2";
    String SiteAddress3 = "SiteAddress3";
    String SitePostalCode = "SitePostalCode";

    String StatutoryComments = "StatutoryComments";

    public String getStatutoryComments() {
        return StatutoryComments;
    }

    public void setStatutoryComments(String statutoryComments) {
        StatutoryComments = statutoryComments;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteAddress1() {
        return SiteAddress1;
    }

    public void setSiteAddress1(String siteAddress1) {
        SiteAddress1 = siteAddress1;
    }

    public String getSiteAddress2() {
        return SiteAddress2;
    }

    public void setSiteAddress2(String siteAddress2) {
        SiteAddress2 = siteAddress2;
    }

    public String getSiteAddress3() {
        return SiteAddress3;
    }

    public void setSiteAddress3(String siteAddress3) {
        SiteAddress3 = siteAddress3;
    }

    public String getSitePostalCode() {
        return SitePostalCode;
    }

    public void setSitePostalCode(String sitePostalCode) {
        SitePostalCode = sitePostalCode;
    }

    public ProfileModel() {
    }

    public String getProfileID() {
        return ProfileID;
    }

    public void setProfileID(String profileID) {
        ProfileID = profileID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getSurveyID() {
        return SurveyID;
    }

    public void setSurveyID(String surveyID) {
        SurveyID = surveyID;
    }

    public String getFund() {
        return Fund;
    }

    public void setFund(String fund) {
        Fund = fund;
    }

    public String getCOManagingAgent() {
        return COManagingAgent;
    }

    public void setCOManagingAgent(String COManagingAgent) {
        this.COManagingAgent = COManagingAgent;
    }

    public String getManagementSurveor() {
        return ManagementSurveor;
    }

    public void setManagementSurveor(String managementSurveor) {
        ManagementSurveor = managementSurveor;
    }

    public String getFacilitiesManager() {
        return FacilitiesManager;
    }

    public void setFacilitiesManager(String facilitiesManager) {
        FacilitiesManager = facilitiesManager;
    }

    /*public String getSiteAddress() {
        return SiteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        SiteAddress = siteAddress;
    }*/

    public String getReportPreparedby() {
        return ReportPreparedby;
    }

    public void setReportPreparedby(String reportPreparedby) {
        ReportPreparedby = reportPreparedby;
    }

    public String getReportCheckedby() {
        return ReportCheckedby;
    }

    public void setReportCheckedby(String reportCheckedby) {
        ReportCheckedby = reportCheckedby;
    }

    public String getAuditDate() {
        return AuditDate;
    }

    public void setAuditDate(String auditDate) {
        AuditDate = auditDate;
    }

    public String getDateofIssue() {
        return DateofIssue;
    }

    public void setDateofIssue(String dateofIssue) {
        DateofIssue = dateofIssue;
    }

    public String getAuditVisit() {
        return AuditVisit;
    }

    public void setAuditVisit(String auditVisit) {
        AuditVisit = auditVisit;
    }

    public String getContractorsPerfomance() {
        return ContractorsPerfomance;
    }

    public void setContractorsPerfomance(String contractorsPerfomance) {
        ContractorsPerfomance = contractorsPerfomance;
    }

    public String getStatutoryAuditScore() {
        return StatutoryAuditScore;
    }

    public void setStatutoryAuditScore(String statutoryAuditScore) {
        StatutoryAuditScore = statutoryAuditScore;
    }

    public String getContractorImprovePerfomance() {
        return ContractorImprovePerfomance;
    }

    public void setContractorImprovePerfomance(String contractorImprovePerfomance) {
        ContractorImprovePerfomance = contractorImprovePerfomance;
    }

    public String getConsultantsComments() {
        return ConsultantsComments;
    }

    public void setConsultantsComments(String consultantsComments) {
        ConsultantsComments = consultantsComments;
    }

    public String getClientsComments() {
        return ClientsComments;
    }

    public void setClientsComments(String clientsComments) {
        ClientsComments = clientsComments;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public ProfileModel(String profileID, String userID, String surveyID, String fund, String COManagingAgent, String managementSurveor,
                        String facilitiesManager, String siteName,  String siteAddress1,  String siteAddress2,  String siteAddress3,
                        String sitePostalCode, String reportPreparedby, String reportCheckedby, String auditDate, String dateofIssue,
                        String auditVisit, String contractorsPerfomance, String statutoryAuditScore, String contractorImprovePerfomance,
                        String consultantsComments, String createdDate, String createdTime,String statutoryComments,String clientsComments) {
        ProfileID = profileID;
        UserID = userID;
        SurveyID = surveyID;
        Fund = fund;
        this.COManagingAgent = COManagingAgent;
        ManagementSurveor = managementSurveor;
        FacilitiesManager = facilitiesManager;
        SiteName =siteName;
        SiteAddress1 = siteAddress1;
        SiteAddress2 = siteAddress2;
        SiteAddress3 = siteAddress3;
        SitePostalCode= sitePostalCode;
        ReportPreparedby = reportPreparedby;
        ReportCheckedby = reportCheckedby;
        AuditDate = auditDate;
        DateofIssue = dateofIssue;
        AuditVisit = auditVisit;
        ContractorsPerfomance = contractorsPerfomance;
        StatutoryAuditScore = statutoryAuditScore;
        ContractorImprovePerfomance = contractorImprovePerfomance;
        ConsultantsComments = consultantsComments;
        ClientsComments = clientsComments;
        CreatedDate = createdDate;
        CreatedTime = createdTime;
        StatutoryComments =statutoryComments;
    }
}
