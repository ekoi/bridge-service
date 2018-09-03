package nl.knaw.dans.dataverse.bridge.service.generated.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import nl.knaw.dans.dataverse.bridge.service.generated.model.DarData;
import nl.knaw.dans.dataverse.bridge.service.generated.model.SrcData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * IngestData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-09-02T19:29:22.525+02:00")

public class IngestData   {
  @JsonProperty("srcData")
  private SrcData srcData = null;

  @JsonProperty("darData")
  private DarData darData = null;

  public IngestData srcData(SrcData srcData) {
    this.srcData = srcData;
    return this;
  }

  /**
   * Get srcData
   * @return srcData
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public SrcData getSrcData() {
    return srcData;
  }

  public void setSrcData(SrcData srcData) {
    this.srcData = srcData;
  }

  public IngestData darData(DarData darData) {
    this.darData = darData;
    return this;
  }

  /**
   * Get darData
   * @return darData
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public DarData getDarData() {
    return darData;
  }

  public void setDarData(DarData darData) {
    this.darData = darData;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IngestData ingestData = (IngestData) o;
    return Objects.equals(this.srcData, ingestData.srcData) &&
        Objects.equals(this.darData, ingestData.darData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(srcData, darData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IngestData {\n");
    
    sb.append("    srcData: ").append(toIndentedString(srcData)).append("\n");
    sb.append("    darData: ").append(toIndentedString(darData)).append("\n");
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

