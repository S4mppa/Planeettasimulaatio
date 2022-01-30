# Planeettasimulaatio

Planeettasimulaatio, jossa planeetat generoidaan sattumanvaraisesti käyttäen erilaisia noise algoritmeja.

Renderöintimoottorin luomiseen on käytetty OpenGL 3.0+ (https://en.wikipedia.org/wiki/OpenGL)

Simulaatiossa on LOD (https://en.wikipedia.org/wiki/Level_of_detail_(computer_graphics)) järjestelmä, joka hyödyntää octree menetelmää. (https://en.wikipedia.org/wiki/Octree)

Ilmakehä luodaan simuloimalla Rayleigh ja Mie sirontailmiöitä, missä otetaan huomioon esimerkiksi otsonin ja vesihöyryn pitoisuus ilmakehässä.
https://en.wikipedia.org/wiki/Rayleigh_scattering
https://en.wikipedia.org/wiki/Mie_scattering

Tekstuurien piirtämisessä käytetään PBR (Physically based rendering) menetelmää, joka saa ympäristön näyttämään vakuuttavammalta.
https://en.wikipedia.org/wiki/Physically_based_rendering

![planet1](https://i.imgur.com/q5vczET.png)
![otherplanet](https://i.imgur.com/lrFgyxo.png)
![snow](https://i.imgur.com/C6OYync.png)
![high](https://i.imgur.com/h3AvvkI.png)
![planet2](https://i.imgur.com/jPdR42U.png)
![sunset1](https://i.imgur.com/NlAKwN2.png)
