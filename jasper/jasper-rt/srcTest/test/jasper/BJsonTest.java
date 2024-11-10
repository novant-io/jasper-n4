//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
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

/** BJsonTest */
@NiagaraType
public class BJsonTest extends BTestNg
{

/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////

@Override
public Type getType() { return TYPE; }
public static final Type TYPE = Sys.loadType(BJsonTest.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  @BeforeMethod public void beforeMethod() {}
  @AfterMethod public void afterMethod() {}

  @Test public void testBool() throws IOException
  {
    verifyEq(read("true"),  Boolean.TRUE);
    verifyEq(read("false"), Boolean.FALSE);
  }

  @Test public void testNum() throws IOException
  {
    // test read
    verifyEq(read("5"),        new Double(5));
    verifyEq(read("-12"),      new Double(-12));
    verifyEq(read("10572"),    new Double(10572));
    verifyEq(read("18.4"),     new Double(18.4));
    verifyEq(read("-0.123"),   new Double(-0.123));
    verifyEq(read("2.351E8"),  new Double(2.351E8));
    verifyEq(read("6.195E-4"), new Double(6.195E-4));

    // test write
    verifyEq(write(5),      "5");
    verifyEq(write(-12),    "-12");
    verifyEq(write(5d),     "5.0");
    verifyEq(write(-12d),   "-12.0");
    verifyEq(write(1.345d), "1.345");
    verifyEq(write(Double.NaN), "\"na\"");
    verifyEq(write(Double.POSITIVE_INFINITY), "\"na\"");
    verifyEq(write(Double.NEGATIVE_INFINITY), "\"na\"");
  }

  @Test public void testStr() throws IOException
  {
    verifyEq(read("\"foo\""), "foo");
    verifyEq(read("\"Unicode \\u00b0F rocks\""), "Unicode °F rocks");
    verifyEq(read("\"this has  some   spaces\""), "this has  some   spaces");
    // TODO: is this test right???
    verifyEq(read("\"this has a \\\" quote\""),   "this has a \\\" quote");
  }

  @Test public void testList() throws IOException
  {
    verifyEq(read("[]"), new ArrayList());
    // verifyEq(read("[ ]"), new ArrayList());

    ArrayList a = new ArrayList();
    a.add(true);
    a.add(new Double(15));
    a.add("foo");
    verifyEq(read("[true,15,\"foo\"]"), a);
    verifyEq(read("[ true,  15, \"foo\"  ]"), a);
  }

  @Test public void testListNested() throws IOException
  {
    ArrayList a1 = new ArrayList();
    a1.add(false);
    a1.add(new Double(-32.5));
    a1.add("bar");

    ArrayList a = new ArrayList();
    a.add(true);
    a.add(new Double(15));
    a.add("foo");
    a.add(a1);
    verifyEq(read("[true, 15, \"foo\"," +
      "  [false, -32.5, \"bar\"]" +
      "]"), a);
  }

  @Test public void testMap() throws IOException
  {
    verifyEq(read("{}"), new HashMap());
    // verifyEq(read("{ }"), new HashMap());

    HashMap m = new HashMap();
    m.put("b", Boolean.TRUE);
    verifyEq(read("{\"b\":true}"), m);
    verifyEq(read("{ \"b\" : true }"), m);

    m.put("n", new Double(15));
    verifyEq(read("{\"b\":true,\"n\":15}"), m);
    verifyEq(read("{\"b\": true, \"n\": 15 }"), m);

    m.put("s", "foo");
    verifyEq(read("{\"b\":true,\"n\":15,\"s\":\"foo\"}"), m);
    verifyEq(read("{ \"b\":true, \"n\":15, \"s\":\"foo\" }"), m);
  }

  @Test public void testMapNested() throws IOException
  {
    HashMap m1 = new HashMap();
    m1.put("b", Boolean.FALSE);
    m1.put("n", new Double(-32.5));
    m1.put("s", "bar");

    HashMap m = new HashMap();
    m.put("b", Boolean.TRUE);
    m.put("n", new Double(15));
    m.put("s", "foo");
    m.put("m", m1);

    verifyEq(read("{ \"b\":true, \"n\":15, \"s\":\"foo\", \"m\":" +
                  "  {\"b\":false,\"n\":-32.5,\"s\":\"bar\" }" +
                  "}"), m);
  }

  @Test public void testPoints() throws IOException
  {
    String json =
    "{" +
    "\"size\": 1," +
    "\"sources\": [" +
    "  {" +
    "    \"name\": \"Meter A\"," +
    "    \"size\": 3," +
    "    \"points\": [" +
    "      { \"id\":\"p1\", \"name\": \"AI-1\", \"kind\":\"num\", \"writable\": false, \"unit\":\"\\u00b0F\" }," +
    "      { \"id\":\"p2\", \"name\": \"AI-2\", \"kind\":\"num\", \"writable\": false, \"unit\":\"kW\" }," +
    "      { \"id\":\"p3\", \"name\": \"DO-1\", \"kind\":\"bool\", \"writable\": true }" +
    "    ]" +
    "  }" +
    "]" +
    "}";

    HashMap p1 = new HashMap();
    p1.put("id",   "p1");
    p1.put("name", "AI-1");
    p1.put("kind", "num");
    p1.put("writable", new Boolean(false));
    p1.put("unit", "°F");

    HashMap p2 = new HashMap();
    p2.put("id",   "p2");
    p2.put("name", "AI-2");
    p2.put("kind", "num");
    p2.put("writable", new Boolean(false));
    p2.put("unit", "kW");

    HashMap p3 = new HashMap();
    p3.put("id",   "p3");
    p3.put("name", "DO-1");
    p3.put("kind", "bool");
    p3.put("writable", new Boolean(true));

    ArrayList points = new ArrayList();
    points.add(p1);
    points.add(p2);
    points.add(p3);

    HashMap s1 = new HashMap();
    s1.put("name", "Meter A");
    s1.put("size", new Double(3));
    s1.put("points", points);

    ArrayList sources = new ArrayList();
    sources.add(s1);

    HashMap map = new HashMap();
    map.put("size", new Double(1));
    map.put("sources", sources);

    verifyEq(read(json), map);
  }

  @Test public void testValues() throws IOException
  {
    String json =
    "{" +
    "\"ts\": \"2020-05-10T02:00:00-04:00\"," +
    "\"size\": 3," +
    "\"data\": [" +
    "  { \"id\": \"p1\", \"val\": 5.25 }," +
    "  { \"id\": \"p2\", \"val\": 7.25 }," +
    "  { \"id\": \"p3\", \"val\": 3.25 }" +
    "]" +
    "}";

    HashMap p1 = new HashMap();
    p1.put("id", "p1");
    p1.put("val", new Double(5.25));

    HashMap p2 = new HashMap();
    p2.put("id", "p2");
    p2.put("val", new Double(7.25));

    HashMap p3 = new HashMap();
    p3.put("id", "p3");
    p3.put("val", new Double(3.25));

    ArrayList data = new ArrayList();
    data.add(p1);
    data.add(p2);
    data.add(p3);

    HashMap map = new HashMap();
    map.put("ts",   "2020-05-10T02:00:00-04:00");
    map.put("size", new Double(3));
    map.put("data", data);

    verifyEq(read(json), map);
  }

  private Object read(String s) throws IOException
  {
    InputStream in = new ByteArrayInputStream(s.getBytes());
    JsonReader r = new JsonReader(in);
    return r.readVal();
  }

  private String write(Object obj) throws IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JsonWriter w = new JsonWriter(out);
    w.writeVal(obj);
    w.flush();
    return out.toString("UTF-8");
  }
}
