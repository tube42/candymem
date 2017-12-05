#
# Makefile is used for assets, because we are too cool for ant
#

INKSCAPE=inkscape

MARM = java -jar submodules/marm/marm_app.jar hiero=libs/bin/hiero
COMPO= java -jar submodules/composition/Composition_app.jar

##

all: icons marm


icons:
	mkdir -p android/res/drawable-ldpi
	mkdir -p android/res/drawable-mdpi
	mkdir -p android/res/drawable-hdpi
	mkdir -p android/res/drawable-xhdpi
	mkdir -p android/res/drawable-xxhdpi
	$(INKSCAPE) -z extra/icon.svg  -w 36 -h 36 -e android/res/drawable-ldpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 48 -h 48 -e android/res/drawable-mdpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 72 -h 72 -e android/res/drawable-hdpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 96 -h 96 -e android/res/drawable-xhdpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 144 -h 144 -e android/res/drawable-xhdpi/ic_launcher.png

marm:
	rm -rf android/assets/1
	rm -rf android/assets/2
	rm -rf android/assets/4
	$(MARM) resize -1 extra/assets android/assets

compo:
	$(COMPO) android/assets/compo/compo.bin
##

clean:
	rm -rf android/assets/1
	rm -rf $(CLEAN_ADD)
	rm -rf android/res/drawable-*/ic_launcher.png
