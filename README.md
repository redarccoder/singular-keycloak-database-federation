# singular-keycloak-database-federation

## Status
This fork is currently maintained (checked working for Keycloak 22 and 23, for older versions of Keycloak see branch `keycloak-21`).
This repository was originally created by OpenSingular at [singular-keycloak-databas-federation](https://github.com/opensingular/singular-keycloak-database-federationhttps:/), then continued at  <https://github.com/redarccoder/singular-keycloak-database-federation> and now here.

## Usage

Fully compatible with Singular Studio NOCODE. See https://www.studio.opensingular.com/

## Configuration

Keycloak User Federation Screen Shot

![Sample Screenshot](screen.png)

There is a new configuration that allows keycloak to remove a user entry from its local database (this option has no effect on the source database). It can be useful when you need to reload user data.
This option can be configured by the following switch:

![Sample Screenshot](deleteuser.png)

## Limitations

- Do not allow user information update, including password update
- Do not supports user roles our groups
- Keycloak must use a non-XA datasource
- User management is expected on the BD Side, most add columns for emailValidation and enabled fields (if not use a view)

## Custom attributes

All additional columns returned by the query are available as user attributes in keycloak.

In order to provide them to applications just add a mapper to client mappers with the same name as the returned column alias in your queries.Use mapper type "User Attribute". See the example below:

![Sample Screenshot 2](screen2.png)

## Groups

The column `groups` is transparently mapped to keycloak groups. It should contain a comma-separated (sorry) list of group paths (just names in case you don't use subgroups).

## Build

- mvn clean package

## Deployment

1) Copy every  `.jar` from dist/ folder  to  /providers folder under your keycloak installation root.
   - i.e, on a default keycloak setup, copy all  `.jar` files to <keycloak_root_dir>/providers
2) run :
   $ ./bin/kc.sh start-dev
   OR if you are using a production configuration:
   $ ./bin/kc.sh build
   $ ./bin/kc.sh start

## For further information see:

- https://github.com/keycloak/keycloak/issues/9833
- https://www.keycloak.org/docs/latest/server_development/#packaging-and-deployment
