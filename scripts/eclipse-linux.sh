#!/bin/bash
workspace=$(pwd)/workspace
eclipseroot=$(pwd)/eclipse
installTargetFeatureGroup=org.eclipse.cdt.feature.group,epp.package.java
bundlepoolDir=$eclipseroot
pluginsDir=$eclipseroot/plugins
eclipsedownload32="http://artfiles.org/eclipse.org/technology/epp/downloads/release/mars/1/eclipse-java-mars-1-linux-gtk.tar.gz"
eclipsedownload64="http://artfiles.org/eclipse.org/technology/epp/downloads/release/mars/1/eclipse-java-mars-1-linux-gtk-x86_64.tar.gz"
hvm="http://icelab.dk/resources/icecaptools_2.1.17.jar"
hvmsdk="http://icelab.dk/resources/icecapSDK.jar"
INSTALL="YES"

UNKNOWNARG=""
while [[ $# > 0 ]]
do
key="$1"

case $key in
    -a|--arch)
    ARCH="$2"
    shift 2
    ;;
    *)
            # unknown option
            UNKNOWNARG="$UNKNOWNARG $1"
            shift
    ;;
esac
done

if [[ -n $UNKNOWNARG ]]; then
    echo "Last line of file specified as non-opt/last argument: $UNKNOWNARG"
fi

if [ $ARCH == "64" ]
then
    eclipsedownload=$eclipsedownload64
    arch=x64
else
    eclipsedownload=$eclipsedownload32
    arch=x86
fi

#pushd $eclipseroot >/dev/null

if [ $INSTALL == "YES" ]
then
    echo "[`date +%H:%M:%S`] Removing old installation ..." 
    rm -fr $eclipseroot $bundlepoolDir icecapSDK.jar
	eclipse=eclipse-linux.tar.gz
	echo "[`date +%H:%M:%S`] Downloading eclipse ...";
    wget --show-progress -q -r -O $eclipse $eclipsedownload
	echo "[`date +%H:%M:%S`] Unpacking $eclipse ...";
    mkdir -p $eclipseroot
	tar xzf $eclipse -C $eclipseroot --strip 1
    rm $eclipse
	echo "[`date +%H:%M:%S`] Installing HVM plugin ...";
    wget --show-progress -q -P $pluginsDir $hvm
	echo "[`date +%H:%M:%S`] Downloading HVM SDK ...";
    wget --show-progress -q -P . $hvmsdk
fi

#vm="/opt/sun-java2-5.0/bin/java"
#vm=/opt/ibm-java2-5.0/bin/java

echo ""
echo "Using:       workspace=$workspace";
echo "Installing:  ${installTargetFeatureGroup}";
echo "Destination: $bundlepoolDir";
echo ""

echo "[`date +%H:%M:%S`] Running p2.director ... ";
#  -console -noexit -debug 
$eclipseroot/eclipse -nosplash \
  -consolelog -clean \
  -application org.eclipse.equinox.p2.director \
  -metadataRepository http://download.eclipse.org/releases/mars/ \
  -artifactRepository http://download.eclipse.org/releases/mars/ \
  -installIU ${installTargetFeatureGroup} \
  -destination $eclipseroot \
  -bundlepool $eclipseroot \
  -profile SDKProfile \
  -roaming \
  -vmargs \
    -Declipse.p2.data.area=$eclipseroot/p2 \
    -Xms128M -Xmx256M 
echo "Install finished";

echo "";
echo -n "[`date +%H:%M:%S`] Starting Eclipse workbench (to verify installation) ...";
#  -console -noexit -debug 
$eclipseroot/eclipse \
  -consolelog -clean \
  -vmargs \
    -Xms128M -Xmx256M \

#popd >/dev/null
echo " done."
