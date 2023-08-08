# Changelog

## Version 0.6 (working)
* Return additional debug info on errors during API requests
* Fix to lease `BComponents` inside `/values` endpoint
* Fix `JsonWriter` to wrap `NaN` as `"na"`
* Add `status` field to `/values` endpoint
* Add support for `max_args` arg on `/points` and `/values` endpoints

## Version 0.5 (13-Jul-2023)
* Add support for `path_prefix` arg on `/points` and `/values` endpoints
* Unescape URL-encoding for point `name` and `path` on `/points` endpoint

## Version 0.4 (3-May-2023)
* Set min build target to `4.10` for better compatibility
* Remove `size` props to match updated Jasper spec

## Version 0.3 (1-Mar-2023)
* Add support for publicly-signed module signing

## Version 0.2 (20-Jan-2023)
* Fix `JaspserPoint.id` -> `addr` to match Jasper spec

## Version 0.1 (20-Jan-2023)
* Initial release
* Unsigned module