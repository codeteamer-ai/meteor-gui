# Meteor GUI

A Meteor Client-style Click GUI mod for **Minecraft 1.21.11** (Fabric).

## Features
- Animated click GUI with top tab bar (Right Shift to open)
- Module system with toggle animations and custom sounds
- Dark purple/blue Meteor-style color palette
- **Fullbright module** — uses shader-level NightVision injection via Mixin

## Building
```bash
./gradlew build
```
JAR will be in `build/libs/meteor-gui-1.0.0.jar`

## Tech Details
- Fabric API `0.141.3+1.21.11`
- Yarn Mappings `1.21.11+build.4`
- Fabric Loader `0.18.4`
- Loom `1.15.5`

### Fullbright Mixin (1.21.11 Shader Architecture)
In 1.21.11, the lightmap is computed on the GPU via `lightmap.fsh`. The Java `update()` method packs parameters into a UBO. Our mixin uses `@Redirect` on three method calls to force NightVisionFactor=1.0 and zero out Darkness.
