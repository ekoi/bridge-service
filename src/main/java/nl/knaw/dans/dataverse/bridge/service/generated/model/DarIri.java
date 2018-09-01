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
 * DarIri
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-09-01T17:21:41.259+02:00")

public class DarIri   {
  @JsonProperty("dar-name")
  private String darName = null;

  @JsonProperty("iri")
  private String iri = null;

  public DarIri darName(String darName) {
    this.darName = darName;
    return this;
  }

  /**
   * Get darName
   * @return darName
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getDarName() {
    return darName;
  }

  public void setDarName(String darName) {
    this.darName = darName;
  }

  public DarIri iri(String iri) {
    this.iri = iri;
    return this;
  }

  /**
   * Get iri
   * @return iri
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getIri() {
    return iri;
  }

  public void setIri(String iri) {
    this.iri = iri;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DarIri darIri = (DarIri) o;
    return Objects.equals(this.darName, darIri.darName) &&
        Objects.equals(this.iri, darIri.iri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(darName, iri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarIri {\n");
    
    sb.append("    darName: ").append(toIndentedString(darName)).append("\n");
    sb.append("    iri: ").append(toIndentedString(iri)).append("\n");
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

