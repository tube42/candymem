Candy Memory
============

This is a FOSS memory game for Android. 

.. image:: http://tube42.github.io/candymem/img/img00.png


To download, go to https://play.google.com/store/apps/details?id=se.tube42.kidsmem.android


Acknowledgements
----------------

The candy graphics was extracted from public domain images created by the talented Luisa Midori ( @lumimae on twitter).



Building
--------

To build this app, you will need

1. java, ant, android SDK and all that
2. The rest (libgdx, marm, tweeny, ks, ...) is downloaded when you do setup
3. Assets, a default set is downloaded during setup

To setup the project and download required libraries, binaries (all FOSS) and assets

* ant setup

To build the project and run on desktop

* ant run

To build for android and upload it to your device

* ant debug install

Assets
------

Asset sources are found under the extra folder. To compile assets you will need the following tools:

1. ImageMagic for converting PNG images
2. Inkscape for rendering SVG files
3. The rest are downloaded during setup...

To build the assets, you should do

* make

Note that "ant setup" will overwrite your generated assets.


Notes
-----

This projects follows the standard Java coding style with one exception: private stuff is snake_case ;)
