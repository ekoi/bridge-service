package nl.knaw.dans.dataverse.bridge.service.db.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/*
    @author Eko Indarto
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"srcMetadataXml", "srcMetadataVersion", "targetIri"}))
public class ArchivingAuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String srcMetadataXml;

  //@NotNull
  private String srcAppName;

  @NotNull
  private String srcMetadataVersion;

  @NotNull
  private String targetIri;

  @NotNull
  private String darName;

  private String pid;

  @NotNull
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date startTime;

  @Temporal(value = TemporalType.TIMESTAMP)
  private Date endTime;

  private String landingPage;

  @NotNull
  private String state;

  private String bagitDir;
  @Column(columnDefinition="TEXT")
  private String log;


  public Long getId() {
    return id;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getLandingPage() {
    return landingPage;
  }

  public void setLandingPage(String landingPage) {
    this.landingPage = landingPage;
  }

  public String getSrcMetadataXml() {
    return srcMetadataXml;
  }

  public void setSrcMetadataXml(String srcMetadataXml) {
    this.srcMetadataXml = srcMetadataXml;
  }

  public String getSrcAppName() {
    return srcAppName;
  }

  public void setSrcAppName(String srcAppName) {
    this.srcAppName = srcAppName;
  }

  public String getSrcMetadataVersion() {
    return srcMetadataVersion;
  }

  public void setSrcMetadataVersion(String srcMetadataVersion) {
    this.srcMetadataVersion = srcMetadataVersion;
  }

  public String getTargetIri() {
    return targetIri;
  }

  public void setTargetIri(String targetIri) {
    this.targetIri = targetIri;
  }

  public String getDarName() {
    return darName;
  }

  public void setDarName(String darName) {
    this.darName = darName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getBagitDir() {
    return bagitDir;
  }

  public void setBagitDir(String bagitDir) {
    this.bagitDir = bagitDir;
  }

  public String getLog() {
    return log;
  }

  public void setLog(String log) {
    this.log = log;
  }
}

