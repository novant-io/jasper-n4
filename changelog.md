# Changelog

## Version 0.17 (working)

## Version 0.16 (29-Nov-2024)
* Support for new `/write` endpoint

## Version 0.15 (9-Nov-2024)
* Beef up `JsonWriter` support for `+/-INF`

## Version 0.14 (8-Oct-2024)
* Log `reindex` action fails to LOG instead of `stderr`
* Trap each `comp` lookup so whole `reindex` does not fail if error

## Version 0.13 (9-Aug-2024)
* Support for new `/batch` endpoint

## Version 0.12 (6-Jun-2024)
* Update to use `getDisplayName()` for source and points

## Version 0.11 (3-Jun-2024)
* Update enum discovery to use `BEnumRange` API instead of manually parsing

## Version 0.10 (14-Mar-2024)
* Update to use HSM code signer
* Add additional `trace` level logging

## Version 0.9 (23-Aug-2023)
* Fix point type assignments (again)

## Version 0.8 (22-Aug-2023)
* Fix point type assignment of writable points

## Version 0.7 (22-Aug-2023)
* New `/sources` endpoint with nested points design
* Change point `addr` to use relative paths
* Bump point lease time to `2min`

## Version 0.6 (8-Aug-2023)
* Return additional debug info on errors during API requests
* Fix to lease `BComponents` inside `/values` endpoint
* Fix `JsonWriter` to wrap `NaN` as `"na"`
* Add `status` field to `/values` endpoint
* Add support for `max_points` arg on `/points` and `/values` endpoints

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