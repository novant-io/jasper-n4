# Jasper N4

[jasper]: https://github.com/novant-io/jasper

An implementation of the [Jasper][jasper] JSON API for Niagara N4.

## Installation

[rel]: https://github.com/novant-io/jasper-n4/releases

To setup Jasper on your JACE:

 1. Install the [latest][rel] `jasper-rt.jar` module onto your system
 2. Open the `jasper` palette
 3. Drag the `JasperService` into your `Services` component
 4. Let the index build and Done! 🏁

 ## About

    $ curl host/jasper/v1/about -u username:password

    {
      "name": "demo",
      "vendor": "Tridium",
      "model": "Niagara 4",
      "version": "4.12.0.156",
      "moduleName": "jasper",
      "moduleVersion": "0.1"
    }

## Points

    $ curl host/jasper/v1/points -u username:password

    {
      "size": 4,
      "points": [
        {
          "addr": "av.1b6b",
          "name": "SetpointTemp",
          "unit": "°F"
          "path": "/PxHome/Graphics/Campus/Building/Floor1/VavZoneC/SetpointTemp",
        },
        {
          "addr": "bv.1b75",
          "name": "FanStatus",
          "path": "/PxHome/Graphics/Campus/Building/Floor1/VavZoneC/FanStatus"
        },
        {
          "addr": "av.1b6d",
          "name": "HeatingCoil",
          "unit": "%"
          "path": "/PxHome/Graphics/Campus/Building/Floor1/VavZoneC/HeatingCoil",
        },
        {
          "addr": "eo.1b83",
          "name": "OccStatus",
          "enum": "occupied,unoccupied"
          "path": "/PxHome/Graphics/Campus/Building/Floor1/OccStatus",
        }
      ]
    }

## Values

    $ curl host/jasper/v1/values -u username:password

    {
      "size": 3,
      "values": [
        { "addr":"av.1b6b", "val":72 },
        { "addr":"bv.1b75", "val":1 },
        { "addr":"av.1b6d", "val":25 }
        { "addr":"eo.1b83", "val":1 }
      ]
    }
