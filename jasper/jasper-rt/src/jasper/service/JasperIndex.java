//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   19 Jan 2023  Andy Frank  Creation
//

package jasper.service;

import java.util.*;
import javax.baja.sys.*;

/**
 * JasperIndex.
 */
public final class JasperIndex
{
  /** Constructor */
  public JasperIndex()
  {
  }

  /** Return number of sources in index. */
  public int sourceSize()
  {
    return smap.size();
  }

  /** Return number of points in index. */
  public int size()
  {
    return map.size();
  }

  /** Clear all entries from index. */
  public void clear()
  {
    map.clear();
  }

  /** Add a new source to index. */
  public void addSource(JasperSource s)
  {
    smap.put(s.id, s);
  }

  /** Get source for given addr or null if not found. */
  public JasperSource getSource(String id)
  {
    return (JasperSource)smap.get(id);
  }

  /** Add a new point to index. */
  public void add(JasperPoint p)
  {
    map.put(p.addr, p);
  }

  /** Get list of sources in index. */
  public Collection<JasperSource> getSources()
  {
    return smap.values();
  }

  /** Get current ids in index. */
  public String[] ids()
  {
    // TODO: should we cache this?
    Set keys = map.keySet();
    String[] acc = (String[])keys.toArray(new String[keys.size()]);
    return acc;
  }

  /** Get the point for given addr or null if not found. */
  public JasperPoint get(String addr)
  {
    return (JasperPoint)map.get(addr);
  }

  private HashMap smap = new HashMap();   // source_id:JasperSource
  private HashMap map = new HashMap();
}