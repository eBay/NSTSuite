package com.ebay.tool.thinmodelgen.gui.menu.export;

import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import com.ebay.nst.NSTServiceModelBase;

public class TestExportModel extends NSTServiceModelBase {

  public TestExportModel(JSONObject jsonRoot, SoftAssert softAssert) {
    super(jsonRoot, softAssert);
  }

  public String getInstrumentId() {
    return readJsonPath("$.instrument_id");
  }

  @Override
  protected void validate(SoftAssert softAssert) {

  }

}
