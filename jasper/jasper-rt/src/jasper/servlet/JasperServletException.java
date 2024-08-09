//
// Copyright (c) 2024, Novant LLC
// Licensed under the MIT License
//
// History:
//   9 Aug 2024  Andy Frank  Creation
//

package jasper.servlet;

/**
 * JasperServletException
 */
public final class JasperServletException extends RuntimeException
{
  /**
   * Construct a new servlet exception with given error status
   * code and error message.
   */
  public JasperServletException(int errCode, String errMessage)
  {
    super(errMessage);
    this.errCode = errCode;
  }

  /**
   * HTTP error status code for this exception.
   */
  final int errCode;
}