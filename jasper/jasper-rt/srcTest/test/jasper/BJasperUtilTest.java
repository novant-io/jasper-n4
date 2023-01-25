//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   20 Jan 2023  Andy Frank  Creation
//

package test.jasper;

import java.io.*;
import java.util.*;

import javax.baja.nre.annotations.*;
import javax.baja.sys.*;
import javax.baja.test.BTestNg;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jasper.util.*;

/** BJasperUtilTest */
@NiagaraType
public class BJasperUtilTest extends BTestNg
{

/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////

@Override
public Type getType() { return TYPE; }
public static final Type TYPE = Sys.loadType(BJasperUtilTest.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  @BeforeMethod public void beforeMethod() {}
  @AfterMethod public void afterMethod() {}

////////////////////////////////////////////////////////////////
// testParseEnumRange
////////////////////////////////////////////////////////////////

  @Test public void testParseEnumRange() throws IOException
  {
    verifyEq(JasperUtil.parseEnumRange(null), null);
    verifyEq(JasperUtil.parseEnumRange(""), null);
    verifyEq(JasperUtil.parseEnumRange("{}"), null);
    verifyEq(JasperUtil.parseEnumRange(" {  } "), null);
    verifyEq(JasperUtil.parseEnumRange("{alpha=0,beta=1,gamma=2}"), "alpha,beta,gamma");
    verifyEq(JasperUtil.parseEnumRange(" { alpha = 0 ,   beta=1, gamma =  2 }"), "alpha,beta,gamma");
  }

////////////////////////////////////////////////////////////////
// testSplitPath
////////////////////////////////////////////////////////////////

  @Test public void testSplitPath() throws IOException
  {
    verifyPath("/", new String[] {});
    verifyPath("/foo", new String[] { "foo" });
    verifyPath("/alpha/beta/gamma", new String[] { "alpha", "beta", "gamma" });
  }

  private void verifyPath(String path, String[] acc)
  {
    String[] test = JasperUtil.splitPath(path);
    verifyEq(test.length, acc.length);
    for (int i=0; i<test.length; i++)
      verifyEq(test[i], acc[i]);
  }
}
