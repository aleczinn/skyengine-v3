# SkyEngine - V3
A Java game engine with lwjgl3.

Informations:
  - Gameloop with interpolation

Functions:
  - Render Shapes [point, line, triangle, quad, circle, arc, ellipse]
  - Render Textures [Also TextureRegions]
  - Basic SoundEngine
  - Input-System with controller support
  
Planned:
  - Update the full engine [own math classes]
  - Shader implementation
  - Better Camera (with own matrix, vector class)
  
Classes:
  Main: de.skyengine.DesktopLauncher (Launch the engine)
  
  Engine-Class: de.skyengine.core.SkyEngine (Manage gameloop with input, audio)
  
  My current game: de.games.factory.FactoryGame
