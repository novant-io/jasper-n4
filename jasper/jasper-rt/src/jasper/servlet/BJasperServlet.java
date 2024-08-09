//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.servlet;

import java.io.*;
import java.util.*;
import javax.baja.io.*;
import javax.baja.naming.*;
import javax.baja.status.*;
import javax.baja.sys.*;
import javax.baja.util.*;
import javax.baja.web.*;
import javax.servlet.http.*;
import jasper.service.*;
import jasper.util.*;

/**
 * BJasperServlet
 */
public final class BJasperServlet extends BWebServlet
{
  /*-
  class BJasperServlet
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $jasper.servlet.BJasperServlet(2852803851)1.0$ @*/
/* Generated Wed Apr 21 15:57:23 EDT 2021 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////

  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BJasperServlet.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

////////////////////////////////////////////////////////////////
// Constructor
////////////////////////////////////////////////////////////////

  /** Constructor. */
  public BJasperServlet()
  {
    super.setServletName("jasper");
    super.setFlags(getSlot("servletName"), Flags.READONLY | Flags.SUMMARY);
  }

  /** Set backing index */
  public void setIndex(JasperIndex index) { this.index = index; }

////////////////////////////////////////////////////////////////
// Servlet
////////////////////////////////////////////////////////////////

  public void doPost(WebOp op) throws IOException
  {
    try
    {
      // NOTE: getPathInfo removes 'jasper' prefix from path already
      HttpServletRequest req = op.getRequest();
      String[] path = JasperUtil.splitPath(req.getPathInfo());

      // sanity check path is long enough
      if (path.length < 2)
      {
        JasperUtil.sendNotFound(op);
        return;
      }

      // key off version
      if (path[0].equals("v1"))
      {
        if (path[1].equals("about"))
        {
          JsonWriter w = startRes(op);
          doAbout(w);
          endRes(w);
          return;
        }
        if (path[1].equals("sources"))
        {
          JsonWriter w = startRes(op);
          doSources(w);
          endRes(w);
          return;
        }
        if (path[1].equals("points"))
        {
          JsonWriter w = startRes(op);
          Map params = req.getParameterMap();
          doPoints(w, params);
          endRes(w);
          return;
        }
        if (path[1].equals("values"))
        {
          JsonWriter w = startRes(op);
          Map params = req.getParameterMap();
          doValues(w, params);
          endRes(w);
          return;
        }
      }

      // if we get here then 404
      JasperUtil.sendNotFound(op);
    }
    catch (JasperServletException jse)
    {
      jse.printStackTrace();
      JasperUtil.sendErr(op, jse.errCode, jse.getMessage(), jse);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      JasperUtil.sendErr(op, 500, "Unexpected error", ex);
    }
  }

  private JsonWriter startRes(WebOp op) throws IOException
  {
    HttpServletResponse res = op.getResponse();
    res.setStatus(200);
    res.setHeader("Content-Type", "application/json");

    JsonWriter json = new JsonWriter(res.getOutputStream());
    return json;
  }

  private void endRes(JsonWriter json) throws IOException
  {
    json.flush().close();
  }

////////////////////////////////////////////////////////////////
// Endpoint /about
////////////////////////////////////////////////////////////////

  /** Service /v1/about request. */
  private void doAbout(JsonWriter json) throws IOException
  {
    // TODO: watch trailing comma bugs; should JsonWriter handle internally?
    json.write('{');

    // required fields
    json.writeKey("name").writeVal(Sys.getStation().getStationName()).write(',');
    json.writeKey("vendor").writeVal("Tridium").write(',');
    json.writeKey("model").writeVal("Niagara 4").write(',');
    json.writeKey("version").writeVal(BComponent.TYPE.getVendorVersion().toString()).write(',');

    // additional fields
    BModule module = BJasperService.TYPE.getModule();
    json.writeKey("moduleName").writeVal(module.getModuleName()).write(',');
    json.writeKey("moduleVersion").writeVal(BJasperService.TYPE.getVendorVersion().toString());

    json.write('}');
  }

////////////////////////////////////////////////////////////////
// Endpoint /sources
////////////////////////////////////////////////////////////////

