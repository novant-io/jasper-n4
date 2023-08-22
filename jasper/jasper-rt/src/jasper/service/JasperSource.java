//
// Copyright (c) 2023, Novant LLC
// Licensed under the MIT License
//
// History:
//   21 Aug 2023  Andy Frank  Creation
//

package jasper.service;

import java.util.*;
import javax.baja.sys.*;

/**
 * JasperSource.
 */
public final class JasperSource
{
  /** Constructor */
  public JasperSource(String id, String name, String path)
  {
    this.id   = id;
    this.name = name;
    this.path = path;
  }

  /** Unique id for this source. */
  public final String id;

  /** Name of this source. */
  public final String name;

  /** Path of this source. */
  public final String path;

  /** Get point list for this source. */
  public Collection<JasperPoint> getPoints() { return pmap.values(); }

  public String toString() { return name; }

  // package private
  void addPoint(JasperPoint p) { pmap.put(p.addr, p); }

  private HashMap pmap = new HashMap();
}