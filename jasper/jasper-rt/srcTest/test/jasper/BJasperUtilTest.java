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

import javax.baja.control.*;
import javax.baja.nre.annotations.*;
import javax.baja.registry.*;
import javax.baja.status.*;
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
    BFolder f = new BFolder();
    verifyEq(JasperUtil.isType(f, "baja:Folder"),    true);
    verifyEq(JasperUtil.isType(f, "baja:Component"), true);
    verifyEq(JasperUtil.isType(f, "baja:Complex"),   true);
    verifyEq(JasperUtil.isType(f, "baja:Object"),    true);

    BComponent c = new BComponent();
    verifyEq(JasperUtil.isType(c, "baja:Folder"),    false);
    verifyEq(JasperUtil.isType(c, "baja:Component"), true);
    verifyEq(JasperUtil.isType(c, "baja:Complex"),   true);
    verifyEq(JasperUtil.isType(c, "baja:Object"),    true);
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
// testGetPriorityArray
////////////////////////////////////////////////////////////////

  @Test public void testGetPriorityArray() throws IOException
  {
    // non-points and read-only points have no priority array
    verifyEq(JasperUtil.getPriorityArray(new BComponent()), null);
    verifyEq(JasperUtil.getPriorityArray(new BNumericPoint()), null);
    verifyEq(JasperUtil.getPriorityArray(new BBooleanPoint()), null);

    // writable point with nothing commanded is all nulls
    BNumericWritable nw = new BNumericWritable();
    verifyPriorityArray(nw, new int[] {}, new double[] {});

    // command a few levels
    JasperUtil.setPointValue(nw, Double.valueOf(72.0), 8);
    JasperUtil.setPointValue(nw, Double.valueOf(50.0), 16);
    verifyPriorityArray(nw, new int[] { 8, 16 }, new double[] { 72.0, 50.0 });

    // release a level
    JasperUtil.setPointValue(nw, null, 8);
    verifyPriorityArray(nw, new int[] { 16 }, new double[] { 50.0 });

    // booleans use the same slot layout
    BBooleanWritable bw = new BBooleanWritable();
    JasperUtil.setPointValue(bw, Double.valueOf(1.0), 10);
    BStatusValue[] bpri = JasperUtil.getPriorityArray(bw);
    verifyEq(bpri.length, 16);
    verifyEq(((BStatusBoolean)bpri[9]).getValue(), true);
  }

  /** Verify only 'levels' are set, with matching 'vals'. */
  private void verifyPriorityArray(BComponent c, int[] levels, double[] vals)
  {
    BStatusValue[] pri = JasperUtil.getPriorityArray(c);
    verifyEq(pri.length, 16);

    for (int i=0; i<pri.length; i++)
    {
      // find level in expected list
      int match = -1;
      for (int j=0; j<levels.length; j++)
        if (levels[j] == i+1) match = j;

      if (match < 0) { verifyEq(pri[i], null); }
      else { verifyEq(((BStatusNumeric)pri[i]).getValue(), vals[match]); }
    }
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