  /** Service /v1/sources request. */
  private void doSources(JsonWriter json) throws IOException
  {
    // response
    json.write('{');
    json.writeKey("sources").write('[');

    Iterator<JasperSource> iter = index.getSources().iterator();
    int num = 0;

    while (iter.hasNext())
    {
      JasperSource s = iter.next();

      // prefix trailing commas
      if (num > 0) json.write(',');

      json.write('{');
      json.writeKey("id").writeVal(s.id).write(',');
      json.writeKey("name").writeVal(s.name).write(',');
      json.writeKey("path").writeVal(s.path);
      json.write('}');
      num++;
    }
    json.write(']');
    json.write('}');
  }

////////////////////////////////////////////////////////////////
// Endpoint /points
////////////////////////////////////////////////////////////////

  /** Service /v1/points request. */
  private void doPoints(JsonWriter json, Map params) throws IOException
  {
    // request args
    String sourceId = reqArgStr(params, "source_id");
    JasperSource source = index.getSource(sourceId);
    if (source == null) throw new JasperServletException(404, "Source not found");

    // response
    json.write('{');
    json.writeKey("points").write('[');
    Iterator<JasperPoint> iter = source.getPoints().iterator();
    int num = 0;
    while (iter.hasNext())
    {
      JasperPoint p = iter.next();

      // prefix trailing commas
      if (num > 0) json.write(',');

      json.write('{');
      json.writeKey("addr").writeVal(p.addr).write(',');
      json.writeKey("name").writeVal(p.name);
      if (p.enums != null)
      {
        json.write(',');
        json.writeKey("enum").writeVal(p.enums);
      }
      if (p.unit != null)
      {
        json.write(',');
        json.writeKey("unit").writeVal(p.unit);
      }
      json.write('}');
      num++;
    }
    json.write(']');
    json.write('}');
  }

////////////////////////////////////////////////////////////////
// Endpoint /values
////////////////////////////////////////////////////////////////

  /** Service /v1/values request. */
  private void doValues(JsonWriter json, Map params) throws IOException
  {
    BJasperService service = (BJasperService)this.getParent();

    // request args
    String sourceId = reqArgStr(params, "source_id");
    JasperSource source = index.getSource(sourceId);
    if (source == null) throw new JasperServletException(404, "Source not found");

    // response
    json.write('{');
    json.writeKey("values").write('[');
    Iterator<JasperPoint> iter = source.getPoints().iterator();
    int num = 0;
    while (iter.hasNext())
    {
      JasperPoint p = iter.next();
      Object val = null;
      String status = "unknown";

      // bump lease time
      p.comp.lease(1, leaseTime);

      // get point value
      BStatusValue pv = JasperUtil.getPointValue(p.comp);
      if (pv != null)
      {
        val = pv.getStatus().isValid() ? pv : "na";
        status = pv.getStatus().flagsToString(null);
      }

      // prefix trailing commas
      if (num > 0) json.write(',');

      json.write('{');
      json.writeKey("addr").writeVal(p.addr).write(',');
      json.writeKey("val").writeVal(val).write(',');
      json.writeKey("status").writeVal(status);
      json.write('}');
      num++;
    }
    json.write(']');
    json.write('}');
  }

////////////////////////////////////////////////////////////////
// Support
////////////////////////////////////////////////////////////////

  /** Get HTTP request argument as 'String' or return 'defVal' if not found. */
  private String reqArgStr(Map params, String name)
  {
    String val = paramVal(params, name);
    if (val == null) throw new IllegalArgumentException("Missing required '" + name + "' param");
    return val;
  }

  /** Get HTTP request argument as 'int' or return 'defVal' if not found. */
  private int reqArgInt(Map params, String name)
  {
    String val = reqArgStr(params, name);
    return Integer.parseInt(val);
  }

  /** Get HTTP request argument as 'String' or return 'defVal' if not found. */
  private String optArgStr(Map params, String name, String defVal)
  {
    String val = paramVal(params, name);
    if (val == null) val = defVal;
    return val;
  }

  private String paramVal(Map params, String key)
  {
    String[] list = (String[])params.get(key);
    if (list == null) return null;
    if (list.length == 0) return null;
    return list[0];
  }

////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////

  private JasperIndex index;
  private final long leaseTime = 120000;   // 2min in millis
}