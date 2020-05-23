# Scrap Mechanic Survival Mods Loader
This is a game patcher for the game Scrap Mechanic.\
Download the latest version here: https://cerus-dev.de/projects/ssm/scrapsurvivalmods-1.0.0.jar

> **IMPORTANT NOTE**\
> You can't load real SM mods with this application! This application directly modifies the game's script files.

The patcher loads its default mods from my website, but you can add your own. These mods are available on my website:
- Oil Drops by me
- Clam Drops by me

## How to build
1. [Download Java](https://adoptopenjdk.net/) (the application requires at least Java 8) and [Maven](http://maven.apache.org/)
2. Clone this repository `git clone https://github.com/RealCerus/scrap-survival-mods`
3. Build the project with Maven `mvn package`

## How to write a mod for this patcher
The mods need to be in the JSON format. Here's an example mod which changes the drops of oil geysers:

```json
{
  "name": "Oil Drops",
  "replaceables": {
    "Scripts\\game\\harvestable\\OilGeyser.lua": [
      {
        "regex": true,
        "replaceKey": "sm.container.collect\\(\\s+container,\\s+obj_resource_crudeoil,\\s+\\d+\\s+\\)",
        "replaceValue": "sm.container.collect( container, obj_resource_crudeoil, {{OIL_QUANTITY}} )"
      }
    ]
  },
  "inputs": [
    {
      "type": "NUMBER",
      "key": "OIL_QUANTITY",
      "description": "Please enter the amount of oil drops you want"
    }
  ]
}
```

Let's break this down. The first element in the json structure is the `name` element - it defines the name of the mod.\
The second element is the `replaceables` element - This object defines which script files need to be modified. The key of the child objects is the relative path to the script file (note: you can't use `..` when describing paths) and the element is a json array with one or more so called 'replaceables'. A replaceable just defines what needs to be replaced with what. If you set `regex` to true the `replaceKey` will be interpreted as a regex. You can use user input keys in the `replaceValue`.\
The third element is the `inputs` array - This array allows you to take input from the user and use it in your replaceables. You can choose from three types: `NUMBER`, `BOOLEAN` and `STRING`.

I know this documentation is not the greatest, but I hope explained everything well.

## Planned features
- Add a way to add crafting recipes

## License
This project is licensed under the [GNU GPLv3](LICENSE).