# Jasper N4

[jasper]: https://github.com/novant-io/jasper

An implementation of the [Jasper][jasper] JSON API for Niagara N4.

## Quick Start

See [Installation](https://github.com/novant-io/jasper-n4/wiki/1.-Installation)
for more details:

  1. [Check supported Niagara versions](https://github.com/novant-io/jasper-n4/wiki/1.-Installation#supported-versions)
  2. [Install Jasper Module](https://github.com/novant-io/jasper-n4/wiki/1.-Installation#install-module)
  3. [User Setup](https://github.com/novant-io/jasper-n4/wiki/1.-Installation#user-setup)
  4. [Jasper Setup](https://github.com/novant-io/jasper-n4/wiki/1.-Installation#jasper-setup)
  5. [Rebuild Index](https://github.com/novant-io/jasper-n4/wiki/1.-Installation#rebuild-index)

## API Examples

See [API Documentation](https://github.com/novant-io/jasper-n4/wiki/2.-API-Documentation)
for more details.

### About

    $ curl host/jasper/v1/about -XPOST -u username:password

    {
      "name": "demo",
      "vendor": "Tridium",
      "model": "Niagara 4",
      "version": "4.12.0.156",
      "moduleName": "jasper",
      "moduleVersion": "0.13"
    }

### Sources

    $ curl host/jasper/v1/sources -XPOST -u username:password

    {
      "sources": [
        {
          "id":   "54d",
          "name": "Chiller",
          "path": "/Drivers/NiagaraNetwork/JACE-02/points/Chiller"
        },
        {
          "id":   "620",
          "name": "VAV-1",
          "path": "/Drivers/NiagaraNetwork/JACE-05/points/VAV-1"
        },
        {
          "id":   "621",
          "name": "VAV-2",
          "path": "/Drivers/NiagaraNetwork/JACE-05/points/VAV-2"
        },
        {
          "id":   "622",
          "name": "VAV-3",
          "path": "/Drivers/NiagaraNetwork/JACE-05/points/VAV-3"
        }
      ]
    }

### Points

    $ curl host/jasper/v1/points -XPOST -d source_id=620 -u username:password

    {
      "points": [
        {
          "addr": "av.DamperPosition",
          "name": "Damper Position"
        },
        {
          "addr": "bv.",
          "name": "Fan Status"
        },
        {
          "addr": "av.ZoneTemp",
          "name": "Zone Temp",
          "unit": "°F"
        },
        {
          "addr": "ao.ReturnTemp",
          "name": "Return Temp",
          "unit": "°F"
        },
        {
          "addr": "ao.DischargeTemp",
          "name": "Discharge Temp",
          "unit": "°F"
        }
      ]
    }

### Values

    $ curl host/jasper/v1/values -XPOST -d source_id=620  -u username:password

    {
      "values": [
        { "addr":"av.DamperPosition", "val":72.0 },
        { "addr":"bv.FanStatus", "val":1 },
        { "addr":"av.ZoneTemp", "val":72.0 },
        { "addr":"ao.ReturnTemp", "val":73.142 },
        { "addr":"ao.DischargeTemp", "val":68.230 }
      ]
    }
