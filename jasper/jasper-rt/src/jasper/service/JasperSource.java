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
  public JasperSource(String addr, String name, String path)
  {
    this.addr  = addr;
    this.name  = name;
    this.path  = path;
  }

  /** Unique address for this point. */
  public final String addr;

  /** Name of this point. */
  public final String name;

  /** Path of this source. */
  public final String path;

  public String toString() { return name; }
}