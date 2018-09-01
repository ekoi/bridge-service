package nl.knaw.dans.dataverse.bridge.service.generated.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * XsltSource
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-09-01T17:21:41.259+02:00")

public class XsltSource   {
  @JsonProperty("xsl-name")
  private String xslName = null;

  @JsonProperty("xsl-url")
  private String xslUrl = null;

  public XsltSource xslName(String xslName) {
    this.xslName = xslName;
    return this;
  }

  /**
   * Get xslName
   * @return xslName
  **/
  @ApiModelProperty(value = "")


  public String getXslName() {
    return xslName;
  }

  public void setXslName(String xslName) {
    this.xslName = xslName;
  }

  public XsltSource xslUrl(String xslUrl) {
    this.xslUrl = xslUrl;
    return this;
  }

  /**
   * Get xslUrl
   * @return xslUrl
  **/
  @ApiModelProperty(value = "")


  public String getXslUrl() {
    return xslUrl;
  }

  public void setXslUrl(String xslUrl) {
    this.xslUrl = xslUrl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XsltSource xsltSource = (XsltSource) o;
    return Objects.equals(this.xslName, xsltSource.xslName) &&
        Objects.equals(this.xslUrl, xsltSource.xslUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(xslName, xslUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XsltSource {\n");
    
    sb.append("    xslName: ").append(toIndentedString(xslName)).append("\n");
    sb.append("    xslUrl: ").append(toIndentedString(xslUrl)).append("\n");
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

