# Jasper N4

[jasper]: https://github.com/novant-io/jasper

An implementation of the [Jasper][jasper] JSON API for Niagara N4.

## Installation

[rel]: https://github.com/novant-io/jasper-n4/releases

### Supported Niagara Versions

Jasper is compatible with Niagara `4.10` and later.

### User Setup

Jasper requires TLS and a user with HTTP Basic Authentication to connect to
Niagara. To create a new user for Jasper:

 1. Open the `baja` palette
 2. Find `HTTPBasicScheme` under `AuthenticationServices/WebServicesSchemes`
 3. Drag `HTTPBasicScheme` into your station under
    `Services/AuthenticationServices/WebServicesSchemes`
 4. Create a new user and set `Authentication Scheme Name` to `HTTPBasicScheme`
 5. Verify user `Roles` has sufficient privileges

### Jasper Setup

To setup Jasper on your JACE:

 1. Install the [latest][rel] `jasper-rt.jar` module onto your system
 2. Open the `jasper` palette
 3. Drag the `JasperService` into your `Services` component
 4. Let the index build and Done! 🏁

### Rebuild Index

If any changes are made to the station after the initial indexing, you will
need to update the index to reflect the current station configuration.

To rebuild the index, right click on the `JasperService` and invoke the
`Rebuild index` action.

## API Examples

### About

    $ curl host/jasper/v1/about -XPOST -u username:password

    {
      "name": "demo",
      "vendor": "Tridium",
      "model": "Niagara 4",
      "version": "4.12.0.156",
      "moduleName": "jasper",
      "moduleVersion": "0.7"
    }

### Sources

    $ curl host/jasper/v1/sources -XPOST -u username:password

    {
      "sources": [
        {
          "id":   "54d",
          "name": "Chiller",
          "path": "/Drivers/NiagaraNetwork/JACE-02/points/Chiller",
        },
        {
          "id":   "620",
          "name": "SetpointTemp",
          "path": "/Drivers/NiagaraNetwork/JACE-05/points/VAV-1",
        },
        {
          "id":   "621",
          "name": "SetpointTemp",
          "path": "/Drivers/NiagaraNetwork/JACE-05/points/VAV-2",
        },
        {
          "id":   "622",
          "name": "SetpointTemp",
          "path": "/Drivers/NiagaraNetwork/JACE-05/points/VAV-3",
        }
      ]
    }

### Points

    $ curl host/jasper/v1/points -XPOST -d source_id:620 -u username:password

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
          "name": "Zone Temp"
          "unit": "°F"
        },
        {
          "addr": "ao.ReturnTemp",
          "name": "Return Temp"
          "unit": "°F"
        },
        {
          "addr": "ao.DischargeTemp",
          "name": "Discharge Temp"
          "unit": "°F"
        }
      ]
    }

### Values

    $ curl host/jasper/v1/values -XPOST -d source_id:620  -u username:password

    {
      "values": [
        { "addr":"av.DamperPosition", "val":72.0    },
        { "addr":"bv.FanStatus",      "val":1       },
        { "addr":"av.ZoneTemp",       "val":72.0    },
        { "addr":"ao.ReturnTemp",     "val":73.142  },
        { "addr":"ao.DischargeTemp",  "val":68.230  }
      ]
    }
