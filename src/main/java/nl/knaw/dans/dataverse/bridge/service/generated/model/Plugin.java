package nl.knaw.dans.dataverse.bridge.service.generated.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import nl.knaw.dans.dataverse.bridge.service.generated.model.XsltSource;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Plugin
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-09-01T17:21:41.259+02:00")

public class Plugin   {
  @JsonProperty("dar-name")
  private String darName = null;

  @JsonProperty("action-class-name")
  private String actionClassName = null;

  @JsonProperty("xsl")
  @Valid
  private List<XsltSource> xsl = null;

  public Plugin darName(String darName) {
    this.darName = darName;
    return this;
  }

  /**
   * Get darName
   * @return darName
  **/
  @ApiModelProperty(value = "")


  public String getDarName() {
    return darName;
  }

  public void setDarName(String darName) {
    this.darName = darName;
  }

  public Plugin actionClassName(String actionClassName) {
    this.actionClassName = actionClassName;
    return this;
  }

  /**
   * Get actionClassName
   * @return actionClassName
  **/
  @ApiModelProperty(value = "")


  public String getActionClassName() {
    return actionClassName;
  }

  public void setActionClassName(String actionClassName) {
    this.actionClassName = actionClassName;
  }

  public Plugin xsl(List<XsltSource> xsl) {
    this.xsl = xsl;
    return this;
  }

  public Plugin addXslItem(XsltSource xslItem) {
    if (this.xsl == null) {
      this.xsl = new ArrayList<>();
    }
    this.xsl.add(xslItem);
    return this;
  }

  /**
   * Get xsl
   * @return xsl
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<XsltSource> getXsl() {
    return xsl;
  }

  public void setXsl(List<XsltSource> xsl) {
    this.xsl = xsl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Plugin plugin = (Plugin) o;
    return Objects.equals(this.darName, plugin.darName) &&
        Objects.equals(this.actionClassName, plugin.actionClassName) &&
        Objects.equals(this.xsl, plugin.xsl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(darName, actionClassName, xsl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Plugin {\n");
    
    sb.append("    darName: ").append(toIndentedString(darName)).append("\n");
    sb.append("    actionClassName: ").append(toIndentedString(actionClassName)).append("\n");
    sb.append("    xsl: ").append(toIndentedString(xsl)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

