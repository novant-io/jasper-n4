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
import javax.baja.registry.*;
import javax.baja.sys.*;
import javax.baja.test.BTestNg;
import javax.baja.util.*;

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
// testIsType
////////////////////////////////////////////////////////////////

  @Test public void testIsType() throws IOException
  {
    Registry reg = Sys.getRegistry();
    TypeInfo typeObj      = reg.getType("baja:Object");
    TypeInfo typeComplex  = reg.getType("baja:Complex");
    TypeInfo typeComp     = reg.getType("baja:Component");
    TypeInfo typeFolder   = reg.getType("baja:Folder");

    BFolder f = new BFolder();
    verifyEq(JasperUtil.isType(f, typeFolder),  true);
    verifyEq(JasperUtil.isType(f, typeComp),    true);
    verifyEq(JasperUtil.isType(f, typeComplex), true);
    verifyEq(JasperUtil.isType(f, typeObj),     true);

    BComponent c = new BComponent();
    verifyEq(JasperUtil.isType(c, typeFolder),  false);
    verifyEq(JasperUtil.isType(c, typeComp),    true);
    verifyEq(JasperUtil.isType(c, typeComplex), true);
    verifyEq(JasperUtil.isType(c, typeObj),     true);
  }

////////////////////////////////////////////////////////////////
// testUnescapeSlotPath
////////////////////////////////////////////////////////////////

  @Test public void testUnescapeSlotPath() throws IOException
  {
    verifyEq(JasperUtil.unescapeSlotPath(""), "");
    verifyEq(JasperUtil.unescapeSlotPath("/Foo"), "/Foo");
    verifyEq(JasperUtil.unescapeSlotPath("/Foo/Bar"), "/Foo/Bar");

    verifyEq(JasperUtil.unescapeSlotPath("/Foo$205"),   "/Foo 5");
    verifyEq(JasperUtil.unescapeSlotPath("/Foo$2dBar"), "/Foo-Bar");
    verifyEq(JasperUtil.unescapeSlotPath("/Foo$24Bar"), "/Foo$Bar");
    verifyEq(JasperUtil.unescapeSlotPath("/Foo$2fBar"), "/Foo/Bar");

    verifyEq(JasperUtil.unescapeSlotPath("/Foo$205$2dBar$2dCar"),"/Foo 5-Bar-Car");

    // these cases should never happen, but just in case cover
    // when not enough chars to decode
    verifyEq(JasperUtil.unescapeSlotPath("/Foo$"),  "/Foo$");
    verifyEq(JasperUtil.unescapeSlotPath("/Foo$2"), "/Foo$2");
  }

////////////////////////////////////////////////////////////////
// testSlotPathToSuffix
////////////////////////////////////////////////////////////////

  @Test public void testSlotPathToSuffix() throws IOException
  {
    verifyEq(JasperUtil.slotPathToSuffix(""), "");
    verifyEq(JasperUtil.slotPathToSuffix("Foo"), "Foo");
    verifyEq(JasperUtil.slotPathToSuffix("Foo/Bar"), "Foo.Bar");
    verifyEq(JasperUtil.slotPathToSuffix("Foo/Bar/Car"), "Foo.Bar.Car");

    verifyEq(JasperUtil.slotPathToSuffix("Foo$205"),   "Foo5");
    verifyEq(JasperUtil.slotPathToSuffix("Foo$2dBar"), "FooBar");
    verifyEq(JasperUtil.slotPathToSuffix("Foo$24Bar"), "FooBar");
    verifyEq(JasperUtil.slotPathToSuffix("Foo$2fBar"), "FooBar");

    verifyEq(JasperUtil.slotPathToSuffix("Foo$205$2dBar$2dCar"),"Foo5BarCar");

    // these cases should never happen, but just in case cover
    // when not enough chars to decode
    verifyEq(JasperUtil.slotPathToSuffix("Foo$"),  "Foo");
    verifyEq(JasperUtil.slotPathToSuffix("Foo$2"), "Foo");
  }

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
