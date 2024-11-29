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
import javax.baja.control.*;
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
          doPoints(getFormParams(req), w);
          endRes(w);
          return;
        }
        if (path[1].equals("values"))
        {
          JsonWriter w = startRes(op);
          doValues(getFormParams(req), w);
          endRes(w);
          return;
        }
        if (path[1].equals("write"))
        {
          JsonWriter w = startRes(op);
          doWrite(getFormParams(req), w);
          endRes(w);
          return;
        }
        if (path[1].equals("batch"))
        {
          // parse input
          HashMap args = (HashMap)(new JsonReader(req.getInputStream()).readVal());

          // service
          JsonWriter w = startRes(op);
          doBatch(args, w);
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

  private HashMap getFormParams(HttpServletRequest req)
  {
    HashMap map = new HashMap();
    Enumeration e = req.getParameterNames();
    while (e.hasMoreElements())
    {
      String key = (String)e.nextElement();
      String val = req.getParameter(key);
      map.put(key, val);
    }
    return map;
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
  private void doPoints(HashMap params, JsonWriter json) throws IOException
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
  private void doValues(HashMap params, JsonWriter json) throws IOException
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
// Endpoint /write
////////////////////////////////////////////////////////////////

  /** Service /v1/write request. */
  private void doWrite(HashMap params, JsonWriter json) throws IOException
  {
    BJasperService service = (BJasperService)this.getParent();

    // verify writable
    if (service.getAllowWrite() == false)
      throw new JasperServletException(403, "Write operations are not permitted");

    // request args
    String sourceId = reqArgStr(params, "source_id");
    String paddr    = reqArgStr(params, "point_addr");
    String sval     = reqArgStr(params, "val");
    String slevel   = optArgStr(params, "level", "16");

    // verify args
    JasperSource source = index.getSource(sourceId);
    if (source == null) throw new JasperServletException(404, "Source not found");
    JasperPoint point = source.getPoint(paddr);
    if (point == null) throw new JasperServletException(404, "Point not found");
    int level = Integer.parseInt(slevel);

    // verify control point
    boolean isControlPoint = point.comp instanceof BControlPoint;
    if (!isControlPoint) throw new JasperServletException(403, "Point is not writable");

    // verify writable
    BControlPoint cpoint = (BControlPoint)point.comp;
    if (!cpoint.isWritablePoint()) throw new JasperServletException(403, "Point is not writable");

    // write point
    Double dval = sval.equals("null") ? null : Double.parseDouble(sval);
    JasperUtil.setPointValue(cpoint, dval, level);

    // response
    json.write('{');
    json.writeKey("status").writeVal("ok");
    json.write('}');
  }

////////////////////////////////////////////////////////////////
// Endpoint /batch
////////////////////////////////////////////////////////////////

  /** Service /v1/batch request. */
  private void doBatch(HashMap args, JsonWriter json) throws IOException
  {
    // response
    json.write('{');
    json.writeKey("results").write('[');

    ArrayList ops = (ArrayList)args.get("ops");
    for (int i=0; i<ops.size(); i++)
    {
      if (i > 0) json.write(',');

      HashMap r = (HashMap)ops.get(i);
      String op = (String)r.get("op");
      if (op.equals("about"))   { doAbout(json);     continue; }
      if (op.equals("sources")) { doSources(json);   continue; }
      if (op.equals("points"))  { doPoints(r, json); continue; }
      if (op.equals("values"))  { doValues(r, json); continue; }
      throw new JasperServletException(400, "Invalid op '" + op + "'");
    }

    json.write(']');
    json.write('}');
  }

////////////////////////////////////////////////////////////////
// Support
////////////////////////////////////////////////////////////////

  /** Get HTTP request argument as 'String' or return 'defVal' if not found. */
  private String reqArgStr(HashMap params, String name)
  {
    String val = (String)params.get(name);
    if (val == null) throw new JasperServletException(400, "Missing required '" + name + "' param");
    return val;
  }

  /** Get HTTP request argument as 'int' or return 'defVal' if not found. */
  private int reqArgInt(HashMap params, String name)
  {
    String val = reqArgStr(params, name);
    return Integer.parseInt(val);
  }

  /** Get HTTP request argument as 'String' or return 'defVal' if not found. */
  private String optArgStr(HashMap params, String name, String defVal)
  {
    String val = (String)params.get(name);
    if (val == null) val = defVal;
    return val;
  }

////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////

  private JasperIndex index;
  private final long leaseTime = 120000;   // 2min in millis
}