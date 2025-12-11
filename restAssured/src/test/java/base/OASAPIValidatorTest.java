package base;

import org.testng.annotations.Test;
import openAPI.OpenAPISpecification;


import static utils.OASAPIValidator.getValidator;

public class OASAPIValidatorTest {

    @Test(enabled = false)
    public void testGitHubOAS() {
        //getValidator(new OpenAPISpecification("src/test/resources/openAPI/anz-cpb-product-catalog-api.yaml"));
        //Assert.assertEquals();
        System.out.println("As long as this is printed, this test cases passes (no exceptions thrown).");
    }
}
