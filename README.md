# physics_engine_mc
 
### About
This was my first ever project in Java. The goal was to create a `struture` and have it move in `x`, `y` and `z`.
Furthermore, it should be able to bounce off of world blocks and be able to rotate at any point. In the future, I would
like to add `stl`/`obj` support to allow importing of 3d models from a URL.

#### [Example video](https://youtu.be/14iEyjfPQpk)

### How to use
#### Basic commands
- `/physics` - start of any command in this mod
  - `pause`
    - `all`
    - `mine`
  - `resume`
    - `all`
    - `mine`
  - `delete`
    - `all`
    - `mine`

**Note:** `all`/`mine` are related to the player using the command (useful for multiplayer).

#### Creating a structure (currently very limited - only for development)
- `/physics`
  - `block`
      - `pos` - `x`, `y` and `z`
          - `item_1` - a block
              - `item_2` - another block
                - `energy_loss` - float between 0 and 1. How much energy is lost on each bounce
                    - `velocity` - `x`, `y` and `z`. *Optional*
                        - `acceleration` - `x`, `y` and `z`. *Optional*
                            - `rotation` - `x`, `y` and `z` (initial rotation). *Optional*
  - `box`
    - `pos` - `x`, `y` and `z`
        - `item` - a block
            - `energy_loss` - float between 0 and 1. How much energy is lost on each bounce
                - `velocity` - `x`, `y` and `z`. *Optional*
                    - `acceleration` - `x`, `y` and `z`. *Optional*

**Note:** Always include the decimal after a number. I.e. `1.0` instead of `1`.

#### Example command
- `/physics box ~ ~ ~ minecraft:sea_lantern minecraft:glowstone 1.0 3.0 20.0 5.0 0.0 -9.81 0.0`

### How to try it
Download [Minecraft Forge 1.19.2](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.19.2.html) and place
the `.jar` found in `/builds` into the `mods` folder. Then run Minecraft.

### Future versions
- Add `stl`/`obj` support
- For `obj`s add texture support

© 2022 Sean McConnachie. All rights reserved