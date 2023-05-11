import java.util.List;

import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import com.ebay.nst.ServiceModelBase;

import java.util.Map;
import java.util.Arrays;
import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.jsonpath.JPStringCheck;
import java.util.HashMap;

public class Test extends ServiceModelBase {

    public Test(JSONObject jsonObject, SoftAssert softAssert) {
        super(jsonObject, softAssert);
    }

    @Override
    protected void validate(SoftAssert softAssert) {
        generatedValidations(softAssert);
    }

    // TMB Generated Validation Method
    private void generatedValidations(SoftAssert softAssert) {
        Map<String, JsonPathExecutor> validations = new HashMap<>();
        validations.put("$.modules.banners._type", new JPStringCheck());
        evaluateJsonPaths(validations, softAssert);
    }
}
