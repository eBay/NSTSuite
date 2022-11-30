import java.util.List;

import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import com.ebay.nst.ServiceModelBase;

public class Test extends ServiceModelBase {

  public Test(JSONObject jsonRoot, SoftAssert softAssert) {
    super(jsonRoot, softAssert);
  }

  @Override
  protected void validate(SoftAssert softAssert) {
  }
}