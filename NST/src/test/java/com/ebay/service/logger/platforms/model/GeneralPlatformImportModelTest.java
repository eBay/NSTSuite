package com.ebay.service.logger.platforms.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

public class GeneralPlatformImportModelTest {

  @Test(groups = "unitTest")
  public void emptyImportStatementBlock() {
    GeneralPlatformImportModel model = new GeneralPlatformImportModel();
    String block = model.getImportStatementBlock();
    assertThat(block, is(comparesEqualTo("")));
  }

  @Test(groups = "unitTest")
  public void addImportStatement() {
    GeneralPlatformImportModel model = new GeneralPlatformImportModel();
    model.addImportStatement("import com.ebay.foo");
    String block = model.getImportStatementBlock();
    assertThat(block, is(comparesEqualTo("import com.ebay.foo\n")));
  }

  @Test(groups = "unitTest")
  public void multipleImportStatments() {
    GeneralPlatformImportModel model = new GeneralPlatformImportModel();
    model.addImportStatement("import com.ebay.foo");
    model.addImportStatement("import com.ebay.bar");
    String block = model.getImportStatementBlock();
    assertThat(block, is(comparesEqualTo("import com.ebay.foo\nimport com.ebay.bar\n")));
  }

  @Test(groups = "unitTest")
  public void removeAnImportStatement() {
    GeneralPlatformImportModel model = new GeneralPlatformImportModel();
    model.addImportStatement("import com.ebay.foo");
    model.addImportStatement("import com.ebay.bar");
    model.removeImportStatement("import com.ebay.foo");
    String block = model.getImportStatementBlock();
    assertThat(block, is(comparesEqualTo("import com.ebay.bar\n")));
  }

  @Test(groups = "unitTest")
  public void removeLastImportStatement() {
    GeneralPlatformImportModel model = new GeneralPlatformImportModel();
    model.addImportStatement("import com.ebay.foo");
    model.removeImportStatement("import com.ebay.foo");
    String block = model.getImportStatementBlock();
    assertThat(block, is(comparesEqualTo("")));
  }

  @Test(groups = "unitTest")
  public void removeUnknownImportStatement() {
    GeneralPlatformImportModel model = new GeneralPlatformImportModel();
    model.addImportStatement("import com.ebay.foo");
    model.removeImportStatement("import com.ebay.dne");
    String block = model.getImportStatementBlock();
    assertThat(block, is(comparesEqualTo("import com.ebay.foo\n")));
  }
}
